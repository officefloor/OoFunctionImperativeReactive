package net.officefloor.demo

// START SNIPPET: tutorial
data class WeavedResponse(val requestIdentifier: Int
						  , val requestNumber: Int
						  , val eventLoopResponses: Array<ServicedThreadResponse>
						  , val threadPerRequestResponses: Array<ServicedThreadResponse>
						  , val standardDeviation: Double)
// END SNIPPET: tutorial