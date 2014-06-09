package org.sameersingh.scalaplot

import collection.mutable.ArrayBuffer

/**
 * @author sameer
 * @date 10/9/12
 */
class XYData(ss: XYSeries*) {
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

  type Func = Double => Double

  def Yfs(fs: Iterable[Double => Double]): Seq[Y] = fs.map(f => Yf(f)).toSeq

  def Yfs(fs: Func*): Seq[Y] = fs.map(f => Yf(f)).toSeq

  implicit def xYsToData(xys: Pair[Seq[Double], Iterable[Y]]): XYData = new XYData(xys._2.map(y => series(xys._1, y)).toSeq: _*)

  implicit def data(xs: Seq[Double], ys: Y*): XYData = new XYData(ys.map(y => series(xs, y)).toSeq: _*)
}

object XYDataImplicits extends XYDataImplicits