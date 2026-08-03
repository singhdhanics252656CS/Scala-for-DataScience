import breeze.linalg._
import breeze.plot._
import scala.io.Source

object LinearRegression {

  def main(args: Array[String]): Unit = {

    val file = Source.fromFile("regression.csv")

    val data = file.getLines().drop(1).map { line =>
      val p = line.split(",")
      (p(0).toDouble, p(1).toDouble)
    }.toList

    file.close()

    val x = DenseVector(data.map(_._1).toArray)
    val y = DenseVector(data.map(_._2).toArray)

    val n = x.length.toDouble

    val slope =
      (n * (x dot y) - sum(x) * sum(y)) /
        (n * (x dot x) - math.pow(sum(x), 2))

    val intercept = (sum(y) - slope * sum(x)) / n

    println(s"Slope = $slope")
    println(s"Intercept = $intercept")

    val hours = 9.0
    val prediction = intercept + slope * hours

    println(s"Predicted marks for 9 hours = $prediction")

    val fig = Figure()
    val p = fig.subplot(0)

    p += scatter(x, y, {_ => 0.1})

    val yPred = x.map(v => intercept + slope * v)
    p += plot(x, yPred)

    p.title = "Linear Regression"
    p.xlabel = "Hours"
    p.ylabel = "Marks"

    fig.saveas("linear_regression.png")

    println("Graph saved as linear_regression.png")
  }
}