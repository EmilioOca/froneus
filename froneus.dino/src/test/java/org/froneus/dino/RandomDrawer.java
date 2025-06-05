package org.froneus.dino;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import org.froneus.dino.model.Dinosaur;

public class RandomDrawer {
    private static SecureRandom rnd = new SecureRandom();

    public static int randomInt() {
        return Math.abs( rnd.nextInt() );
    }

    public static String someWord() {
        return "TestWord" + randomInt();
    }

    public static String someCode() {
        return "" + randomInt();
    }

    public static String someWord( int trim ) {
        String word = someWord();
        return word.substring( 0, Math.min( trim, word.length() ) );
    }

    public static Dinosaur someDinoAlive() {
        return new Dinosaur( someWord(), someWord(),
                             LocalDateTime.of( 2015, 6, 12, 0, 0 ),
                             LocalDateTime.of( 2023, 12, 31, 23, 59 ),
                             Dinosaur.Status.ALIVE );
    }

    public static Dinosaur blue() {
        return new Dinosaur( "Blue",
                             "Velociraptor",
                             LocalDateTime.of( 2015, 6, 12, 0, 0 ),
                             LocalDateTime.of( 2025, 12, 31, 23, 59 ),
                             Dinosaur.Status.ALIVE
        );
    }

}