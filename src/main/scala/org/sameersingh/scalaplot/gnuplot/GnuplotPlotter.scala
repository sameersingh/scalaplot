package org.sameersingh.scalaplot.gnuplot

import org.sameersingh.scalaplot._
import collection.mutable.ArrayBuffer
import java.io.{File, PrintWriter}

/**
 * @author sameer
 * @date 10/25/12
 */
class GnuplotPlotter(chart: Chart) extends Plotter(chart) {

  var lines = new ArrayBuffer[String]
  var directory: String = ""
  var filename: String = ""
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
    if (chart.isLogX && chart.isLogY) lines += "set logscale"
    else if (chart.isLogX) lines += "set logscale x"
    else if (chart.isLogY) lines += "set logscale y"
    else lines += "set nologscale"
    var xr1s = if (chart.minX.isDefined) chart.minX.get.toString else "*"
    var xr2s = if (chart.maxX.isDefined) chart.maxX.get.toString else "*"
    var yr1s = if (chart.minY.isDefined) chart.minY.get.toString else "*"
    var yr2s = if (chart.maxY.isDefined) chart.maxY.get.toString else "*"
    lines += "set xr [%s:%s] %sreverse" format(xr1s, xr2s, if (chart.isBackwardX) "" else "no")
    lines += "set yr [%s:%s] %sreverse" format(yr1s, yr2s, if (chart.isBackwardY) "" else "no")
    lines += "set xlabel \"%s\"" format (chart.xlabel)
    lines += "set ylabel \"%s\"" format (chart.ylabel)
    plotXYData(chart.data)
  }

  def plotXYData(data: XYData) {
    lines += "# XYData Plotting"
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
    var dataFilename = if (series.isLarge) filename + "-" + series.name + ".dat" else "-"
    var everyString = if (!series.every.isDefined) "" else "every %d" format (series.every.get)
    lines += "'%s' %s using %d:%d title \"%s\" %s %s" format(dataFilename, everyString, 1, 2, series.name, getLineStyle(series), suffix)
  }

  def plotFileXYSeries(series: FileXYSeries) {
    var suffix = if (isLast) "" else ", \\"
    var everyString = if (!series.every.isDefined) "" else "every %d" format (series.every.get)
    lines += "'%s' %s using %d:%d title \"%s\" %s %s" format(series.dataFilename, everyString, series.xcol, series.ycol, series.name, getLineStyle(series), suffix)
  }

  def postPlotMemXYSeries(series: MemXYSeries) {
    if (series.isLarge) {
      // write to file, then refer to it in the script
      series.writeToFile(directory + filename + "-" + series.name + ".dat")
    } else {
      // write directly to the lines
      lines += "# %s" format (series.name)
      lines ++= series.toStrings()
      lines += "end"
    }
  }

  def reset = {
    lines.clear
    directory = ""
    filename = ""
    isFirst = false
    isLast = false
  }

  def postPlotFileXYSeries(series: FileXYSeries) {}

  def writeToPdf(directory: String, filenamePrefix: String) {
    // write the description
    assert(new File(directory).isDirectory)
    assert(directory.endsWith("/"))
    reset
    this.directory = directory
    filename = filenamePrefix
    plotChart(chart)
    chart match {
      case xyc: XYChart => plotXYChart(xyc)
    }
    lines += "# Wrapup"
    var monochromeString = if (chart.monochrome) "monochrome" else ""
    var sizeString = if (chart.size.isDefined) "size %f,%f" format(chart.size.get._1, chart.size.get._2) else ""
    lines += "set terminal pdf enhanced linewidth 3.0 %s %s" format(monochromeString, sizeString)
    lines += "set output \"%s\"" format (filename + ".pdf")
    lines += "refresh"
    lines += "unset output"
    val scriptFile = directory + filenamePrefix + ".gpl"
    val writer = new PrintWriter(scriptFile)
    for (line <- lines) {
      writer.println(line)
    }
    writer.close()
  }
}

object GnuplotPlotter {
  def pdf(chart: Chart, directory:String, filePrefix: String): Unit = new GnuplotPlotter(chart).writeToPdf(directory, filePrefix)
}
