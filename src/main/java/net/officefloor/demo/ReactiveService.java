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

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.frame.api.function.AsynchronousFlow;
import net.officefloor.plugin.section.clazz.NextFunction;
import net.officefloor.plugin.variable.Out;
import net.officefloor.plugin.variable.Val;
import reactor.core.publisher.Flux;

/**
 * Reactive service.
 * 
 * @author Daniel Sagenschneider
 */
public class ReactiveService {

	// START SNIPPET: tutorial
	private final static String URL = "http://localhost:7878/{path}";

	@NextFunction("useData")
	public static void retrieveData(WebClient client, AsynchronousFlow eventLoopFlow,
			@EventLoopResponse Out<ServicedThreadResponse[]> eventLoopResponse, @Val WeavedRequest request,
			AsynchronousFlow threadPerRequestFlow,
			@ThreadPerRequestResponse Out<ServicedThreadResponse[]> threadPerRequestResponse) {

		Flux.range(1, 10)
				.map((index) -> client.get().uri(URL, "event-loop").retrieve().bodyToMono(ServicedThreadResponse.class))
				.flatMap((response) -> response).collectList().subscribe((responses) -> eventLoopFlow.complete(
						() -> eventLoopResponse.set(responses.stream().toArray(ServicedThreadResponse[]::new))));

		Flux.range(1, 10)
				.map((index) -> client.post().uri(URL, "thread-per-request").contentType(MediaType.APPLICATION_JSON)
						.syncBody(new ServicedThreadRequest(request.getId())).retrieve()
						.bodyToMono(ServicedThreadResponse.class))
				.flatMap((response) -> response).collectList().subscribe((response) -> threadPerRequestFlow.complete(
						() -> threadPerRequestResponse.set(response.stream().toArray(ServicedThreadResponse[]::new))));
	}
	// END SNIPPET: tutorial

}