lazy val root = (project in file("."))
  .aggregate(actorModule, serviceModule,commonmodule)

lazy val actorModule = (project in file("actor-module")).dependsOn(commonmodule)

lazy val serviceModule = (project in file("service-module")).enablePlugins(PlayJava).dependsOn(actorModule,commonmodule)

scalaVersion := "3.3.1"

lazy val commonmodule = (project in file("commonmodule"))

libraryDependencies ++= Seq(
  "org.apache.pekko" %% "pekko-actor-typed"          % "1.1.3",
  "org.apache.pekko" %% "pekko-actor"                % "1.1.3",
  "org.apache.pekko" %% "pekko-slf4j"                % "1.1.3",
  "org.apache.pekko" %% "pekko-stream"               % "1.1.3",
  "org.apache.pekko" %% "pekko-protobuf-v3"          % "1.1.3",
  "org.apache.pekko" %% "pekko-serialization-jackson" % "1.1.3"
)