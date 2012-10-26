package org.sameersingh.scalaplot

import collection.mutable.ArrayBuffer

/**
 * @author sameer
 * @date 10/9/12
 */
class XYData(val xlabel: String, val ylabel: String, ss: Seq[XYSeries]) {
  def this(ss: XYSeries*) = this("x", "y", ss)

  val _serieses = new ArrayBuffer[XYSeries]()
  _serieses ++= ss

  def serieses: Seq[XYSeries] = _serieses

  def +=(s: XYSeries) = _serieses += s
}
