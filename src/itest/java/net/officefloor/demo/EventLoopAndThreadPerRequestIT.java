package net.officefloor.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;

import net.officefloor.server.http.HttpMethod;
import net.officefloor.server.http.mock.MockHttpResponse;
import net.officefloor.woof.mock.MockWoofServer;
import net.officefloor.woof.mock.MockWoofServerRule;

/**
 * Tests the event loop handling.
 * 
 * @author Daniel Sagenschneider
 */
public class EventLoopAndThreadPerRequestIT {

	private static final ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.registerModule(new KotlinModule());
	}

	@Rule
	public final MockWoofServerRule server = new MockWoofServerRule(this);

	@Test
	public void eventLoop() throws IOException {
		String currentThreadName = Thread.currentThread().getName();
		String payload = mapper.writeValueAsString(new ServicedThreadRequest(1));
		this.doRequest(() -> server.send(
				MockWoofServer.mockRequest("/event-loop").header("Content-Type", "application/json").entity(payload)),
				(response) -> {
					assertEquals("Should be same thread", currentThreadName, response.getThreadName());
					assertEquals("Should be event loop value", "Event", response.getLookupName());
				});
	}

	@Test
	public void threadPerRequest() throws IOException {
		String currentThreadName = Thread.currentThread().getName();
		String payload = mapper.writeValueAsString(new ServicedThreadRequest(1));
		this.doRequest(() -> server.send(MockWoofServer.mockRequest("/thread-per-request").method(HttpMethod.POST)
				.header("Content-Type", "application/json").entity(payload)), (response) -> {
					assertNotEquals("Should be different thread", currentThreadName, response.getThreadName());
					assertEquals("Should look up name for identifier", "One", response.getLookupName());
				});
	}

	private void doRequest(Supplier<MockHttpResponse> requester, Consumer<ServicedThreadResponse> validator)
			throws IOException {
		MockHttpResponse response = requester.get();
		String entity = response.getEntity(null);
		assertEquals("Should be successful: " + entity, 200, response.getStatus().getStatusCode());
		ServicedThreadResponse entityResponse = mapper.readValue(entity, ServicedThreadResponse.class);
		validator.accept(entityResponse);
	}
}