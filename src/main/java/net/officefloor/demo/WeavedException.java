package net.officefloor.demo;

import net.officefloor.demo.entity.WeavedRequest;

/**
 * Weaved {@link Exception}.
 * 
 * @author Daniel Sagenschneider
 */
public class WeavedException extends Exception {
	private static final long serialVersionUID = 1L;

	private WeavedRequest request;

	public WeavedException(WeavedRequest request) {
		this.request = request;
	}

	public WeavedRequest getWeavedRequest() {
		return this.request;
	}
}