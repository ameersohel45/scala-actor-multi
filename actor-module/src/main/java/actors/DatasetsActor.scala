package actors

import actors.DatasetsActor.{ProcessRequest, ResponseBody}
import models.Datasets
import org.apache.pekko.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import org.apache.pekko.actor.typed.{ActorRef, Behavior}
import play.api.libs.json.JsObject
import services.DatasetsService
import scala.jdk.CollectionConverters.*



object DatasetsActor {

  sealed trait Command
  final case class ProcessRequest(operation: String, request: String, replyTo: ActorRef[ResponseBody]) extends Command
  final case class ResponseBody(responseBody: String)

  def apply(datasetsService: DatasetsService): Behavior[Command] = {
    Behaviors.setup(context => new DatasetsActor(context, datasetsService))
  }
}

class DatasetsActor(context: ActorContext[DatasetsActor.Command], datasetsService: DatasetsService)
  extends AbstractBehavior[DatasetsActor.Command](context) {

  override def onMessage(msg: DatasetsActor.Command): Behavior[DatasetsActor.Command] = {
    msg match {
      case ProcessRequest(operation, request, replyTo) =>
        operation match {
          case "READ_OPERATION" =>
            val responseBody: String = getAllDatasets
            replyTo.tell(ResponseBody(responseBody))
            this

          case "READ_BY_ID" =>
            val responseBody: String = getDatasetById(request)
            replyTo.tell(ResponseBody(responseBody))
            this

          case "CREATE_OPERATION" =>
            val responseBody: String = create(request)
            replyTo.tell(ResponseBody(responseBody))
            this

          case "UPDATE_OPERATION" =>
            val responseBody: String = update(request)
            replyTo.tell(ResponseBody(responseBody))
            this

          case "DELETE_OPERATION" =>
            val responseBody: String = delete(request)
            replyTo.tell(ResponseBody(responseBody))
            this
        }
    }
  }

  def getAllDatasets: String = {
    val javaMap = datasetsService.getAllDatasets()
    val scalaMap = javaMap.asScala.toMap
    scalaMap.toString()
  }

  def getDatasetById(request: String): String = {
      val dataset = datasetsService.getDatasetById(request)
      dataset.toString()
    }

  def create(request: String): String = {
      val javaMap=datasetsService.createDataset(request)
      val scalaMap = javaMap.asScala.toMap
      scalaMap.toString()
    }

  def update(request: String): String = {
      val javaMap=datasetsService.updateDataset(request)
      val scalaMap = javaMap.asScala.toMap
      scalaMap.toString()
    }

  def delete(request: String): String = {
      val javaMap = datasetsService.deleteDataset(request)
      val scalaMap = javaMap.asScala.toMap
      scalaMap.toString()
  }
}

