package net.officefloor.demo;

import net.officefloor.demo.entity.WeavedRequest;

/**
 * Weaved commit {@link Exception}.
 * 
 * @author Daniel Sagenschneider
 */
public class WeavedRollbackException extends WeavedException {
	private static final long serialVersionUID = 1L;

	public WeavedRollbackException(WeavedRequest request) {
		super(request);
	}
}