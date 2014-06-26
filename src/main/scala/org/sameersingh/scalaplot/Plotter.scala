package org.sameersingh.scalaplot

import org.sameersingh.scalaplot.gnuplot.GnuplotPlotter
import org.sameersingh.scalaplot.jfreegraph.JFGraphPlotter

/**
 * @author sameer
 * @date 10/25/12
 */
abstract class Plotter(val chart: Chart) {

  def pdf(directory: String, filenamePrefix: String): Unit

  def png(directory: String, filenamePrefix: String): Unit = throw new Error("png() not implemented")

  def svg(directory: String, filenamePrefix: String): String = throw new Error("svg() not implemented")

  def gui(): Unit = throw new Error("gui() not implemented")
}

trait PlotterImplicits {

  trait OutputSpecification

  case class PDF(dir: String, fnamePrefix: String) extends OutputSpecification

  case class PNG(dir: String, fnamePrefix: String) extends OutputSpecification

  object ASCII extends OutputSpecification

  object SVG extends OutputSpecification

  object GUI extends OutputSpecification

  private def defaultPlotter(c: Chart) = new GnuplotPlotter(c)

  def output(output: OutputSpecification, chart: Chart): String = output match {
    case PDF(dir, fnamePrefix) => {
      defaultPlotter(chart).pdf(dir, fnamePrefix)
      ""
    }
    case PNG(dir, fnamePrefix) => {
      defaultPlotter(chart).png(dir, fnamePrefix)
      ""
    }
    case GUI => {
      val gpl = new JFGraphPlotter(chart)
      gpl.gui()
      ""
    }
    case ASCII => {
      val file = java.io.File.createTempFile("scalaplot", System.currentTimeMillis().toString)
      file.delete()
      file.mkdir()
      defaultPlotter(chart).string(file.getCanonicalPath + "/", "ascii")
    }
    case SVG => {
      val file = java.io.File.createTempFile("scalaplot", System.currentTimeMillis().toString)
      file.delete()
      file.mkdir()
      defaultPlotter(chart).svg(file.getCanonicalPath + "/", "svg")
    }
  }

}
