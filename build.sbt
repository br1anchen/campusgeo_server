name := "bowler-quickstart"

version := "1.0-SNAPSHOT"

organization := "bowler"

scalaVersion := "2.9.1"

seq(webSettings :_*)

resolvers ++= Seq("Sonatype Nexus releases" at "https://oss.sonatype.org/content/repositories/releases", 
	"Sonatype Nexus snapshots" at "https://oss.sonatype.org/content/repositories/snapshots", "Scala-Tools repo" at "http://scala-tools.org/repo-releases/")

libraryDependencies ++= Seq(
	"org.bowlerframework" %% "core" % "0.5.1",
	"org.slf4j" % "slf4j-nop" % "1.6.0" % "runtime",
	"javax.servlet" % "servlet-api" % "2.5" % "provided",
	"org.eclipse.jetty" % "jetty-webapp" % "7.4.1.v20110513" % "container",
	"org.eclipse.jetty" % "jetty-webapp" % "7.4.1.v20110513",
	"org.scalatra" %% "scalatra-scalatest" % "2.1.0-SNAPSHOT" % "test",
	"org.specs2" % "specs2_2.9.0" % "1.3" % "test",
	"net.liftweb" %% "lift-json" % "2.3" % "test",
	"net.java.dev.jets3t" % "jets3t" % "0.8.1",
	"org.scala-tools.time" %% "time" % "0.5",
  	"org.bowlerframework" %% "squeryl-mapper" % "0.5.1",
  	"com.h2database" % "h2" % "1.2.144", 
  	"c3p0" % "c3p0" % "0.9.1.2",
  	"org.squeryl" %% "squeryl" % "0.9.5-RC1",
  	"postgresql" % "postgresql" % "8.4-701.jdbc4",
  	"org.imgscalr" % "imgscalr-lib" % "4.1",
  	"net.databinder" %% "dispatch-http" % "0.8.7"
	)
