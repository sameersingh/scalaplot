package org.sameersingh.scalaplot

import gnuplot.GnuplotPlotter
import org.junit._

/**
 * @author sameer
 * @date 10/6/12
 */
@Test
class BarPlotTest {

  @Test
  def testGnuplotOneColOneFile(): Unit = {
    // TODO
  }

  @Test
  def testGnuplotTwoColOneFile(): Unit = {
    // TODO
  }

  @Test
  def testGnuplotOneSmallColMem(): Unit = {
    val series = new MemBarSeries(0 until 10, (0 until 10).map(i => ((i+1) * (i+1)).toDouble), "Series")
    val data = new BarData((x:Int)=> "Label" + x, Seq(series))
    val chart = new BarChart("Chart", data)
    val plotter = new GnuplotPlotter(chart)
    val tmpFile = java.io.File.createTempFile("bar", "1ser")
    println(tmpFile.getCanonicalPath)
    //plotter.pdf(tmpFile.getParent + "/", tmpFile.getName)
    plotter.pdf("/Users/sameer/Work/debug/scalaplot/", "bar1")
  }

  @Test
  def testGnuplotTwoSmallColMem(): Unit = {
    val rand = new scala.util.Random(0)
    val series1 = new MemBarSeries(0 until 10, (0 until 10).map(i => (rand.nextDouble())), "Series1") {
      override def isLarge = true
    }
    val series2 = new MemBarSeries(0 until 10, (0 until 10).map(i => (rand.nextDouble())), "Series2") {
      override def isLarge = true
    }
    val data = new BarData((x:Int)=> "Label" + x, Seq(series1, series2))
    val chart = new BarChart("Chart", data)
    val plotter = new GnuplotPlotter(chart)
    val tmpFile = java.io.File.createTempFile("bar", "2ser")
    println(tmpFile.getCanonicalPath)
    //plotter.pdf(tmpFile.getParent + "/", tmpFile.getName)
    plotter.pdf("/Users/sameer/Work/debug/scalaplot/", "bar2")
  }

}
