import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "mis_nsu"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      javaCore, javaJdbc, javaEbean, jdbc,
      "pdf" % "pdf_2.10" % "0.4.1",
      "com.typesafe.akka" % "akka-actor" % "2.0.2" ,
      "com.typesafe.akka" % "akka-remote" % "2.0.2" ,
      "com.typesafe.akka" % "akka-kernel" % "2.0.2",
      "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
      "commons-io" % "commons-io" % "2.3"
      // ,"org.xhtmlrenderer" % "core-renderer" % "R8",
      // "net.sf.jtidy" % "jtidy" % "r938"
      // "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
      resolvers += Resolver.url("Violas Play Modules", url("http://www.joergviola.de/releases/"))(Resolver.ivyStylePatterns)
    )

}
