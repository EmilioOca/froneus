package org.froneus.dino.service;

import java.util.Optional;
import java.util.UUID;
import org.froneus.dino.model.Dinosaur;
import org.springframework.data.repository.CrudRepository;

public interface DinosaurRepository extends CrudRepository<Dinosaur, UUID> {
        Optional<Dinosaur> findByName( String name );

}