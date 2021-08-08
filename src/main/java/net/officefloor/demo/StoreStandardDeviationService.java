package net.officefloor.demo;

import net.officefloor.demo.entity.RequestStandardDeviation;
import net.officefloor.demo.entity.WeavedRequest;
import net.officefloor.demo.entity.WeavedRequestRepository;
import net.officefloor.plugin.clazz.FlowInterface;
import net.officefloor.plugin.clazz.FlowSuccessful;
import net.officefloor.plugin.section.clazz.Parameter;
import net.officefloor.plugin.variable.Out;
import net.officefloor.plugin.variable.Val;

/**
 * Stores the standard deviation.
 *
 * @author Daniel Sagenschneider
 */
public class StoreStandardDeviationService {

    // START SNIPPET: tutorial
    @FlowInterface
    public static interface Flows {
        void handleSpecialCases(FlowSuccessful callback);

        void stored();
    }

    public static void store(@Parameter double standardDeviation, Flows flows, @Val WeavedRequest request,
                             WeavedRequestRepository repository, Out<RequestStandardDeviation> stDevOut) {
        flows.handleSpecialCases(() -> {
            request.setRequestStandardDeviation(new RequestStandardDeviation(standardDeviation, request));
            repository.save(request);
            stDevOut.set(request.getRequestStandardDeviation());
            flows.stored();
        });
    }
    // END SNIPPET: tutorial
}