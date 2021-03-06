package edu.knoldus

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import edu.knoldus.SalaryDepositActor.{BillersRequest, Deposit}
import edu.knoldus.models.Biller

import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable
import scala.util.{Failure, Success}

/**
  * Created by Neelaksh on 6/8/17.
  */
class SalaryDepositActor(databaseRepoActor: ActorRef,billerManagerActor :ActorRef) extends Actor with ActorLogging {
  var stringToActor: mutable.Map[String, ActorRef] = mutable.Map()

  override def receive: Receive = {

    case (name: String, accountnum: Long, amount: Long) =>
      val originalSender = sender()
      implicit val timeout = Timeout(10 seconds)
      log.info(s"sending request to deposit salary")
      databaseRepoActor ! Deposit(name, accountnum, amount)
      log.info(s"sending request to fetch billers")
      val billerRequest = {
        databaseRepoActor ? BillersRequest(accountnum)
      }.mapTo[List[Biller]]
      log.info(s"got $billerRequest")
      billerRequest.onComplete {
        case Success(billers) =>
          log.info(s"successful retrieval of billers")
          billers.foreach(biller => billerManagerActor.tell((accountnum,biller),originalSender))
        case Failure(ex) => log.error(s"failed to pay billers because of : $ex")
      }
  }
}

object SalaryDepositActor {

  case class Deposit(name: String, accountNum: Long, amount: Long)

  case class BillersRequest(accountNum: Long)

  def props(databaseRepo: ActorRef,billerManager :ActorRef): Props = Props(classOf[SalaryDepositActor],databaseRepo,billerManager)
}
