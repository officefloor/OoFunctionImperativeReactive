package net.officefloor.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * Thread per request {@link Entity}.
 * 
 * @author Daniel Sagenschneider
 */
@Entity
@Data
public class ThreadPerRequest {

	@Id
	private Integer id;

	private String name;

}