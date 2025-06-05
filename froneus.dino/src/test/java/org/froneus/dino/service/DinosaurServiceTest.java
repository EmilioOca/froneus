package org.froneus.dino.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.froneus.dino.RandomDrawer;
import org.froneus.dino.model.Dinosaur;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DinosaurServiceTest {
    @Autowired private DinosaurService service;

    @Test public void testEntitySave() {
        Dinosaur model = newSample();
        Dinosaur retrieved = service.save( model );
        assertNotNull( retrieved.getName() );
        assertNotNull( model.getName() );
        assertEquals( retrieved, model );
    }

    @Test public void testEntityUpdate() {
        Dinosaur model = savedSample();

        updateUser( model );
        service.save( model );
        Dinosaur retrieved = service.findByName( model.getName() );
        assertEquals( model, retrieved );
    }

    @Test public void testDeletionByObject() {
        Dinosaur model = savedSample();

        service.delete( model );
        assertThrows( RuntimeException.class, () -> service.findByName( model.getName() ) );

    }

    @Test public void testDeletionById() {
        Dinosaur model = savedSample();

        service.delete( model.getName() );
        assertThrows( RuntimeException.class, () -> service.findByName( model.getName() ) );
    }

    @Test public void testFindAll() {
        Dinosaur model = savedSample();
        List list = service.findAll();
        assertFalse( list.isEmpty() );
        assertTrue( list.contains( model ) );
    }

    protected Dinosaur newSample() {
        return RandomDrawer.someDinoAlive();
    }

    protected Dinosaur updateUser( Dinosaur user ) {
        user.setSpecies( "John2do" );
        return user;
    }

    protected Dinosaur savedSample() {
        return service.save( newSample() );
    }
}