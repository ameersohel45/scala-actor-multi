name := "service-module"
scalaVersion := "3.3.1"

libraryDependencies += guice

libraryDependencies ++= Seq(
  "org.hibernate" % "hibernate-core" % "6.6.1.Final",
  "jakarta.persistence" % "jakarta.persistence-api" % "3.1.0",
  "org.postgresql" % "postgresql" % "42.7.1",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.14.3"
)
libraryDependencies += "org.apache.pekko" %% "pekko-actor-typed" % "1.1.3"
