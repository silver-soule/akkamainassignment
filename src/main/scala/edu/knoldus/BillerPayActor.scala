package edu.knoldus

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import edu.knoldus.models.Biller

import scala.concurrent.Future

/**
  * Created by Neelaksh on 7/8/17.
  */
class BillerPayActor(databaseRepoActor: ActorRef) extends Actor with BillerCategories with ActorLogging{
  val billerCategories: List[String] = List("phone", "electricity", "food", "car", "internet")

  override def receive: Receive = {
    case (accountNum: Long, biller: Biller) =>
      if(billerCategories contains biller.billerCategory) {
        log.info(s"valid biller----------->\n")
        databaseRepoActor.forward((accountNum, biller.updateBiller()))
      }
      else{
        sender()!false
      }
  }
}

object BillerPayActor {
  def props(databaseRepoActor: ActorRef): Props = Props(classOf[BillerPayActor],databaseRepoActor)
}

trait BillerCategories {
  val billerCategories: List[String]
}