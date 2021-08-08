package net.officefloor.demo;

import static org.junit.Assert.assertEquals;

import net.officefloor.plugin.clazz.Dependency;
import org.junit.Test;

import net.officefloor.demo.entity.WeavedError;
import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.demo.entity.WeavedRequestRepository;
import net.officefloor.woof.mock.MockObjectResponse;

/**
 * Tests the {@link HandleCommitExceptionService}.
 * 
 * @author Daniel Sagenschneider
 */
public class HandleCommitExceptionServiceTest extends AbstractBaseRunning {

	private @Dependency WeavedRequestRepository repository;

	@Test
	public void ensureWriteError() {
		MockObjectResponse<WeavedErrorResponse> response = new MockObjectResponse<>();

		// Load request
		WeavedRequest request = new WeavedRequest(10);
		this.repository.save(request);

		// Service
		HandleCommitExceptionService.handle(new WeavedCommitException(request), this.repository, response);

		// Ensure correct response
		WeavedErrorResponse error = response.getObject();
		assertEquals("Incorrect request", 10, error.getRequestIdentifier());
		assertEquals("Incorrect identifier", 1, error.getRequestNumber());

		// Ensure error added to database
		WeavedRequest entity = repository.findById(1).get();
		WeavedError errorEntity = entity.getWeavedError();
		assertEquals("Incorrect error", "Request Identifier (10) is special case", errorEntity.getMessage());
	}
}