package sameersingh.scalaplot

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

  def gnuplotDescription(): Buffer[String] = {
    val lines = new ArrayBuffer[String]
    lines += "# Chart settings"
    title.foreach(t => lines += "set title %s" format (t))
    outputType.foreach(t => lines += "set terminal %s" format (t))
    outputFilename.foreach(t => lines += "set output %s" format (t))
    pointSize.foreach(t => lines += "set pointSize %f" format (t))
    lines
  }
}
