package net.officefloor.demo.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link WeavedRequest} {@link Repository}.
 * 
 * @author Daniel Sagenschneider
 */
@Repository
public interface ThreadPerRequestRepository extends CrudRepository<ThreadPerRequest, Integer> {
}