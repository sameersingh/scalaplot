package org.sameersingh.scalaplot

import collection.mutable.{ArrayBuffer, Buffer}

/**
 * @author sameer
 * @date 10/9/12
 */
class XYChart(chartTitle: Option[String], val data: XYData) extends Chart {

  def this(chartTitle: String, data: XYData) = this(Some(chartTitle), data)

  def this(data: XYData) = this(None, data)

  private var _xlabel: String = ""
  private var _ylabel: String = ""
  private var _xrange: Pair[Option[Double], Option[Double]] = (None, None)
  private var _yrange: Pair[Option[Double], Option[Double]] = (None, None)
  private var _reverseX: Boolean = false
  private var _reverseY: Boolean = false
  private var _logX: Boolean = false
  private var _logY: Boolean = false

  override def title = chartTitle

  def xlabel = _xlabel

  def xlabel_=(xl: String) = _xlabel = xl

  def ylabel = _ylabel

  def ylabel_=(yl: String) = _ylabel = yl

  def isBackwardX = _reverseX

  def isBackwardY = _reverseY

  def backwardX = _reverseX = true

  def forwardX = _reverseX = false

  def backwardY = _reverseY = true

  def forwardY = _reverseY = false

  def isLogX = _logX

  def isLogY = _logY

  def logX = _logX = true

  def linearX = _logX = false

  def logY = _logY = true

  def linearY = _logY = false

  def xrange_=(min: Double, max: Double) = _xrange = (Some(min), Some(max))

  def minX = _xrange._1

  def maxX = _xrange._2

  def resetXRange = _xrange = (None, None)

  def yrange_=(min: Double, max: Double) = _yrange = (Some(min), Some(max))

  def minY = _yrange._1

  def maxY = _yrange._2

  def resetYRange = _yrange = (None, None)
}
