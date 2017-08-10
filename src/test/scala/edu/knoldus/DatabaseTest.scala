package edu.knoldus

import edu.knoldus.models.{Account, Biller}
import org.scalatest.FunSuite

/**
  * Created by Neelaksh on 6/8/17.
  */
class DatabaseTest extends FunSuite {

  val db = new {} with Database {}
  val accountnum1 = 123
  val accountnum2 = 456
  val initamount1 = 1000
  val initamount2 = 20000
  val account1 = Account(1L, "Neelaksh", "c-123", "silversoule", initamount1)
  val account2 = Account(2L, "Suryansh", "b-213", "potato", initamount2)
  val account3 = Account(3L, "Suryansh", "b-213", "po", initamount2)
  val poor = Account(4L, "Suryansh", "b-213", "pot", 0)
  val invalidUser = Account(5L, "Suryansh", "b-213", "pot", 0)
  db.addAccount(account1)
  val biller = Biller("food", "panda", 1L, "food", 22L, 1, 1, 0)


  test("testUpdateAccountBalance") {
    assert(db.updateAccountBalance(1, initamount1))
    //assert(db.getAccountByAccountnum(1L).get.initialAmount == initamount1 * 2)
    assert(!db.updateAccountBalance(6L,1000L))
  }

  test("testGetAccount") {
    db.addAccount(account2)
    assert(db.getAccountByAccountnum(2L).get == account2)
  }

  test("testAddAccount") {
    assert(!db.addAccount(account2))
    assert(db.addAccount(account3))
  }

  test("testAddBiller") {
    assert(db.addBillerToAccount(1,biller))
  }

  test("testPayBIller") {
    db.addAccount(poor)
    assert(db.payBiller(account1.accountNumber,biller))
    //assert(!db.payBiller(poor.accountNumber,biller))
    assert(!db.payBiller(invalidUser.accountNumber,biller))
    assert(!db.payBiller(account2.accountNumber,biller))
  }

  test("testGetBillersByAccountnum") {
    assert(db.getBillersByAccountnum(2L).isEmpty)
  }

}
