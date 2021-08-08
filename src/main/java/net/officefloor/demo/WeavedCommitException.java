package net.officefloor.demo;

import net.officefloor.demo.entity.WeavedRequest;

/**
 * Weaved commit {@link Exception}.
 * 
 * @author Daniel Sagenschneider
 */
public class WeavedCommitException extends WeavedException {
	private static final long serialVersionUID = 1L;

	public WeavedCommitException(WeavedRequest request) {
		super(request);
	}
}