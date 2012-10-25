package org.sameersingh.scalaplot

import collection.mutable.{ArrayBuffer, Buffer}

/**
 * @author sameer
 * @date 10/9/12
 */
class XYChart extends Chart {
  var xrange: Option[Pair[Double, Double]] = None
  var yrange: Option[Pair[Double, Double]] = None
  var logX: Boolean = false
  var logY: Boolean = false
  var xlabel: String = ""
  var ylabel: String = ""
  var data: XYData = _
  // TODO

}
