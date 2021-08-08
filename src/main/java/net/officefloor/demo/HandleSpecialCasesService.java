package net.officefloor.demo;

import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.plugin.variable.Val;

/**
 * Handles special cases.
 * 
 * @author Daniel Sagenschneider
 */
public class HandleSpecialCasesService {

	// START SNIPPET: tutorial
	public static void handleSpecialCase(@Val WeavedRequest request) throws WeavedRollbackException, WeavedCommitException {
		switch (request.getRequestIdentifier()) {
		case 3:
			throw new WeavedRollbackException(request);
		case 4:
			throw new WeavedCommitException(request);
		}
	}
	// END SNIPPET: tutorial
}