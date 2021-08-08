package net.officefloor.demo.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Request {@link Entity}.
 * 
 * @author Daniel Sagenschneider
 */
@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class WeavedRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NonNull
	private Integer requestIdentifier;

	@OneToOne(mappedBy = "weavedRequest", cascade = CascadeType.ALL)
	private RequestStandardDeviation requestStandardDeviation;

	@OneToOne(mappedBy = "weavedRequest", cascade = CascadeType.ALL)
	private WeavedError weavedError;

}