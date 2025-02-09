package com.leroy.merlin.repository;


import com.leroy.merlin.model.Commande;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommandeRepositoryTest {

    @Autowired
    private CommandeRepository commandeRepository;

    @Test
    void testSaveAndFindById() {
        // Créer une nouvelle commande
        Commande commande = new Commande();
        commande.setDate(LocalDate.of(2025, 1, 6));
        commande.setDateLivraison(LocalDate.of(2025, 1, 9));
        commande.setQuantite(12);

        // Sauvegarder la commande
        Commande savedCommande = commandeRepository.save(commande);
        assertNotNull(savedCommande.getId(), "L'ID de la commande ne doit pas être nul après la sauvegarde");

        // Récupérer la commande par ID
        Optional<Commande> foundCommande = commandeRepository.findById(savedCommande.getId());
        assertTrue(foundCommande.isPresent(), "La commande doit être trouvée dans le repository");
        assertEquals(12, foundCommande.get().getQuantite(), "La quantité de la commande doit être correcte");
    }
}
