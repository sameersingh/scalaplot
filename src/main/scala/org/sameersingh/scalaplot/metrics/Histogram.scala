package org.sameersingh.scalaplot.metrics

/**
 * @author sameer
 */
class Histogram(val numBins: Int) {
  assert(numBins > 0)

  def bin(points: Seq[Double]): Seq[(Double, Int)] = bin(points, points.min, points.max)

  def bin(points: Seq[Double], min: Double, max: Double): Seq[(Double, Int)] = {
    val step = (max - min) / numBins
    val bins = Array.fill(numBins)(0)
    for (p <- points) {
      val bin = ((p - min) / step).floor.toInt
      if (bin == numBins) {
        bins(bin - 1) += 1
      } else bins(bin) += 1
    }
    (0 until numBins).map(i => (min + step * i + step / 2.0, bins(i)))
  }
}
