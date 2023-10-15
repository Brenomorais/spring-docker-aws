package com.breno.beerstore.model;

import java.math.BigDecimal;

import com.breno.beerstore.model.type.BeerType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
	
	@NotBlank(message = "beers-1")
	private String nome;

	@NotNull(message =  "beers-2")
	private BeerType type;
	
	@NotNull(message = "beers-3")
	@DecimalMin(value = "0", message = "beers-4")
	private BigDecimal volume;
	
    @JsonIgnore
    public boolean isNew() {
        return getId() == null;
    }

    @JsonIgnore
    public boolean alreadyExist() {
        return getId() != null;
    }

}
