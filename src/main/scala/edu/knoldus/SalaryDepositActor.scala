package edu.knoldus

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import edu.knoldus.SalaryDepositActor.Deposit
import edu.knoldus.models.{Biller}

import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable
import scala.util.{Failure, Success}
/**
  * Created by Neelaksh on 6/8/17.
  */
class SalaryDepositActor(databaseRepoActor: ActorRef) extends Actor with ActorLogging{
  var stringToActor:mutable.Map[String,ActorRef] = mutable.Map()
  override def receive: Receive = {
    case (name:String,accountnum:Long,amount:Long) =>
      implicit val timeout = Timeout(10 seconds)
      val deposit= {databaseRepoActor ? Deposit(name,accountnum,amount)}.mapTo[List[Biller]]
      deposit.onComplete{
        case Success(billers)=>
          log.info(s"successful retrieval of billers")
          billers.foreach(biller=>context.actorOf(BillerPayActor.props(databaseRepoActor)).forward(accountnum,biller))
        case Failure(ex)=> log.warning(s"failed to link account with billers because of : $ex")
      }
  }
}

object SalaryDepositActor{
  case class Deposit(name:String,accountNum:Long,amount:Long)
  def props(databaseRepo:ActorRef):Props = Props(new SalaryDepositActor(databaseRepo))
}
