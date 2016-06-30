name := "SparkOrientDbConnectorDemo"

version := "1.0"

scalaVersion := "2.11.4"

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

libraryDependencies ++= Seq(
  "com.orientechnologies" % "orientdb-core" % "2.2.3",
  "com.orientechnologies" % "orientdb-client" % "2.2.3",
  "com.orientechnologies" % "orientdb-graphdb" % "2.2.3",
  "com.orientechnologies" % "orientdb-distributed" % "2.2.3",
  "org.apache.spark" % "spark-core_2.11" % "1.6.1",
  "org.apache.spark" % "spark-graphx_2.11" % "1.6.1",
  "org.scala-lang" % "scala-compiler" % "2.11.4",
  "org.scala-lang" % "scala-library" % "2.11.4",
  "org.scala-lang" % "scala-reflect" % "2.11.4",
  "jline" % "jline" % "2.12",
  "com.tinkerpop.blueprints" % "blueprints-core" % "2.6.0",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.7.4",
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.7.4"
)

