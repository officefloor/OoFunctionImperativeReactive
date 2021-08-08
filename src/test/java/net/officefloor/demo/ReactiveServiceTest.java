package net.officefloor.demo;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.web.reactive.function.client.WebClient;

import net.officefloor.compile.test.managedfunction.MockAsynchronousFlow;
import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.model.test.variable.MockVar;

/**
 * Tests the {@link ReactiveService}.
 * 
 * @author Daniel Sagenschneider
 */
public class ReactiveServiceTest extends AbstractBaseRunning {

	@Test
	public void makeCalls() throws Throwable {

		// Create asynchronous flows
		MockAsynchronousFlow eventLoopFlow = new MockAsynchronousFlow();
		MockAsynchronousFlow threadPerRequestFlow = new MockAsynchronousFlow();

		// Create web client
		WebClient client = WebClient.create();

		// Capture results
		MockVar<ServicedThreadResponse[]> eventLoopResponse = new MockVar<>();
		MockVar<ServicedThreadResponse[]> threadPerRequestResponse = new MockVar<>();

		// Request
		WeavedRequest request = new WeavedRequest(10);
		request.setId(1);

		// Retrieve data
		ReactiveService.retrieveData(client, eventLoopFlow, eventLoopResponse, request, threadPerRequestFlow,
				threadPerRequestResponse);

		// Wait for flows to complete
		eventLoopFlow.waitOnCompletion().run();
		threadPerRequestFlow.waitOnCompletion().run();

		// Ensure have event loop responses
		assertEquals("Incorrect number of event loop responses", 10, eventLoopResponse.get().length);
		Arrays.asList(eventLoopResponse.get()).stream()
				.forEach((response) -> assertEquals("Incorrect event loop lookup", "Event", response.getLookupName()));

		// Ensure have thread-per-request responses
		assertEquals("Incorrect number of thread-per-request responses", 10, threadPerRequestResponse.get().length);
		Arrays.asList(threadPerRequestResponse.get()).stream().forEach(
				(response) -> assertEquals("Incorrect thread-per-request lookup", "One", response.getLookupName()));
	}

}