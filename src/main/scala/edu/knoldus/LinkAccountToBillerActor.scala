package edu.knoldus

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import edu.knoldus.LinkAccountToBillerActor.{InvalidMessage, Link}
import edu.knoldus.models.Biller

/**
  * Created by Neelaksh on 7/8/17.
  */
class LinkAccountToBillerActor(databaseRepo: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case (accnum: Long, biller: Biller) =>
      val originalSender = sender
      log.info(s"linking new billers for $accnum")
      databaseRepo.tell(Link(accnum, biller),sender)
    case _=> sender() ! InvalidMessage
  }
}

object LinkAccountToBillerActor {
  def props(databaseRepo: ActorRef): Props = Props(classOf[LinkAccountToBillerActor],databaseRepo)
  case class Link(accountnum: Long, biller: Biller)
  case object InvalidMessage
}
