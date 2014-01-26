package org.sameersingh.scalaplot

import collection.mutable.ArrayBuffer
import java.io.PrintWriter

/**
 * @author sameer
 * @date 10/9/12
 */
abstract class XYSeries {
  def name: String

  var plotStyle: XYPlotStyle.Type = XYPlotStyle.LinesPoints
  var color: Option[Color.Type] = None
  // point config
  var pointSize: Option[Double] = None
  var pointType: Option[PointType.Type] = None
  // line config
  var lineWidth: Option[Double] = None
  var lineType: Option[LineType.Type] = None
  // how many points to skip between plots
  var every: Option[Int] = None

  def points: Iterable[(Double, Double)]
}

class FileXYSeries(val xcol: Int, val ycol: Int, val name: String, val dataFilename: String) extends XYSeries {
  def points = throw new Error("not implemented")
}

class MemXYSeries(val xs: Seq[Double], val ys: Seq[Double], val name: String) extends XYSeries {
  def this(points: Seq[(Double, Double)], name: String) = this(points.map(_._1), points.map(_._2), name)

  assert(xs.length == ys.length)

  def isLarge: Boolean = xs.length > 1000

  def writeToFile(filename: String) {
    val writer = new PrintWriter(filename)
    for (i <- 0 until xs.length)
      writer.println(xs(i).toString + "\t" + ys(i).toString)
    writer.flush()
    writer.close()
  }

  def toStrings(): Seq[String] = (0 until xs.length).map(i => xs(i).toString + "\t" + ys(i).toString)

  def points = xs.zip(ys).seq
}

object XYPlotStyle extends Enumeration {
  type Type = Value
  val Lines, Points, LinesPoints, Dots, Impulses = Value
}

object LineType extends Enumeration {
  type Type = Value
  val Solid = Value
}

object PointType extends Enumeration {
  type Type = Value
  val Dot, +, X, *, emptyBox, fullBox, emptyO, fullO, emptyTri, fullTri = Value
}

object Color extends Enumeration {
  type Type = Value
  val Black, Grey, Red, Green, Blue, Magenta, Cyan, Maroon, Mustard, RoyalBlue, Gold, DarkGreen, Purple, SteelBlue, Yellow = Value
}

