package org.froneus.dino.service;

import java.util.function.Supplier;
import java.util.stream.StreamSupport;
import org.froneus.dino.model.Dinosaur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DinosaurService {

    @Autowired private DinosaurRepository repository;

    public Dinosaur save( Dinosaur dinosaur ) {
        if (findByName( dinosaur.getName(), () -> null ) != null)
            throw new IllegalArgumentException( "A dinosaur with this name already exists." );

        return repository.save( dinosaur );
    }

    @Transactional(readOnly = true)
    public List<Dinosaur> findAll() {
        return StreamSupport.stream( repository.findAll().spliterator(), false ).toList();
    }

    @Transactional(readOnly = true)
    public Dinosaur findByName( String name ) {
        return findByName( name, () -> {
            throw new RuntimeException( "Object of class " + Dinosaur.class + " and name: " + name + " not found" );
        } );
    }

    @Transactional(readOnly = true)
    public Dinosaur findByName( String name, Supplier<? extends Dinosaur> supplier ) {
        return repository.findByName( name ).orElseGet( supplier );
    }

    public Dinosaur update( String name, Dinosaur updated ) {
        Dinosaur existing = repository.findByName( name )
                .orElseThrow( () -> new NoSuchElementException( "Dinosaur not found" ) );

        if (existing.getStatus() == Dinosaur.Status.EXTINCT) {
            throw new IllegalStateException( "Cannot modify an extinct dinosaur." );
        }

        existing.setSpecies( updated.getSpecies() );
        existing.setDiscoveryDate( updated.getDiscoveryDate() );
        existing.setExtinctionDate( updated.getExtinctionDate() );
        existing.setStatus( updated.getStatus() );
        existing.enforceDates();
        return repository.save( existing );
    }

    public boolean delete( String name ) {
        Optional<Dinosaur> existing = repository.findByName( name );
        if (existing.isEmpty()) return false;

        delete( existing.get() );
        return true;
    }

    public boolean delete( Dinosaur existing ) {
        repository.delete( existing );
        return true;
    }

}

