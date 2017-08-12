package edu.knoldus.service

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import edu.knoldus.LinkAccountToBillerActor.Link
import edu.knoldus.SalaryDepositActor.{BillersRequest, Deposit}
import edu.knoldus.UserAccountGenerator.AccountCreate
import edu.knoldus.models.{Account, Biller}
import edu.knoldus.service.DatabaseRepoActor._
import edu.knoldus.{Database}
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FunSuiteLike}

/**
  * Created by Neelaksh on 9/8/17.
  */
class DatabaseRepoActorTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike
  with BeforeAndAfterAll with ImplicitSender with MockitoSugar with BeforeAndAfter {
  override protected def afterAll(): Unit = {
    system.terminate()
  }

  val initamount1 = 1000
  val account1 = Account(1L, "Neelaksh", "c-123", "silversoule", initamount1)
  val biller = Biller("food", "panda", 1L, "food", 22L, 1, 1, 0)

  val database = mock[Database]
  val databaserepoActor = system.actorOf(DatabaseRepoActor.props(database))
  test("check add account req") {

    when(database.addAccount(account1)).thenReturn(true)
    databaserepoActor ! AccountCreate(account1)
    expectMsgPF() {
      case _: Created => true
    }
  }

  test("check fetch account") {
    val accountnum = 1L
    when(database.getAccountByAccountnum(accountnum)).thenReturn(Option(account1))
    databaserepoActor ! RequestAccountInfo(accountnum)
    expectMsgPF() {
      case _@ RespondAccountInfo(accountnum,account) => true
    }
  }

  test("check if data gets added to account"){
    val accountnum = 1L
    when(database.addBillerToAccount(accountnum,biller)).thenReturn(true)
    databaserepoActor ! Link(accountnum,biller)
    expectMsgPF() {
      case _@ SuccessfulLink(accountnum,true) => true
    }
  }

  test("check if money is being deposited"){
    val accountnum = 1L
    val name = "Neelaksh"
    val amount = 1000L
    when(database.updateAccountBalance(accountnum,amount)).thenReturn(true)
    databaserepoActor ! Deposit(name,accountnum,amount)
    expectMsgPF() {
      case _@ SuccessfulDeposit(accountnum,true) => true
    }
  }

  test("check if money is not being deposited"){
    val accountnum = 1L
    val name = "Neelaksh"
    val amount = 1000L
    when(database.updateAccountBalance(accountnum,amount)).thenReturn(false)
    databaserepoActor ! Deposit(name,accountnum,amount)
    expectMsgPF() {
      case _@ NoDeposit => true
    }
  }

  test("check if billers in account"){
    val accountnum = 1L
    val name = "Neelaksh"
    val amount = 1000L
    when(database.getBillersByAccountnum(accountnum)).thenReturn(Option(List(biller)))
    databaserepoActor ! BillersRequest(accountnum)
    expectMsgPF(){
      case _@List(biller)=>true
    }
  }

  test("check if billers not in account"){
    val accountnum = 1L
    val name = "Neelaksh"
    val amount = 1000L
    when(database.getBillersByAccountnum(accountnum)).thenReturn(Option(Nil))
    databaserepoActor ! BillersRequest(accountnum)
    expectMsgPF(){
      case _@Nil=>true
    }
  }

  test("check if billers paid"){
    val accountnum = 1L
    val name = "Neelaksh"
    val amount = 1000L
    when(database.payBiller(accountnum,biller)).thenReturn(true)
    databaserepoActor ! (accountnum,biller)
    expectMsgPF(){
      case _@true=>true
    }
  }


}
