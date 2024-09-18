name := "P4B4"

version := "1.0"

scalaVersion := "2.12.15"

// Spark and Hadoop Dependencies
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.3.0",      // Spark Core
  "org.apache.spark" %% "spark-sql" % "3.3.0",       // Spark SQL for DataFrame operations
  "org.apache.hadoop" % "hadoop-client" % "3.3.6",   // Hadoop Client for HDFS interaction
  "org.apache.spark" %% "spark-hadoop-cloud" % "3.3.0" // Hadoop Cloud API (optional)
)

// HBase Dependencies
libraryDependencies ++= Seq(
  "org.apache.hbase" % "hbase-client" % "2.4.12",    // HBase Client
  "org.apache.hbase" % "hbase-common" % "2.4.12",    // HBase Common
  "org.apache.hbase" % "hbase-server" % "2.4.12",    // HBase Server
  "org.apache.hbase" % "hbase-hadoop-compat" % "2.4.12" // HBase Hadoop Compatibility Layer
)