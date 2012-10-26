package org.sameersingh.scalaplot

import collection.mutable.{ArrayBuffer, Buffer}

/**
 * @author sameer
 * @date 10/9/12
 */
abstract class Chart {
  def title: Option[String] = None

  var pointSize: Option[Double] = None

  var legendPosX: LegendPosX.Type = LegendPosX.Right

  var legendPosY: LegendPosY.Type = LegendPosY.Center

  var showLegend: Boolean = false

  var monochrome: Boolean = false
}

object LegendPosX extends Enumeration {
  type Type = Value
  val Left, Center, Right = Value
}

object LegendPosY extends Enumeration {
  type Type = Value
  val Top, Center, Bottom = Value
}

