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
import static org.junit.Assert.assertTrue;

import org.hibernate.AssertionFailure;
import org.junit.Test;

import net.officefloor.demo.StoreStandardDeviationService.Flows;
import net.officefloor.demo.entity.RequestStandardDeviation;
import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.demo.entity.WeavedRequestRepository;
import net.officefloor.model.test.variable.MockVar;
import net.officefloor.plugin.managedfunction.clazz.FlowSuccessful;

/**
 * Tests the {@link StoreStandardDeviationService}.
 * 
 * @author Daniel Sagenschneider
 */
public class StoreStandardDeviationServiceTest extends AbstractBaseRunning {

	@Test
	public void storeStandardDeviation() {
		WeavedRequestRepository repository = spring.getBean(WeavedRequestRepository.class);

		WeavedRequest request = new WeavedRequest(10);
		MockVar<RequestStandardDeviation> stDevOut = new MockVar<>();

		// Store the standard deviation
		boolean[] isStored = new boolean[] { false };
		StoreStandardDeviationService.store(5.0, new Flows() {

			@Override
			public void handleSpecialCases(FlowSuccessful callback) {
				try {
					callback.run();
				} catch (Throwable ex) {
					throw new AssertionFailure("Failure running callback", ex);
				}
			}

			@Override
			public void stored() {
				isStored[0] = true;
			}
		}, request, repository, stDevOut);

		// Ensure flagged stored
		assertTrue("Should be stored to continue flow", isStored[0]);

		// Ensure store standard deviation
		WeavedRequest entity = repository.findById(request.getId()).get();
		assertEquals("Incorrect standard deviation", 5.0, entity.getRequestStandardDeviation().getStandardDeviation(),
				0.000001);
	}

}