package org.sameersingh.scalaplot.metrics

/**
 * @author sameer
 */
object Stats {

  def mean(points: Seq[Double]): Double = points.sum / points.size

  def meanAndVariance(points: Seq[Double]): (Double, Double) = {
    var n = 0.0
    var mv = 0.0
    var m2 = 0.0
    for (x <- points) {
      n += 1.0
      // delta = x - mean
      val delta = x - mv
      // mean = mean + delta/n
      mv = mv + delta / n
      // M2 = M2 + delta*(x - mean)
      m2 = m2 + delta * (x - mv)
    }
    (mv, m2 / (n - 1))
  }

  def variance(points: Seq[Double]): Double = meanAndVariance(points)._2

  def standardDev(points: Seq[Double]): Double = StrictMath.sqrt(variance(points))
}
