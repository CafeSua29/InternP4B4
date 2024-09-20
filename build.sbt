name := "P4B4"

version := "1.0"

scalaVersion := "2.12.15"

libraryDependencies ++= Seq(
  //spark
  "org.apache.spark" %% "spark-core" % "3.5.2",
  "org.apache.spark" %% "spark-sql" % "3.5.2",
  "org.apache.spark" %% "spark-hadoop-cloud" % "3.3.0",
  //hadoop
  "org.apache.hadoop" % "hadoop-client" % "3.3.6",
  "org.apache.hadoop" % "hadoop-common" % "3.3.6",
  //hbase
  "org.apache.hbase" % "hbase-client" % "2.4.12",    // HBase Client
  "org.apache.hbase" % "hbase-common" % "2.4.12",    // HBase Common
  "org.apache.hbase" % "hbase-mapreduce" % "2.4.12",  // For HBase integration with Hadoop
  "org.apache.hbase" % "hbase-hadoop2-compat" % "2.4.12",
  "org.apache.hbase" % "hbase-shaded-client" % "2.4.12",
  "org.apache.hbase" % "hbase-shaded-mapreduce" % "2.4.12",
  //3-party
  "com.google.guava" % "guava" % "31.1-jre" % "provided"
)