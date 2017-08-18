package edu.knoldus

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestActor, TestKit}
import edu.knoldus.BillerPayActor.PayBiller
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FunSuiteLike}

/**
  * Created by Neelaksh on 12/8/17.
  */
class BillerManagerActorTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike
  with BeforeAndAfterAll with ImplicitSender with MockitoSugar with BeforeAndAfter {


  test("test pay biller in food category") {



}}
