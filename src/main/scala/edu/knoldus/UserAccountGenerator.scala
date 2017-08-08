package edu.knoldus

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import akka.dispatch.{BoundedMessageQueueSemantics, RequiresMessageQueue}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import edu.knoldus.UserAccountGenerator.AccountCreate
import edu.knoldus.models.Account

/**
  * Created by Neelaksh on 5/8/17.
  */

object UserAccountGenerator {
  def props(databaseRepo: ActorRef): Props = Props( classOf[UserAccountGenerator],databaseRepo)
  case class AccountCreate(account: Account)

}

class UserAccountGenerator(databaseRepo: ActorRef) extends Actor with ActorLogging with RequiresMessageQueue[BoundedMessageQueueSemantics] {
  /**
    *
    * @return forwards a request to database repo to check and create a new account if possible
    */
  var accountNum = 0

  override def receive: Receive = {
    case (acc: List[String]) =>
      accountNum += 1
      log.info(s"adding a new account with accountnum $accountNum")
      databaseRepo.forward(AccountCreate(Account.apply(accountNum, acc)))
  }
}
