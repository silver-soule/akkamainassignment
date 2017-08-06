package edu.knoldus

/**
  * Created by Neelaksh on 6/8/17.
  */
case class Account(accountNumber:Long,accountHolderName:String,address:String,userName:String,initialAmount:Long){
  def updateBalance(balance:Int):Account = Account(this.accountNumber,this.accountHolderName,this.address,this.userName,balance + this.initialAmount)
}
object Account{
  def apply(accountNum:Long,inp:List[String]): Account = new Account(accountNum,inp(0), inp(1), inp(2), inp(3).toLong)
}
case class UniqueAccount(account:Account)

case class Link(acc:Account,billers:List[Biller])

case class Biller()

case class Deposit(name:String,accountNum:Long)