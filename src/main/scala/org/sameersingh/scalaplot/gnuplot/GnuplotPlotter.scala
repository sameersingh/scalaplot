package org.sameersingh.scalaplot.gnuplot

import org.sameersingh.scalaplot._
import collection.mutable.ArrayBuffer

/**
 * @author sameer
 * @date 10/25/12
 */
class GnuplotPlotter(chart: Chart) extends Plotter(chart) {

  var lines = new ArrayBuffer[String]
  var seriesPrefix = "plot"

  def plotChart(chart: Chart) {
    lines += "# Chart settings"
    chart.title.foreach(t => lines += "set title %s" format (t))
    chart.outputType.foreach(t => lines += "set terminal %s" format (t))
    chart.outputFilename.foreach(t => lines += "set output %s" format (t))
    chart.pointSize.foreach(t => lines += "set pointSize %f" format (t))
  }

  def plotXYChart(chart: XYChart) {
    lines += "# XYChart settings"
    lines += "set xlabel %s" format (chart.xlabel)
    lines += "set ylabel %s" format (chart.ylabel)
    if (chart.logX && chart.logY) lines += "set logscale"
    else if (chart.logX) lines += "set logscale x"
    else if (chart.logY) lines += "set logscale y"
    else lines += "set nologscale"
    if (chart.xrange.isEmpty && chart.yrange.isEmpty) lines += "set autoscale"
    if (chart.xrange.isDefined) lines += "set xrange [%f:%f]" format(chart.xrange.get._1, chart.xrange.get._2)
    if (chart.yrange.isDefined) lines += "set yrange [%f:%f]" format(chart.yrange.get._1, chart.yrange.get._2)
    plotXYData(chart.data)
  }

  def plotXYData(data: XYData) {
    var first = true
    lines += "# XYData Plotting"
    for (series: XYSeries <- data.serieses) {
      if (first) {
        seriesPrefix = "plot"
        first = false
      } else seriesPrefix = "replot"
      plotXYSeries(series)
    }
  }

  def plotXYSeries(series: XYSeries) {
    series match {
      case m: MemXYSeries => plotMemXYSeries(m)
      case f: FileXYSeries => plotFileXYSeries(f)
    }
  }

  def plotMemXYSeries(series: MemXYSeries) {
    // TODO write the series to a file? and then refer to it from the plotting script?
  }

  def plotFileXYSeries(series: FileXYSeries) {
    lines += "%s '%s' using %d:%d title %s with linepoints" format(seriesPrefix, series.dataFilename, series.xCol, series.yCol, series.seriesName)
  }

  def writeToPdf(filenamePrefix: String) {
    // write the description
    val scriptFile = filenamePrefix + ".gpl"
    lines.clear()
    plotChart(chart)
    chart match {
      case xyc: XYChart => plotXYChart(xyc)
    }
  }
}
