package edu.knoldus


import scala.collection.mutable

/**
  * Created by Neelaksh on 6/8/17.
  */
trait Database {
  var usernameToAccount:mutable.Map[String,Account] = mutable.Map()
  var accountnumToUsername : mutable.Map[Long,String] = mutable.Map()
  var accountnumToBiller:mutable.Map[Long,Biller] = mutable.Map()

  def addAccount(account:Account):Boolean = {
    usernameToAccount.get(account.userName).fold{
        true
    }(_=>false)
/*      accountnumToUsername.get(account.accountNumber).fold{
      true
    }(_=>false)*/ match{
      case true =>
        accountnumToUsername +=(account.accountNumber->account.userName)
        usernameToAccount+=(account.userName->account)
        true
      case false => false
    }
  }

  def updateAccountBalance(name:String,balance:Int):Boolean = {
    usernameToAccount.get(name).fold(false)
    {acc=>usernameToAccount+=(name->acc.updateBalance(balance))
      true}
  }

  def getAccount(username:String):Option[Account] = {
    usernameToAccount.get(username)
  }
}
