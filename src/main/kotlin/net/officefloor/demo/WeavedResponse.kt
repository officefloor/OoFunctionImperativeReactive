package net.officefloor.demo

data class WeavedResponse(val requestIdentifier: Int, val requestNumber: Int, val eventLoopResponse: ServicedThreadResponse, val threadPerRequestResponse: ServicedThreadResponse)