<template>
  <div class="container">
    <h1>Optimisation des Commandes</h1>
    <div class="form-group-inline">
      <label>Délai de livraison :</label>
      <input type="number" v-model="delaiLivraison" min="1" class="input-field" />
      <button class="btn" @click="calculerCommandes">Calculer</button>
    </div>

    <h2>Tableau des Commandes</h2>
    <table class="styled-table">
      <thead>
        <tr>
          <th>Date de Commande</th>
          <!--<th>Date de Livraison</th>-->
          <th>Quantité</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="commande in commandes" :key="commande.date">
          <td>{{ getDayNameWithDate(commande.date) }}</td>
          <!--<td>{{ getDayNameWithDate(commande.dateLivraison) }}</td>-->
          <td>{{ commande.quantite }}</td>
        </tr>
      </tbody>
    </table>

    <h2>Courbe d'Évolution des Stocks</h2>
    <canvas id="stockChart"></canvas>
  </div>
</template>

<script>
import axios from 'axios';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

export default {
  data() {
    return {
      delaiLivraison: 3,
      multipleCommande: 12,
      commandes: [],
      evolutionStock: []
    };
  },
  methods: {
    async calculerCommandes() {
      try {
        const response = await axios.get('http://localhost:8080/api/commandes/calcul', {
          params: { delaiLivraison: this.delaiLivraison, multipleCommande: this.multipleCommande }
        });
        this.commandes = response.data.commandes;
        this.evolutionStock = response.data.evolutionStock;
        this.afficherGraphique();
      } catch (error) {
        console.error("Erreur lors du calcul des commandes :", error);
      }
    },
    getDayNameWithDate(dateString) {
      const days = ["Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"];
      const date = new Date(dateString);
      const dayName = days[date.getDay()];
      const formattedDate = date.toLocaleDateString('fr-FR'); // Format : DD/MM/YYYY
      return `${dayName} ${formattedDate}`;
    },
    afficherGraphique() {
      // Regrouper les données par mois pour stock mini, maxi et moyen
      const stocksParMois = {};
      this.evolutionStock.forEach(stockData => {
        const mois = stockData.date.substring(0, 7); // Récupérer Année-Mois
        const stock = stockData.stock;
        if (!stocksParMois[mois]) {
          stocksParMois[mois] = { min: stock, max: stock, total: stock, count: 1 };
        } else {
          stocksParMois[mois].min = Math.min(stocksParMois[mois].min, stock);
          stocksParMois[mois].max = Math.max(stocksParMois[mois].max, stock);
          stocksParMois[mois].total += stock;
          stocksParMois[mois].count += 1;
        }
      });

      // Transformer les données pour le graphique
      const moisLabels = Object.keys(stocksParMois);
      const stockMin = moisLabels.map(mois => stocksParMois[mois].min);
      const stockMax = moisLabels.map(mois => stocksParMois[mois].max);
      const stockMoyen = moisLabels.map(mois => Math.round(stocksParMois[mois].total / stocksParMois[mois].count));

      const ctx = document.getElementById('stockChart');
      if (ctx) {
        new Chart(ctx, {
          type: 'line',
          data: {
            labels: moisLabels,
            datasets: [
              {
                label: 'Stock Minimum',
                data: stockMin,
                borderColor: 'red',
                fill: false
              },
              {
                label: 'Stock Maximum',
                data: stockMax,
                borderColor: 'green',
                fill: false
              },
              {
                label: 'Stock Moyen',
                data: stockMoyen,
                borderColor: 'blue',
                fill: false
              }
            ]
          },
          options: {
            responsive: true,
            scales: {
              x: { title: { display: true, text: 'Mois' } },
              y: { title: { display: true, text: 'Stock (Unités)' } }
            }
          }
        });
      }
    }
  }
};
</script>

<style>
.container {
  max-width: 800px;
  margin: auto;
  text-align: center;
  font-family: Arial, sans-serif;
}
.form-group-inline {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin: 15px 0;
}
.input-field {
  padding: 8px;
  width: 100px;
}
.btn {
  background-color: #007BFF;
  color: white;
  padding: 10px 15px;
  border: none;
  cursor: pointer;
  margin: 10px 0;
}
.styled-table {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0;
}
.styled-table th, .styled-table td {
  padding: 10px;
  border: 1px solid #ddd;
}
.styled-table th {
  background-color: #007BFF;
  color: white;
}
</style>
