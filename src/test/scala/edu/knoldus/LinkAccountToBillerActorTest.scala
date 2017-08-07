package edu.knoldus

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestActor, TestKit, TestProbe}
import edu.knoldus.LinkAccountToBillerActor.Link
import edu.knoldus.models.Biller
import edu.knoldus.service.DatabaseRepoActor.SuccessfulLink
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, FunSuite, FunSuiteLike}

/**
  * Created by Neelaksh on 8/8/17.
  */
class LinkAccountToBillerActorTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike
with BeforeAndAfterAll with ImplicitSender with MockitoSugar {

  val databaseRepo = TestProbe()
  val linkAccountToBiller: ActorRef = system.actorOf(LinkAccountToBillerActor.props(databaseRepo.ref))
  val biller = Biller("food","panda",1L,"food",22L,1,1,0)
  test("Testing LinkBillerToAccountActor and linking an account with a biller")
  {
    linkAccountToBiller ! (1L, biller)

    databaseRepo.setAutoPilot((sender: ActorRef, msg: Any) => {
      val resturnMsg = msg match {
        case Link(_: Long, _: Biller) =>
          SuccessfulLink(true)
        case _=>println("ppotsto")
      }
      sender ! resturnMsg
      TestActor.KeepRunning
    })
    expectMsgPF(){
      case _ @ SuccessfulLink(true)=> true
    }
  }


}
