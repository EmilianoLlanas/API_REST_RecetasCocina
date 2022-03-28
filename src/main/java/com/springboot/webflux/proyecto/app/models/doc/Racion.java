package com.springboot.webflux.proyecto.app.models.doc;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.data.annotation.Id;

public class Racion {
	@Id
	private String id;

	@NotNull
	@Positive
	private int cantidad;

	@NotNull
	@Valid
	private Ingrediente ingrediente;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Ingrediente getIngrediente() {
		return ingrediente;
	}

	public void setIngrediente(Ingrediente i) {
		this.ingrediente=i;
	}
	public Racion() {
		
	}
	public Racion(int can, Ingrediente ing) {
		this.cantidad = can;
		this.ingrediente = ing;
	}

}
