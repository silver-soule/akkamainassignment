package edu.knoldus.service

import akka.actor.{Actor, Props}
import edu.knoldus.LinkAccountToBillerActor.Link
import edu.knoldus.SalaryDepositActor.Deposit
import edu.knoldus.UserAccountGenerator.AccountCreate
import edu.knoldus._
import edu.knoldus.models.{Account, Biller}
import edu.knoldus.service.DatabaseRepoActor._

/**
  * Created by Neelaksh on 6/8/17.
  */
class DatabaseRepoActor extends Database with Actor {
  override def receive: Receive = {
    case uniqueAccount: AccountCreate =>
      sender() ! Created(uniqueAccount.requestId, addAccount(uniqueAccount.account))

    case info: RequestAccountInfo =>
      sender() ! getAccountByAccountnum(info.accountNum).map {
        acc => RespondAccountInfo(info.requestId, acc)
      }.getOrElse(UserNotFound(info.requestId))

    case info: Link =>
      sender() ! SuccessfulLink(addBillerToAccount(info.accountnum, info.biller))

    case deposit: Deposit =>
      sender()!getBillersByAccountnum(deposit.accountNum)

    case (accountnum:Long,biller:Biller) =>
      sender()!payBiller(accountnum,biller)
  }
}
object DatabaseRepoActor{
  sealed class Response
  case class Created(requestId:Long,created:Boolean)
  case class RequestAccountInfo(requestId:Long,accountNum:Long)
  case class UserNotFound(requestId: Long) extends Response
  case class RespondAccountInfo(requestId:Long, account:Account) extends Response
  case class SuccessfulLink(success:Boolean) extends Response
  def props():Props = Props(new DatabaseRepoActor())
}

