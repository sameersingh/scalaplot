package org.sameersingh.scalaplot

import gnuplot.GnuplotPlotter
import org.junit._
import java.io.PrintWriter

/**
 * @author sameer
 * @date 10/6/12
 */
@Test
class PlotTest {

  @Test
  def testGnuplotOneColOneFile(): Unit = {
    // write data file
    val dataFile = java.io.File.createTempFile("test", "dat")
    println(dataFile.getCanonicalPath)
    val writer = new PrintWriter(dataFile)
    for (i <- 0 until 100) {
      writer.println(i + "\t" + (i * i))
    }
    writer.flush()
    writer.close()

    val series = new FileXYSeries(1, 2, "Series", dataFile.getCanonicalPath)
    val data = new XYData(series)
    val chart = new XYChart("Chart", data)

    val plotter = new GnuplotPlotter(chart)
    plotter.pdf(dataFile.getParent + "/", dataFile.getName + "-onecol")
  }

  @Test
  def testGnuplotTwoColOneFile(): Unit = {
    // write data file
    val dataFile = java.io.File.createTempFile("test", "dat")
    println(dataFile.getCanonicalPath)
    val writer = new PrintWriter(dataFile)
    for (i <- 0 until 100) {
      writer.println(i + "\t" + (i * i) + "\t" + (50 * i))
    }
    writer.flush()
    writer.close()

    val series1 = new FileXYSeries(1, 2, "Series1", dataFile.getCanonicalPath)
    val series2 = new FileXYSeries(1, 3, "Series2", dataFile.getCanonicalPath)
    val data = new XYData(series1, series2)
    val chart = new XYChart("Chart", data)

    val plotter = new GnuplotPlotter(chart)
    plotter.pdf(dataFile.getParent + "/", dataFile.getName + "-twocol")
  }

  @Test
  def testGnuplotOneSmallColMem(): Unit = {
    val series = new MemXYSeries((0 until 100).map(_.toDouble), (0 until 100).map(i => (i * i).toDouble), "Series")
    val data = new XYData(series)
    val chart = new XYChart("Chart", data)
    val plotter = new GnuplotPlotter(chart)
    val tmpFile = java.io.File.createTempFile("test", "dat")
    println(tmpFile.getCanonicalPath)
    //plotter.pdf(tmpFile.getCanonicalPath)
  }

  @Test
  def testGnuplotTwoLargeColMem(): Unit = {
    val series1 = new MemXYSeries((0 until 100).map(_.toDouble), (0 until 100).map(i => (i * i).toDouble), "Series1") {
      override def isLarge = true
    }
    val series2 = new MemXYSeries((0 until 100).map(_.toDouble), (0 until 100).map(i => (50 * i).toDouble), "Series2") {
      override def isLarge = true
    }
    val data = new XYData(series1, series2)
    val chart = new XYChart("Chart", data)
    val plotter = new GnuplotPlotter(chart)
    val tmpFile = java.io.File.createTempFile("test", "dat")
    println(tmpFile.getCanonicalPath)
    plotter.pdf(tmpFile.getParent + "/", tmpFile.getName)
  }

}
