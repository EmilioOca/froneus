package org.froneus.dino.controller;

import java.util.Map;
import org.froneus.dino.model.Dinosaur;
import org.froneus.dino.service.DinosaurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/dinosaur")
public class DinosaurController {
    @Autowired private DinosaurService service;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body( ex.getMessage() );
    }

    @PostMapping
    public ResponseEntity<Dinosaur> create( @RequestBody Map dinosaur) {
        Dinosaur created = service.save( Dinosaur.from( dinosaur ) );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<Iterable<Dinosaur>> findAll() {
        return ResponseEntity.ok( service.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Dinosaur> findByName( @PathVariable String name ) {
        try {
            return ResponseEntity.ok( service.findByName( name ));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{name}")
    public ResponseEntity<Dinosaur> update(@PathVariable String name, @RequestBody Map dinosaur) {
        Dinosaur existing = service.findByName( name, () -> null );
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        if (existing.getStatus() == Dinosaur.Status.EXTINCT) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok( service.update( name, Dinosaur.from( dinosaur ) ) );
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> delete(@PathVariable String name ) {
        if (service.delete( name )) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
