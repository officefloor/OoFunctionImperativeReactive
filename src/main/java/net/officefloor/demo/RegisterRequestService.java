package net.officefloor.demo;

import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.demo.entity.WeavedRequestRepository;
import net.officefloor.plugin.section.clazz.Next;
import net.officefloor.plugin.variable.Out;
import net.officefloor.plugin.variable.Val;

/**
 * Registers the request.
 * 
 * @author Daniel Sagenschneider
 */
public class RegisterRequestService {

	// START SNIPPET: tutorial
	public static void registerRequest(@Val int requestIdentifier, WeavedRequestRepository repository,
			Out<WeavedRequest> weavedRequest) {
		WeavedRequest entity = new WeavedRequest(requestIdentifier);
		repository.save(entity);
		weavedRequest.set(entity);
	}
	// END SNIPPET: tutorial
}