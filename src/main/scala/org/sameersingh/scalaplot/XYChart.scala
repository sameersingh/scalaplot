package org.sameersingh.scalaplot

import collection.mutable.{ArrayBuffer, Buffer}

/**
 * @author sameer
 * @date 10/9/12
 */
class NumericAxis {
  private var _label: String = ""
  private var _range: Pair[Option[Double], Option[Double]] = (None, None)
  protected var _reverse: Boolean = false
  private var _log: Boolean = false

  def label = _label

  def label_=(l: String) = _label = l

  def isBackward = _reverse

  def backward = _reverse = true

  def forward = _reverse = false

  def isLog = _log

  def log = _log = true

  def linear = _log = false

  def range_=(minMax: Pair[Double, Double]) = _range = (Some(minMax._1), Some(minMax._2))

  def min = _range._1

  def max = _range._2

  def resetRange = _range = (None, None)
}

class XYChart(chartTitle: Option[String], val data: XYData,
              val x: NumericAxis = new NumericAxis, val y: NumericAxis = new NumericAxis) extends Chart {

  def this(chartTitle: String, data: XYData) = this(Some(chartTitle), data)

  def this(data: XYData) = this(None, data)

  override def title = chartTitle
}

trait XYChartImplicits extends XYDataImplicits {
  implicit def dataToChart(d: XYData): XYChart = new XYChart(d)

  implicit def stringToOptionString(string: String): Option[String] = if (string.isEmpty) None else Some(string)

  def Axis(label: String = "", backward: Boolean = false, log: Boolean = false,
           range: Option[(Double, Double)] = None): NumericAxis = {
    val a = new NumericAxis
    a.label = label
    if (backward) a.backward else a.forward
    if (log) a.log else a.linear
    range.foreach({
      case (min, max) => a.range_=(min -> max)
    })
    a
  }

  def plot(data: XYData, title: String = "",
           x: NumericAxis = new NumericAxis,
           y: NumericAxis = new NumericAxis): XYChart = new XYChart(stringToOptionString(title), data, x, y)
}

object XYChartImplicits extends XYChartImplicits