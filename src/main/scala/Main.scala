import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

object Main {
  val spark = SparkSession.builder()
    .appName("P4B4")
    //.master("local[*]")
    //.config("spark.hadoop.fs.defaultFS", "hdfs://namenode:50070")
    //.config("spark.executor.memory", "4g") 
    //.config("spark.driver.memory", "4g") 
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

    val repartitiondf = df.repartition(200)

    repartitiondf.write
      .mode("overwrite")
      .parquet("hdfs://namenode:9000/Phan4/Bai4")
  }

  def main(args: Array[String]): Unit = {
    createDataFrameAndPutToHDFS()
    //readHDFSThenPutToHBase()
    //readHBaseThenWriteToHDFS()
  }
}
