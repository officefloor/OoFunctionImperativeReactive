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

import net.officefloor.demo.entity.WeavedError;
import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.demo.entity.WeavedRequestRepository;
import net.officefloor.woof.mock.MockObjectResponse;

/**
 * Tests the {@link HandleCommitExceptionService}.
 * 
 * @author Daniel Sagenschneider
 */
public class HandleCommitExceptionServiceTest extends AbstractBaseRunning {

	@Test
	public void ensureWriteError() {
		MockObjectResponse<WeavedErrorResponse> response = new MockObjectResponse<>();
		WeavedRequestRepository repository = spring.getBean(WeavedRequestRepository.class);

		// Load request
		WeavedRequest request = new WeavedRequest(10);
		repository.save(request);

		// Service
		HandleCommitExceptionService.handle(new WeavedCommitException(request), repository, response);

		// Ensure correct response
		WeavedErrorResponse error = response.getObject();
		assertEquals("Incorrect request", 10, error.getRequestIdentifier());
		assertEquals("Incorrect identifier", 1, error.getRequestNumber());

		// Ensure error added to database
		WeavedRequest entity = repository.findById(1).get();
		WeavedError errorEntity = entity.getWeavedError();
		assertEquals("Incorrect error", "Request Identifier (10) is special case", errorEntity.getMessage());
	}
}