package edu.knoldus


import edu.knoldus.models.{Account, Biller}

import scala.collection.mutable

/**
  * Created by Neelaksh on 6/8/17.
  */
class Database {

  var accountNumToAccount: mutable.Map[Long, Account] = mutable.Map()
  var userNames: mutable.Set[String] = mutable.Set()
  var accountNumToBiller: mutable.Map[Long, List[Biller]] = mutable.Map()

  def addAccount(account: Account): Boolean = {
    if (!userNames.contains(account.userName)) {
      accountNumToAccount += (account.accountNumber -> account)
      userNames += account.userName
      true
    }
    else {
      false
    }
  }


  def updateAccountBalance(accountNum: Long, balance: Long): Boolean = {
    accountNumToAccount.get(accountNum).fold(false) { acc =>
      accountNumToAccount += (accountNum -> acc.updateBalance(balance))
      true
    }
  }

  def getBillersByAccountnum(accountnum: Long): List[Biller] = {
    accountNumToBiller.get(accountnum).fold(List[Biller]()){billers=>billers}
  }

  def getAccountByAccountnum(accountnum: Long): Option[Account] = {
    accountNumToAccount.get(accountnum)
  }

  def addBillerToAccount(accountnum: Long, biller: Biller): Boolean = {
    accountNumToAccount.get(accountnum).fold(false) { _ =>
      accountNumToBiller.get(accountnum)
        .fold {
          accountNumToBiller += (accountnum -> List(biller))
        } {
          currentBillers =>
            val allBillers = biller :: currentBillers
            accountNumToBiller += (accountnum -> allBillers)
        }
      true
    }
  }

  def payBiller(accountnum: Long, biller: Biller): Boolean = {
    val invalidBalance = -1L
    val balance = accountNumToAccount.get(accountnum).fold(invalidBalance){ account=>account.initialAmount}
    if (balance > biller.amount) {
      accountNumToBiller.get(accountnum).fold(false) {
        billers =>
          accountNumToBiller += (accountnum -> (biller :: billers.filter(_.billerCategory != biller.billerCategory)))
          true
      }
    }
    else {
      false
    }
  }
}
