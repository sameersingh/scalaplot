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

  override def gnuplotDescription(): Buffer[String] = {
    val lines = super.gnuplotDescription()
    lines += "# XYChart settings"
    lines += "set xlabel %s" format (xlabel)
    lines += "set ylabel %s" format (ylabel)
    if (logX && logY) lines += "set logscale"
    else if (logX) lines += "set logscale x"
    else if (logY) lines += "set logscale y"
    else lines += "set nologscale"
    if (xrange.isEmpty && yrange.isEmpty) lines += "set autoscale"
    if (xrange.isDefined) lines += "set xrange [%f:%f]" format(xrange.get._1, xrange.get._2)
    if (yrange.isDefined) lines += "set yrange [%f:%f]" format(yrange.get._1, yrange.get._2)
    lines ++= data.gnuplotDescription()
    lines
  }

}
