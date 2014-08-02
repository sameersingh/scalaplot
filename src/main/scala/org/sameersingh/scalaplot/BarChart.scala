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

class MemBarSeries(val xs: Seq[Int], val ys: Seq[Double], val name: String = "Label") extends BarSeries {
  def this(points: Seq[(Int, Double)], name: String = "Label") = this(points.map(_._1), points.map(_._2), name)

  assert(xs.length == ys.length)

  def isLarge: Boolean = xs.length > 1000

  def writeToFile(filename: String, names: Int => String) {
    val writer = new PrintWriter(filename)
    for (i <- 0 until xs.length)
      writer.println(xs(i).toString + "\t" + ys(i).toString + "\t" + names(i))
    writer.flush()
    writer.close()
  }

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
