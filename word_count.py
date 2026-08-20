from pyspark.sql import SparkSession

# Create Spark session
spark = SparkSession.builder \
    .appName("WordCount") \
    .master("local[*]") \
    .getOrCreate()

# Read the input text file
text_file = spark.sparkContext.textFile("input.txt")

# Count the frequency of each word
word_counts = (
    text_file
    .flatMap(lambda line: line.split())
    .map(lambda word: (word, 1))
    .reduceByKey(lambda a, b: a + b)
)

# Display the word counts
for word, count in word_counts.collect():
    print(word, ":", count)

# Stop Spark
spark.stop()

