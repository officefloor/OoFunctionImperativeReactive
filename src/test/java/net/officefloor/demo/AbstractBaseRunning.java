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

import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.rules.RuleChain;

import net.officefloor.spring.test.SpringRule;
import net.officefloor.test.OfficeFloorRule;

/**
 * Provides running application with access to Spring resources.
 * 
 * @author Daniel Sagenschneider
 */
public abstract class AbstractBaseRunning {

	public static final SpringRule spring = new SpringRule();

	public static final OfficeFloorRule officeFloor = new OfficeFloorRule();

	@ClassRule
	public static final RuleChain ordered = RuleChain.outerRule(spring).around(officeFloor);

	@Before
	public void resetDatabase() {
		DataSource dataSource = spring.getBean(DataSource.class);
		Flyway flyway = Flyway.configure().dataSource(dataSource).load();
		flyway.clean();
		flyway.migrate();
	}

}