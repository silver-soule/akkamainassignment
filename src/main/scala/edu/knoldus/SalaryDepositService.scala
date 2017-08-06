package edu.knoldus

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout

import akka.pattern.ask
import scala.concurrent.duration.DurationInt
/**
  * Created by Neelaksh on 6/8/17.
  */
class SalaryDepositService(salaryDepositActor:ActorRef) {
  def depositSalary(name:String,accountNumber:Long):Unit = {
    implicit val timeout = Timeout(1000 seconds)

    salaryDepositActor ? Deposit(name,accountNumber)
  }
}
