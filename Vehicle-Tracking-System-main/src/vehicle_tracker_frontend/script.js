document.addEventListener('DOMContentLoaded', () => {
    const fetchVehiclesButton = document.getElementById('fetchVehicles');
    const vehicleList = document.getElementById('vehicleList');
    const map = L.map('map').setView([0, 0], 2); // Initialize map with a default view

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '© OpenStreetMap'
    }).addTo(map);

    fetchVehiclesButton.addEventListener('click', loadVehicles);

    function loadVehicles() {
        fetch('http://localhost:8080/vehicles/history?licensePlate=ALL') 
        .then(response => response.json())
        .then(data => {
            console.log("Fetched Vehicles: ", data); // Debugging line
            vehicleList.innerHTML = ""; // Clear previous list
            clearMapMarkers();

            if (Array.isArray(data)) {
                data.forEach(vehicle => {
                    addVehicleToList(vehicle);
                    addMarkerToMap(vehicle);
                });
            } else {
                alert('No vehicles found or invalid response format.');
            }
        })
        .catch(error => console.error('Error:', error));
    }

    function clearMapMarkers() {
        map.eachLayer(layer => {
            if (layer instanceof L.Marker) {
                map.removeLayer(layer); // Remove old markers
            }
        });
    }

    function addVehicleToList(vehicle) {
        const li = document.createElement('li');
        li.textContent = `License Plate: ${vehicle.licensePlate}, Owner: ${vehicle.owner}, Location: ${vehicle.currentLocation}`;
        vehicleList.appendChild(li);
    }

    function addMarkerToMap(vehicle) {
        if (vehicle.currentLocation && vehicle.currentLocation !== "Unknown") {
            const [lat, lng] = vehicle.currentLocation.split(",").map(Number);
            const marker = L.marker([lat, lng]).addTo(map)
                .bindPopup(`${vehicle.licensePlate} - ${vehicle.owner}`)
                .openPopup();
        }
    }
});

