package net.officefloor.demo

import net.officefloor.web.HttpObject

// START SNIPPET: tutorial
@HttpObject
data class ServicedThreadRequest(val identifier: Int)
// END SNIPPET: tutorial