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

import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.plugin.variable.Val;

/**
 * Handles special cases.
 * 
 * @author Daniel Sagenschneider
 */
public class HandleSpecialCasesService {

	// START SNIPPET: tutorial
	public static void handleSpecialCase(@Val WeavedRequest request) throws WeavedRollbackException, WeavedCommitException {
		switch (request.getRequestIdentifier()) {
		case 3:
			throw new WeavedRollbackException(request);
		case 4:
			throw new WeavedCommitException(request);
		}
	}
	// END SNIPPET: tutorial
}