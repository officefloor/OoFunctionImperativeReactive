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

import org.junit.Test;

import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.woof.mock.MockObjectResponse;

/**
 * Tests the {@link HandleRollbackExceptionService}.
 * 
 * @author Daniel Sagenschneider
 */
public class HandleRollbackExceptionServiceTest {

	@Test
	public void ensureResponseWithError() {
		MockObjectResponse<WeavedErrorResponse> response = new MockObjectResponse<>();

		// Create request
		WeavedRequest request = new WeavedRequest(10);
		request.setId(1);

		// Service
		HandleRollbackExceptionService.handle(new WeavedRollbackException(request), response);

		// Ensure correct response
		WeavedErrorResponse error = response.getObject();
		assertEquals("Incorrect request", 10, error.getRequestIdentifier());
		assertEquals("Incorrect identifier", 1, error.getRequestNumber());
	}
}