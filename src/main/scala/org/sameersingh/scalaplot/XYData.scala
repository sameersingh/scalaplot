package org.sameersingh.scalaplot

import collection.mutable.ArrayBuffer

/**
 * @author sameer
 * @date 10/9/12
 */
class XYData(ss: XYSeries*) extends Data {
  //def this() = this(ss)

  val _serieses = new ArrayBuffer[XYSeries]()
  _serieses ++= ss

  def serieses: Seq[XYSeries] = _serieses

  def +=(s: XYSeries) = _serieses += s
}

trait XYDataImplicits extends XYSeriesImplicits {
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

object XYDataImplicits extends XYDataImplicits