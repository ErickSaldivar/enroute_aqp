// Solo ejecutar el código del mapa si existe el elemento #map
        if (document.getElementById('map')) {
            // Inicializar el mapa
            let map;
            let markers = [];
            let originMarker = null;
            let destMarker = null;
            let routeControl = null;
            // Bounding box duro para Arequipa (aprox): lon,lat
            const AQP_BOUNDS = {
                left: -71.65,   // oeste
                right: -71.40,  // este
                top: -16.30,    // norte (mayor lat, menos negativo)
                bottom: -16.60  // sur (menor lat, más negativo)
            };
            function isInAqpBounds(latlng) {
                if (!latlng) return false;
                const lat = latlng.lat, lon = latlng.lng;
                return (
                    lon >= AQP_BOUNDS.left && lon <= AQP_BOUNDS.right &&
                    lat >= AQP_BOUNDS.bottom && lat <= AQP_BOUNDS.top
                );
            }
            
            function initMap() {
                // Centrar el mapa en Arequipa, Perú
                map = L.map('map').setView([-16.4090, -71.5375], 12);
                
                // Agregar capa de OpenStreetMap
                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                }).addTo(map);
                
                // Instrucciones mínimas
                addHelpControl();

                // Click para elegir origen/destino
                map.on('click', onMapClick);
            }
            
            // Buscar ruta usando los inputs de texto (origen/destino) o marcadores existentes
            async function buscarRuta() {
                const btn = document.getElementById('btn-buscar');
                if (btn) btn.disabled = true;
                try {
                    const origenVal = document.getElementById('origen')?.value?.trim();
                    const destinoVal = document.getElementById('destino')?.value?.trim();

                    // Coordenadas candidatas
                    let originLatLng = originMarker ? originMarker.getLatLng() : null;
                    let destLatLng = destMarker ? destMarker.getLatLng() : null;

                    // Geocodificar si hay texto y no hay marcador
                    if (origenVal && !originLatLng) {
                        const g = await geocode(origenVal);
                        if (!g) { alert('Origen no encontrado: ' + origenVal); return; }
                        originLatLng = L.latLng(parseFloat(g.lat), parseFloat(g.lon));
                        if (!isInAqpBounds(originLatLng)) {
                            alert('El origen solicitado esta fuera de los limites de Arequipa.');
                            return;
                        }
                    }
                    if (destinoVal && !destLatLng) {
                        const g2 = await geocode(destinoVal);
                        if (!g2) { alert('Destino no encontrado: ' + destinoVal); return; }
                        destLatLng = L.latLng(parseFloat(g2.lat), parseFloat(g2.lon));
                        if (!isInAqpBounds(destLatLng)) {
                            alert('El destino solicitado esta fuera de los limites de Arequipa.');
                            return;
                        }
                    }

                    if (!originLatLng || !destLatLng) {
                        // Si ya existen ambos marcadores, dibuja. Si falta alguno, instructivo.
                        if (originMarker && destMarker) {
                            drawRoute();
                        } else {
                            alert('Proporcione origen y destino (por formulario) o haga clic en el mapa para definirlos.');
                        }
                        return;
                    }

                    // Crear o mover marcadores según corresponda
                    if (!originMarker) originMarker = createDraggableMarker(originLatLng, 'Origen'); else originMarker.setLatLng(originLatLng);
                    if (!destMarker) destMarker = createDraggableMarker(destLatLng, 'Destino'); else destMarker.setLatLng(destLatLng);

                    // Centrar vista entre ambos
                    try {
                        const group = L.featureGroup([originMarker, destMarker]);
                        map.fitBounds(group.getBounds().pad(0.2));
                    } catch (e) {}

                    // Validación final por si vinieron de marcadores anteriores
                    if (!isInAqpBounds(originLatLng) || !isInAqpBounds(destLatLng)) {
                        alert('Alguno de los puntos esta fuera de los limites de Arequipa.');
                        return;
                    }
                    drawRoute();
                } catch (err) {
                    console.error('Error en buscarRuta:', err);
                    alert('Ocurrio un error buscando la ruta. Reintenta.');
                } finally {
                    if (btn) btn.disabled = false;
                }
            }

            // Hacer la función accesible globalmente para que el onclick HTML la encuentre en Edge/IE
            try { window.buscarRuta = buscarRuta; } catch (e) { /* noop */ }
            
            // Geocodificador simple usando Nominatim (OpenStreetMap)
            // Geocodificador simple usando Nominatim (OpenStreetMap)
            async function geocode(query) {
                if (!query) return null;
                // Geocodificación limitada (hard bound) al rectángulo de Arequipa
                const viewbox = [
                    AQP_BOUNDS.left,
                    AQP_BOUNDS.top,
                    AQP_BOUNDS.right,
                    AQP_BOUNDS.bottom
                ].join(',');
                const params = new URLSearchParams({
                    format: 'json',
                    limit: '1',
                    q: query,
                    countrycodes: 'pe',
                    viewbox: viewbox,
                    bounded: '1'
                });
                const url = 'https://nominatim.openstreetmap.org/search?' + params.toString();
                try {
                    const resp = await fetch(url, { headers: { 'Accept': 'application/json', 'Accept-Language': 'es' } });
                    if (!resp.ok) return null;
                    const data = await resp.json();
                    if (data && data.length > 0) return data[0];
                    return null;
                } catch (e) {
                    console.error('Geocode error', e);
                    return null;
                }
            }

            function onMapClick(e) {
                // 1er clic: origen, 2do clic: destino; 3er clic: reinicia y pone nuevo origen
                if (!originMarker) {
                    originMarker = createDraggableMarker(e.latlng, 'Origen');
                } else if (!destMarker) {
                    destMarker = createDraggableMarker(e.latlng, 'Destino');
                    drawRoute();
                } else {
                    resetRoute();
                    originMarker = createDraggableMarker(e.latlng, 'Origen');
                }
            }

            function createDraggableMarker(latlng, label) {
                const marker = L.marker(latlng, { draggable: true }).addTo(map).bindPopup(label).openPopup();
                markers.push(marker);
                marker.on('dragend', function() {
                    if (originMarker && destMarker) {
                        updateRouteWaypoints();
                    }
                });
                return marker;
            }

            function drawRoute() {
                if (!originMarker || !destMarker || !window.L || !L.Routing) return;
                if (routeControl) {
                    try { map.removeControl(routeControl); } catch (_) {}
                    routeControl = null;
                }
                routeControl = L.Routing.control({
                    waypoints: [originMarker.getLatLng(), destMarker.getLatLng()],
                    router: L.Routing.osrmv1({ serviceUrl: 'https://router.project-osrm.org/route/v1' }),
                    show: false,
                    addWaypoints: false,
                    draggableWaypoints: true,
                    routeWhileDragging: true,
                    fitSelectedRoutes: true,
                    lineOptions: {
                        styles: [
                        // trazo externo (halo)
                        { color: '#000000', opacity: 0.12, weight: 12 },
                        // trazo interior (principal)
                        { color: '#DD6B20', opacity: 1.0, weight: 6 }
                        ],
                        extendToWaypoints: true
                    }
                });

                // Registrar listeners ANTES de agregar al mapa para no perder el primer evento
                routeControl.on('routesfound', function(e) {
                    try {
                        if (e && e.routes && e.routes.length > 0) {
                            const summary = e.routes[0].summary;
                            const distKm = (summary.totalDistance / 1000).toFixed(2);
                            const timeMin = Math.round(summary.totalTime / 60);
                            const infoEl = document.getElementById('route-info');
                            if (infoEl) infoEl.innerText = 'Distancia: ' + distKm + ' km - Duracion: ' + timeMin + ' min';
                        }
                    } catch (err) { console.error('routesfound handler error', err); }
                });
                routeControl.on('routingerror', function(err){
                    console.error('Routing error', err);
                    const infoEl = document.getElementById('route-info');
                    if (infoEl) infoEl.innerText = 'No se pudo calcular la ruta.';
                });

                routeControl.addTo(map);
            }

            function updateRouteWaypoints() {
                if (!routeControl) return;
                routeControl.setWaypoints([originMarker.getLatLng(), destMarker.getLatLng()]);
            }

            function resetRoute() {
                // Eliminar ruta
                if (routeControl) {
                    try { map.removeControl(routeControl); } catch (_) {}
                    routeControl = null;
                }
                // Eliminar marcadores creados
                if (originMarker) { map.removeLayer(originMarker); originMarker = null; }
                if (destMarker) { map.removeLayer(destMarker); destMarker = null; }
                // Limpiar array auxiliar si se usa
                markers.forEach(m => { try { map.removeLayer(m); } catch (_) {} });
                markers = [];
                // Limpiar info de ruta
                const infoEl = document.getElementById('route-info');
                if (infoEl) infoEl.innerText = '';
                // Limpiar inputs de búsqueda si existen
                try {
                    const o = document.getElementById('origen');
                    const d = document.getElementById('destino');
                    if (o) o.value = '';
                    if (d) d.value = '';
                } catch (e) { /* noop */ }
            }

            function addHelpControl() {
                // Botón limpiar y ayuda breve como control Leaflet
                const HelpControl = L.Control.extend({
                    options: { position: 'topright' },
                    onAdd: function() {
                        const div = L.DomUtil.create('div', 'leaflet-bar');
                        div.style.background = 'white';
                        div.style.padding = '8px';
                        div.style.lineHeight = '1.5';
                        div.style.minWidth = '200px';
                        div.innerHTML = `
                            <div style="font-size:12px; margin-bottom:6px;">
                                1) Clic para Origen<br/>
                                2) Clic para Destino<br/>
                                Puedes arrastrar los marcadores.
                            </div>
                            <div id="route-info" style="font-size:12px; margin-bottom:6px; color:#333;"></div>
                            <button id="btn-clear-route" class="btn btn-sm btn-outline-secondary w-100">Limpiar ruta</button>
                        `;
                        // Evitar que el mapa capture clics sobre el control
                        L.DomEvent.disableClickPropagation(div);
                        setTimeout(() => {
                            const btn = document.getElementById('btn-clear-route');
                            if (btn) btn.addEventListener('click', resetRoute);
                        }, 0);
                        return div;
                    }
                });
                map.addControl(new HelpControl());
            }
            
            // Inicializar el mapa cuando cargue la página
            document.addEventListener('DOMContentLoaded', function() {
                initMap();
            });
            
            // Hacer que el mapa se redimensione correctamente
            window.addEventListener('resize', function() {
                if (map) {
                    setTimeout(() => {
                        map.invalidateSize();
                    }, 100);
                }
            });

            // Redimensionar mapa cuando se abra/cierre el offcanvas en móviles
            document.getElementById('mobileSidebar')?.addEventListener('hidden.bs.offcanvas', function () {
                if (map) {
                    setTimeout(() => {
                        map.invalidateSize();
                    }, 100);
                }
            });

            // Ajustar altura del mapa en móviles
            function adjustMapHeight() {
                const mapElement = document.getElementById('map');
                if (window.innerWidth < 992) { // Móviles
                    const viewportHeight = window.innerHeight;
                    const searchBarHeight = document.querySelector('.bg-white.shadow-sm').offsetHeight;
                    const mobileHeaderHeight = document.querySelector('.d-lg-none.p-3') ? 
                        document.querySelector('.d-lg-none.p-3').offsetHeight : 0;
                    const remainingHeight = viewportHeight - searchBarHeight - mobileHeaderHeight;
                    mapElement.style.height = Math.max(remainingHeight, 300) + 'px';
                }
            }

            // Ajustar altura al cargar y redimensionar
            window.addEventListener('load', adjustMapHeight);
            window.addEventListener('resize', adjustMapHeight);
        }