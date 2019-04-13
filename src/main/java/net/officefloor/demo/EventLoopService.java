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

import net.officefloor.web.ObjectResponse;

/**
 * Servicer on event loop.
 * 
 * @author Daniel Sagenschneider
 */
public class EventLoopService {

	public void service(ObjectResponse<ServicedThreadResponse> response) {
		response.send(
				new ServicedThreadResponse(Thread.currentThread().getName(), "Event", System.currentTimeMillis()));
	}
}