package com.springboot.webflux.proyecto.app.models.doc;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.data.annotation.Id;

public class Racion {
	@Id
	private int id;

	@NotNull
	@Positive
	private int cantidad;
	
	@NotNull
	private Ingrediente ingrediente;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	
}
