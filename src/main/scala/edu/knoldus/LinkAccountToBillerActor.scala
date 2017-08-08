package edu.knoldus

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import edu.knoldus.LinkAccountToBillerActor.Link
import edu.knoldus.models.Biller

/**
  * Created by Neelaksh on 7/8/17.
  */
class LinkAccountToBillerActor(databaseRepo: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case (accnum: Long, biller: Biller) =>
      log.info(s"linking new billers for $accnum")
      databaseRepo.forward(Link(accnum, biller))
  }
}

object LinkAccountToBillerActor {
  def props(databaseRepo: ActorRef): Props = Props(new LinkAccountToBillerActor(databaseRepo))

  case class Link(accountnum: Long, biller: Biller)

}
