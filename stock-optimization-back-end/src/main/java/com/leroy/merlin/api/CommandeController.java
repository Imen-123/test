package com.leroy.merlin.api;

import com.leroy.merlin.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/commandes")
class CommandeController {
    @Autowired
    private CommandeService commandeService;

    @GetMapping("/calcul")
    public Map<String, Object> calculerCommandes(
            @RequestParam(defaultValue = "3") int delaiLivraison) {
        return commandeService.calculerCommandesEtSuiviStock(delaiLivraison);
    }
}

