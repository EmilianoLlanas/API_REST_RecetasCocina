package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.example.demo.SpringbootApirestParcialApplication;
import com.springboot.webflux.proyecto.app.models.dao.RecetaDao;

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

}
