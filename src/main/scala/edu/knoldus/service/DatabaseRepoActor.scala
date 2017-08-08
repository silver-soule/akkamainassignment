package edu.knoldus.service

import akka.actor.{Actor, ActorLogging, Props}
import edu.knoldus.LinkAccountToBillerActor.Link
import edu.knoldus.SalaryDepositActor.{BillersRequest, Deposit}
import edu.knoldus.UserAccountGenerator.AccountCreate
import edu.knoldus._
import edu.knoldus.models.{Account, Biller}
import edu.knoldus.service.DatabaseRepoActor._

/**
  * Created by Neelaksh on 6/8/17.
  */
class DatabaseRepoActor extends Database with Actor with ActorLogging {
  override def receive: Receive = {
    case uniqueAccount: AccountCreate =>
      sender() ! Created(uniqueAccount.account.accountNumber, addAccount(uniqueAccount.account))

    case info: RequestAccountInfo =>
      sender() ! getAccountByAccountnum(info.accountNum).map {
        acc => RespondAccountInfo(info.accountNum, acc)
      }.getOrElse(UserNotFound(info.accountNum))

    case info: Link =>
      log.info(s"sending link request for ${info.accountnum}")
      sender() ! SuccessfulLink(info.accountnum, addBillerToAccount(info.accountnum, info.biller))

    case deposit: Deposit =>
      val deposited:Boolean= updateAccountBalance(deposit.accountNum, deposit.amount)
      if(!deposited) log.info(s"amount was not deposited to ${deposit.accountNum}")

    case billers: BillersRequest =>
      sender() ! getBillersByAccountnum(billers.accountNum).getOrElse(Nil)

    case (accountnum: Long, biller: Biller) =>
      sender() ! payBiller(accountnum, biller)
  }
}

object DatabaseRepoActor {

  trait Response

  case class Created(accountnum: Long, created: Boolean)

  case class RequestAccountInfo(accountnum: Long, accountNum: Long)

  case class UserNotFound(accountnum: Long) extends Response

  case class RespondAccountInfo(accountnum: Long, account: Account) extends Response

  case class SuccessfulLink(accountnum: Long, success: Boolean) extends Response

  def props(): Props = Props(new DatabaseRepoActor())
}

