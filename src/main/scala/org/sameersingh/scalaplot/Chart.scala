package org.sameersingh.scalaplot

import collection.mutable.{ArrayBuffer, Buffer}

/**
 * @author sameer
 * @date 10/9/12
 */
abstract class Chart {
  def title: Option[String] = None

  def pointSize: Option[Double] = None

  def legendPosX: LegendPosX.Type = LegendPosX.Right

  def legendPosY: LegendPosY.Type = LegendPosY.Center

  def showLegend: Boolean = false

  def monochrome: Boolean = false
}

object LegendPosX extends Enumeration {
  type Type = Value
  val Left, Center, Right = Value
}

object LegendPosY extends Enumeration {
  type Type = Value
  val Top, Center, Bottom = Value
}

