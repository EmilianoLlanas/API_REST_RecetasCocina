package com.springboot.webflux.proyecto.app.models.doc;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="receta")

public class Receta {
	@Id
	private String id;
	
	@NotNull
	@NotEmpty
	private String nombre;
	
	@NotNull 
	@Min(value = 1, message = "La dificultad no debe ser menor que 1")
	@Max(value = 10, message = "La dificultad no debe ser mayor que 10")
	private Integer dificultad;
	
	@NotNull
	@NotEmpty
	private String tiempoPreparacion;
	
	@NotNull
	@NotEmpty
	private List<Racion> raciones;
	
	@NotNull
	@NotEmpty
	private String descripcion;
	
	public Receta() {
		
	}
	
	public Receta(String nombre) {
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
	public Integer getDificultad() {
		return dificultad;
	}
	public void setDificultad(Integer dificultad) {
		this.dificultad = dificultad;
	}
	public String getTiempoPreparacion() {
		return tiempoPreparacion;
	}
	public void setTiempoPreparacion(String tiempoPreparacion){
		this.tiempoPreparacion = tiempoPreparacion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion){
		this.descripcion = descripcion;
	}
}
