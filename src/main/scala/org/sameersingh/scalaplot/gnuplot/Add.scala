package org.sameersingh.scalaplot.gnuplot

/**
 * @author sameer
 * @date 10/9/12
 */
object Add {

  val alpha = "{/Symbol a}"
  val Alpha = "{/Symbol A}"
  val beta = "{/Symbol b}"
  val Beta = "{/Symbol B}"
  val delta = "{/Symbol d}"
  val Delta = "{/Symbol D}"
  val phi = "{/Symbol f}"
  val Phi = "{/Symbol F}"
  val gamma = "{/Symbol g}"
  val Gamma = "{/Symbol G}"
  val lambda = "{/Symbol l}"
  val Lambda = "{/Symbol L}"
  val pi = "{/Symbol p}"
  val Pi = "{/Symbol P}"
  val theta = "{/Symbol q}"
  val Theta = "{/Symbol Q}"
  val tau = "{/Symbol t}"
  val Tau = "{/Symbol T}"

  def func(function: String): Seq[String] = {
    Seq("replot %s" format (function))
  }

  def label(labelStr: String, xpos: Double, ypos: Double): Seq[String] = {
    Seq("set label \"%s\" at %f,%f" format(labelStr, xpos, ypos))
  }

  def arrow(x1: Double, y1: Double, x2: Double, y2: Double): Seq[String] = {
    Seq("set arrow from %f,%f to %f,%f" format(x1, y1, x2, y2))
  }

}
