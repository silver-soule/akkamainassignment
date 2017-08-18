package edu.knoldus

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import edu.knoldus.BillerPayActor.PayBiller
import edu.knoldus.models.Biller

/**
  * Created by Neelaksh on 7/8/17.
  */
class BillerPayActor(databaseRepoActor: ActorRef, billerCategory: String) extends Actor with ActorLogging {

  override def preStart(): Unit = {
    log.info(s"starting actor of$billerCategory")
    super.preStart()
  }
  override def receive: Receive = {
    case (accountNum: Long, biller: Biller) =>
      log.info(s"payment to biller type $billerCategory----------->\n")
      databaseRepoActor.forward(PayBiller(accountNum, biller.updateBiller()))
  }
}

object BillerPayActor {

  def props(databaseRepoActor: ActorRef, billerCategory: String): Props = Props(classOf[BillerPayActor], databaseRepoActor, billerCategory)
  case class PayBiller(accoutNum: Long, biller: Biller)

}
