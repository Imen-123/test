package com.leroy.merlin.api;


import com.leroy.merlin.service.CommandeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CommandeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CommandeService commandeService;

    @InjectMocks
    private CommandeController commandeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commandeController).build();
    }

    @Test
    void testCalculerCommandes() throws Exception {
        // Mock des données retournées par le service
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("commandes", List.of());
        mockResponse.put("evolutionStock", List.of());
        when(commandeService.calculerCommandesEtSuiviStock(3)).thenReturn(mockResponse);

        // Exécuter la requête GET sur l'endpoint /api/commandes/calcul
        mockMvc.perform(get("/api/commandes/calcul")
                        .param("delaiLivraison", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Vérifie que le statut HTTP est 200

        // Vérifie que le service a été appelé avec le bon paramètre
        verify(commandeService, times(1)).calculerCommandesEtSuiviStock(3);
    }
}
