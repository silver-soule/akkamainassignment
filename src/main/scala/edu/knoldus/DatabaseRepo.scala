package edu.knoldus

import akka.actor.{Actor, Props}
import edu.knoldus.DatabaseRepo.{Created, RequestInfo, RespondInfo, UserNotFound}
import edu.knoldus.UserAccountGenerator.AccountCreate

/**
  * Created by Neelaksh on 6/8/17.
  */
class DatabaseRepo extends Actor with Database {
  override def receive: Receive = {
    case uniqueAccount:AccountCreate =>
      sender()! Created(uniqueAccount.requestId,addAccount(uniqueAccount.account))

    case info:RequestInfo =>
      sender() ! getAccount(info.name).map{
        acc=>
        RespondInfo(info.requestId,acc)}.getOrElse(UserNotFound(info.requestId))

    case _:Link =>
  }

}
object DatabaseRepo{
  sealed class Response
  case class Created(requestId:Long,created:Boolean)
  case class RequestInfo(requestId:Long,name:String)
  case class UserNotFound(requestId: Long) extends Response
  case class RespondInfo(requestId:Long,account:Account) extends Response
  def props():Props = Props(new DatabaseRepo())
}

