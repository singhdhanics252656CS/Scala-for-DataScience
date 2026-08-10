import breeze.linalg._
import com.github.tototoshi.csv.CSVReader
import org.knowm.xchart._
import org.knowm.xchart.style.markers.SeriesMarkers
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle
import java.io.InputStreamReader
import java.awt.Color

object Dhani_KMeansCSV {

  def main(args: Array[String]): Unit = {

    val stream = getClass.getResourceAsStream("/trees.csv")

    if (stream == null) {
      println("Dataset not found!")
      return
    }

    val reader = CSVReader.open(new InputStreamReader(stream))
    val rows = reader.allWithHeaders()

    val points = rows.map { row =>
      DenseVector(row("X").toDouble, row("Y").toDouble)
    }.toArray

    reader.close()

    val data = DenseMatrix(points: _*)

    println("Dataset:")
    for (i <- 0 until data.rows) {
      println(s" Point ${i + 1}: (${data(i,0)}, ${data(i,1)})")
    }

    val k = 2

    var centroids = DenseMatrix(
      data(0, ::).t,
      data(data.rows - 1, ::).t
    )

    val assignments = DenseVector.zeros[Int](data.rows)

    for (_ <- 0 until 20) {

      for (i <- 0 until data.rows) {

        val point = data(i, ::).t

        val d1 = norm(point - centroids(0, ::).t)
        val d2 = norm(point - centroids(1, ::).t)

        assignments(i) = if (d1 < d2) 0 else 1
      }

      for (c <- 0 until k) {

        val clusterPoints =
          (0 until data.rows)
            .filter(assignments(_) == c)
            .map(data(_, ::).t)

        if (clusterPoints.nonEmpty) {

          val mean =
            clusterPoints.reduce(_ + _) /:/ clusterPoints.length.toDouble

          centroids(c, ::) := mean.t
        }
      }
    }

    println()
    println("Final Cluster Assignments:")
    for (i <- 0 until data.rows) {
      println(
        s" Point ${i + 1} (${data(i,0)}, ${data(i,1)}) -> Cluster ${assignments(i) + 1}"
      )
    }

    println()
    println("Final Centroids:")
    for (c <- 0 until k) {
      println(
        s" Cluster ${c + 1}: (${centroids(c,0)}, ${centroids(c,1)})"
      )
    }

    val chart =
      new XYChartBuilder()
        .width(800)
        .height(600)
        .title("K-Means Clustering")
        .xAxisTitle("X")
        .yAxisTitle("Y")
        .build()

    val x1 =
      (0 until data.rows).filter(assignments(_) == 0).map(data(_,0)).toArray
    val y1 =
      (0 until data.rows).filter(assignments(_) == 0).map(data(_,1)).toArray

    val x2 =
      (0 until data.rows).filter(assignments(_) == 1).map(data(_,0)).toArray
    val y2 =
      (0 until data.rows).filter(assignments(_) == 1).map(data(_,1)).toArray

    val s1 = chart.addSeries("Cluster 1", x1, y1)
    s1.setXYSeriesRenderStyle(XYSeriesRenderStyle.Scatter)
    s1.setMarker(SeriesMarkers.CIRCLE)
    s1.setMarkerColor(Color.BLUE)
    s1.setLineStyle(new java.awt.BasicStroke(0))

    val s2 = chart.addSeries("Cluster 2", x2, y2)
    s2.setXYSeriesRenderStyle(XYSeriesRenderStyle.Scatter)
    s2.setMarker(SeriesMarkers.CIRCLE)
    s2.setMarkerColor(Color.RED)
    s2.setLineStyle(new java.awt.BasicStroke(0))

    val cent = chart.addSeries(
      "Centroids",
      centroids(::,0).toArray,
      centroids(::,1).toArray
    )
    cent.setXYSeriesRenderStyle(XYSeriesRenderStyle.Scatter)
    cent.setMarker(SeriesMarkers.DIAMOND)
    cent.setMarkerColor(Color.BLACK)
    cent.setLineStyle(new java.awt.BasicStroke(0))

    new SwingWrapper(chart).displayChart()

    BitmapEncoder.saveBitmap(
      chart,
      "kmeans_plot",
      BitmapEncoder.BitmapFormat.PNG
    )

    println()
    println("Graph saved as kmeans_plot.png")
  }
}