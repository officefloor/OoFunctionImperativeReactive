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
import static org.junit.Assert.assertNotNull;

import net.officefloor.plugin.clazz.Dependency;
import org.junit.Test;

import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.demo.entity.WeavedRequestRepository;
import net.officefloor.model.test.variable.MockVar;

/**
 * Tests the {@link RegisterRequestService}.
 * 
 * @author Daniel Sagenschneider
 */
public class RegisterRequestServiceTest extends AbstractBaseRunning {

	private @Dependency WeavedRequestRepository repository;

	@Test
	public void registerRequest() {

		// Capture request
		MockVar<WeavedRequest> weavedRequest = new MockVar<>();

		// Register the request
		RegisterRequestService.registerRequest(10, this.repository, weavedRequest);

		// Ensure have request
		assertEquals("Incorrect request identifier", Integer.valueOf(10), weavedRequest.get().getRequestIdentifier());

		// Ensure in database
		WeavedRequest entity = repository.findById(weavedRequest.get().getId()).get();
		assertNotNull("Should persist request", entity);
		assertEquals("Incorrect persisted identifier", Integer.valueOf(10), entity.getRequestIdentifier());
	}

}