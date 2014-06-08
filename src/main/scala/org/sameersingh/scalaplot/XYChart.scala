package org.sameersingh.scalaplot

import collection.mutable.{ArrayBuffer, Buffer}

/**
 * @author sameer
 * @date 10/9/12
 */
trait NumericX {
  private var _xrange: Pair[Option[Double], Option[Double]] = (None, None)
  protected var _reverseX: Boolean = false
  private var _logX: Boolean = false

  def isBackwardX = _reverseX

  def backwardX = _reverseX = true

  def forwardX = _reverseX = false

  def isLogX = _logX

  def logX = _logX = true

  def linearX = _logX = false

  def xrange_=(min: Double, max: Double) = _xrange = (Some(min), Some(max))

  def minX = _xrange._1

  def maxX = _xrange._2

  def resetXRange = _xrange = (None, None)
}

trait NumericY {
  private var _yrange: Pair[Option[Double], Option[Double]] = (None, None)
  private var _reverseY: Boolean = false
  private var _logY: Boolean = false

  def isBackwardY = _reverseY

  def backwardY = _reverseY = true

  def forwardY = _reverseY = false

  def isLogY = _logY

  def logY = _logY = true

  def linearY = _logY = false

  def yrange_=(min: Double, max: Double) = _yrange = (Some(min), Some(max))

  def minY = _yrange._1

  def maxY = _yrange._2

  def resetYRange = _yrange = (None, None)

}

class XYChart(chartTitle: Option[String], val data: XYData) extends Chart with NumericX with NumericY {

  def this(chartTitle: String, data: XYData) = this(Some(chartTitle), data)

  def this(data: XYData) = this(None, data)

  private var _xlabel: String = ""
  private var _ylabel: String = ""

  override def title = chartTitle

  def xlabel = _xlabel

  def xlabel_=(xl: String) = _xlabel = xl

  def ylabel = _ylabel

  def ylabel_=(yl: String) = _ylabel = yl
}

trait XYChartImplicits extends XYDataImplicits {
  implicit def dataToChart(d: XYData): XYChart = new XYChart(d)

  implicit def stringToOptionString(string: String): Option[String] = if(string.isEmpty) None else Some(string)

  def plot(data: XYData, title: String = ""): XYChart = new XYChart(stringToOptionString(title), data)
}

object XYChartImplicits extends XYChartImplicits