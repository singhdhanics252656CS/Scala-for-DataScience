import breeze.linalg.{DenseVector, euclideanDistance}
import com.github.tototoshi.csv.CSVReader
import org.knowm.xchart._
import org.knowm.xchart.style.markers.SeriesMarkers
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle
import java.io.InputStreamReader
import java.awt.Color

object Dhani_KnnCSV {

  case class DataPoint(features: DenseVector[Double], label: String)

  def main(args: Array[String]): Unit = {

    val stream = getClass.getResourceAsStream("/employees.csv")

    if (stream == null) {
      println("CSV file not found!")
      return
    }

    val reader = CSVReader.open(new InputStreamReader(stream))
    val rows = reader.allWithHeaders()

    val dataset = rows.map { row =>
      DataPoint(
        DenseVector(
          row("Age").toDouble,
          row("Experience").toDouble
        ),
        row("Label")
      )
    }

    reader.close()

    println("Training data points:")
    dataset.foreach(p =>
      println(s" Features: ${p.features}, Label: ${p.label}")
    )

    val newPoint = DenseVector(33.0, 6.0)

    println(s"\nNew data point to classify: ${newPoint}")

    var minDistance = Double.MaxValue
    var predictedLabel = ""

    for (point <- dataset) {
      val dist = euclideanDistance(newPoint, point.features)
      println(s" Distance to point with label '${point.label}': $dist")

      if (dist < minDistance) {
        minDistance = dist
        predictedLabel = point.label
      }
    }

    println("\nClassification Result:")
    println(s" The nearest neighbor is at a distance of: $minDistance")
    println(s" The predicted label for the new point is: $predictedLabel")

    val chart =
      new XYChartBuilder()
        .width(800)
        .height(600)
        .title("KNN Classification")
        .xAxisTitle("Age")
        .yAxisTitle("Experience")
        .build()

    val junior = dataset.filter(_.label == "Junior")
    val senior = dataset.filter(_.label == "Senior")

    val s1 = chart.addSeries(
      "Junior",
      junior.map(_.features(0)).toArray,
      junior.map(_.features(1)).toArray
    )
    s1.setXYSeriesRenderStyle(XYSeriesRenderStyle.Scatter)
    s1.setMarker(SeriesMarkers.CIRCLE)
    s1.setMarkerColor(Color.BLUE)
    s1.setLineStyle(new java.awt.BasicStroke(0))

    val s2 = chart.addSeries(
      "Senior",
      senior.map(_.features(0)).toArray,
      senior.map(_.features(1)).toArray
    )
    s2.setXYSeriesRenderStyle(XYSeriesRenderStyle.Scatter)
    s2.setMarker(SeriesMarkers.CIRCLE)
    s2.setMarkerColor(Color.RED)
    s2.setLineStyle(new java.awt.BasicStroke(0))

    val s3 = chart.addSeries(
      "New Point",
      Array(newPoint(0)),
      Array(newPoint(1))
    )
    s3.setXYSeriesRenderStyle(XYSeriesRenderStyle.Scatter)
    s3.setMarker(SeriesMarkers.DIAMOND)
    s3.setMarkerColor(Color.BLACK)
    s3.setLineStyle(new java.awt.BasicStroke(0))

    new SwingWrapper(chart).displayChart()

    BitmapEncoder.saveBitmap(
      chart,
      "knn_plot",
      BitmapEncoder.BitmapFormat.PNG
    )

    println("Graph saved as knn_plot.png")
  }
}