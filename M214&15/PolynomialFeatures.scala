object PolynomialFeatures {
  def main(args: Array[String]): Unit = {

    val numbers = List(1, 2, 3)

    val features = numbers.flatMap { x =>
      List(x, x * x, x * x * x)
    }

    println("Polynomial Features:")
    println(features)
  }
}