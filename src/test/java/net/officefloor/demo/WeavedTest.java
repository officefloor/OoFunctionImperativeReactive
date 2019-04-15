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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.function.Consumer;

import javax.sql.DataSource;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;

import net.officefloor.demo.entity.RequestStandardDeviation;
import net.officefloor.demo.entity.WeavedError;
import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.demo.entity.WeavedRequestRepository;
import net.officefloor.server.http.HttpClientRule;
import net.officefloor.server.http.HttpException;
import net.officefloor.spring.test.SpringRule;
import net.officefloor.test.OfficeFloorRule;
import net.officefloor.web.json.JacksonHttpObjectResponderFactory;

/**
 * Ensure appropriate weaving to service request.
 * 
 * @author Daniel Sagenschneider
 */
public class WeavedTest {

	public final SpringRule spring = new SpringRule();

	public final OfficeFloorRule officeFloor = new OfficeFloorRule();

	@Rule
	public final RuleChain ordered = RuleChain.outerRule(this.spring).around(this.officeFloor);

	@Rule
	public final HttpClientRule client = new HttpClientRule();

	private static final ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.registerModule(new KotlinModule());
	}

	@Before
	public void resetDatabase() {
		DataSource dataSource = this.spring.getBean(DataSource.class);
		Flyway flyway = Flyway.configure().dataSource(dataSource).load();
		flyway.clean();
		flyway.migrate();
	}

	@Test
	public void invalidRequest() throws Exception {
		HttpPost post = new HttpPost(this.client.url("/weave/-1"));
		HttpResponse response = this.client.execute(post);
		assertEquals("Should be successful", 422, response.getStatusLine().getStatusCode());
		assertEquals("Incorrect response",
				JacksonHttpObjectResponderFactory.getEntity(new HttpException(422, "Invalid identifier"), mapper),
				EntityUtils.toString(response.getEntity()));
	}

	@Test
	public void returnIdentifier() throws Exception {
		this.doRequest(10, (response) -> assertEquals("Incorrect identifier", 10, response.getRequestIdentifier()));
	}

	@Test
	public void returnRequestNumber() throws Exception {
		this.doRequest(10, (response) -> assertEquals("Incorrect request number", 1, response.getRequestNumber()));
	}

	@Test
	public void returnEventLoopResponse() throws Exception {
		this.doRequest(10, (response) -> {
			assertEquals("Incorrect event loop result", 10, response.getEventLoopResponses().length);
			for (int i = 0; i < 10; i++) {
				assertEquals("Incorrect event", "Event", response.getEventLoopResponses()[i].getLookupName());
			}
		});
	}

	@Test
	public void returnThreadPerRequestResponse() throws Exception {
		this.doRequest(10, (response) -> {
			assertEquals("Incorrect thread-per-request result", 10, response.getThreadPerRequestResponses().length);
			for (int i = 0; i < 10; i++) {
				assertEquals("Incorrect event", "One", response.getThreadPerRequestResponses()[i].getLookupName());
			}
		});
	}

	@Test
	public void returnStandardDeviation() throws Exception {
		long startTimestamp = System.currentTimeMillis();
		this.doRequest(10, (response) -> {
			long serviceTime = System.currentTimeMillis() - startTimestamp;
			assertTrue("Standard deviation lower than service time (stdev: " + response.getStandardDeviation()
					+ ", service: " + serviceTime + ")", response.getStandardDeviation() <= serviceTime);
		});
	}

	@Test
	public void storeResults() throws Exception {
		this.doRequest(10, (response) -> {
			WeavedRequestRepository repository = this.spring.getBean(WeavedRequestRepository.class);
			WeavedRequest entity = repository.findById(response.getRequestNumber()).get();
			assertNotNull("Should have request", entity);
			RequestStandardDeviation standardDeviation = entity.getRequestStandardDeviation();
			assertNotNull("Should have standard deviation", standardDeviation);
			assertEquals("Incorrect standard deviation", response.getStandardDeviation(),
					standardDeviation.getStandardDeviation(), 0.0001);
		});
	}

	@Test
	public void rollbackEscalation() throws Exception {
		this.doErrorRequest(3, (error) -> {
			WeavedRequestRepository repository = this.spring.getBean(WeavedRequestRepository.class);
			Optional<WeavedRequest> notAvailable = repository.findById(error.getRequestNumber());
			assertFalse("Should rollback exception", notAvailable.isPresent());
		});
	}

	@Test
	public void commitEscalation() throws Exception {
		this.doErrorRequest(4, (error) -> {
			WeavedRequestRepository repository = this.spring.getBean(WeavedRequestRepository.class);
			WeavedRequest entity = repository.findById(error.getRequestNumber()).get();
			assertNull("Should not have standard deviation", entity.getRequestStandardDeviation());
			WeavedError weavedError = entity.getWeavedError();
			assertNotNull("Should have weaved error", weavedError);
			assertEquals("Incorrect error message", "4 is divisible by 4", weavedError.getMessage());
		});
	}

	private WeavedResponse doRequest(int identifier, Consumer<WeavedResponse> validator) throws Exception {
		HttpPost post = new HttpPost(this.client.url("/weave/" + identifier));
		HttpResponse response = this.client.execute(post);
		String entity = EntityUtils.toString(response.getEntity());
		assertEquals("Should be successful: " + entity, 200, response.getStatusLine().getStatusCode());
		WeavedResponse weaved = mapper.readValue(entity, WeavedResponse.class);
		if (validator != null) {
			validator.accept(weaved);
		}
		return weaved;
	}

	private WeavedErrorResponse doErrorRequest(int identifier, Consumer<WeavedErrorResponse> validator)
			throws Exception {
		HttpPost post = new HttpPost(this.client.url("/weave/" + identifier));
		HttpResponse response = this.client.execute(post);
		String entity = EntityUtils.toString(response.getEntity());
		assertEquals("Should be successful: " + entity, 200, response.getStatusLine().getStatusCode());
		WeavedErrorResponse error = mapper.readValue(entity, WeavedErrorResponse.class);
		if (validator != null) {
			validator.accept(error);
		}
		return error;
	}

}