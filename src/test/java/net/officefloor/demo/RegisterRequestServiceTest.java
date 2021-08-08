package net.officefloor.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import net.officefloor.plugin.clazz.Dependency;
import org.junit.Test;

import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.demo.entity.WeavedRequestRepository;
import net.officefloor.model.test.variable.MockVar;

/**
 * Tests the {@link RegisterRequestService}.
 * 
 * @author Daniel Sagenschneider
 */
public class RegisterRequestServiceTest extends AbstractBaseRunning {

	private @Dependency WeavedRequestRepository repository;

	@Test
	public void registerRequest() {

		// Capture request
		MockVar<WeavedRequest> weavedRequest = new MockVar<>();

		// Register the request
		RegisterRequestService.registerRequest(10, this.repository, weavedRequest);

		// Ensure have request
		assertEquals("Incorrect request identifier", Integer.valueOf(10), weavedRequest.get().getRequestIdentifier());

		// Ensure in database
		WeavedRequest entity = repository.findById(weavedRequest.get().getId()).get();
		assertNotNull("Should persist request", entity);
		assertEquals("Incorrect persisted identifier", Integer.valueOf(10), entity.getRequestIdentifier());
	}

}