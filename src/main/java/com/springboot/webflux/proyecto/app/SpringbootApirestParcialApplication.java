package com.springboot.webflux.proyecto.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.springboot.webflux.proyecto.app.SpringbootApirestParcialApplication;
import com.springboot.webflux.proyecto.app.models.dao.RecetaDao;
import com.springboot.webflux.proyecto.app.models.doc.Receta;

@SpringBootApplication
public class SpringbootApirestParcialApplication implements CommandLineRunner{
	
	private Logger log = LoggerFactory.getLogger(SpringbootApirestParcialApplication.class);
	
	@Autowired
	private RecetaDao recetaDao;
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApirestParcialApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
	}
	
	public Flux<Receta> cargarDatos() 
	{
		return Flux.just(new Receta("Huevo con Jamon", 4, "10 minutos", "Rico huevo con jamon"),
				new Receta("Molletes", 2, "10 minutos", "Rico huevo con jamon"),
				new Receta("Cochinita", 7, "10 minutos", "Rico huevo con jamon"),
				new Receta("Cereal", 1, "10 minutos", "Rico huevo con jamon"))
		.flatMap(receta -> {
		return recetaDao.save(receta);
		});
	}
	
	public Mono<Void> limpiarRecetas() 
	{
		return mongoTemplate.dropCollection("recetas");
	}

}
