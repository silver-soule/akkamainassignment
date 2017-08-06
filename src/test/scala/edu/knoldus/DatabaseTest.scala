package edu.knoldus

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
  val account1 = Account(accountnum1,"Neelaksh","c-123","silversoule",initamount1)
  val account2 = Account(accountnum2,"Suryansh","b-213","potato",initamount2)
  db.addAccount(account1)


  test("testUpdateAccountBalance") {
    db.updateAccountBalance("silversoule",initamount1)
    assert(db.getAccount("silversoule").get.initialAmount == initamount1*2)
  }

  test("testGetAccount") {
    db.addAccount(account2)
    assert(db.getAccount("potato").get == account2)
  }

  test("testAddAccount") {

  }

}
