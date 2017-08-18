package edu.knoldus

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestActor, TestKit, TestProbe}
import edu.knoldus.LinkAccountToBillerActor.{InvalidMessage, Link}
import edu.knoldus.models.Biller
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}

/**
  * Created by Neelaksh on 8/8/17.
  */
class LinkAccountToBillerActorTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike
   with BeforeAndAfterAll with ImplicitSender  {

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  val databaseRepo1 = TestProbe()
  val linkAccountToBiller: ActorRef = system.actorOf(LinkAccountToBillerActor.props(databaseRepo1.ref))
  val biller = Biller("food", "panda", 1L, "food", 22L, 1, 1, 0)
  test("Testing LinkBillerToAccountActor and linking an account with a biller") {
    linkAccountToBiller ! (1L, biller)
    databaseRepo1.expectMsg(Link(1L,this.biller))
  }

  test("Testing linking account to biller false") {
    linkAccountToBiller ! 1L
    expectMsg(InvalidMessage)
  }


}
