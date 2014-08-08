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
              ss: Seq[BarSeries]) extends Data {
  def this(series: BarSeries*) = this(_.toString, series)

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

  case class Bar(yp: Seq[Double],
                 label: String = "Label",
                 color: Option[Color.Type] = None,
                 fillStyle: Option[FillStyle.Type] = None,
                 density: Option[Double] = None,
                 pattern: Option[Int] = None,
                 border: Option[Boolean] = None,
                 borderLineType: Option[LineType.Type] = None)

  def series(y: Bar): BarSeries = {
    val s = new MemBarSeries(y.yp, y.label)
    s.color = y.color
    s.fillStyle = y.fillStyle
    s.density = y.density
    s.pattern = y.pattern
    s.border = y.border
    s.borderLineType = y.borderLineType
    s
  }

  implicit def seqToBar(ys: Seq[Double]): Bar = Bar(ys.toSeq)

  implicit def seqToSeries(ys: Seq[Double]): BarSeries = new MemBarSeries(ys.toSeq)

  implicit def barToSeries(bar: Bar): BarSeries = series(bar)
}

object BarSeriesImplicits extends BarSeriesImplicits

trait BarDataImplicits extends BarSeriesImplicits {
  def data(names: Int => String, ss: Iterable[BarSeries]): BarData = new BarData(names, ss.toSeq)

  implicit def seriesSeqToBarData(ss: Iterable[BarSeries]): BarData = new BarData(ss = ss.toSeq)

  implicit def barSeriesSeqToBarData(ss: Iterable[Bar]): BarData = new BarData(ss = ss.map(b => series(b)).toSeq)

  implicit def seriesToBarData(s: BarSeries): BarData = seriesSeqToBarData(Seq(s))

  implicit def barSeriesSeqToSeqBarSeries(ss: Iterable[Bar]): Iterable[BarSeries] = ss.map(b => series(b)).toSeq

  implicit def seriesToSeqBarSeries(s: BarSeries): Iterable[BarSeries] = Seq(s)

  // seq of double
  implicit def seqYToData(y: Seq[Double]): BarData = seqToSeries(y)

  implicit def seqY2ToData(ys: Product2[Seq[Double], Seq[Double]]): BarData = seriesSeqToBarData(Seq(barToSeries(ys._1), barToSeries(ys._2)))

  implicit def seqY3ToData(ys: Product3[Seq[Double], Seq[Double], Seq[Double]]): BarData = seriesSeqToBarData(Seq(barToSeries(ys._1), barToSeries(ys._2), barToSeries(ys._3)))

  implicit def seqY4ToData(ys: Product4[Seq[Double], Seq[Double], Seq[Double], Seq[Double]]): BarData = seriesSeqToBarData(Seq(barToSeries(ys._1), barToSeries(ys._2), barToSeries(ys._3), barToSeries(ys._4)))

  implicit def seqY5ToData(ys: Product5[Seq[Double], Seq[Double], Seq[Double], Seq[Double], Seq[Double]]): BarData = seriesSeqToBarData(Seq(barToSeries(ys._1), barToSeries(ys._2), barToSeries(ys._3), barToSeries(ys._4), barToSeries(ys._5)))

  implicit def seqY6ToData(ys: Product6[Seq[Double], Seq[Double], Seq[Double], Seq[Double], Seq[Double], Seq[Double]]): BarData = seriesSeqToBarData(Seq(barToSeries(ys._1), barToSeries(ys._2), barToSeries(ys._3), barToSeries(ys._4), barToSeries(ys._5), barToSeries(ys._6)))

  implicit def seqYsToData(ys: Seq[Seq[Double]]): BarData = ys.map(y => series(y))

  // names => seq of double
  implicit def namedSeqYToData(ny: Product2[Int => String, Seq[Double]]): BarData = data(ny._1, Seq(ny._2))

  implicit def namedSeqY2ToData(nys: Product2[Int => String, Product2[Seq[Double], Seq[Double]]]): BarData =
    data(nys._1, Seq(barToSeries(nys._2._1), barToSeries(nys._2._2)))

  implicit def namedSeqY3ToData(nys: Product2[Int => String, Product3[Seq[Double], Seq[Double], Seq[Double]]]): BarData =
    data(nys._1, Seq(barToSeries(nys._2._1), barToSeries(nys._2._2), barToSeries(nys._2._3)))

  implicit def namedSeqY4ToData(nys: Product2[Int => String, Product4[Seq[Double], Seq[Double], Seq[Double], Seq[Double]]]): BarData =
    data(nys._1, Seq(barToSeries(nys._2._1), barToSeries(nys._2._2), barToSeries(nys._2._3), barToSeries(nys._2._4)))

  implicit def namedSeqY5ToData(nys: Product2[Int => String, Product5[Seq[Double], Seq[Double], Seq[Double], Seq[Double], Seq[Double]]]): BarData =
    data(nys._1, Seq(barToSeries(nys._2._1), barToSeries(nys._2._2), barToSeries(nys._2._3), barToSeries(nys._2._4), barToSeries(nys._2._5)))

  implicit def namedSeqY6ToData(nys: Product2[Int => String, Product6[Seq[Double], Seq[Double], Seq[Double], Seq[Double], Seq[Double], Seq[Double]]]): BarData =
    data(nys._1, Seq(barToSeries(nys._2._1), barToSeries(nys._2._2), barToSeries(nys._2._3), barToSeries(nys._2._4), barToSeries(nys._2._5), barToSeries(nys._2._6)))

  implicit def namedSeqYsToData(nys: Product2[Int => String, Seq[Seq[Double]]]): BarData = data(nys._1, nys._2.map(y => barToSeries(y)))

  // seq of Bars
  implicit def barToData(y: Bar): BarData = series(y)

  implicit def bar2ToData(ys: Product2[Bar, Bar]): BarData = Seq(ys._1, ys._2)

  implicit def bar3ToData(ys: Product3[Bar, Bar, Bar]): BarData = Seq(ys._1, ys._2, ys._3)

  implicit def bar4ToData(ys: Product4[Bar, Bar, Bar, Bar]): BarData = Seq(ys._1, ys._2, ys._3, ys._4)

  implicit def bar5ToData(ys: Product5[Bar, Bar, Bar, Bar, Bar]): BarData = Seq(ys._1, ys._2, ys._3, ys._4, ys._5)

  implicit def bar6ToData(ys: Product6[Bar, Bar, Bar, Bar, Bar, Bar]): BarData = Seq(ys._1, ys._2, ys._3, ys._4, ys._5, ys._6)

  // names => seq of Bars
  implicit def namedBarToData(ny: Product2[Int => String, Bar]): BarData = data(ny._1, Seq(ny._2))

  implicit def namedBar2ToData(nys: Product2[Int => String, Product2[Bar, Bar]]): BarData = data(nys._1, Seq(nys._2._1, nys._2._2))

  implicit def namedBar3ToData(nys: Product2[Int => String, Product3[Bar, Bar, Bar]]): BarData = data(nys._1, Seq(nys._2._1, nys._2._2, nys._2._3))

  implicit def namedBar4ToData(nys: Product2[Int => String, Product4[Bar, Bar, Bar, Bar]]): BarData = data(nys._1, Seq(nys._2._1, nys._2._2, nys._2._3, nys._2._4))

  implicit def namedBar5ToData(nys: Product2[Int => String, Product5[Bar, Bar, Bar, Bar, Bar]]): BarData = data(nys._1, Seq(nys._2._1, nys._2._2, nys._2._3, nys._2._4, nys._2._5))

  implicit def namedBar6ToData(nys: Product2[Int => String, Product6[Bar, Bar, Bar, Bar, Bar, Bar]]): BarData = data(nys._1, Seq(nys._2._1, nys._2._2, nys._2._3, nys._2._4, nys._2._5, nys._2._6))

  implicit def namedBarsToData(nys: Product2[Int => String, Seq[Bar]]): BarData = data(nys._1, nys._2.map(b => barToSeries(b)))
}

object BarDataImplicits extends BarDataImplicits

trait BarChartImplicits extends BarDataImplicits {
  implicit def dataToChart(d: BarData): BarChart = new BarChart(d)

  def barChart(data: BarData, title: String = "",
               xLabel: String = "",
               y: NumericAxis = new NumericAxis,
               pointSize: Option[Double] = None,
               legendPosX: LegendPosX.Type = LegendPosX.Right,
               legendPosY: LegendPosY.Type = LegendPosY.Center,
               showLegend: Boolean = false,
               monochrome: Boolean = false,
               size: Option[(Double, Double)] = None
                ): BarChart = {
    val c = new BarChart(GlobalImplicits.stringToOptionString(title), data, {
      val d = new DiscreteAxis();
      d.label = xLabel;
      d
    }, y)
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