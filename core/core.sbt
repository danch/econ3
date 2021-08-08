name := "core"

val AkkaVersion = "2.6.15"
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-cluster-typed" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-cluster-tools" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-persistence-typed" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-persistence-testkit" % AkkaVersion % Test
// libraryDependencies += "org.danch" % "math" % "0.9.6-SNAPSHOT"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test
//libraryDependencies += "org.apache.commons" % "commons-math3" % "3.6.1"
// https://mvnrepository.com/artifact/org.apache.kafka/kafka
//libraryDependencies += "org.apache.kafka" % "kafka-clients" % "1.1.0"
//libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.6"
//libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.6"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"
//libraryDependencies += "org.jocl" % "jocl" % "2.0.2-SNAPSHOT"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.3"
