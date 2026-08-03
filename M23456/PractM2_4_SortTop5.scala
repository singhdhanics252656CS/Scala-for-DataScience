import scala.io.Source

object SortTop5 {

  def main(args: Array[String]): Unit = {

    val file = Source.fromFile("workers.csv")

    val data = file.getLines().drop(1).map { line =>
      val p = line.split(",")
      (p(1), p(3).toInt)
    }.toList

    file.close()

    val top5 = data.sortBy(-_._2).take(5)

    println("Top 5 Employees by Salary")

    top5.foreach { case (name, salary) =>
      println(s"$name - $salary")
    }
  }
}
