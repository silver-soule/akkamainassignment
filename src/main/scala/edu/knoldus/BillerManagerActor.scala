package edu.knoldus

import akka.actor.{Actor, ActorRef, Props}
import edu.knoldus.models.Biller

/**
  * Created by Neelaksh on 12/8/17.
  */
class BillerManagerActor(databaseRepoActor: ActorRef) extends Actor {

  val billers = List("phone", "electricity", "food", "car", "internet")
  var billerMap: Map[String, ActorRef] = Map()

  override def preStart(): Unit = {
    super.preStart()
    billers.foreach(biller =>
      billerMap += (biller -> context.actorOf(BillerPayActor.props(databaseRepoActor, biller))))
  }

  override def receive: Receive = {
    case (accountnum: Long, biller: Biller) =>
      biller.billerCategory match {
        case phone@"phone" => billerMap(phone) forward(accountnum, biller)
        case food@"food" => billerMap(food) forward(accountnum, biller)
        case electricity@"electricity" => billerMap(electricity) forward(accountnum, biller)
        case car@"car" => billerMap(car) forward(accountnum, biller)
        case internet@"internet" => billerMap(internet) forward(accountnum, biller)
        case _ => sender() ! s"sorry but could not pay to ${biller.billerCategory}"
      }
  }
}

object BillerManagerActor {
  def props(databaseRepoActor: ActorRef): Props = Props(classOf[BillerManagerActor], databaseRepoActor)
}
