
name := "econ3"

version := "0.1"

scalaVersion := "2.12.10"


lazy val commonSettings = Seq(
  version := "0.1",
  organization := "danch",
  scalaVersion := "2.12.10",
//  javacOptions ++= Seq("-source", "1.11", "-target", "1.11"),
//  scalacOptions += "-target:10",
  resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
  test in assembly := {}
)

lazy val model = (project in file("model"))
   .settings(commonSettings)
lazy val core = (project in file("core"))
   .settings(commonSettings)
   .enablePlugins(JavaAppPackaging)
   .dependsOn(model)


enablePlugins(JavaAppPackaging)