package edu.knoldus

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestActor, TestKit, TestProbe}
import edu.knoldus.LinkAccountToBillerActor.Link
import edu.knoldus.SalaryDepositActor.{BillersRequest, Deposit}
import edu.knoldus.models.Biller
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncFunSuiteLike, BeforeAndAfter, BeforeAndAfterAll, FunSuiteLike}

import scala.concurrent.Await

/**
  * Created by Neelaksh on 17/8/17.
  */
class SalaryDepositActorTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike
  with BeforeAndAfterAll with MockitoSugar with BeforeAndAfter {

  val databaseRepoProbe = TestProbe()
  val billerManagerProbe = TestProbe()
  val biller = Biller("phone", "panda", 10L, "food", 9L, 1, 1, 0L)
  test("check if valid salary"){
    val salaryDepositActor: ActorRef = system.actorOf(SalaryDepositActor.props(databaseRepoProbe.ref,billerManagerProbe.ref))
    salaryDepositActor ! ("Neelaksh",1L,100L)

    databaseRepoProbe.setAutoPilot((sender: ActorRef, msg: Any) => {
      val resturnMsg = msg match {
        case  BillersRequest(1L) =>
          sender ! List(biller)
        case _=>sender ! true
      }
      sender ! resturnMsg
      TestActor.NoAutoPilot
    })

    databaseRepoProbe.expectMsg(Deposit("Neelaksh",1L,100L))
    //billerManagerProbe.expectMsg((1L,biller))

  }
}
