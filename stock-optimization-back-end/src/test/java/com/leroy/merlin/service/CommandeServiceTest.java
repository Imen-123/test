package com.leroy.merlin.service;

import com.leroy.merlin.model.Commande;
import com.leroy.merlin.repository.CommandeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommandeServiceTest {

    @Mock
    private CommandeRepository commandeRepository;

    @InjectMocks
    private CommandeService commandeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculerCommandesEtSuiviStock() {
        // Paramètre de test
        int delaiLivraison = 3;

        // Simuler le comportement du repository
        when(commandeRepository.save(any(Commande.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Appeler la méthode à tester
        Map<String, Object> result = commandeService.calculerCommandesEtSuiviStock(delaiLivraison);

        // Vérifier les résultats
        List<Map<String, Object>> evolutionStock = (List<Map<String, Object>>) result.get("evolutionStock");
        List<Commande> commandes = (List<Commande>) result.get("commandes");

        // Assertions basiques
        assertEquals(365, evolutionStock.size(), "L'évolution des stocks doit contenir 365 jours.");
        assertEquals(5, commandes.get(0).getQuantite(), "La première commande doit avoir une quantité correspondant au multiple de commande par défaut.");

    }
}
