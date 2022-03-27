package com.springboot.webflux.proyeto.app.controllers;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.springboot.webflux.proyecto.app.models.dao.RacionDao;
import com.springboot.webflux.proyecto.app.models.doc.Racion;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/racion-controller/raciones")
public class RacionController {
	
	@Autowired
	private RacionDao racionDao;
	
	@GetMapping("/")
	public Flux<Racion> listar(){
		return racionDao.findAll();
	}
	
	@PostMapping("")
	public Mono<ResponseEntity<Map<String,Object>>> crear(@Valid @RequestBody Mono<Racion> monoRacion){
		Map<String, Object> respuesta = new HashMap<String, Object>();
		return monoRacion.flatMap(racion -> {
			return racionDao
					.save(racion)
					.map(rac -> {
						respuesta.put("racion", rac);
						respuesta.put("mensaje", "racion creada con exito");
						
							return ResponseEntity.created(URI.create("/racion-controller/raciones/" + rac.getId()))
							.contentType(MediaType.APPLICATION_JSON)
							.body(respuesta);
			});
		}).onErrorResume(ex -> {
			return generarError(ex);		
		});
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Map<String, Object>>> borrar(@PathVariable String id) {
		Map<String, Object> respuesta = new HashMap<String, Object>();			
		return racionDao.findById(id)
		
		          .flatMap(rac -> {
		        	  			
		        	  			return racionDao.delete(rac).then(Mono.just(id));
		        	  			
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
		        	  respuesta.put("mensaje", "baja exitosa del ingrediente con el id"+id);
						respuesta.put("timestamp", new Date());
		        	  
		        	  return ResponseEntity.ok()
		        	  .contentType(MediaType.APPLICATION_JSON)
		        	  	.body(respuesta);
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

}
