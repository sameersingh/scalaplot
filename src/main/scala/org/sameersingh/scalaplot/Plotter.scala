package org.sameersingh.scalaplot

/**
 * @author sameer
 * @date 10/25/12
 */
abstract class Plotter(val chart: Chart) {

  def writeToPdf(filenamePrefix: String): Unit

  def plotChart(chart: Chart): Unit

  def plotXYChart(chart: XYChart): Unit

  def plotXYData(data: XYData): Unit

  def plotXYSeries(series: XYSeries): Unit

  def plotMemXYSeries(series: MemXYSeries): Unit

  def plotFileXYSeries(series: FileXYSeries): Unit
}
