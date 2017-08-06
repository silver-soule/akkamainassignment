package edu.knoldus

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import edu.knoldus.UserAccountGenerator.AccountCreate

/**
  * Created by Neelaksh on 5/8/17.
  */

object UserAccountGenerator{
  def props(databaseRepo:ActorRef):Props = Props(new UserAccountGenerator(databaseRepo))
  case class AccountCreate(requestId:Long,account: Account)
}

class UserAccountGenerator(databaseRepo:ActorRef) extends Actor{
  /**
    *
    * @return forwards a request to database repo to check and create a new account if possible
    */
  var accountNum = 0
  override def receive: Receive = {
    case (rid:Long,acc : List[String])=>
      accountNum += 1
      databaseRepo.forward(AccountCreate(rid,Account.apply(accountNum,acc)))
  }
}
