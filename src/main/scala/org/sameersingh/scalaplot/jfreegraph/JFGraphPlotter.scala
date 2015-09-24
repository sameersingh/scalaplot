package org.sameersingh.scalaplot.jfreegraph

import org.sameersingh.scalaplot._
import javax.swing.JFrame
import org.jfree.chart.{JFreeChart => JChart, ChartPanel, ChartFactory, ChartUtilities}
import org.jfree.data.xy.{XYSeries => XYS}
import org.jfree.data.xy.XYSeriesCollection
import com.itextpdf.text.pdf.DefaultFontMapper
import java.io.{FileOutputStream, BufferedOutputStream}
import com.itextpdf.text.Rectangle
import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfWriter
import java.awt.geom.Rectangle2D
import com.itextpdf.text.DocumentException
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.title.LegendTitle
import org.jfree.chart.annotations.XYTitleAnnotation
import org.jfree.chart.axis.LogarithmicAxis
import java.awt.Color
import org.jfree.ui.RectangleEdge

/**
 * @author sameer
 * @date 11/5/12
 */
class JFGraphPlotter(chart: Chart) extends Plotter(chart) {

  val width = 500
  val height = 400
  lazy val jchart = jfreeChart(chart)

  def jfreeChart(chart: Chart): JChart = {
    val jchart = chart match {
      case xyc: XYChart => plotXYChart(xyc)
    }
    jchart
  }

  def plotXYChart(xyc: XYChart): JChart = {
    val data = plotXYData(xyc.data)
    val jchart = ChartFactory.createXYLineChart(chart.title.getOrElse(""), xyc.x.label, xyc.y.label, data, PlotOrientation.VERTICAL, false, false, false)
    val plot = jchart.getXYPlot()
    plot.setBackgroundPaint(Color.white)
    // add legend
    if (xyc.showLegend) {
      val legendTitle = new LegendTitle(plot)
      val legendPosX = xyc.legendPosX match {
        case LegendPosX.Left => 0.1
        case LegendPosX.Center => 0.5
        case LegendPosX.Right => 0.9
      }
      val legendPosY = xyc.legendPosY match {
        case LegendPosY.Bottom => 0.1
        case LegendPosY.Center => 0.5
        case LegendPosY.Top => 0.9
      }
      legendTitle.setPosition(RectangleEdge.RIGHT)
      val ta = new XYTitleAnnotation(legendPosX, legendPosY, legendTitle)
      ta.setMaxWidth(0.48)
      plot.addAnnotation(ta)
    }
    // log axis
    if (xyc.x.isLog) plot.setDomainAxis(new LogarithmicAxis(plot.getDomainAxis.getLabel))
    if (xyc.y.isLog) plot.setRangeAxis(new LogarithmicAxis(plot.getRangeAxis.getLabel))
    // axis ranges
    // TODO
    jchart
  }

  def plotXYData(xydata: XYData): XYSeriesCollection = JFGraphPlotter.xyCollection(xydata)

  override def pdf(directory: String, filenamePrefix: String) {
    val mapper = new DefaultFontMapper
    val filename = directory + filenamePrefix + ".pdf"
    val out = new BufferedOutputStream(new FileOutputStream(filename))
    val pagesize = new Rectangle(width, height)
    val document = new Document(pagesize, 50, 50, 50, 50)
    try {
      val writer = PdfWriter.getInstance(document, out)
      document.addAuthor("Sameer Singh")
      document.addSubject("Plotting Using JFreeChart and ScalaPlot")
      document.open()
      val cb = writer.getDirectContent()
      val tp = cb.createTemplate(width, height)
      val g2 = tp.createGraphics(width, height, mapper)
      val r2D = new Rectangle2D.Double(0, 0, width, height)
      jchart.draw(g2, r2D, null)
      g2.dispose
      cb.addTemplate(tp, 0, 0)
    } catch {
      case de: DocumentException => System.err.println(de.getMessage)
    }
    document.close
    out.close
  }

  override def png(directory: String, filenamePrefix: String) {
    val filename = directory + filenamePrefix + ".png"
    ChartUtilities.saveChartAsPNG(new java.io.File(filename), jchart, 1280, 720)
  }

  override def gui() { JFGraphPlotter.gui(jchart) }
}

object JFGraphPlotter {

  def xySeries(series: XYSeries): XYS = {
    val result = new XYS(series.name)
    for (p <- series.points) {
      result.add(p._1, p._2)
    }
    result
  }

  def xyCollection(data: XYData): XYSeriesCollection = {
    val coll = new XYSeriesCollection()
    for (series <- data.serieses) {
      coll.addSeries(xySeries(series))
    }
    coll
  }

  def gui(jchart: JChart): Unit = {
    val frame = new JFrame(jchart.getTitle.getText)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setSize(640, 420)
    frame.add(new ChartPanel(jchart))
    frame.pack()
    frame.setVisible(true)
    println("Done")
  }

  def main(args:Array[String]) {
    val series = new MemXYSeries((1 until 100).map(_.toDouble), (1 until 100).map(i => (i * i).toDouble), "Series")
    val data = new XYData(series)
    val chart = new XYChart("Chart", data)
    chart.x.log
    chart.y.log
    val plotter = new JFGraphPlotter(chart)
    plotter.gui()
  }

}
