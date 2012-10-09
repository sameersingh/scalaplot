package sameersingh.scalaplot

import collection.mutable.ArrayBuffer

/**
 * @author sameer
 * @date 10/9/12
 */
class XYData {

  var serieses: Seq[XYSeries] = _

  def gnuplotDescription(): Seq[String] = {
    val lines = new ArrayBuffer[String]
    var first = true
    lines += "# XYData Plotting"
    for (series: XYSeries <- serieses)
      if (first) {
        lines += series.gnuplotDescription()
        first = false
      } else lines += "re" + series.gnuplotDescription()
    lines
  }

}
