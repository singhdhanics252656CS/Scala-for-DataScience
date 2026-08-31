from pyspark.sql import SparkSession
from pyspark.ml.feature import VectorAssembler
from pyspark.ml.classification import LogisticRegression
from pyspark.ml import Pipeline
from pyspark.ml.evaluation import MulticlassClassificationEvaluator

spark = SparkSession.builder \
    .appName("Spark ML Classification") \
    .master("local[*]") \
    .getOrCreate()

data = spark.read.csv("data.csv", header=True, inferSchema=True)

print("Dataset:")
data.show()

assembler = VectorAssembler(
    inputCols=["age", "income"],
    outputCol="features"
)

lr = LogisticRegression(
    featuresCol="features",
    labelCol="label"
)

pipeline = Pipeline(stages=[assembler, lr])

train_data, test_data = data.randomSplit([0.8, 0.2], seed=42)

model = pipeline.fit(train_data)

predictions = model.transform(test_data)

print("Predictions:")
predictions.select(
    "age",
    "income",
    "label",
    "prediction"
).show()

evaluator = MulticlassClassificationEvaluator(
    labelCol="label",
    predictionCol="prediction",
    metricName="accuracy"
)

accuracy = evaluator.evaluate(predictions)

print("Accuracy:", accuracy)

spark.stop()