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

import net.officefloor.demo.entity.RequestStandardDeviation;
import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.demo.entity.WeavedRequestRepository;
import net.officefloor.plugin.managedfunction.clazz.FlowInterface;
import net.officefloor.plugin.managedfunction.clazz.FlowSuccessful;
import net.officefloor.plugin.section.clazz.Parameter;
import net.officefloor.plugin.variable.Out;
import net.officefloor.plugin.variable.Val;

/**
 * Stores the standard deviation.
 * 
 * @author Daniel Sagenschneider
 */
public class StoreStandardDeviationService {

	@FlowInterface
	public static interface Flows {
		void handleSpecialCases(FlowSuccessful callback);

		void stored();
	}

	public static void store(@Parameter double standardDeviation, Flows flows, @Val WeavedRequest request,
			WeavedRequestRepository repository, Out<RequestStandardDeviation> stDevOut) {
		flows.handleSpecialCases(() -> {
			request.setRequestStandardDeviation(new RequestStandardDeviation(standardDeviation, request));
			repository.save(request);
			stDevOut.set(request.getRequestStandardDeviation());
			flows.stored();
		});
	}
}