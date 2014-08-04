package org.sameersingh.scalaplot

/**
 * @author sameer
 * @since 6/8/14.
 */
class GlobalImplicits {
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

  implicit def stringToOptionString(string: String): Option[String] = if (string.isEmpty) None else Some(string)

  implicit def anyToOptionAny[A](a: A): Option[A] = Some(a)

}

object GlobalImplicits extends GlobalImplicits

object Implicits extends GlobalImplicits
  with XYDataImplicits with XYChartImplicits with XYSeriesImplicits
  with BarDataImplicits with BarChartImplicits with BarSeriesImplicits
  with PlotterImplicits