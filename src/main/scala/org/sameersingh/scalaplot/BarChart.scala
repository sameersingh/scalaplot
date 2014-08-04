package org.sameersingh.scalaplot

import scala.collection.mutable.ArrayBuffer
import java.io.PrintWriter
import Style._

/**
 * @author sameer
 * @since 7/21/14.
 */

class DiscreteAxis {
  private var _label: String = ""

  def label = _label

  def label_=(l: String) = _label = l
}

abstract class BarSeries {
  def name: String

  def points: Seq[(Int, Double)]

  var color: Option[Color.Type] = None
  // fill style
  var fillStyle: Option[FillStyle.Type] = None
  var density: Option[Double] = None
  var pattern: Option[Int] = None
  // border config
  var border: Option[Boolean] = None
  var borderLineType: Option[LineType.Type] = None
}

class MemBarSeries(val ys: Seq[Double], val name: String = "Label") extends BarSeries {
  def isLarge: Boolean = ys.length > 1000

  def writeToFile(filename: String, names: Int => String) {
    val writer = new PrintWriter(filename)
    for (i <- 0 until xs.length)
      writer.println(xs(i).toString + "\t" + ys(i).toString + "\t" + names(i))
    writer.flush()
    writer.close()
  }

  val xs = (0 until ys.length)

  def toStrings(names: Int => String): Seq[String] = (0 until xs.length).map(i => xs(i).toString + "\t" + ys(i).toString + "\t" + names(i))

  def points = xs.zip(ys).seq
}


class BarData(val names: (Int) => String = _.toString,
              ss: Seq[BarSeries]) {
  val _serieses = new ArrayBuffer[BarSeries]()
  _serieses ++= ss

  def serieses: Seq[BarSeries] = _serieses

  def +=(s: BarSeries) = _serieses += s
}


class BarChart(chartTitle: Option[String], val data: BarData,
               val x: DiscreteAxis = new DiscreteAxis, val y: NumericAxis = new NumericAxis) extends Chart {
  def this(chartTitle: String, data: BarData) = this(Some(chartTitle), data)

  def this(data: BarData) = this(None, data)
}

trait BarSeriesImplicits {

  trait YPoints {
    def xsToYs(xs: Seq[Double]): Seq[Double]
  }

  //  implicit def seqToYpoints(ys: Seq[Double]): YPoints = new YPoints {
  //    override def xsToYs(xs: Seq[Double]): Seq[Double] = ys
  //  }
  //
  //  implicit def funcToYpoints(f: Double => Double): YPoints = new YPoints {
  //    override def xsToYs(xs: Seq[Double]): Seq[Double] = xs.map(f)
  //  }

  //  implicit def paraFuncToYpoints(f: (Double) => Double): YPoints = new YPoints {
  //    override def xsToYs(xs: Seq[Double]): Seq[Double] = xs.map(x => f(x))
  //  }
  implicit def anyToOptionAny[A](a: A): Option[A] = Some(a)

  abstract class Y(val label: String = "Label",
                   val style: XYPlotStyle.Type = XYPlotStyle.LinesPoints,
                   val color: Option[Color.Type] = None,
                   val ps: Option[Double] = None,
                   val pt: Option[PointType.Type] = None,
                   val lw: Option[Double] = None,
                   val lt: Option[LineType.Type] = None,
                   val every: Option[Int] = None) extends YPoints

  object Y {
    def apply(yp: Seq[Double],
              label: String = "Label",
              style: XYPlotStyle.Type = XYPlotStyle.LinesPoints,
              color: Option[Color.Type] = None,
              ps: Option[Double] = None,
              pt: Option[PointType.Type] = None,
              lw: Option[Double] = None,
              lt: Option[LineType.Type] = None,
              every: Option[Int] = None): Y = new Y(label, style, color, ps, pt, lw, lt, every) {
      override def xsToYs(xs: Seq[Double]): Seq[Double] = yp
    }
  }

  object Yf {
    def apply(f: Double => Double,
              label: String = "Label",
              style: XYPlotStyle.Type = XYPlotStyle.LinesPoints,
              color: Option[Color.Type] = None,
              ps: Option[Double] = None,
              pt: Option[PointType.Type] = None,
              lw: Option[Double] = None,
              lt: Option[LineType.Type] = None,
              every: Option[Int] = None): Y = new Y(label, style, color, ps, pt, lw, lt, every) {
      override def xsToYs(xs: Seq[Double]): Seq[Double] = xs.map(f)
    }
  }

  object XY {
    def apply(points: Seq[(Double, Double)],
              label: String = "Label",
              style: XYPlotStyle.Type = XYPlotStyle.LinesPoints,
              color: Option[Color.Type] = None,
              ps: Option[Double] = None,
              pt: Option[PointType.Type] = None,
              lw: Option[Double] = None,
              lt: Option[LineType.Type] = None,
              every: Option[Int] = None): XYSeries = {
      val s = new MemXYSeries(points, label)
      s.color = color
      s.plotStyle = style
      s.pointSize = ps
      s.pointType = pt
      s.lineWidth = lw
      s.lineType = lt
      s.every = every
      s
    }
  }

  def series(xs: Seq[Double], y: Y): XYSeries = {
    val s = new MemXYSeries(xs, y.xsToYs(xs), y.label)
    s.color = y.color
    s.plotStyle = y.style
    s.pointSize = y.ps
    s.pointType = y.pt
    s.lineWidth = y.lw
    s.lineType = y.lt
    s.every = y.every
    s
  }

  implicit def pairSeqToSeries(xys: Iterable[(Double, Double)]): XYSeries = new MemXYSeries(xys.toSeq)

  implicit def seqPairToSeries(xy: Pair[Iterable[Double], Iterable[Double]]): XYSeries = new MemXYSeries(xy._1.toSeq, xy._2.toSeq)

  implicit def seqFuncPairToSeries(xy: Pair[Iterable[Double], Double => Double]): XYSeries = new MemXYSeries(xy._1.toSeq, xy._1.map(xy._2).toSeq)

  implicit def xyspectoSeries(xy: Pair[Seq[Double], Y]): XYSeries = series(xy._1, xy._2)
}

object BarSeriesImplicits extends BarSeriesImplicits

trait BarDataImplicits extends BarSeriesImplicits {
  implicit def seriesSeqToData(ss: Iterable[XYSeries]): XYData = new XYData(ss.toSeq: _*)

  implicit def seriesToData(s: XYSeries): XYData = new XYData(s)

  // implicit def seriesStarToData(ss: XYSeries*): XYData = new XYData(ss: _*)

  // implicit def data(xys: Pair[Seq[Double], Y]): XYData = new XYData(series(xys._1, xys._2))

  def Ys(ys: Iterable[Seq[Double]]): Seq[Y] = ys.map(y => Y(y)).toSeq

  def Ys(ys: Seq[Double]*): Seq[Y] = ys.map(y => Y(y)).toSeq

  // seq of double
  implicit def xSeqYToData(xy: Pair[Seq[Double], Seq[Double]]): XYData = xy._1 -> Ys(xy._2)

  implicit def xSeqY2ToData(xy: Pair[Seq[Double], Product2[Seq[Double], Seq[Double]]]): XYData = xy._1 -> Ys(xy._2._1, xy._2._2)

  implicit def xSeqY3ToData(xy: Pair[Seq[Double], Product3[Seq[Double], Seq[Double], Seq[Double]]]): XYData = xy._1 -> Ys(xy._2._1, xy._2._2, xy._2._3)

  implicit def xSeqY4ToData(xy: Pair[Seq[Double], Product4[Seq[Double], Seq[Double], Seq[Double], Seq[Double]]]): XYData = xy._1 -> Ys(xy._2._1, xy._2._2, xy._2._3, xy._2._4)

  implicit def xSeqY5ToData(xy: Pair[Seq[Double], Product5[Seq[Double], Seq[Double], Seq[Double], Seq[Double], Seq[Double]]]): XYData = xy._1 -> Ys(xy._2._1, xy._2._2, xy._2._3, xy._2._4, xy._2._5)

  implicit def xSeqY6ToData(xy: Pair[Seq[Double], Product6[Seq[Double], Seq[Double], Seq[Double], Seq[Double], Seq[Double], Seq[Double]]]): XYData = xy._1 -> Ys(xy._2._1, xy._2._2, xy._2._3, xy._2._4, xy._2._5, xy._2._6)

  // seq of funcs
  implicit def xFYToData(xy: Pair[Seq[Double], Double => Double]): XYData = xy._1 -> Yfs(xy._2)

  implicit def xFY2ToData(xy: Pair[Seq[Double], Product2[Double => Double, Double => Double]]): XYData = xy._1 -> Yfs(xy._2._1, xy._2._2)

  implicit def xFY3ToData(xy: Pair[Seq[Double], Product3[Double => Double, Double => Double, Double => Double]]): XYData = xy._1 -> Yfs(xy._2._1, xy._2._2, xy._2._3)

  implicit def xFY4ToData(xy: Pair[Seq[Double], Product4[Double => Double, Double => Double, Double => Double, Double => Double]]): XYData = xy._1 -> Yfs(xy._2._1, xy._2._2, xy._2._3, xy._2._4)

  implicit def xFY5ToData(xy: Pair[Seq[Double], Product5[Double => Double, Double => Double, Double => Double, Double => Double, Double => Double]]): XYData = xy._1 -> Yfs(xy._2._1, xy._2._2, xy._2._3, xy._2._4, xy._2._5)

  implicit def xFY6ToData(xy: Pair[Seq[Double], Product6[Double => Double, Double => Double, Double => Double, Double => Double, Double => Double, Double => Double]]): XYData = xy._1 -> Yfs(xy._2._1, xy._2._2, xy._2._3, xy._2._4, xy._2._5, xy._2._6)

  // seq of Ys
  implicit def xYToData(xy: Pair[Seq[Double], Y]): XYData = xy._1 -> Seq(xy._2)

  implicit def xY2ToData(xy: Pair[Seq[Double], Product2[Y, Y]]): XYData = xy._1 -> Seq(xy._2._1, xy._2._2)

  implicit def xY3ToData(xy: Pair[Seq[Double], Product3[Y, Y, Y]]): XYData = xy._1 -> Seq(xy._2._1, xy._2._2, xy._2._3)

  implicit def xY4ToData(xy: Pair[Seq[Double], Product4[Y, Y, Y, Y]]): XYData = xy._1 -> Seq(xy._2._1, xy._2._2, xy._2._3, xy._2._4)

  implicit def xY5ToData(xy: Pair[Seq[Double], Product5[Y, Y, Y, Y, Y]]): XYData = xy._1 -> Seq(xy._2._1, xy._2._2, xy._2._3, xy._2._4, xy._2._5)

  implicit def xY6ToData(xy: Pair[Seq[Double], Product6[Y, Y, Y, Y, Y, Y]]): XYData = xy._1 -> Seq(xy._2._1, xy._2._2, xy._2._3, xy._2._4, xy._2._5, xy._2._6)

  type Func = Double => Double

  def Yfs(fs: Iterable[Double => Double]): Seq[Y] = fs.map(f => Yf(f)).toSeq

  def Yfs(fs: Func*): Seq[Y] = fs.map(f => Yf(f)).toSeq

  implicit def xYsToData(xys: Pair[Seq[Double], Iterable[Y]]): XYData = new XYData(xys._2.map(y => series(xys._1, y)).toSeq: _*)

  implicit def xYSeqToData(xys: Iterable[Pair[Seq[Double], Y]]): XYData = new XYData(xys.map(xy => series(xy._1, xy._2)).toSeq: _*)

  implicit def data(xs: Seq[Double], ys: Y*): XYData = new XYData(ys.map(y => series(xs, y)).toSeq: _*)
}

object BarDataImplicits extends BarDataImplicits

trait BarChartImplicits extends BarDataImplicits {
  implicit def dataToChart(d: XYData): XYChart = new XYChart(d)

  implicit def stringToOptionString(string: String): Option[String] = if (string.isEmpty) None else Some(string)

  def Axis(label: String = "", backward: Boolean = false, log: Boolean = false,
           range: Option[(Double, Double)] = None): NumericAxis = {
    val a = new NumericAxis
    a.label = label
    if (backward) a.backward else a.forward
    if (log) a.log else a.linear
    range.foreach({
      case (min, max) => a.range_=(min -> max)
    })
    a
  }

  def bar(data: XYData, title: String = "",
          x: NumericAxis = new NumericAxis,
          y: NumericAxis = new NumericAxis,
          pointSize: Option[Double] = None,
          legendPosX: LegendPosX.Type = LegendPosX.Right,
          legendPosY: LegendPosY.Type = LegendPosY.Center,
          showLegend: Boolean = false,
          monochrome: Boolean = false,
          size: Option[(Double, Double)] = None
           ): XYChart = {
    val c = new XYChart(stringToOptionString(title), data, x, y)
    c.pointSize = pointSize
    c.legendPosX = legendPosX
    c.legendPosY = legendPosY
    c.showLegend = showLegend
    c.monochrome = monochrome
    c.size = size
    c
  }
}

object BarChartImplicits extends BarChartImplicits