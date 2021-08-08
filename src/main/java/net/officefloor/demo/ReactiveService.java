package net.officefloor.demo;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.frame.api.function.AsynchronousFlow;
import net.officefloor.plugin.section.clazz.Next;
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
				.flatMap((response) -> response).collectList().subscribe((responses) -> threadPerRequestFlow.complete(
						() -> threadPerRequestResponse.set(responses.stream().toArray(ServicedThreadResponse[]::new))));
	}
	// END SNIPPET: tutorial

}