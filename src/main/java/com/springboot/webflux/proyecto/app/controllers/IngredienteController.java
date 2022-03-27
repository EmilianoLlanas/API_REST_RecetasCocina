package com.springboot.webflux.proyecto.app.controllers;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.springboot.webflux.proyecto.app.models.dao.IngredienteDao;
import com.springboot.webflux.proyecto.app.models.doc.*;

import ch.qos.logback.core.status.Status;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ingrediente-controller/ingredientes")
public class IngredienteController {
	
	@Autowired
	private IngredienteDao ingredienteDao;
	
	@GetMapping
	public Flux<Ingrediente> listar(@RequestParam(required=false) Map<String,String> qparams) 
	{
		qparams.forEach((a,b) -> {
			System.out.println(String.format("%s -> %s", a,b));
			
		});
		String nombre = qparams.get("nombre");
		return ingredienteDao.findAll()
							.filter(ingrediente -> null!=nombre ? ingrediente.getNombre().contains(nombre) : true);
	}
	
	/*@PostMapping("")
	public Mono<ResponseEntity<Map<String, Object>>> crear(@Valid @RequestBody Mono<Ingrediente> monoReceta) 
	{
		Map<String, Object> respuesta = new HashMap<String, Object>();
		return monoReceta.flatMap(ingrediente -> {
			return ingredienteDao
					.save(ingrediente)
					.map(ing -> {
						respuesta.put("ingrediente", ing);
						respuesta.put("mensaje", "Ingrediente creado con exito");
						
							return ResponseEntity.created(URI.create("/ingrediente-controller/ingredientes/" + ing.getId()))
							.contentType(MediaType.APPLICATION_JSON)
							.body(respuesta);
			});
		}).onErrorResume(ex -> {
			return generarError(ex);		
		});
	}*/
	
	private Mono<ResponseEntity<Map<String, Object>>> generarError(Throwable ex)
	{
		Map<String, Object> respuesta = new HashMap<String, Object>();
					return Mono.just(ex)
					
					 .cast(WebExchangeBindException.class)
					 .flatMapMany(e-> Flux.fromIterable(e.getFieldErrors()))
					.map(e -> {
						
						return "Campo:" + e.getField() + " Error:" + e.getDefaultMessage();
						
					}).collectList()
					.flatMap(list->{
						
						respuesta.put("errors", list);
						respuesta.put("mensaje", "Ocurrio un error");
						respuesta.put("timestamp", new Date());
						
							return Mono.just(
									ResponseEntity
								.badRequest()
								.body(respuesta));		
					});
	}
	
	/*@GetMapping("/{id}")
	public Mono<ResponseEntity<Receta>> consultar(@PathVariable String id) 
	{
	  return recetaDao.findById(id)
	          .map(receta -> {
	        	  			return ResponseEntity
	                        .ok()
	                        .contentType(MediaType.APPLICATION_JSON)
	                      .body(receta);
	          })
	          
	          .defaultIfEmpty(
	        		  ResponseEntity.notFound().build()
	        		  
	        		  );
	}
	
	@GetMapping("/{nombre}")
	public Mono<ResponseEntity<Receta>> consultarN(@PathVariable String nombre) 
	{
	  return recetaDao.findById(nombre)
	          .map(receta -> {
	        	  			return ResponseEntity
	                        .ok()
	                        .contentType(MediaType.APPLICATION_JSON)
	                      .body(receta);
	          })
	          
	          .defaultIfEmpty(
	        		  ResponseEntity.notFound().build()
	        		  
	        		  );
	}
	
	
	
	*/

}