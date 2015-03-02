package org.sameersingh.scalaplot

import Style._
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

class MemXYSeries(val xs: Seq[Double], val ys: Seq[Double], val name: String = "Label") extends XYSeries {
  def this(points: Seq[(Double, Double)], name: String) = this(points.map(_._1), points.map(_._2), name)

  def this(points: Seq[(Double, Double)]) = this(points, "Label")

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

trait XYSeriesImplicits {

  trait YPoints {
    def xsToYs(xs: Seq[Double]): Seq[Double]
  }

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

object XYSeriesImplicits extends XYSeriesImplicits