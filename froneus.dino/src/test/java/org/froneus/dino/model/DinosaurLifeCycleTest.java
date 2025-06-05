package org.froneus.dino.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.froneus.dino.RandomDrawer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DinosaurLifeCycleTest {

    @Test public void aDinosaurIsAlive() {
        Assertions.assertTrue( RandomDrawer.blue().isAlive() );
    }

    @Test public void aDinosaurIsAliveWhenUpdatedeErly() {
        Dinosaur blue = RandomDrawer.blue();
        blue.updateStatusAt( blue.getDiscoveryDate() );
        assertTrue( blue.isAlive() );

        assertTrue( isMutable( blue ) );
    }

    @Test public void aDinosaurIsEndangeredWithinAnHour() {
        Dinosaur blue = RandomDrawer.blue();
        blue.updateStatusAt( blue.getExtinctionDate().minusHours( 23 ) );
        assertTrue( blue.isEndangered() );

        assertTrue( isMutable( blue ) );
    }

    @Test public void aDinosaurIsExtinctWhenUpdatedLate() {
        Dinosaur blue = RandomDrawer.blue();
        blue.updateStatusAt( blue.getExtinctionDate().plusSeconds( 1 ) );
        assertTrue( blue.isExtinct() );
        assertFalse( isMutable( blue ) );
    }

    @Test public void aDinosaurIsExtinctWhenUpdatedTooLate() {
        Dinosaur blue = RandomDrawer.blue();
        blue.updateStatusAt( blue.getExtinctionDate().plusHours( 1 ) );
        assertTrue( blue.isExtinct() );
        assertFalse( isMutable( blue ) );
    }

    private boolean isMutable( Dinosaur blue ) {
        blue.setSpecies( "greyhound" );
        return "greyhound".equals( blue.getSpecies() );
    }

}