import sbt.Keys.{libraryDependencies, organization, scalaVersion, version, _}
import play.sbt.PlaySettings

fork := true
Runtime / javaOptions ++= Seq(
  "--add-exports=java.base/java.lang=ALL-UNNAMED",
  "--add-opens=java.base/java.lang=ALL-UNNAMED"
)

Test / javaOptions ++= Seq(
  "--add-opens=java.base/java.lang=ALL-UNNAMED"
)

lazy val root = (project in file("."))
  .enablePlugins(PlayService, PlayLayoutPlugin, Common, PlayScala)
  .settings(
    name := """noteweb-paas-dsl-service""",
    organization := "com.noteinweb",
    scalaVersion := "2.13.8",
    version := "1.0-SNAPSHOT",
    javaOptions ++= Seq(
      "--add-exports=java.base/java.lang=ALL-UNNAMED",
      "--add-opens=java.base/java.lang=ALL-UNNAMED"
    ),
    fork := true,
    run / javaOptions += "--add-opens=java.base/java.lang=ALL-UNNAMED",
    libraryDependencies ++= Seq(
      guice,
      "org.joda" % "joda-convert" % "2.2.1",
      "net.logstash.logback" % "logstash-logback-encoder" % "6.2",
      "io.lemonlabs" %% "scala-uri" % "1.5.1",
      "net.codingwell" %% "scala-guice" % "4.2.6",
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
      "com.noteinweb" %% "noteweb-paas-dsl" % "1.0-SNAPSHOT",
      "com.noteinweb" %% "noteweb-paas-aws" % "1.0-SNAPSHOT"
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    )
  )

lazy val gatlingVersion = "3.3.1"
lazy val gatling = (project in file ("gatling"))
  .enablePlugins(GatlingPlugin)
  .settings(
    scalaVersion := "2.12.15",
    libraryDependencies ++=Seq(
      "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % Test,
      "io.gatling" % "gatling-test-framework" % gatlingVersion % Test
    )
  )


// Documentation for this project:
//    sbt "project docs" "~ paradox"
//    open docs/target/paradox/site/index.html
lazy val docs = (project in file("docs")).enablePlugins(ParadoxPlugin).
  settings(
    scalaVersion := "2.13.8",
    paradoxProperties += ("download_url" -> "https://example.lightbend.com/v1/download/play-samples-play-scala-rest-api-example")
  )

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.noteinweb.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.noteinweb.binders._"
