package org.sameersingh.scalaplot

import java.io.File

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
