package org.sameersingh.scalaplot

import collection.mutable.ArrayBuffer
import java.io.PrintWriter

/**
 * @author sameer
 * @date 10/9/12
 */
abstract class XYSeries {
  def seriesName: String

  var plotStyle: XYPlotStyle.Type = XYPlotStyle.LinesPoints
  var color: Option[Color.Type] = None
  // point config
  var pointSize: Option[Double] = None
  var pointType: Option[PointType.Type] = None
  // line config
  var lineWidth: Option[Double] = None
  var lineType: Option[LineType.Type] = None
}

class FileXYSeries(val xcol: Int, val ycol: Int, val seriesName: String, val dataFilename: String) extends XYSeries {

}

class MemXYSeries(val xs: Seq[Double], val ys: Seq[Double], val seriesName: String) extends XYSeries {
  assert(xs.length == ys.length)

  def isLarge: Boolean = xs.length > 1000

  def writeToFile(filename: String) {
    val writer = new PrintWriter(filename)
    for (i <- 0 until xs.length)
      writer.println("%f\t%f" format(xs(i), ys(i)))
    writer.flush()
    writer.close()
  }

  def toStrings(): Seq[String] = (0 until xs.length).map(i => "%f\t%f" format(xs(i), ys(i)))
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

