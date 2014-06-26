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
  def testExplicit(): Unit = {
    // seqs
    val x = (1 until 100).map(_.toDouble)
    val y = (1 until 100).map(j => math.pow(j, 2))

    // dataset
    val series = new MemXYSeries(x, y, "Square")
    val data = new XYData(series)

    // add cube
    data += new MemXYSeries(x, x.map(i => i * i * i), "Cube")

    // chart
    val chart = new XYChart("Powers!", data)
    chart.showLegend = true

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
    gpl.png(file.getCanonicalPath + "/", "plot_png")
  }

  @Test
  def testDataImplicit(): Unit = {
    import org.sameersingh.scalaplot.Implicits._
    // seqs
    val x = (1 until 100).map(_.toDouble)
    val y1 = (1 until 100).map(j => math.pow(j, 1))
    val y2 = (1 until 100).map(j => math.pow(j, 2))
    val y3 = (1 until 100).map(j => math.pow(j, 3))
    val xy1 = x zip y1
    val xy2 = x zip y2

    // series
    val s1: XYSeries = x -> y1
    val s2: XYSeries = x zip y2
    val s3: XYSeries = x -> Y(y3)
    val s4: XYSeries = XY(xy1)
    val f1 = math.sin(_)
    val s1f: XYSeries = x -> f1
    val s2f: XYSeries = x -> Yf(math.sin)

    // data using series
    val d1: XYData = s1
    val d2: XYData = Seq(s1, s2)
    val d2l: XYData = s1 :: s2 :: List()

    // data without series
    val d3: XYData = x ->(y1, y2, y3) // easiest, limited to 6
    val d4: XYData = x -> Ys(y1, y2, y3) // unlimited
    val d5: XYData = x ->(Y(y1, "Y1"), Y(y2, color = Color.Blue), Y(y3, lw = 3.0)) // <=6, arbitrary customization
    val d6: XYData = x -> Seq(Y(y1), Y(y2), Y(y3)) // unlimited, arbitrary customization

    // same as above, but with functions instead
    val d3f: XYData = x ->(math.sin(_), math.cos(_))
    val d4f: XYData = x -> Yfs(math.sin, math.cos)
    val d5f: XYData = x -> Seq(Yf(math.sin, "sin"), Yf(math.cos, color = Color.Blue), Yf(math.tan, lw = 3.0))
    val d6f: XYData = x ->(Yf(math.sin), Yf(math.cos), Yf(math.tan))
  }

  @Test
  def testChartImplicit(): Unit = {
    import org.sameersingh.scalaplot.Implicits._
    // seqs
    val x = (1 until 100).map(_.toDouble)
    val y1 = (1 until 100).map(j => math.pow(j, 1))
    val y2 = (1 until 100).map(j => math.pow(j, 2))
    val y3 = (1 until 100).map(j => math.pow(j, 3))
    val xy1 = x zip y1
    val xy2 = x zip y2
    // series
    val s1: XYSeries = x -> y1
    val s2: XYSeries = x zip y2
    // data
    val d: XYData = x -> Seq(Y(y1, "1"), Y(y2, "2"), Y(y3, "3"))

    // chart with data
    val c1: XYChart = d

    // chart with series
    val c2 = plot(data = s1)
    val c3 = plot(s1 :: s2 :: List())

    // chart without series
    val c4 = plot(x ->(y1, y2, y3))
    val c5 = plot(x -> Y(y1) :: x -> Y(y2) :: List())
    val c6 = plot(XY(xy1) :: XY(xy2) :: List())
    val c7 = plot(x ->(math.sin(_), math.cos(_)))
    val c8 = plot(x ->(y1, y2, y3), x = Axis(label = "X!", log = true), y = Axis(label = "Y!"))
  }

  @Test
  def testOutputImplicit(): Unit = {
    import org.sameersingh.scalaplot.Implicits._
    // seqs
    val x = (1 until 5).map(_.toDouble)
    val y1 = (1 until 5).map(j => math.pow(j, 1))
    val y2 = (1 until 5).map(j => math.pow(j, 2))
    val y3 = (1 until 5).map(j => math.pow(j, 3))
    // data
    val d: XYData = x -> Seq(Y(y1, "1"), Y(y2, "2"), Y(y3, "3"))
    // chart with data
    val c: XYChart = d

    val file = java.io.File.createTempFile("scalaplot.test", "example")
    file.delete()
    file.mkdir()
    println(file.getCanonicalPath)
    val dir = file.getCanonicalPath + "/"

    println(output(ASCII, c))
    // println(output(SVG, c))
    output(GUI, c)
    output(PDF(dir, "pdf"), c)
    output(PNG(dir, "png"), c)
  }
}
