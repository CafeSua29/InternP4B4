name := "P4B4"

version := "1.0"

scalaVersion := "2.12.18"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.2",
  "org.apache.spark" %% "spark-sql" % "3.5.2",
  "org.apache.hadoop" % "hadoop-client" % "3.3.6", 
  "org.apache.spark" %% "spark-hadoop-cloud" % "3.3.0"
)