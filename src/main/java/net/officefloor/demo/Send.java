package net.officefloor.demo;

import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.plugin.variable.Val;
import net.officefloor.web.ObjectResponse;

public class Send {

	public void send(@RequestIdentifier @Val int requestIdentifier, @Val WeavedRequest request,
			ObjectResponse<WeavedResponse> response) {
		response.send(new WeavedResponse(requestIdentifier, request.getId()));
	}
}