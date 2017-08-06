package edu.knoldus

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import edu.knoldus.DatabaseRepo.Created
import org.scalatest.{BeforeAndAfterAll, FunSuite, FunSuiteLike}

/**
  * Created by Neelaksh on 6/8/17.
  */
class UserAccountGeneratorTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike
  with BeforeAndAfterAll with ImplicitSender {
  override protected def afterAll(): Unit = {
    system.terminate()
  }
  val databaseRepo = system.actorOf(DatabaseRepo.props)
  val userAccountGenerator = system.actorOf(UserAccountGenerator.props(databaseRepo))

  test("check if account is generated "){
    userAccountGenerator ! (12L,List("neelaksh","c-123","silver","12345"))
    expectMsgPF(){
      case Created(12,true) => true
    }
  }

  test("check if account not made") {
    userAccountGenerator ! (1234L,List("potato","c-123","silver","12345"))
    expectMsgPF(){
      case _@ Created(1234,false) =>assert(true)
    }
  }


}
