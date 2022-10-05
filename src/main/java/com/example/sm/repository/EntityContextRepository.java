package com.example.sm.repository;

import com.example.sm.model.Entity;

import org.springframework.data.mongodb.repository.MongoRepository;



public interface EntityContextRepository extends MongoRepository<Entity, String> {


}
