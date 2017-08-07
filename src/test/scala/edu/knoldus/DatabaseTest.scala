package edu.knoldus

import edu.knoldus.models.Account
import org.scalatest.FunSuite

/**
  * Created by Neelaksh on 6/8/17.
  */
class DatabaseTest extends FunSuite {

  val db = new {} with Database{}
  val accountnum1 = 123
  val accountnum2 = 456
  val initamount1 = 1000
  val initamount2 = 20000
  val account1 = Account(1L,"Neelaksh","c-123","silversoule",initamount1)
  val account2 = Account(2L,"Suryansh","b-213","potato",initamount2)
  val account3 = Account(3L,"Suryansh","b-213","po",initamount2)
  db.addAccount(account1)


  test("testUpdateAccountBalance") {
    db.updateAccountBalance(1,initamount1)
    assert(db.getAccountByAccountnum(1L).get.initialAmount == initamount1*2)
  }

  test("testGetAccount") {
    db.addAccount(account2)
    assert(db.getAccountByAccountnum(2L).get == account2)
  }

  test("testAddAccount") {
    assert(!db.addAccount(account2))
    assert(db.addAccount(account3))
  }

}
