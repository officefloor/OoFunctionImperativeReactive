package net.officefloor.demo;

import net.officefloor.demo.entity.ThreadPerRequest;
import net.officefloor.demo.entity.ThreadPerRequestRepository;
import net.officefloor.web.ObjectResponse;

/**
 * Servicer for thread-per-request.
 * 
 * @author Daniel Sagenschneider
 */
public class ThreadPerRequestService {

	public void service(ServicedThreadRequest request, ThreadPerRequestRepository repository,
			ObjectResponse<ServicedThreadResponse> response) {
		int identifier = request.getIdentifier() % 10;
		ThreadPerRequest entity = repository.findById(identifier).get();
		response.send(new ServicedThreadResponse(Thread.currentThread().getName(), entity.getName(),
				System.currentTimeMillis()));
	}
}