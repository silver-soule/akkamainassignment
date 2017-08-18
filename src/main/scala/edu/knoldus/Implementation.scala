package edu.knoldus

import akka.actor.ActorSystem
import edu.knoldus.models.Biller
import edu.knoldus.service.{DatabaseRepoActor, SalaryDepositService, UserAccountService}
import scala.concurrent.duration._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by Neelaksh on 17/8/17.
  */
object Implementation extends App{
  val person1 = List("Neelaksh","c-138","silversoul","100")
  val person2 = List("Suryansh","c-138","phantomstrike","100")
  val actorSystem = ActorSystem("AccountSystemActor")
  val database = new Database
  val databaseServiceActor = actorSystem.actorOf(DatabaseRepoActor.props(database),name = "databaseServiceActor")
  val accountGeneratorActor = actorSystem.actorOf(UserAccountGenerator.props(databaseServiceActor))
  val linkBillerToAccountActor = actorSystem.actorOf(LinkAccountToBillerActor.props(databaseServiceActor))
  val userAccountService = new UserAccountService
  val abc = userAccountService.createAccounts(List(person1,person2),accountGeneratorActor)
  val link =   userAccountService.linkAccount(1L,Biller("food","lays",1000,"chips",10,0,0,1000),linkBillerToAccountActor)
  val billerAccountManager = actorSystem.actorOf(BillerManagerActor.props(databaseServiceActor))
  val salaryDepositorActor = actorSystem.actorOf(SalaryDepositActor.props(databaseServiceActor,billerAccountManager),name = "managerActor")
  val salaryDepositService = new SalaryDepositService(salaryDepositorActor)
  val deposit = salaryDepositService.depositSalary("neelaksh",1,100)
  val deposit2 = salaryDepositService.depositSalary("suryansh",2,100)
  abc.map(println(_))
  link.map(println(_))
  val d = deposit.map(dep=>println("POTATO" + dep))
/*
  Await.ready(d, 10 seconds)
*/



}
