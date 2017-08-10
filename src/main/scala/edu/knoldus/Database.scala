package edu.knoldus


import edu.knoldus.models.{Account, Biller}

import scala.collection.mutable

/**
  * Created by Neelaksh on 6/8/17.
  */
class Database {

  var accountnumToAccount: mutable.Map[Long, Account] = mutable.Map()
  var usernames: mutable.Set[String] = mutable.Set()
  var accountnumToBiller: mutable.Map[Long, List[Biller]] = mutable.Map()

  def addAccount(account: Account): Boolean = {
    if (!usernames.contains(account.userName)) {
      accountnumToAccount += (account.accountNumber -> account)
      usernames += account.userName
      true
    }
    else {
      false
    }
  }


  def updateAccountBalance(accountNum: Long, balance: Long): Boolean = {
    accountnumToAccount.get(accountNum).fold(false) { acc =>
      accountnumToAccount += (accountNum -> acc.updateBalance(balance))
      true
    }
  }

  def getBillersByAccountnum(accountnum: Long): Option[List[Biller]] = {
    accountnumToBiller.get(accountnum)
  }

  def getAccountByAccountnum(accountnum: Long): Option[Account] = {
    accountnumToAccount.get(accountnum)
  }

  def addBillerToAccount(accountnum: Long, biller: Biller): Boolean = {
    accountnumToBiller.get(accountnum)
      .fold {
        accountnumToBiller += (accountnum -> List(biller))
      } {
        currentBillers =>
          val allBillers = biller :: currentBillers
          accountnumToBiller += (accountnum -> allBillers)
      }
    true
  }

  def payBiller(accountnum: Long, biller: Biller): Boolean = {
    val invalidBalance = -1L
    val balance = accountnumToAccount.get(accountnum).fold(invalidBalance){account=>account.initialAmount}
    if (balance > biller.amount) {
      accountnumToBiller.get(accountnum).fold(false) {
        billers =>
          accountnumToBiller += (accountnum -> (biller :: billers.filter(_.billerCategory != biller.billerCategory)))
          true
      }
    }
    else {
      false
    }
  }
}
