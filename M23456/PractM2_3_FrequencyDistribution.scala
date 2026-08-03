import scala.io.Source

object FrequencyDistribution {

  def main(args: Array[String]): Unit =

    val stream = getClass.getResourceAsStream("/scores.csv")
    val file = Source.fromInputStream(stream)

    val scores = file.getLines().drop(1).map(_.split(",")(1).toInt).toList
    file.close()

    val frequency = scores.groupBy(identity).view.mapValues(_.size).toMap
    val sorted = frequency.toSeq.sortBy(_._1)

    println("Score\tFrequency\tCumulative Frequency")

    var cumulative = 0

    sorted.foreach { case (score, freq) =>
      cumulative += freq
      println(s"$score\t$freq\t\t$cumulative")
    }
  }
}
