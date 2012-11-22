package org.sameersingh.scalaplot

import gnuplot.GnuplotPlotter
import jfreegraph.JFGraphPlotter
import org.junit._

/**
 * Examples that demonstrate how to use the library. Not really tests.
 * @author sameer
 */
@Test
class ExampleTest {

  @Test
  def testExample1(): Unit = {
    // seqs
    val x = (1 until 100).map(_.toDouble)
    val y = (1 until 100).map(j => math.pow(j, 2))

    // dataset
    val series = new MemXYSeries(x, y, "Square")
    val data = new XYData(series)

    // add cube
    data += new MemXYSeries(x, x.map(i => i*i*i), "Cube")

    // chart
    val chart = new XYChart("Powers!", data)
    chart.showLegend = true

    val file = java.io.File.createTempFile("example1", "pdf")
    println(file.getCanonicalPath)
    new JFGraphPlotter(chart).writeToPdf(file)
    new GnuplotPlotter(chart).writeToPdf(file)
  }

}
