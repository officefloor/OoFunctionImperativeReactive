package net.officefloor.demo

import Numeric.Implicits._
import net.officefloor.plugin.section.clazz.Next
import net.officefloor.plugin.variable.Val

object StandardDeviationService {

  // START SNIPPET: tutorial
  def mean(timestamps: Iterable[Long]): Double = timestamps.sum.toDouble / timestamps.size

  def variance(timestamps: Iterable[Long]): Double = {
    val avg = mean(timestamps)
    timestamps.map(timestamp => math.pow(timestamp.toDouble - avg, 2)).sum / timestamps.size
  }

  def stdDev(timestamps: Iterable[Long]): Double = math.sqrt(variance(timestamps))

  @Next("use")
  def standardDeviation(@EventLoopResponse @Val eventLoopResponses: Array[ServicedThreadResponse], @ThreadPerRequestResponse @Val threadPerRequestResponses: Array[ServicedThreadResponse]): Double =
    stdDev((eventLoopResponses ++ threadPerRequestResponses).map(response => response.getTimestamp))
  // END SNIPPET: tutorial
    
}