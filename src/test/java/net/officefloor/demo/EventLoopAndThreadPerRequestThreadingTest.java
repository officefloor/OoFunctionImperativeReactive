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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import net.officefloor.test.OfficeFloorRule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Tests the threading of event loop.
 * 
 * @author Daniel Sagenschneider
 */
public class EventLoopAndThreadPerRequestThreadingTest {

	private final static String URL = "http://localhost:7878/{path}";

	@Rule
	public final OfficeFloorRule officeFloor = new OfficeFloorRule();

	@Test
	public void eventLoop() throws Exception {

		// Undertake load higher than CPUs available
		int cpuCount = Runtime.getRuntime().availableProcessors();
		int requestCount = cpuCount * 10;
		Set<String> threadNames = this.doRequests("event-loop", requestCount, (webClient, requestPath) -> webClient
				.get().uri(URL, requestPath).retrieve().bodyToMono(ServicedThreadResponse.class));

		// Ensure only one thread per CPU
		assertTrue("Should be event loop of loop per cpu: " + threadNames, threadNames.size() <= cpuCount);
	}

	@Test
	public void threadPerRequest() throws Exception {

		// Undertake load higher than CPUs available
		int cpuCount = Runtime.getRuntime().availableProcessors();
		int requestCount = cpuCount * 10;
		Set<String> threadNames = this.doRequests("thread-per-request", requestCount,
				(webClient, requestPath) -> webClient.post().uri(URL, requestPath)
						.contentType(MediaType.APPLICATION_JSON).syncBody(new ServicedThreadRequest(1)).retrieve()
						.bodyToMono(ServicedThreadResponse.class));

		// Obtain the event loop threads
		Set<String> eventLoopThreadNames = this.doRequests("event-loop", requestCount,
				(webClient, requestPath) -> webClient.get().uri(URL, requestPath).retrieve()
						.bodyToMono(ServicedThreadResponse.class));

		// Ensure serviced on different threads
		threadNames.forEach((threadName) -> assertFalse("Should not be event loop thread: " + threadName,
				eventLoopThreadNames.contains(threadName)));
	}

	private Set<String> doRequests(String path, int requestCount,
			BiFunction<WebClient, String, Mono<ServicedThreadResponse>> doRequest) {
		WebClient webClient = WebClient.create();
		String[] iterations = new String[requestCount];
		Arrays.fill(iterations, path);
		List<ServicedThreadResponse> responses = Flux.fromArray(iterations)
				.flatMap((requestPath) -> doRequest.apply(webClient, requestPath)).buffer()
				.blockFirst(Duration.ofSeconds(10));

		// Ensure correct number of responses
		assertEquals("Incorrect number of requests", requestCount, responses.size());

		// Return the unique thread names
		return responses.stream().map((response) -> response.getThreadName()).collect(Collectors.toSet());
	}

}