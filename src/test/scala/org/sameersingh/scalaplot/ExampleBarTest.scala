package org.sameersingh.scalaplot

import org.junit.Test
import org.sameersingh.scalaplot.gnuplot.GnuplotPlotter
import org.sameersingh.scalaplot.Style.Color

/**
 * Examples that demonstrate how to use the library. Not really tests.
 * @author sameer
 * @since 8/4/14.
 */
@Test
class ExampleBarTest {

  @Test
  def testExplicit(): Unit = {
    // seqs
    val x = (0 until 10)
    val y = x.map(j => math.pow(j, 1.3))

    // dataset
    val series = new MemBarSeries(y, "p=1.3")
    val data = new BarData(x.map("x" + _), Seq(series))

    // add cube
    data += new MemBarSeries(x.map(i => math.pow(i, 1.5)), "p=1.5")

    // chart
    val chart = new BarChart("Powers!", data)
    chart.showLegend = true
    chart.legendPosX = LegendPosX.Left
    chart.legendPosY = LegendPosY.Top

    val file = java.io.File.createTempFile("example1", "pdf")
    file.delete()
    file.mkdir()
    println(file.getCanonicalPath)
    // new JFGraphPlotter(chart).writeToPdf(file)
    val gpl = new GnuplotPlotter(chart)
    println(gpl.string(file.getCanonicalPath + "/", "plot_string"))
    gpl.js(file.getCanonicalPath + "/", "plot_js")
    gpl.svg(file.getCanonicalPath + "/", "plot_svg")
    gpl.html(file.getCanonicalPath + "/", "plot_html")
    gpl.pdf(file.getCanonicalPath + "/", "plot_pdf")
    //gpl.png(file.getCanonicalPath + "/", "plot_png")
  }

  @Test
  def testDataImplicit(): Unit = {
    import org.sameersingh.scalaplot.Implicits._
    // seqs
    val x = (1 until 100)
    val y1 = x.map(j => math.pow(j, 1))
    val y2 = x.map(j => math.pow(j, 0.75))
    val y3 = x.map(j => math.pow(j, 0.5))

    // series
    val s1: BarSeries = y1
    val s2: BarSeries = Bar(y2)

    // data using series
    val d1: BarData = s1
    val d2: BarData = Seq(s1, s2)
    val d2l: BarData = s1 :: s2 :: List()

    // data without series
    val d3: BarData = (y1, y2, y3) // easiest, limited to 6
    val d4: BarData = Seq(y1, y2, y3) // unlimited
    val d5: BarData = (Bar(y1, "Y1"), Bar(y2, color = Color.Blue), Bar(y3, density = 0.5)) // <=6, arbitrary customization
    val d6: BarData = Seq(Bar(y1), Bar(y2), Bar(y3)) // unlimited, arbitrary customization
  }

  @Test
  def testChartImplicit(): Unit = {
    import org.sameersingh.scalaplot.Implicits._
    // seqs
    val x = (1 until 10)
    val names = x.map(x => "Lab" + x)
    val y1 = x.map(j => math.pow(j, 1))
    val y2 = x.map(j => math.pow(j, 0.75))
    val y3 = x.map(j => math.pow(j, 0.5))
    // series
    val s1: BarSeries = y1
    val s2: BarSeries = Bar(y2)
    // data
    val d: BarData = Seq(Bar(y1, "1.0"), Bar(y2, "0.75"), Bar(y3, "0.5")) // unlimited, arbitrary customization

    // chart with data
    val c1: BarChart = d

    // chart with series
    val c2 = barChart(data = s1)
    val c3 = barChart(s1 :: s2 :: List())

    // chart without series
    val c4 = barChart(names ->(y1, y2, y3))
    val c5 = barChart((Bar(y1) -> Bar(y2)))
    val c8 = barChart(names ->(y1, y2, y3), xLabel = "X!", y = Axis(label = "Y!"))
    val c9 = barChart(Seq(Bar(y1, "1.0"), Bar(y2, "0.75"), Bar(y3, "0.5")))
  }

  @Test
  def testOutputImplicit(): Unit = {
    import org.sameersingh.scalaplot.Implicits._
    // seqs
    val x = (1 until 5)
    val y1 = x.map(j => math.pow(j, 1))
    val y2 = x.map(j => math.pow(j, 0.75))
    val y3 = x.map(j => math.pow(j, 0.5))
    // data
    val d: BarData = Seq(Bar(y1, "1"), Bar(y2, "2"), Bar(y3, "3"))
    // chart with data
    val c: BarChart = d

    val file = java.io.File.createTempFile("scalaplot.test", "example")
    file.delete()
    file.mkdir()
    println(file.getCanonicalPath)
    val dir = file.getCanonicalPath + "/"

    println(output(ASCII, c))
    // println(output(SVG, c))
    // output(GUI, c) // fails on X11-less nodes
    output(PDF(dir, "pdf"), c)
    //output(PNG(dir, "png"), c)
  }

  @Test
  def testExamples(): Unit = {
    import org.sameersingh.scalaplot.Implicits._
    val x = 0 until 5
    println(output(ASCII, barChart(x.map("x" + _) ->(x.map(i => math.sin(i / 3.0) + 0.5), x.map(i => math.cos(i / 3.0) + 0.5)))))
  }
}
