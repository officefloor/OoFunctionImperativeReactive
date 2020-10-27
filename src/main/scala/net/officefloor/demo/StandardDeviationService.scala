package net.officefloor.demo

import net.officefloor.plugin.variable.Val

import Numeric.Implicits._

object StandardDeviationService {

  // START SNIPPET: tutorial
  def mean(timestamps: Iterable[Long]): Double = timestamps.sum.toDouble / timestamps.size

  def variance(timestamps: Iterable[Long]): Double = {
    val avg = mean(timestamps)
    timestamps.map(timestamp => math.pow(timestamp.toDouble - avg, 2)).sum / timestamps.size
  }

  def stdDev(timestamps: Iterable[Long]): Double = math.sqrt(variance(timestamps))

  def standardDeviation(@EventLoopResponse @Val eventLoopResponses: Array[ServicedThreadResponse], @ThreadPerRequestResponse @Val threadPerRequestResponses: Array[ServicedThreadResponse]): Double =
    stdDev((eventLoopResponses ++ threadPerRequestResponses).map(response => response.getTimestamp))
  // END SNIPPET: tutorial
    
}