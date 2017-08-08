package edu.knoldus

import akka.actor.{Actor, ActorRef, Props}
import edu.knoldus.models.Biller

/**
  * Created by Neelaksh on 7/8/17.
  */
class BillerPayActor(databaseRepoActor: ActorRef) extends Actor with BillerCategories {
  val billerCategories: List[String] = List("phone", "electricity", "food", "car", "internet")

  override def receive: Receive = {
    case (accountNum: Long, biller: Biller) =>
      biller.billerCategory contains billerCategories match {
        case true => databaseRepoActor.forward((accountNum, biller.updateBiller()))
        case false => sender() ! false
      }
  }
}

object BillerPayActor {
  def props(databaseRepoActor: ActorRef): Props = Props(classOf[BillerPayActor],databaseRepoActor)
}

trait BillerCategories {
  val billerCategories: List[String]
}