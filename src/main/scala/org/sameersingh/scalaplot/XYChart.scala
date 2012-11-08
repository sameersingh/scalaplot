package org.sameersingh.scalaplot

import collection.mutable.{ArrayBuffer, Buffer}

/**
 * @author sameer
 * @date 10/9/12
 */
class XYChart(chartTitle: Option[String], val data: XYData) extends Chart {

  def this(chartTitle:String, data:XYData) = this(Some(chartTitle), data)
  def this(data:XYData) = this(None, data)

  override def title = chartTitle

  var xrange: Pair[Option[Double], Option[Double]] = (None, None)
  var yrange: Pair[Option[Double], Option[Double]] = (None, None)
  var reverseX: Boolean = false
  var reverseY: Boolean = false
  var logX: Boolean = false
  var logY: Boolean = false
}
