package edu.knoldus

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.util.Timeout
import scala.collection.immutable.Map
import scala.concurrent.Future
import akka.pattern.ask
import scala.concurrent.duration.DurationInt

/**
  * Created by Neelaksh on 5/8/17.
  */
class UserAccountService{
  def createAccounts(accounts:List[List[String]],accountGeneratorRef: ActorRef)= {
    implicit val timeout = Timeout(1000 seconds)

    val createdAccounts =
    for{
      account<-accounts
    } yield accountGeneratorRef ? account
  createdAccounts
  }


}


