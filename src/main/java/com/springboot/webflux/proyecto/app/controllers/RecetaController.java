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

import com.springboot.webflux.proyecto.app.models.dao.RecetaDao;
import com.springboot.webflux.proyecto.app.models.doc.Receta;

import ch.qos.logback.core.status.Status;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/receta-controller/recetas")
public class RecetaController {
	
	@Autowired
	private RecetaDao recetaDao;
	
	@GetMapping
	public Flux<Receta> listar(@RequestParam(required=false) Map<String,String> qparams) 
	{
		qparams.forEach((a,b) -> {
			System.out.println(String.format("%s -> %s", a,b));
			
		});
		String nombre = qparams.get("nombre");
		return recetaDao.findAll()
							.filter(receta -> null!=nombre ? receta.getNombre().contains(nombre) : true);
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Map<String, Object>>> consultar(@PathVariable String id) {
		Map<String, Object> respuesta = new HashMap<String, Object>();

		return recetaDao.findById(id)
			.defaultIfEmpty(new Receta())
			.map(rec -> {
				if(null == rec.getId()) {
					respuesta.put("message", "No se encontró la receta con ID "+id);
					respuesta.put("timestamp", new Date());
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.contentType(MediaType.APPLICATION_JSON)
							.body(respuesta);
				} 
				respuesta.put("receta", rec);
				return ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(respuesta);
			});
	}
	
	/*@GetMapping("/{id}")
	public Mono<ResponseEntity<Map<String, Object>>> consultar(@PathVariable String id) {
		Map<String, Object> respuesta = new HashMap<String, Object>();

		return recetaDao.findById(id)
			.defaultIfEmpty(new Receta())
			.map(rec -> {
				if(null == rec.getId()) {
					respuesta.put("message", "No se encontró la receta con ID "+id);
					respuesta.put("timestamp", new Date());
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.contentType(MediaType.APPLICATION_JSON)
							.body(respuesta);
				} 
				respuesta.put("receta", rec);
				respuesta.put("message", "Consulta Exitosa");
				respuesta.put("timestamp", new Date());
				return ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(respuesta);
			});
	}*/
	
	@PostMapping("")
	public Mono<ResponseEntity<Map<String, Object>>> crear(@Valid @RequestBody Mono<Receta> monoReceta) 
	{
		Map<String, Object> respuesta = new HashMap<String, Object>();
		return monoReceta.flatMap(receta -> {
			return recetaDao
					.save(receta)
					.map(rec -> {
						respuesta.put("receta", rec);
						respuesta.put("mensaje", "producto creado con exito");
						
							return ResponseEntity.created(URI.create("/receta-controller/recetas/" + rec.getId()))
							.contentType(MediaType.APPLICATION_JSON)
							.body(respuesta);
			});
		}).onErrorResume(ex -> {
			return generarError(ex);		
		});
	}
	
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
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Map<String, Object>>> borrar(@PathVariable String id) {
		Map<String, Object> respuesta = new HashMap<String, Object>();			
		return recetaDao.findById(id)
		
		          .flatMap(rec -> {
		        	  			
		        	  			return recetaDao.delete(rec).then(Mono.just(id));
		        	  			
		          })
		          .defaultIfEmpty("")
		          .map(check -> {
		        	  if(check.equals("")) {
		        		  	
							respuesta.put("mensaje", "no existe el producto id"+id);
							respuesta.put("timestamp", new Date());
							
							return ResponseEntity.status(HttpStatus.NOT_FOUND)
									.contentType(MediaType.APPLICATION_JSON)
									.body(respuesta);
		        	  }
		        	  respuesta.put("mensaje", "baja exitosa de la receta con el id"+id);
						respuesta.put("timestamp", new Date());
		        	  
		        	  return ResponseEntity.ok()
		        	  .contentType(MediaType.APPLICATION_JSON)
		        	  	.body(respuesta);
		          });
	}

}