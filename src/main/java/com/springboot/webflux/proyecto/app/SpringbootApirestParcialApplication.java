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
import com.springboot.webflux.proyecto.app.models.dao.IngredienteDao;
import com.springboot.webflux.proyecto.app.models.dao.RacionDao;
import com.springboot.webflux.proyecto.app.models.dao.RecetaDao;
import com.springboot.webflux.proyecto.app.models.doc.Ingrediente;
import com.springboot.webflux.proyecto.app.models.doc.Racion;
import com.springboot.webflux.proyecto.app.models.doc.Receta;

@SpringBootApplication
public class SpringbootApirestParcialApplication implements CommandLineRunner{
	
	private Logger log = LoggerFactory.getLogger(SpringbootApirestParcialApplication.class);
	Ingrediente ingrediente1,ingrediente2,ingrediente3,ingrediente4;
	@Autowired
	private RecetaDao recetaDao;
	@Autowired
	private RacionDao racionDao;
	@Autowired
	private IngredienteDao ingredienteDao;
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApirestParcialApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		limpiarRecetas()
		.thenMany(limpiarIngredientes())
		.thenMany(limpiarRaciones())
		.thenMany(cargarDatos())
		.thenMany(cargarIngredientes())
		.thenMany(cargarRaciones())
		.subscribe(item-> log.info(item.getId()));
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
	
	public Flux<Ingrediente> cargarIngredientes() 
	{
		ingrediente1 = new Ingrediente("Huevo");
		ingrediente2 = new Ingrediente("Jamón");
		ingrediente3 = new Ingrediente("Pollo");
		ingrediente4 = new Ingrediente("Sal");
		return Flux.just(ingrediente1,
				ingrediente2,
				ingrediente3,
				ingrediente4)
		.flatMap(ingrediente -> {
		return ingredienteDao.save(ingrediente);
		});
	}
	
	public Mono<Void> limpiarIngredientes() 
	{
		return mongoTemplate.dropCollection("ingredientes");
	}
	
	public Mono<Void> limpiarRaciones() 
	{
		return mongoTemplate.dropCollection("racion");
	}
	
	public Flux<Racion> cargarRaciones() 
	{
		return Flux.just(
				new Racion(10,ingrediente1),
				new Racion(20,ingrediente2),
				new Racion(30,ingrediente3),
				new Racion(40,ingrediente4))
				
		.flatMap(racion -> {
		return racionDao.save(racion);
		});
	}

}
