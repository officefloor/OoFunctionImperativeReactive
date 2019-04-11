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
import static org.junit.Assert.fail;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.junit.Rule;
import org.junit.Test;

import net.officefloor.server.http.HttpClientRule;
import net.officefloor.test.OfficeFloorRule;

/**
 * Ensure appropriate weaving to service request.
 * 
 * @author Daniel Sagenschneider
 */
public class WeavedTest {

	@Rule
	public final OfficeFloorRule officeFloor = new OfficeFloorRule();

	@Rule
	public final HttpClientRule client = new HttpClientRule();

	@Test
	public void invalidRequest() throws Exception {

		// Send invalid request
		HttpPost post = new HttpPost(this.client.url("/weave"));
		HttpResponse response = this.client.execute(post);
		assertEquals("Should be successful", 422, response.getStatusLine().getStatusCode());

		// TODO implement
		fail("TODO implement ensuring correct invalid request");
	}

}