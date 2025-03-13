
package controllers

import actors.DatasetsActor
import actors.DatasetsActor.{ProcessRequest, ResponseBody}
import org.apache.pekko.actor.typed.{ActorRef, ActorSystem}
import org.apache.pekko.util.Timeout
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Result}
import org.apache.pekko.actor.typed.Scheduler
import org.apache.pekko.actor.typed.scaladsl.AskPattern.Askable
import play.api.libs.json.{JsObject, JsValue, Json}
import services.DatasetsService

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.DurationInt

class DatasetsController @Inject()(cc: ControllerComponents, datasetService: DatasetsService, actorSystem: ActorSystem[Void])(implicit ec: ExecutionContext) extends AbstractController(cc) {

  private implicit val timeout: Timeout = 5.seconds
  private implicit val scheduler: Scheduler = actorSystem.scheduler

  private val datasetActor: ActorRef[DatasetsActor.Command] = {
    actorSystem.systemActorOf(DatasetsActor(datasetService), "dataset-actor")
  }

  private def process(operation: String, request: String = "Null"): Future[Result] =
    datasetActor.ask(replyTo => ProcessRequest(operation, request, replyTo)).map {
      case ResponseBody(responseBody) =>
        val statusCode: Int = 200
        Status(statusCode)(Json.toJson(responseBody))
    }.recover {
      case ex => InternalServerError(Json.obj("error" -> s"Error: ${ex.getMessage}"))
    }

  def getAllDatasets: Action[AnyContent] = Action.async {
    process(Operation.Read)
  }

  def getDatasetById(id: String): Action[AnyContent] = Action.async {
    val request = id
    process(Operation.ReadById, request)
  }

  def createDataset: Action[JsValue] = Action.async(parse.json) { request =>
    val dataset: JsObject = request.body.as[JsObject]
    val requestbody = dataset.toString
    process(Operation.Create, requestbody)
  }

  def updateDataset: Action[JsValue] = Action.async(parse.json) { request =>
    val updateDataset: JsObject = request.body.as[JsObject]
    val requestbody = updateDataset.toString
    process(Operation.Update, requestbody)
  }

  def deleteDatasetById(id: String): Action[AnyContent] = Action.async {
    val request = id
    process(Operation.DeleteById, request)
  }
}

