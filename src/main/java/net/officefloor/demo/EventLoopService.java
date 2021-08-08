package net.officefloor.demo;

import net.officefloor.web.ObjectResponse;

/**
 * Servicer on event loop.
 * 
 * @author Daniel Sagenschneider
 */
public class EventLoopService {

	// START SNIPPET: tutorial
	public void service(ObjectResponse<ServicedThreadResponse> response) {
		response.send(
				new ServicedThreadResponse(Thread.currentThread().getName(), "Event", System.currentTimeMillis()));
	}
	// END SNIPPET: tutorial
}