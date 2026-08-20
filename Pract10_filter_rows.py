from pyspark.sql import SparkSession

# Create Spark session
spark = SparkSession.builder \
    .appName("FilterRows") \
    .master("local[*]") \
    .getOrCreate()

# Read the CSV file
df = spark.read.csv(
    "students.csv",
    header=True,
    inferSchema=True
)

# Display the original data
print("Original Data:")
df.show()

# Set the threshold
threshold = 75

# Filter rows where Marks is greater than the threshold
filtered_df = df.filter(df["Marks"] > threshold)

# Display the filtered data
print("Students with Marks greater than", threshold)
filtered_df.show()

# Stop Spark
spark.stop()

