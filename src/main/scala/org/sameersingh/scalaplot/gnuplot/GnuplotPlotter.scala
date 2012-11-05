package org.sameersingh.scalaplot.gnuplot

import org.sameersingh.scalaplot._
import collection.mutable.ArrayBuffer
import java.io.PrintWriter

/**
 * @author sameer
 * @date 10/25/12
 */
class GnuplotPlotter(chart: Chart) extends Plotter(chart) {

  var lines = new ArrayBuffer[String]
  var outputFilename: String = ""
  var isFirst = false
  var isLast = false

  protected def getColorname(color: Color.Type): String = {
    import Color._
    color match {
      case Black => "black"
      case Grey => "dark-grey"
      case Red => "red"
      case Green => "web-green"
      case Blue => "web-blue"
      case Magenta => "dark-magenta"
      case Cyan => "dark-cyan"
      case Maroon => "dark-orange"
      case Mustard => "dark-yellow"
      case RoyalBlue => "royalblue"
      case Gold => "goldenrod"
      case DarkGreen => "dark-spring-green"
      case Purple => "purple"
      case SteelBlue => "steelblue"
      case Yellow => "yellow"
    }
  }

  protected def getPointType(pt: PointType.Type): Int = {
    pt match {
      case PointType.Dot => 0
      case PointType.+ => 1
      case PointType.X => 2
      case PointType.* => 3
      case PointType.emptyBox => 4
      case PointType.fullBox => 5
      case PointType.emptyO => 6
      case PointType.fullO => 7
      case PointType.emptyTri => 8
      case PointType.fullTri => 9
    }
  }

  protected def getLineStyle(s: XYSeries): String =
    getLineStyle(s.plotStyle, s.color, s.pointSize, s.pointType, s.lineWidth, s.lineType)

  protected def getLineStyle(plotStyle: XYPlotStyle.Type,
                             col: Option[Color.Type],
                             pointSize: Option[Double],
                             pointType: Option[PointType.Type],
                             lineWidth: Option[Double],
                             lineType: Option[LineType.Type]): String = {
    val sb = new StringBuffer()
    sb append "with "
    sb append (plotStyle match {
      case XYPlotStyle.Lines => "lines"
      case XYPlotStyle.LinesPoints => "linespoints"
      case XYPlotStyle.Points => "points"
      case XYPlotStyle.Dots => "dots"
      case XYPlotStyle.Impulses => "impulses"
    })
    if (col.isDefined)
      sb.append(" linecolor rgbcolor \"%s\"" format (getColorname(col.get)))
    if (pointSize.isDefined)
      sb.append(" pointsize %f" format (pointSize.get))
    if (pointType.isDefined)
      sb.append(" pointtype %d" format (getPointType(pointType.get)))
    if (lineWidth.isDefined)
      sb.append(" linewidth %f" format (lineWidth.get))
    // TODO line type
    sb.toString
  }

  def plotChart(chart: Chart) {
    lines += "# Chart settings"
    chart.title.foreach(t => lines += "set title \"%s\"" format (t))
    chart.pointSize.foreach(t => lines += "set pointSize %f" format (t))
    // legend
    if (chart.showLegend) {
      lines += "set key %s %s" format(chart.legendPosX.toString.toLowerCase, chart.legendPosY.toString.toLowerCase)
    } else lines += "unset key"
    lines += "set terminal dumb enhanced"
    lines += ""
  }

  def plotXYChart(chart: XYChart) {
    lines += "# XYChart settings"
    if (chart.logX && chart.logY) lines += "set logscale"
    else if (chart.logX) lines += "set logscale x"
    else if (chart.logY) lines += "set logscale y"
    else lines += "set nologscale"
    var xr1s = if (chart.xrange._1.isDefined) chart.xrange._1.get.toString else "*"
    var xr2s = if (chart.xrange._2.isDefined) chart.xrange._2.get.toString else "*"
    var yr1s = if (chart.yrange._1.isDefined) chart.yrange._1.get.toString else "*"
    var yr2s = if (chart.yrange._2.isDefined) chart.yrange._2.get.toString else "*"
    lines += "set xr [%s:%s] %sreverse" format(xr1s, xr2s, if (chart.reverseX) "" else "no")
    lines += "set yr [%s:%s] %sreverse" format(yr1s, yr2s, if (chart.reverseY) "" else "no")
    plotXYData(chart.data)
  }

  def plotXYData(data: XYData) {
    lines += "# XYData Plotting"
    lines += "set xlabel \"%s\"" format (data.xlabel)
    lines += "set ylabel \"%s\"" format (data.ylabel)
    lines += "plot \\"
    for (series: XYSeries <- data.serieses) {
      isFirst = (series == data.serieses.head)
      isLast = (series == data.serieses.last)
      plotXYSeries(series)
    }
    // store the data if required
    for (series: XYSeries <- data.serieses) {
      isFirst = (series == data.serieses.head)
      isLast = (series == data.serieses.last)
      postPlotXYSeries(series)
    }
    lines += ""
  }

  def plotXYSeries(series: XYSeries) {
    series match {
      case m: MemXYSeries => plotMemXYSeries(m)
      case f: FileXYSeries => plotFileXYSeries(f)
    }
  }

  def postPlotXYSeries(series: XYSeries) {
    series match {
      case m: MemXYSeries => postPlotMemXYSeries(m)
      case f: FileXYSeries => postPlotFileXYSeries(f)
    }
  }

  def plotMemXYSeries(series: MemXYSeries) {
    var suffix = if (isLast) "" else ", \\"
    var filename = if (series.isLarge) outputFilename + "-" + series.seriesName + ".dat" else "-"
    lines += "'%s' using %d:%d title \"%s\" %s %s" format(filename, 1, 2, series.seriesName, getLineStyle(series), suffix)
  }

  def plotFileXYSeries(series: FileXYSeries) {
    var suffix = if (isLast) "" else ", \\"
    lines += "'%s' using %d:%d title \"%s\" %s %s" format(series.dataFilename, series.xcol, series.ycol, series.seriesName, getLineStyle(series), suffix)
  }

  def postPlotMemXYSeries(series: MemXYSeries) {
    if (series.isLarge) {
      // write to file, then refer to it in the script
      series.writeToFile(outputFilename + "-" + series.seriesName + ".dat")
    } else {
      // write directly to the lines
      lines += "# %s" format (series.seriesName)
      lines ++= series.toStrings()
      lines += "end"
    }
  }

  def postPlotFileXYSeries(series: FileXYSeries) {}

  def writeToPdf(filenamePrefix: String) {
    // write the description
    val scriptFile = filenamePrefix + ".gpl"
    outputFilename = filenamePrefix + ".pdf"
    lines.clear()
    plotChart(chart)
    chart match {
      case xyc: XYChart => plotXYChart(xyc)
    }
    lines += "# Wrapup"
    var monochromeString = if (chart.monochrome) "monochrome" else ""
    lines += "set terminal pdf enhanced linewidth 5.0 %s" format (monochromeString)
    lines += "set output \"%s\"" format (outputFilename)
    lines += "refresh"
    lines += "unset output"
    val writer = new PrintWriter(scriptFile)
    for (line <- lines) {
      writer.println(line)
    }
    writer.close()
  }
}

object GnuplotPlotter {
  def pdf(chart: Chart, filePrefix: String): Unit = new GnuplotPlotter(chart).writeToPdf(filePrefix)
}
