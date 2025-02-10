package com.leroy.merlin.service;

import com.leroy.merlin.model.Commande;
import com.leroy.merlin.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommandeService {
    private static final int STOCK_INITIAL = 20;
    private static final int MULTIPLE_COMMANDE = 12;
    private static final int NB_JOURS_ANNEE = 365;

    @Autowired
    private CommandeRepository commandeRepository;

    public Map<String, Object> calculerCommandesEtSuiviStock(int delaiLivraison) {
        List<Commande> commandes = new ArrayList<>();
        List<Map<String, Object>> evolutionStock = new ArrayList<>(); // Pour suivre le stock
        LocalDate dateActuelle = LocalDate.of(2025, 1, 6); // Départ : lundi 6 janvier 2025
        int stock = STOCK_INITIAL; // Stock initial

        for (int jour = 0; jour < NB_JOURS_ANNEE; jour++) {
            // Ajouter l'état du stock pour ce jour
            Map<String, Object> stockInfo = new HashMap<>();
            stockInfo.put("date", dateActuelle.toString());
            stockInfo.put("stock", stock);
            evolutionStock.add(stockInfo);

            DayOfWeek jourSemaine = dateActuelle.getDayOfWeek();

            // Consommer les ventes quotidienne
            int quantiteCommande = (jourSemaine.getValue() > DayOfWeek.FRIDAY.getValue()) ? 10 : 5;
            stock -= quantiteCommande;

            // Créer et enregistrer la commande
            Commande commande = new Commande();
            commande.setDate(dateActuelle);
            commande.setDateLivraison(dateActuelle.plusDays(delaiLivraison)); // Date de livraison
            commande.setQuantite(quantiteCommande);
            commandes.add(commandeRepository.save(commande)); // Sauvegarder

            if (stock <= 5) { // Seuil critique
                stock += MULTIPLE_COMMANDE; // Mise à jour du stock
            }

            // Passer au jour suivant
            dateActuelle = dateActuelle.plusDays(1);
        }

        // Retourner les commandes et l'évolution du stock
        Map<String, Object> result = new HashMap<>();
        result.put("commandes", commandes);
        result.put("evolutionStock", evolutionStock);
        return result;
    }


}