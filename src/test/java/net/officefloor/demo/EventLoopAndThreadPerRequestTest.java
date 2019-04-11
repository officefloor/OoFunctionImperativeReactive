/*
 * OfficeFloor - http://www.officefloor.net
 * Copyright (C) 2005-2019 Daniel Sagenschneider
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package net.officefloor.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;

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
public class EventLoopAndThreadPerRequestTest {

	private static final ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.registerModule(new KotlinModule());
	}

	@Rule
	public final MockWoofServerRule server = new MockWoofServerRule();

	@Test
	public void eventLoop() throws IOException {
		String currentThreadName = Thread.currentThread().getName();
		MockHttpResponse response = this.server
				.send(MockWoofServer.mockRequest("/event-loop").header("Content-Type", "application/json")
						.entity(mapper.writeValueAsString(new ServicedThreadRequest(1))));
		response.assertResponse(200, mapper.writeValueAsString(new ServicedThreadResponse(currentThreadName, "None")),
				"Content-Type", "application/json");
	}

	@Test
	public void threadPerRequest() throws IOException {
		String currentThreadName = Thread.currentThread().getName();
		MockHttpResponse response = this.server.send(MockWoofServer.mockRequest("/thread-per-request")
				.method(HttpMethod.POST).header("Content-Type", "application/json")
				.entity(mapper.writeValueAsString(new ServicedThreadRequest(1))));
		String entity = response.getEntity(null);
		assertEquals("Should be successful: " + entity, 200, response.getStatus().getStatusCode());
		ServicedThreadResponse entityResponse = mapper.readValue(entity, ServicedThreadResponse.class);
		assertNotEquals("Should be different thread", currentThreadName, entityResponse.getThreadName());
		assertEquals("Should look up name for identifier", "One", entityResponse.getLookupName());
	}
}