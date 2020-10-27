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

import javax.sql.DataSource;

import net.officefloor.plugin.clazz.Dependency;
import org.flywaydb.core.Flyway;
import org.junit.Before;

import net.officefloor.test.OfficeFloorRule;
import org.junit.Rule;

/**
 * Provides running application with access to Spring resources.
 * 
 * @author Daniel Sagenschneider
 */
public abstract class AbstractBaseRunning {

	@Rule
	public final OfficeFloorRule officeFloor = new OfficeFloorRule(this);

	private @Dependency DataSource dataSource;

	@Before
	public void resetDatabase() {
		Flyway flyway = Flyway.configure().dataSource(this.dataSource).load();
		flyway.clean();
		flyway.migrate();
	}

}