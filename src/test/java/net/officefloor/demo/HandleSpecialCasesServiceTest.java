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