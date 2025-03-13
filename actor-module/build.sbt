name :="actor-module"
scalaVersion := "3.3.1"

libraryDependencies += "org.apache.pekko" %% "pekko-actor-typed" % "1.1.3"
libraryDependencies ++= Seq(
  "org.apache.pekko" %% "pekko-actor-typed"          % "1.1.3",
  "org.apache.pekko" %% "pekko-actor"                % "1.1.3",
  "org.apache.pekko" %% "pekko-slf4j"                % "1.1.3",
  "org.apache.pekko" %% "pekko-stream"               % "1.1.3",
  "org.apache.pekko" %% "pekko-protobuf-v3"          % "1.1.3",
  "org.apache.pekko" %% "pekko-serialization-jackson" % "1.1.3"
)

libraryDependencies ++= Seq(
  "org.playframework" %% "play" % "3.0.0" // Use the appropriate Play version
)