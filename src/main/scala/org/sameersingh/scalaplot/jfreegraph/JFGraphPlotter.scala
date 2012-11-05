package org.sameersingh.scalaplot.jfreegraph

import org.sameersingh.scalaplot._
import javax.swing.JFrame

/**
 * @author sameer
 * @date 11/5/12
 */
class JFGraphPlotter(chart: Chart) extends Plotter(chart) {
  def writeToPdf(filenamePrefix: String) {}

  def plotChart(chart: Chart) {}

  def plotXYChart(chart: XYChart) {}

  def plotXYData(data: XYData) {}

  def plotXYSeries(series: XYSeries) {}

  def plotMemXYSeries(series: MemXYSeries) {}

  def plotFileXYSeries(series: FileXYSeries) {}
}

object JFGraphPlotter {
  def gui(chart: Chart): Unit = {
    val frame = new JFrame(chart.title.mkString(""))
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setSize(640, 420)
    //frame.add(new ChartPanel(chart))
    frame.pack()
    frame.setVisible(true)
  }

}