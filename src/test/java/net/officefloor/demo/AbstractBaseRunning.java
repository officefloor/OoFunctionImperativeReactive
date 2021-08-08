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