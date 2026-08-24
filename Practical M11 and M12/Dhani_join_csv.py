from pyspark.sql import SparkSession

spark = SparkSession.builder.appName("Join Teachers and Marks").master("local[*]").getOrCreate()

teachers_df = spark.read.csv("teachers.csv", header=True, inferSchema=True)

marks_df = spark.read.csv("marks.csv", header=True, inferSchema=True)

teachers_df.show()

marks_df.show()

joined_df = teachers_df.join(
    marks_df,
    teachers_df.teacher_id == marks_df.teacher_id,
    "inner"
)

joined_df.show()

writer = joined_df.write
writer = writer.mode("overwrite")
writer = writer.option("header", "true")
writer.csv("teachers_marks_output")

spark.stop()