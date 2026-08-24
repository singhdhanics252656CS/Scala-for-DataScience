from pyspark.sql import SparkSession
from pyspark.sql.functions import avg

# Create Spark session
spark = SparkSession.builder \
    .appName("GroupByAverage") \
    .master("local[*]") \
    .getOrCreate()

# Read the CSV file
df = spark.read.csv(
    "learners.csv",
    header=True,
    inferSchema=True
)

# Display the original data
print("Original Data:")
df.show()

# Group by Department and calculate average marks
result = df.groupBy("Department").agg(
    avg("Marks").alias("Average_Marks")
)

# Display the result
print("Average Marks by Department:")
result.show()

# Stop Spark
spark.stop()
