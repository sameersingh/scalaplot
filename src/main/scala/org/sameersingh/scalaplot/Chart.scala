package org.sameersingh.scalaplot

import collection.mutable.{ArrayBuffer, Buffer}

/**
 * @author sameer
 * @date 10/9/12
 */
trait Data

abstract class Chart {
  def title: Option[String] = None

  var pointSize: Option[Double] = None

  var legendPosX: LegendPosX.Type = LegendPosX.Right

  var legendPosY: LegendPosY.Type = LegendPosY.Center

  var showLegend: Boolean = false

  var monochrome: Boolean = false

  var size: Option[(Double, Double)] = None
}

object LegendPosX extends Enumeration {
  type Type = Value
  val Left, Center, Right = Value
}

object LegendPosY extends Enumeration {
  type Type = Value
  val Top, Center, Bottom = Value
}

