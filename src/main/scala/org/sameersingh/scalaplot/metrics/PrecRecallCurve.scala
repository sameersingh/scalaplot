package org.sameersingh.scalaplot.metrics

import scala.collection.mutable.ArrayBuffer
import org.sameersingh.scalaplot.{MemXYSeries, XYData, XYChart}

/**
 * Given a squence of predictions and true values (unsorted), return a list of points that
 * is used in plotting PR curve, ROC, finding maximum F1, etc.
 *
 * @author sameer
 */

case class PRPoint(thresh: Double, tp: Int, tn: Int, fp: Int, fn: Int) {
  def precNumerator: Double = tp

  def precDenominator: Double = tp + fp

  def recallNumerator: Double = tp

  def recallDenominator: Double = tp + fn

  def precision: Double = {
    if (precDenominator == 0.0) {
      1.0
    } else {
      precNumerator / precDenominator
    }
  }

  def recall: Double = {
    if (recallDenominator == 0.0) {
      1.0
    } else {
      recallNumerator / recallDenominator
    }
  }

  def f1: Double = {
    val r: Double = recall
    val p: Double = precision
    if (p + r == 0.0) 0.0
    else (2 * p * r) / (p + r)
  }

  def specifity: Double = if(tn + fp == 0) 1.0 else tn.toDouble / (fp + tn)

  def sensitivity = recall

  override def toString: String = {
    "%1.4f p:%6.3f r:%6.3f f1:%6.3f sp:%6.3f".format(thresh, precision * 100.0, recall * 100.0, f1 * 100.0, specifity*100.0)
  }
}

class PrecRecallCurve(data: Seq[(Double, Boolean)]) {

  lazy val curve: Seq[PRPoint] = {
    val points = new ArrayBuffer[PRPoint]
    var tp = 0
    var fp = 0
    var tn = data.count(!_._2)
    var fn = data.count(_._2)

    points += PRPoint(data.maxBy(_._1)._1, tp, tn, fp, fn)
    for (d <- data.sortBy(_._1)) {
      // i am turning d from negative to positive
      if (!d._2) {
        // mistake: it was tn, now fp
        tn -= 1
        fp += 1
      } else {
        // not a mistake: it was fn, now tp
        fn -= 1
        tp += 1
      }
      points += PRPoint(d._1, tp, tn, fp, fn)
    }
    points
  }

  private def createChart(f: PRPoint => (Double,Double), title: String, xname: String = "", yname: String = ""): XYChart = {
    val c = new XYChart(title, new XYData(new MemXYSeries(curve.map(d => f(d)), title)))
    c.x.label = xname
    c.y.label = yname
    c
  }

  def prThreshChart(title: String) : XYChart = {
    val xyData = new XYData()
    xyData += new MemXYSeries(curve.map(d => (d.thresh, d.precision)), "Precision")
    xyData += new MemXYSeries(curve.map(d => (d.thresh, d.recall)), "Recall")
    xyData += new MemXYSeries(curve.map(d => (d.thresh, d.f1)), "F1")
    val c = new XYChart(title, xyData)
    c.x.label = "Threshold"
    c.y.label = "Measure"
    c
  }

  def prChart(title: String): XYChart = createChart(d => (d.recall, d.precision), title, "Recall", "Precision")

  def rocChart(title: String): XYChart = createChart(d => (d.sensitivity, 1.0 - d.specifity), title, "Sensitivity", "1-Specificity")

}
