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