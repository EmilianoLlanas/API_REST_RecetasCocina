package com.springboot.webflux.proyecto.app.models.doc;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="ingrediente")

public class Ingrediente {
	@Id
	private String id;
	
	@NotNull
	@NotEmpty
	private String nombre;
	
	public Ingrediente() {
		
	}
	
	public Ingrediente(String nombre) {
		this.nombre = nombre;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
