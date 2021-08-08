package net.officefloor.demo;

import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.plugin.section.clazz.Parameter;
import net.officefloor.web.ObjectResponse;

/**
 * Handles {@link WeavedException}.
 * 
 * @author Daniel Sagenschneider
 */
public class HandleRollbackExceptionService {

	// START SNIPPET: tutorial
	public static void handle(@Parameter WeavedRollbackException exception, ObjectResponse<WeavedErrorResponse> response) {
		WeavedRequest request = exception.getWeavedRequest();
		response.send(new WeavedErrorResponse(request.getRequestIdentifier(), request.getId()));
	}
	// END SNIPPET: tutorial
}