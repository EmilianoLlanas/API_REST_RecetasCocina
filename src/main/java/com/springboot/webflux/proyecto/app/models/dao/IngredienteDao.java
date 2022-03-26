package com.springboot.webflux.proyecto.app.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.springboot.webflux.proyecto.app.models.doc.Ingrediente;

public interface IngredienteDao extends ReactiveMongoRepository<Ingrediente, String>{

}
