document.addEventListener('DOMContentLoaded', () => {
    // Ensure Leaflet is loaded
    if (typeof L === 'undefined') {
        console.error('Leaflet library not loaded');
        alert('Map functionality unavailable. Please check library inclusion.');
        return;
    }

    // Page Elements
    const loginPage = document.getElementById('loginPage');
    const registrationPage = document.getElementById('registrationPage');
    const vehiclesPage = document.getElementById('vehiclesPage');
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const viewVehiclesBtn = document.getElementById('viewVehiclesBtn');
    const backToRegistrationBtn = document.getElementById('backToRegistrationBtn');
    const logoutButtons = document.querySelectorAll('[id^="logoutButton"]');
    const vehicleList = document.getElementById('vehicleList');

    // Map Handling
    let vehiclesPageMap = null;

    function initializeMap(elementId) {
        // Remove existing map if it exists
        if (vehiclesPageMap) {
            vehiclesPageMap.remove();
        }
        
        // Ensure the map container exists
        const mapContainer = document.getElementById(elementId);
        if (!mapContainer) {
            console.error(`Map container ${elementId} not found`);
            return null;
        }
        
        // Clear any existing map content
        mapContainer.innerHTML = '';
        
        // Create new map
        const newMap = L.map(elementId).setView([0, 0], 2);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            maxZoom: 19,
            attribution: '© OpenStreetMap contributors'
        }).addTo(newMap);
        
        return newMap;
    }

    // Initialize map when vehicles page is loaded
    function ensureMapInitialized() {
        if (!vehiclesPageMap) {
            vehiclesPageMap = initializeMap('map');
        }
        return vehiclesPageMap;
    }

    // View Vehicles Button Handler
    if (viewVehiclesBtn) {
        viewVehiclesBtn.addEventListener('click', function() {
            // Ensure map is initialized
            const map = ensureMapInitialized();
            
            if (!map) {
                console.error('Map initialization failed');
                alert('Could not load map. Please try again.');
                return;
            }

            // Clear existing map markers and list
            map.eachLayer((layer) => {
                if (layer instanceof L.Marker) {
                    map.removeLayer(layer);
                }
            });
            vehicleList.innerHTML = '';

            // Fetch vehicles
            fetch('http://localhost:8082/vehicles/history?licensePlate=ALL', {
                method: 'GET',
                credentials: 'include'
            })
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then(vehicles => {
                if (!vehicles || vehicles.length === 0) {
                    vehicleList.innerHTML = '<li>No vehicles found</li>';
                    return;
                }

                // Populate vehicle list and map
                const markers = [];
                vehicles.forEach(vehicle => {
                    // Add to list
                    const li = document.createElement('li');
                    li.textContent = `License: ${vehicle.licensePlate}, Owner: ${vehicle.owner}, Location: ${vehicle.currentLocation || 'Unknown'}`;
                    vehicleList.appendChild(li);

                    // Add to map if location is valid
                    try {
                        if (vehicle.currentLocation && vehicle.currentLocation !== 'Unknown') {
                            const [lat, lng] = vehicle.currentLocation.split(',').map(Number);
                            
                            // Validate coordinates
                            if (!isNaN(lat) && !isNaN(lng) && 
                                lat >= -90 && lat <= 90 && 
                                lng >= -180 && lng <= 180) {
                                
                                const marker = L.marker([lat, lng])
                                    .bindPopup(`${vehicle.licensePlate} - ${vehicle.owner}`)
                                    .addTo(map);
                                
                                markers.push(marker);
                            }
                        }
                    } catch (error) {
                        console.error('Map marker error for vehicle:', vehicle, error);
                    }
                });

                // Adjust map view
                if (markers.length > 0) {
                    const group = new L.featureGroup(markers);
                    map.fitBounds(group.getBounds(), { 
                        padding: [50, 50],
                        maxZoom: 10
                    });
                } else {
                    map.setView([0, 0], 2);
                }
            })
            .catch(error => {
                console.error('Vehicles Fetch Error:', error);
                vehicleList.innerHTML = `<li>Error loading vehicles: ${error.message}</li>`;
            });
        });
    }

    // Additional page navigation and form handling code remains the same...
});