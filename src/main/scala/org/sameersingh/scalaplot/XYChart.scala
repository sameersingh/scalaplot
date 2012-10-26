package org.sameersingh.scalaplot

import collection.mutable.{ArrayBuffer, Buffer}

/**
 * @author sameer
 * @date 10/9/12
 */
class XYChart(val chartTitle: String, val data: XYData) extends Chart {

  override def title = Some(chartTitle)

  var xrange: Pair[Option[Double], Option[Double]] = (None, None)
  var yrange: Pair[Option[Double], Option[Double]] = (None, None)
  var reverseX: Boolean = false
  var reverseY: Boolean = false
  var logX: Boolean = false
  var logY: Boolean = false
}
