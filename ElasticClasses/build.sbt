name := "ElasticClasses"

version := "0.1"

scalaVersion := "2.12.5"

resolvers += Resolver.bintrayRepo("cakesolutions", "maven")

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.42"

libraryDependencies += "net.liftweb" %% "lift-json" % "3.2.0"

libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.3.0"

libraryDependencies += "net.cakesolutions" %% "scala-kafka-client" % "0.10.2.2"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api"       % "1.7.7",
  "org.slf4j" % "jcl-over-slf4j"  % "1.7.7"
).map(_.force())

libraryDependencies ~= { _.map(_.exclude("org.slf4j", "slf4j-jdk14")) }