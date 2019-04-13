package net.officefloor.demo

data class WeavedResponse(val requestIdentifier: Int, val requestNumber: Int, val eventLoopResponses: Array<ServicedThreadResponse>, val threadPerRequestResponses: Array<ServicedThreadResponse>)