package org.sameersingh.scalaplot

import collection.mutable.{ArrayBuffer, Buffer}

/**
 * @author sameer
 * @date 10/9/12
 */
abstract class Chart {

  var title: Option[String] = None
  var outputType: Option[String] = None
  var outputFilename: Option[String] = None
  var pointSize: Option[Double] = None
  // TODO
  var legendPos: Option[Int] = None
  var showLegend: Boolean = false
}
