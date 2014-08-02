package org.sameersingh.scalaplot

/**
 * @author sameer
 * @since 8/2/14.
 */
object Style {
  object LineType extends Enumeration {
    type Type = Value
    val Solid = Value
  }

  object PointType extends Enumeration {
    type Type = Value
    val Dot, +, X, *, emptyBox, fullBox, emptyO, fullO, emptyTri, fullTri = Value
  }

  object Color extends Enumeration {
    type Type = Value
    val Black, Grey, Red, Green, Blue, Magenta, Cyan, Maroon, Mustard, RoyalBlue, Gold, DarkGreen, Purple, SteelBlue, Yellow = Value
  }

  object FillStyle extends Enumeration {
    type Type = Value
    val Empty, Solid, Pattern = Value
  }
}
