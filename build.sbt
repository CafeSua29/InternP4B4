name := "P4B4"

version := "1.0"

scalaVersion := "2.12.15"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.2",
  "org.apache.spark" %% "spark-sql" % "3.5.2",
  "org.apache.spark" %% "spark-hadoop-cloud" % "3.3.0",
  "org.apache.hadoop" % "hadoop-client" % "3.3.6"
)

// HBase Dependencies
libraryDependencies ++= Seq(
  "org.apache.hbase" % "hbase-client" % "2.4.12",    // HBase Client
  "org.apache.hbase" % "hbase-common" % "2.4.12",    // HBase Common
  "org.apache.hbase" % "hbase-server" % "2.4.12",    // HBase Server
  "org.apache.hbase" % "hbase-hadoop-compat" % "2.4.12", // HBase Hadoop Compatibility Layer
  "org.apache.hbase" % "hbase-mapreduce" % "2.4.12",  // For HBase integration with Hadoop
  "org.apache.hbase" % "hbase-thirdparty" % "2.4.12",
  "com.google.guava" % "guava" % "30.1-jre" % "provided"
)