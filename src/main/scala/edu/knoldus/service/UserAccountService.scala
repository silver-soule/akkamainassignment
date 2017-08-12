package edu.knoldus.service

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import edu.knoldus.models.Biller
import edu.knoldus.service.DatabaseRepoActor.{Created, SuccessfulLink}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Neelaksh on 5/8/17.
  */
class UserAccountService {

  def createAccounts(accounts: List[List[String]], accountGeneratorRef: ActorRef): Future[List[Created]] = {
    implicit val timeout = Timeout(1000 seconds)

    val createdAccounts =
      for {
        account <- accounts
        accountnumToBool = (accountGeneratorRef ? account).mapTo[Created]
      } yield accountnumToBool
    Future.sequence(createdAccounts)
  }

  def linkAccount(accountnum: Long, biller:Biller, accountBillerLinker: ActorRef): Future[SuccessfulLink] = {
    implicit val timeout = Timeout(10 seconds)
    (accountBillerLinker ? (accountnum, biller)).mapTo[SuccessfulLink]
  }
}
