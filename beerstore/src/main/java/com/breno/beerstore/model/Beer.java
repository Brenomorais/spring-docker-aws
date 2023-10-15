package com.breno.beerstore.model;

import java.math.BigDecimal;

import com.breno.beerstore.model.type.BeerType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
public class Beer {
	
	@Id
	@SequenceGenerator(name ="beer_seq", sequenceName = "beer_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "beer_seq")
	@EqualsAndHashCode.Include
	private Long id;
	private String nome;
	private BeerType type;
	private BigDecimal volume;

}
