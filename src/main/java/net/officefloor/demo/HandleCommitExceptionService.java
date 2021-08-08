package net.officefloor.demo;

import net.officefloor.demo.entity.WeavedError;
import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.demo.entity.WeavedRequestRepository;
import net.officefloor.plugin.section.clazz.Parameter;
import net.officefloor.web.ObjectResponse;

/**
 * Handles {@link WeavedException}.
 * 
 * @author Daniel Sagenschneider
 */
public class HandleCommitExceptionService {

	// START SNIPPET: tutorial
	public static void handle(@Parameter WeavedCommitException exception, WeavedRequestRepository repository,
			ObjectResponse<WeavedErrorResponse> response) {
		WeavedRequest request = exception.getWeavedRequest();
		request.setWeavedError(
				new WeavedError("Request Identifier (" + request.getRequestIdentifier() + ") is special case", request));
		repository.save(request);
		response.send(new WeavedErrorResponse(request.getRequestIdentifier(), request.getId()));
	}
	// END SNIPPET: tutorial
}