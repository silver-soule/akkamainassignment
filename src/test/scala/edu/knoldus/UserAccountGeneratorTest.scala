package edu.knoldus

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestActor, TestKit, TestProbe}
import edu.knoldus.UserAccountGenerator.AccountCreate
import edu.knoldus.service.DatabaseRepoActor
import edu.knoldus.service.DatabaseRepoActor.Created
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}

/**
  * Created by Neelaksh on 6/8/17.
  */

//unit test

class UserAccountGeneratorTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike
  with BeforeAndAfterAll with ImplicitSender {
  override protected def afterAll(): Unit = {
    system.terminate()
  }

  val databaseRepo = TestProbe()
  val userAccountGenerator = system.actorOf(UserAccountGenerator.props(databaseRepo.ref))

  test("check if account is generated ") {
    val userInfo = List("neelaksh", "c-123", "silver", "12345")

    databaseRepo.setAutoPilot((sender: ActorRef, msg: Any) => {
      val resturnMsg = msg match {
        case _: AccountCreate => Created(1, true)
      }
      sender ! resturnMsg
      TestActor.NoAutoPilot
    })
    userAccountGenerator ! userInfo
    expectMsgPF() {
      case Created(1, true) => true
    }
  }


  test("check if account not made") {
    //val databaseRepo2 = TestProbe()
    //val userAccountGenerator2 = system.actorOf(UserAccountGenerator.props(databaseRepo2.ref))
    databaseRepo.setAutoPilot((sender: ActorRef, msg: Any) => {
      val resturnMsg = msg match {
        case _: AccountCreate => Created(2, false)
      }
      sender ! resturnMsg
      TestActor.NoAutoPilot
    })
    val user2 = List("potato", "c-123", "silver", "12345")
    userAccountGenerator ! user2
    expectMsgPF() {
      case Created(2, false) => true
    }
  }
}