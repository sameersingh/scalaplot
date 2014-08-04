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
    val series = new MemBarSeries((0 until 10).map(i => ((i+1) * (i+1)).toDouble), "Series")
    val data = new BarData((x:Int)=> "Label" + x, Seq(series))
    val chart = new BarChart("Chart", data)
    val plotter = new GnuplotPlotter(chart)
    val tmpFile = java.io.File.createTempFile("bar", "1ser")
    println(tmpFile.getCanonicalPath)
    plotter.pdf(tmpFile.getParent + "/", tmpFile.getName)
  }

  @Test
  def testGnuplotTwoLargeColMem(): Unit = {
    val rand = new scala.util.Random(0)
    val series1 = new MemBarSeries((0 until 10).map(i => (rand.nextDouble())), "Series1") {
      override def isLarge = true
    }
    series1.color = Some(Style.Color.Purple)
    series1.fillStyle = Some(Style.FillStyle.Pattern)
    series1.pattern = Some(2)
    val series2 = new MemBarSeries((0 until 10).map(i => (rand.nextDouble())), "Series2") {
      override def isLarge = true
    }
    series2.fillStyle = Some(Style.FillStyle.Solid)
    series2.density = Some(0.2)
    val data = new BarData((x:Int)=> "Label" + x, Seq(series1, series2))
    val chart = new BarChart("Chart", data)
    val plotter = new GnuplotPlotter(chart)
    val tmpFile = java.io.File.createTempFile("bar", "2serLmem")
    println(tmpFile.getCanonicalPath)
    plotter.pdf(tmpFile.getParent + "/", tmpFile.getName)
  }

  @Test
  def testGnuplotTwoSmallColMem(): Unit = {
    val rand = new scala.util.Random(0)
    val series1 = new MemBarSeries((0 until 10).map(i => (rand.nextDouble())), "Series1") {
      override def isLarge = false
    }
    val series2 = new MemBarSeries((0 until 10).map(i => (rand.nextDouble())), "Series2") {
      override def isLarge = false
    }
    val data = new BarData((x:Int)=> "Label" + x, Seq(series1, series2))
    val chart = new BarChart("Chart", data)
    val plotter = new GnuplotPlotter(chart)
    val tmpFile = java.io.File.createTempFile("bar", "2serSmem")
    println(tmpFile.getCanonicalPath)
    plotter.pdf(tmpFile.getParent + "/", tmpFile.getName)
  }
}
