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
class DatabaseRepoActor(db: Database) extends Actor with ActorLogging {
  override def receive: Receive = {
    case uniqueAccount: AccountCreate =>
      sender() ! Created(uniqueAccount.account.accountNumber, db.addAccount(uniqueAccount.account))

    case info: RequestAccountInfo =>
      sender() ! db.getAccountByAccountnum(info.accountNum).map {
        acc => RespondAccountInfo(info.accountNum, acc)
      }.getOrElse(UserNotFound(info.accountNum))

    case info: Link =>
      log.info(s"sending link request for ${info.accountnum}")
      sender() ! SuccessfulLink(info.accountnum, db.addBillerToAccount(info.accountnum, info.biller))

    case deposit: Deposit =>
      log.info(s"trying to deposit money to ${deposit.accountNum}")
      if (db.updateAccountBalance(deposit.accountNum, deposit.amount)) {
        log.info(s"money added to ${deposit.accountNum}")
        sender() ! SuccessfulDeposit(deposit.accountNum, true)
      }
      else {
        log.info(s"amount was not deposited to ${deposit.accountNum}")
        sender() ! NoDeposit
      }

    case biller: BillersRequest =>
      sender() ! db.getBillersByAccountnum(biller.accountNum).getOrElse(Nil)

    case (accountnum: Long, biller: Biller) =>
      sender() ! db.payBiller(accountnum, biller)
  }
}

object DatabaseRepoActor {

  trait Response

  case class Created(accountnum: Long, created: Boolean)

  case class RequestAccountInfo(accountNum: Long)

  case object NoDeposit

  case class UserNotFound(accountnum: Long) extends Response

  case class RespondAccountInfo(accountnum: Long, account: Account) extends Response

  case class SuccessfulLink(accountnum: Long, success: Boolean) extends Response

  case class SuccessfulDeposit(accountnum: Long, success: Boolean)

  def props(db: Database): Props = Props(classOf[DatabaseRepoActor], db)
}

