package org.sameersingh.scalaplot

import collection.mutable.ArrayBuffer

/**
 * @author sameer
 * @date 10/9/12
 */
class XYSeries {
  var xCol: Int = _
  var yCol: Int = _
  var seriesName: String = ""
  // TODO
  var plotLine: Boolean = true
  var plotPoints: Boolean = true
  var lineColor: Option[String] = None
  var pointColor: Option[String] = None
  var dataFilename: String = ""
  var every: Option[Int] = None

  def gnuplotDescription(): String = {
    "plot '%s' using %d:%d title %s with linepoints" format(dataFilename, xCol, yCol, seriesName)
  }
}
