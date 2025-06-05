package org.froneus.dino.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.core.type.TypeReference;
import java.time.LocalDateTime;
import org.froneus.dino.RandomDrawer;
import org.froneus.dino.model.Dinosaur;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.froneus.dino.service.DinosaurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
public class DinosaurControllerTest {
//    @Autowired DinosaurController controller;
    @Autowired DinosaurService service;
    @Autowired MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
//    @MockBean Dealer dealer;
//
//    @BeforeEach void beforeEach() {
//        when( dealer.fullDeck() ).thenReturn( UnoServiceTest.fullDeck() );
//    }



    @Test void shouldCreateDinosaurSuccessfully() throws Exception {
        Dinosaur request = RandomDrawer.someDinoAlive();

        mockMvc.perform( post( "/v1/dinosaur" )
                                 .contentType( MediaType.APPLICATION_JSON )
                                 .content( objectMapper.writeValueAsString( request ) ) )
                .andExpect( status().is( 201 ) )
                .andExpect( jsonPath( "$.name" ).value( request.getName() ) )
                .andExpect( jsonPath( "$.species" ).value( request.getSpecies() ) )
                .andExpect( jsonPath( "$.status" ).value( request.getStatus().toString() ) );
    }


    @Test void shouldNotCreateDinosaurTwice() throws Exception {
        Dinosaur dino = RandomDrawer.someDinoAlive();
        service.save( dino );

        mockMvc.perform( post( "/v1/dinosaur" )
                                 .contentType( MediaType.APPLICATION_JSON )
                                 .content( objectMapper.writeValueAsString( dino ) ) )
                .andExpect( status().isBadRequest() );
    }


    @Test void shouldNotCreateDinosaurWithoutTime() throws Exception {
        Dinosaur dino = RandomDrawer.someDinoAlive();
        mockMvc.perform( post( "/v1/dinosaur" )
                                 .contentType( MediaType.APPLICATION_JSON )
                                 .content( objectMapper.writeValueAsString( dino )
                                                   .replace( dino.getDiscoveryDate().toString(),
                                                             dino.getExtinctionDate().toString() ) ) )
                .andExpect( status().isBadRequest() )
                .andExpect( content().string( containsString( "Discovery date must be before extinction date" ) ) );
    }

    @Test void shouldReturnDinosaurByName() throws Exception {
        Dinosaur dino = RandomDrawer.someDinoAlive();
        service.save( dino );

        mockMvc.perform( get( "/v1/dinosaur/" + dino.getName() ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.name" ).value( dino.getName() ) )
                .andExpect( jsonPath( "$.species" ).value( dino.getSpecies() ) );
    }

    @Test void shouldReturnNotFoundIfDinosaurNotExists() throws Exception {
        mockMvc.perform( get( "/v1/dinosaur/Unknown" ) )
                .andExpect( status().isNotFound() );
    }

    @Test void shouldUpdateDinosaurSuccessfully() throws Exception {
        Dinosaur dino = RandomDrawer.someDinoAlive();
        service.save( dino );

        mockMvc.perform(put("/v1/dinosaur/" + dino.getName() )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content( objectMapper.writeValueAsString( dino )
                                                  .replace( dino.getSpecies(),
                                                            "REX" ) ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.species").value("REX"));
    }

    @Test void shouldRejectUpdateIfDinosaurIsExtinct() throws Exception {
        Dinosaur dino = RandomDrawer.someDinoAlive();
        dino.updateStatusAt( dino.getExtinctionDate().plusHours( 1 ) );
        assertTrue( dino.isExtinct() );
        service.save( dino );

        mockMvc.perform(put("/v1/dinosaur/" + dino.getName() )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content( objectMapper.writeValueAsString( dino )
                                                      .replace( dino.getSpecies(),
                                                                "REX" ) ))
                .andExpect(status().isForbidden());
    }

    @Test void shouldReturnNotFoundIfDinosaurDoesNotExist() throws Exception {
        mockMvc.perform(put("/v1/dinosaur/Unknown")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(RandomDrawer.someDinoAlive())))
                .andExpect(status().isNotFound());
    }
}