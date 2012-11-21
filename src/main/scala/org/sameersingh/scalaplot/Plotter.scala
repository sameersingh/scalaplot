package org.sameersingh.scalaplot

import java.io.File

/**
 * @author sameer
 * @date 10/25/12
 */
abstract class Plotter(val chart: Chart) {

  def writeToPdf(directory: String, filenamePrefix: String): Unit

  def writeToPdf(file: File): Unit = writeToPdf(file.getParent + "/", file.getName)

  def gui(): Unit = throw new Error("gui() not implemented")
}
