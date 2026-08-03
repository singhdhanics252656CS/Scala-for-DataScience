import breeze.linalg._
import breeze.plot._
import scala.io.Source

object LogisticRegression {

  def sigmoid(z: Double): Double =
    1.0 / (1.0 + math.exp(-z))

  def main(args: Array[String]): Unit = {

    val file = Source.fromFile("logistic.csv")

    val data = file.getLines().drop(1).map { line =>
      val p = line.split(",")
      (p(0).toDouble, p(1).toDouble)
    }.toList

    file.close()

    val x = DenseVector(data.map(_._1).toArray)
    val y = DenseVector(data.map(_._2).toArray)

    val b0 = -8.0
    val b1 = 1.6

    val testHours = 4.5
    val probability = sigmoid(b0 + b1 * testHours)

    println(s"Probability of passing for 4.5 hours = $probability")

    val fig = Figure()
    val p = fig.subplot(0)

    p += scatter(x, y, {_ => 0.1})

    val yPred = x.map(v => sigmoid(b0 + b1 * v))
    p += plot(x, yPred)

    p.title = "Logistic Regression"
    p.xlabel = "Hours"
    p.ylabel = "Probability of Passing"

    fig.saveas("logistic_regression.png")

    println("Graph saved as logistic_regression.png")
  }
}