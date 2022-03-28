package com.springboot.webflux.proyecto.app;

import java.util.ArrayList;
import java.util.List;

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
		.thenMany(limpiarRecetas())
		.thenMany(cargarDatos())
		.subscribe(item-> log.info(item.getId()));
	}
	
	public Flux<Receta> cargarDatos() 
	{
		
		Ingrediente ingrediente1 = new Ingrediente("Huevo");
		Ingrediente ingrediente2 = new Ingrediente("Jamón");
		Ingrediente ingrediente3 = new Ingrediente("Pollo");
		Ingrediente ingrediente4 = new Ingrediente("Sal");
		
		Racion racion1= new Racion(10,ingrediente1);
		Racion racion2= new Racion(20,ingrediente2);
		Racion racion3= new Racion(30,ingrediente3);
		Racion racion4= new Racion(40,ingrediente4);
		
		
		List<Racion> raciones1 = new ArrayList();
		
		raciones1.add(racion1);
		raciones1.add(racion2);
		
		List<Racion> raciones2 = new ArrayList();
		
		raciones2.add(racion2);
		raciones2.add(racion3);
		
		List<Racion> raciones3 = new ArrayList();
		
		raciones3.add(racion3);
		raciones3.add(racion4);
		
		List<Racion> raciones4 = new ArrayList();
		
		raciones1.add(racion4);
		raciones1.add(racion1);
		
		
		return Flux.just(ingrediente1,ingrediente2,ingrediente3, ingrediente4)
				.flatMap(ingrediente -> {
				    return ingredienteDao.save(ingrediente);
				}).thenMany(
				
				Flux.just(racion1, racion2, racion3,racion4)
				.flatMap(racion -> {
				    return racionDao.save(racion);
				}))
				.thenMany(		
				
				Flux.just(new Receta("Huevo con Jamon", 4, "10 minutos", "Rico huevo con jamon",raciones1),
				new Receta("Molletes", 2, "10 minutos", "Rico huevo con jamon",raciones2),
				new Receta("Cochinita", 7, "10 minutos", "Rico huevo con jamon",raciones3),
				new Receta("Cereal", 1, "10 minutos", "Rico huevo con jamon",raciones4))
		.flatMap(receta -> {
		return recetaDao.save(receta);
		}));
	}
	
	public Mono<Void> limpiarRecetas() 
	{
		return mongoTemplate.dropCollection("recetas");
	}
		
	public Mono<Void> limpiarIngredientes() 
	{
		return mongoTemplate.dropCollection("ingredientes");
	}
	
	public Mono<Void> limpiarRaciones() 
	{
		return mongoTemplate.dropCollection("racion");
	}
	
}
