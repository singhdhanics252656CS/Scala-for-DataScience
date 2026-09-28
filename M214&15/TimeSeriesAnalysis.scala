import scala.util.Random

object TimeSeriesAnalysis {
  def main(args: Array[String]): Unit = {
    
    val sales = (1 to 30).map { day =>
      val value = 100 + Random.nextInt(50)
      (day, value)
    }

    println("Daily Sales:")
    sales.foreach { case (day, value) =>
      println(s"Day $day: $value")
    }

    val average = sales.map(_._2).sum.toDouble / sales.length

    val maxSales = sales.maxBy(_._2)
    val minSales = sales.minBy(_._2)

    println(f"\nAverage Sales: $average%.2f")
    println(s"Maximum Sales: Day ${maxSales._1}, ${maxSales._2}")
    println(s"Minimum Sales: Day ${minSales._1}, ${minSales._2}")
  }
}