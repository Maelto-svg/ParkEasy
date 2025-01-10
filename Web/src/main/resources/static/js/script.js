document.addEventListener("DOMContentLoaded", function () {
    const parkingGrid = document.getElementById('layout');
    const lotSelector = document.getElementById('lotSelector');
    const spotsPerColumn = 6;

    // Charger les lots de parking
    fetch("/api/parking/lots")
        .then(response => response.json())
        .then(lots => {
            // Ajouter les options au menu déroulant
            lots.forEach(lot => {
                const option = document.createElement("option");
                option.value = lot.id;
                option.textContent = lot.name;
                lotSelector.appendChild(option);
            });

            // Appeler la fonction pour afficher les places du premier lot après le chargement des lots
            if (lots.length > 0) {
                loadSpotsForLot(lots[0].id); // Charger les spots pour le premier lot
            }
        })
        .catch(error => console.error("Erreur lors du chargement des lots de parking :", error));

    // Fonction pour afficher les spots d’un lot spécifique
    function loadSpotsForLot(lotId) {
        // Vider la grille existante
        parkingGrid.innerHTML = "";

        fetch(`/api/parking/${lotId}/spots`)
            .then(response => response.json())
            .then(spots => {
                let currentColumn;

                spots.forEach((spot, index) => {
                    if (index % spotsPerColumn === 0) {
                        currentColumn = document.createElement("div");
                        currentColumn.classList.add("parking-column");
                        parkingGrid.appendChild(currentColumn);
                    }

                    const spotElement = document.createElement("div");
                    const avElement = document.createElement("span");

                    spotElement.classList.add("parking-slot");

                    if (spot.isOccupied) {
                        spotElement.classList.add("occupied");
                        avElement.textContent = "Occupied";
                    } else {
                        spotElement.classList.add("available");
                        avElement.textContent = "Available";

                        // Rendre la place cliquable
                        spotElement.addEventListener("click", function () {
                            reserveSpot(spot.id, spotElement, avElement);
                        });
                    }

                    spotElement.textContent = spot.spotNumber;
                    currentColumn.appendChild(spotElement);
                    spotElement.appendChild(avElement);
                });
            })
            .catch(error => console.error("Erreur lors du chargement des places de parking :", error));
    }

    // Gérer la sélection d’un lot
    lotSelector.addEventListener("change", function () {
        const selectedLotId = lotSelector.value;
        if (selectedLotId) {
            loadSpotsForLot(selectedLotId);
        }
    });

    // Réserver une place
    function reserveSpot(spotId, spotElement, avElement) {
        fetch(`/api/parking/spots/${spotId}/reserve`, { method: "PUT" })
            .then(response => {
                if (response.ok) {
                    spotElement.classList.remove("available");
                    spotElement.classList.add("reserved");
                    avElement.textContent = "Reserved";
                } else {
                    return response.text().then(errorMessage => {
                        alert(`Erreur : ${errorMessage}`);
                    });
                }
            })
            .catch(error => console.error("Erreur lors de la réservation :", error));
    }
});
