package org.sameersingh.scalaplot

import org.junit._
import Assert._
import scala.util.Random
import org.sameersingh.scalaplot.metrics.{PrecRecallCurve, Stats, Histogram}

/**
 * @author sameer
 */
class MetricsTest {

  val random = new Random()

  @Test
  def testSingleBin() {
    val points = (0 until 100).map(i => random.nextDouble())
    val hist = new Histogram(1)
    val bins = hist.bin(points, 0.0, 1.0)
    assertEquals(1, bins.size)
    assertEquals(0.5, bins.head._1, 1e-9)
    assertEquals(100, bins.head._2)
    //println(bins)
  }

  @Test
  def testUniformBin() {
    val numPoints = 10000
    val numBins = 10
    val points = (0 until numPoints).map(i => random.nextDouble())
    val hist = new Histogram(numBins)
    val bins = hist.bin(points, 0.0, 1.0)
    //println(bins)
    assertEquals(numBins, bins.size)
    assertEquals(numPoints, bins.map(_._2).sum)
    val stdDev = Stats.standardDev(bins.map(_._2.toDouble / (numPoints / numBins)))
    val mean = Stats.mean(bins.map(_._2.toDouble / (numPoints / numBins)))
    println(mean)
    assertEquals(1.0, mean, 1e-5)
    assertTrue(stdDev < 0.025)
  }

  @Test
  def testGaussianBin() {
    val numPoints = 10000
    val numBins = 10
    val points = (0 until numPoints).map(i => random.nextGaussian())
    val hist = new Histogram(numBins)
    val bins = hist.bin(points)
    println(bins)
    assertEquals(numBins, bins.size)
    assertEquals(numPoints, bins.map(_._2).sum)
    val stdDev = Stats.standardDev(bins.map(_._2.toDouble / (numPoints / numBins)))
    println(stdDev)
    assertEquals(1.0, stdDev, 0.1)
  }

  @Test
  def testPRCurve() {
    val numPoints = 250
    val idealThresh = 0.5
    val probError = 1.0
    val data = for (i <- 0 until numPoints) yield {
      val pred = random.nextDouble()
      val effProbError = (0.5 - math.abs(pred - idealThresh)) / 0.5 * probError
      val truth =
        if (random.nextDouble() < effProbError) pred < idealThresh else pred > idealThresh
      (pred, truth)
    }
    //println(data.sortBy(_._1).mkString("\n"))
    val curve = new PrecRecallCurve(data).curve
    println(curve.mkString("\n"))
  }
}
