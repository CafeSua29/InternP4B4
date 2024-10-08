import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.{Get, Put, Connection, ConnectionFactory, Table}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client._

import java.util

object Main {
  val spark = SparkSession.builder()
    .appName("P4B4")
    .getOrCreate()

    import spark.implicits._

  private def createDataFrameAndPutToHDFS(): Unit = {
    val schema = StructType(Array(
        StructField("timeCreate", TimestampType, true),
        StructField("cookieCreate", TimestampType, true),
        StructField("browserCode", IntegerType, true),
        StructField("browserVer", StringType, true),
        StructField("osCode", IntegerType, true),
        StructField("osVer", StringType, true),
        StructField("ip", LongType, true),
        StructField("locId", IntegerType, true),
        StructField("domain", StringType, true),
        StructField("siteId", IntegerType, true),
        StructField("cId", IntegerType, true),
        StructField("path", StringType, true),
        StructField("referer", StringType, true),
        StructField("guid", LongType, true),
        StructField("flashVersion", StringType, true),
        StructField("jre", StringType, true),
        StructField("sr", StringType, true),
        StructField("sc", StringType, true),
        StructField("geographic", IntegerType, true),
        StructField("field19", StringType, true),
        StructField("field20", StringType, true),
        StructField("url", StringType, true),
        StructField("field22", StringType, true),
        StructField("category", StringType, true),
        StructField("field24", StringType, true)
      ))

    val df = spark.read
        .option("delimiter", "\t")
        .option("header", "false")
        .schema(schema)
        .csv("sample text")

    df.show()

    df.write
      .mode("overwrite")
      .parquet("hdfs://namenode:9000/Phan4/Bai4")
  }

  private def readHDFSThenPutToHBase(): Unit = {
    var df = spark.read.parquet("hdfs://namenode:9000/Phan4/Bai4")
    df = df
      .withColumn("day", date_format(col("timeCreate"), "yyyy-MM-dd"))
      .repartition(5)

    val batchPutSize = 100

    df.foreachPartition((rows: Iterator[Row]) => {
      val conf = HBaseConfiguration.create()
      val hbaseConnection = ConnectionFactory.createConnection(conf)

      try {
        val table = hbaseConnection.getTable(TableName.valueOf("pageviewlog", "pageviewlog_info"))
        val puts = new util.ArrayList[Put]()
        for (row <- rows) {
          val timeCreate = row.getAs[String]("timeCreate")
          val cookieCreate = row.getAs[String]("cookieCreate")
          val browserCode = row.getAs[Int]("browserCode")
          val browserVer = row.getAs[String]("browserVer")
          val osCode = row.getAs[Int]("osCode")
          val osVer = row.getAs[String]("osVer")
          val ip = row.getAs[Long]("ip")
          val locId = row.getAs[Int]("locId")
          val domain = row.getAs[String]("domain")
          val siteId = row.getAs[Int]("siteId")
          val cId = row.getAs[Int]("cId")
          val path = row.getAs[String]("path")
          val referer = row.getAs[String]("referer")
          val guid = row.getAs[Long]("guid")
          val flashVersion = row.getAs[String]("flashVersion")
          val jre = row.getAs[String]("jre")
          val sr = row.getAs[String]("sr")
          val sc = row.getAs[String]("sc")
          val geographic = row.getAs[Int]("geographic")
          val url = row.getAs[String]("url")
          val category = row.getAs[String]("category")
          val day = row.getAs[String]("day")

          val put = new Put(Bytes.toBytes(guid))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("timeCreate"), Bytes.toBytes(timeCreate))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("cookieCreate"), Bytes.toBytes(cookieCreate))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("browserCode"), Bytes.toBytes(browserCode))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("browserVer"), Bytes.toBytes(browserVer))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("osCode"), Bytes.toBytes(osCode))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("osVer"), Bytes.toBytes(osVer))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("ip"), Bytes.toBytes(ip))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("locId"), Bytes.toBytes(locId))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("domain"), Bytes.toBytes(domain))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("siteId"), Bytes.toBytes(siteId))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("cId"), Bytes.toBytes(cId))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("path"), Bytes.toBytes(path))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("referer"), Bytes.toBytes(referer))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("flashVersion"), Bytes.toBytes(flashVersion))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("jre"), Bytes.toBytes(jre))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("sr"), Bytes.toBytes(sr))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("sc"), Bytes.toBytes(sc))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("geographic"), Bytes.toBytes(geographic))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("url"), Bytes.toBytes(url))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("category"), Bytes.toBytes(category))
          put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("day"), Bytes.toBytes(day))

          puts.add(put)
          if (puts.size > batchPutSize) {
            table.put(puts)
            puts.clear()
          }
        }
        if (puts.size() > 0) { 
          table.put(puts)
        }
      } finally {
        hbaseConnection.close()
      }
    })
  }

  def main(args: Array[String]): Unit = {
    createDataFrameAndPutToHDFS()
    readHDFSThenPutToHBase()
    //readHBaseThenWriteToHDFS()
  }
}
