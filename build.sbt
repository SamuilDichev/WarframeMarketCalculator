lazy val root = project.in(file(".")).enablePlugins(PlayJava, LauncherJarPlugin)

name := "AutomarkingApp"

version := "1.0"

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.datatype" % "jackson-datatype-joda" % "2.8.6",
  "junit" % "junit" % "4.12",
  "com.mashape.unirest" % "unirest-java" % "1.4.9",
  "org.mongodb.morphia" % "morphia" % "1.3.1",
  "org.jongo" % "jongo" % "1.3.0",
  "org.jsoup" % "jsoup" % "1.10.3"
)