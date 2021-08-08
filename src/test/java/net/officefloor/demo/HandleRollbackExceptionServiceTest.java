package net.officefloor.demo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.woof.mock.MockObjectResponse;

/**
 * Tests the {@link HandleRollbackExceptionService}.
 * 
 * @author Daniel Sagenschneider
 */
public class HandleRollbackExceptionServiceTest {

	@Test
	public void ensureResponseWithError() {
		MockObjectResponse<WeavedErrorResponse> response = new MockObjectResponse<>();

		// Create request
		WeavedRequest request = new WeavedRequest(10);
		request.setId(1);

		// Service
		HandleRollbackExceptionService.handle(new WeavedRollbackException(request), response);

		// Ensure correct response
		WeavedErrorResponse error = response.getObject();
		assertEquals("Incorrect request", 10, error.getRequestIdentifier());
		assertEquals("Incorrect identifier", 1, error.getRequestNumber());
	}
}