//package modules
//
//import actors.ActorSystemProviders
//import actorsmessages.Command
//import com.google.inject.{AbstractModule, Provides}
//import org.apache.pekko.actor.typed.{ActorRef, ActorSystem}
//
//import javax.inject.{Named, Singleton}
//
//class ActorModule extends AbstractModule {
//  override def configure(): Unit = {
//    bind(classOf[ActorSystemProviders]).asEagerSingleton()
//  }
//
//  @Provides
//  @Singleton
//  def provideActorSystem(provider: ActorSystemProviders): ActorSystem[Nothing] =
//    provider.actorSystem
//
//  @Provides
//  @Singleton
//  @Named("datasets-actor")
//  def provideDatasetsActor(provider: ActorSystemProviders): ActorRef[Command] =
//    provider.datasetsActor
//}
