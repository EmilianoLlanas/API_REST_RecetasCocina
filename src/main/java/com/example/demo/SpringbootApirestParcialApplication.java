package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.springboot.webflux.proyecto.app.models.doc.Ingrediente;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.example.demo.SpringbootApirestParcialApplication;
import com.springboot.webflux.proyecto.app.models.dao.IngredienteDao;


@SpringBootApplication
public class SpringbootApirestParcialApplication {

private Logger log = LoggerFactory.getLogger(SpringbootApirestParcialApplication.class);
	
	@Autowired
	private Ingrediente ingredienteDao;
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApirestParcialApplication.class, args);
	}

	//@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
	}
	
	public Flux<Ingrediente> cargarDatos() 
	{
		return Flux.just(new Ingrediente("Huevo"),
				new Ingrediente("Jamón"),
				new Ingrediente("Pollo"),
				new Ingrediente("Sal"))
		.flatMap(ingrediente -> {
		return ((ReactiveMongoOperations) ingredienteDao).save(ingrediente);
		});
	}
	
	public Mono<Void> limpiarIngredientes() 
	{
		return mongoTemplate.dropCollection("ingredientes");
	}

}
