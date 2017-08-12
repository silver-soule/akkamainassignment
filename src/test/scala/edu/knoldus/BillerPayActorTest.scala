package edu.knoldus

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestActor, TestKit, TestProbe}
import edu.knoldus.models.Biller
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FunSuiteLike}

/**
  * Created by Neelaksh on 9/8/17.
  */
class BillerPayActorTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike
  with BeforeAndAfterAll with ImplicitSender  with BeforeAndAfter {
  val databaseRepo = TestProbe()
  val payBiller: ActorRef = system.actorOf(BillerPayActor.props(databaseRepo.ref,"phone"))
  val biller = Biller("phone", "panda", 1L, "food", 22L, 1, 1, 0)
  val updatedBiller = biller.updateBiller()

  test("paying biller") {

    databaseRepo.setAutoPilot((sender: ActorRef, msg: Any) => {
      val resturnMsg = msg match {
        case (1L, this.updatedBiller) => true
        case _ => false
      }
      sender ! resturnMsg
      TestActor.NoAutoPilot
    })
    payBiller ! (1L, this.biller)
    expectMsg(true)
  }

/*  test("paying biller failed") {
    val biller2 = Biller("invalid", "panda", 1L, "invalid", 22L, 1, 1, 0)
    payBiller ! (1L,biller2)
    expectMsg(false)
  }*/
}
