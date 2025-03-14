package actors

import actors.DatasetsActor.{ProcessRequest, ResponseBody}
import models.Datasets
import org.apache.pekko.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import org.apache.pekko.actor.typed.{ActorRef, Behavior}
import play.api.libs.json.{JsObject, Json, Writes}
import services.DatasetsService

import scala.jdk.CollectionConverters.*



object DatasetsActor {

  sealed trait Command
  final case class ProcessRequest(operation: String, request: JsObject, replyTo: ActorRef[ResponseBody]) extends Command
  final case class ResponseBody(responseBody: JsObject)

  def apply(datasetsService: DatasetsService): Behavior[Command] = {
    Behaviors.setup(context => new DatasetsActor(context, datasetsService))
  }
}

class DatasetsActor(context: ActorContext[DatasetsActor.Command], datasetsService: DatasetsService) extends AbstractBehavior[DatasetsActor.Command](context) {

        override def onMessage(msg: DatasetsActor.Command): Behavior[DatasetsActor.Command] = {
          msg match {
            case ProcessRequest(operation, request, replyTo) =>
              val responseBody: JsObject = operation match {
                case "READ_OPERATION" => getAllDatasets
                case "READ_BY_ID" => getDatasetById((request \ "id").as[String])
                case "CREATE_OPERATION" => create(request)
                case "UPDATE_OPERATION" => update(request)
                case "DELETE_OPERATION" => delete((request \ "id").as[String])
              }
              replyTo.tell(ResponseBody(responseBody))
              this
          }
        }

  def getAllDatasets: JsObject = {
    val responseFromservice = datasetsService.getAllDatasets()
    val finalResponse= responseFromservice.asScala.toMap
    Json.toJson(finalResponse).as[JsObject]
  }

  def getDatasetById(id: String): JsObject = {
    val responseFromservice = datasetsService.getDatasetById(id)
    Json.toJson(responseFromservice).as[JsObject]
  }

  def create(request: JsObject): JsObject = {
    val responseFromservice = datasetsService.createDataset(request.toString())
    Json.toJson(responseFromservice).as[JsObject]
  }

  def update(request: JsObject): JsObject = {
    val responseFromservice = datasetsService.updateDataset(request.toString())
    Json.toJson(responseFromservice).as[JsObject]
  }

  def delete(id: String): JsObject = {
    val responseFromservice = datasetsService.deleteDataset(id)
    Json.toJson(responseFromservice).as[JsObject]
  }


  implicit val mapWrites: Writes[Map[String, Object]] = new Writes[Map[String, Object]] {
    def writes(map: Map[String, Object]) = Json.obj(
      map.map { case (key, value) =>
        key -> Json.toJsFieldJsValueWrapper(value.toString)
      }.toSeq: _*
    )
  }

  implicit val javaMapWrites: Writes[java.util.Map[String, Object]] = new Writes[java.util.Map[String, Object]] {
    def writes(map: java.util.Map[String, Object]) = Json.obj(
      map.asScala.map { case (key, value) =>
        key -> Json.toJsFieldJsValueWrapper(value.toString)
      }.toSeq: _*
    )
  }
}