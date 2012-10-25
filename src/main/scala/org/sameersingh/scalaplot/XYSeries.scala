package org.sameersingh.scalaplot

import collection.mutable.ArrayBuffer

/**
 * @author sameer
 * @date 10/9/12
 */
abstract class XYSeries {
  var seriesName: String = ""
  // TODO
  var plotLine: Boolean = true
  var plotPoints: Boolean = true
  var lineColor: Option[String] = None
  var pointColor: Option[String] = None
  var every: Option[Int] = None
  var errCols: Option[Seq[Int]] = None
}

class FileXYSeries extends XYSeries {
  var xCol: Int = _
  var yCol: Int = _
  var dataFilename: String = ""
}

class MemXYSeries extends XYSeries {
  var xs: Seq[Double] = _
  var ys: Seq[Double] = _
}
