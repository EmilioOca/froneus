package org.froneus.dino.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Getter
@Entity
@Table(name = "dinosaur")
public class Dinosaur {

    public static Dinosaur from( Map<String, String> dinosaur ) {
        try {
            return new Dinosaur( dinosaur.get( "name" ),
                                 dinosaur.get( "species" ),
                                 LocalDateTime.parse( dinosaur.get( "discoveryDate" ) ),
                                 LocalDateTime.parse( dinosaur.get( "extinctionDate" ) ),
                                 Status.valueOf( dinosaur.get( "status" ) ) );
        } catch (Exception e ) {
            throw new IllegalArgumentException( e );
        }
    }

    public enum Status {ALIVE, ENDANGERED, EXTINCT}

    @Id @Column(nullable = false, unique = true) private String name;
    private String species;
    private LocalDateTime discoveryDate;
    private LocalDateTime extinctionDate;
    @Enumerated(EnumType.STRING) private Dinosaur.Status status;

    public Dinosaur() {}
    public Dinosaur( String name, String species, LocalDateTime discoveryDate,
                     LocalDateTime extinctionDate, Dinosaur.Status status ) {
        this.name = name;
        this.species = species;
        this.discoveryDate = discoveryDate;
        this.extinctionDate = extinctionDate;
        this.status = status;
        enforceDates();
    }

    public Dinosaur updateStatusAt( LocalDateTime moment ) {
        if ( extinctionDate.isBefore( moment ) ) {
            status = Status.EXTINCT;
        } else if ( extinctionDate.minusDays( 1 ).isBefore( moment ) ) {
            status = Status.ENDANGERED;
        }
        return this;
    }

    public void enforceDates() {
        if (!discoveryDate.isBefore( extinctionDate )) {
            throw new IllegalArgumentException( "Discovery date must be before extinction date." );
        }
    }

    public boolean equals( Object o ) {
        return this == o ||
               ( o != null && getClass() == o.getClass() && name.equals( ((Dinosaur)o).name ) );
    }

    public int hashCode() {
        return Objects.hash( name );
    }

    @JsonIgnore public boolean isAlive() {      return status == Status.ALIVE; }
    @JsonIgnore public boolean isEndangered() { return status == Status.ENDANGERED; }
    @JsonIgnore public boolean isExtinct() {    return status == Status.EXTINCT; }

    public void setSpecies( String species ) {
        if ( !isExtinct() ) this.species = species;
    }

    public void setStatus( Status status ) {
        if ( !isExtinct() ) this.status = status;
    }

    public void setDiscoveryDate( LocalDateTime discoveryDate ) {
        if ( !isExtinct() ) {
            this.discoveryDate = discoveryDate;
            enforceDates();
        }
    }

    public void setExtinctionDate( LocalDateTime extinctionDate ) {
        if ( !isExtinct() ) {
            this.extinctionDate = extinctionDate;
            enforceDates();
        }
    }
//    public static Dinosaur fromDomain( Dinosaur dino ) {
//        return new Dinosaur(
//                dino.getName(),
//                dino.getSpecies(),
//                dino.getDiscoveryDate(),
//                dino.getExtinctionDate(),
//                dino.getStatus()
//        );
//    }

//    public Dinosaur toDomain() {
//        Dinosaur dino = new Dinosaur( this.name );
//        dino.setSpecies( this.species );
//        dino.setDiscoveryDate( this.discoveryDate );
//        dino.setExtinctionDate( this.extinctionDate );
//        dino.setStatus( this.status );
//        return dino;
//    }

}
