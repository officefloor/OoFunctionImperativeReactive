package net.officefloor.demo;

import net.officefloor.plugin.clazz.Qualifier;
import net.officefloor.plugin.variable.VariableManagedObjectSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Event loop response {@link VariableManagedObjectSource}.
 * 
 * @author Daniel Sagenschneider
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface EventLoopResponse {
}
