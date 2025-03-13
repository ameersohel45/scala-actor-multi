//package actorsmessages
//
//import org.apache.pekko.actor.typed.ActorRef
//
//
//sealed trait Command
//
//final case class GetAllDatasetsCommand(replyTo: ActorRef[Response]) extends Command
//final case class GetDatasetByIdCommand(id: String, replyTo: ActorRef[Response]) extends Command
//final case class CreateDatasetCommand(jsonData: String, replyTo: ActorRef[Response]) extends Command
//final case class UpdateDatasetCommand(id: String, jsonData: String, replyTo: ActorRef[Response]) extends Command
//final case class DeleteDatasetCommand(id: String, replyTo: ActorRef[Response]) extends Command
//
//sealed trait Response
//
//final case class DatasetResponse(response: Map[String, Any]) extends Response
//
//
