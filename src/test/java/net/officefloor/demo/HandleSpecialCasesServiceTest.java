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

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.junit.Test;

import net.officefloor.demo.entity.WeavedRequest;

/**
 * Tests the {@link HandleSpecialCasesService}.
 * 
 * @author Daniel Sagenschneider
 */
public class HandleSpecialCasesServiceTest {

	@Test
	public void noSpecialCase() throws Exception {
		HandleSpecialCasesService.handleSpecialCase(new WeavedRequest(1));
	}

	@Test
	public void rollback() throws Exception {
		WeavedRequest request = new WeavedRequest(3);
		try {
			HandleSpecialCasesService.handleSpecialCase(request);
			fail("Should not be successful");
		} catch (WeavedRollbackException ex) {
			assertSame("Incorrect request", request, ex.getWeavedRequest());
		}
	}

	@Test
	public void commit() throws Exception {
		WeavedRequest request = new WeavedRequest(4);
		try {
			HandleSpecialCasesService.handleSpecialCase(request);
			fail("Should not be successful");
		} catch (WeavedCommitException ex) {
			assertSame("Incorrect request", request, ex.getWeavedRequest());
		}
	}

}