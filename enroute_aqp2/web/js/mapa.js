/**
 * Construye un array de paraderos que forman el trayecto en bus entre dos índices.
 * 
 * @param {Array} paraderos - Array completo de paraderos de la línea de bus
 * @param {number} idxA - Índice(primer paradero a tomar) inicial del paradero de subida en el array de paraderos
 * @param {number} idxB - Índice(ultimo paradero donde se debe bajar) final del paradero de bajada en el array de paraderos
 * @returns {Array} Array de paraderos que conforman el segmento del viaje en bus (incluye ambos extremos)
 * 
 * @description
 * Esta función recorre el array de paraderos desde el índice idxA hasta el índice idxB (ambos inclusive),
 * construyendo una secuencia ordenada de paraderos que representa el trayecto del bus.
 * La dirección del recorrido se determina automáticamente: si idxA < idxB avanza hacia adelante,
 * si idxA > idxB retrocede. Si idxA === idxB retorna un array con un solo paradero.
 */
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
            // Iconos SVG embebidos (pequeños) para marcadores - definidos en ámbito superior
            const busSvg = `<?xml version="1.0" encoding="UTF-8"?><svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' width='32' height='32'><rect x='2' y='6' width='20' height='10' rx='2' ry='2' fill='#FFC107'/><circle cx='7' cy='17' r='1.5' fill='#ffffff'/><circle cx='17' cy='17' r='1.5' fill='#ffffff'/><rect x='4' y='8' width='4' height='4' fill='#ffffff'/><rect x='16' y='8' width='2' height='4' fill='#ffffff'/></svg>`;
            const paraderoSubidaSvg = `<?xml version="1.0" encoding="UTF-8"?><svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' width='32' height='32'><circle cx='12' cy='12' r='10' fill='#4CAF50'/><path d='M12 8l-4 4h2.5v4h3v-4H16z' fill='#ffffff'/></svg>`;
            const paraderoBajadaSvg = `<?xml version="1.0" encoding="UTF-8"?><svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' width='32' height='32'><circle cx='12' cy='12' r='10' fill='#F44336'/><path d='M12 16l4-4h-2.5V8h-3v4H8z' fill='#ffffff'/></svg>`;
            const originSvg = `<?xml version="1.0" encoding="UTF-8"?><svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' width='42' height='42'><path d='M12 2C8 2 5 5 5 9c0 5 7 13 7 13s7-8 7-13c0-4-3-7-7-7z' fill='#FF8C00'/><circle cx='12' cy='9' r='2.2' fill='#ffffff'/></svg>`;
            const destSvg = `<?xml version="1.0" encoding="UTF-8"?><svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' width='42' height='42'><path d='M12 2C8 2 5 5 5 9c0 5 7 13 7 13s7-8 7-13c0-4-3-7-7-7z' fill='#1f2937'/><circle cx='12' cy='9' r='2.2' fill='#ffffff'/></svg>`;
            function svgToDataUrl(svg){ return 'data:image/svg+xml;charset=UTF-8,' + encodeURIComponent(svg); }

            const busIconSmall = L.icon({ iconUrl: svgToDataUrl(busSvg), iconSize: [32,32], iconAnchor: [16,16], popupAnchor: [0,-16] });
            const paraderoSubidaIcon = L.icon({ iconUrl: svgToDataUrl(paraderoSubidaSvg), iconSize: [32,32], iconAnchor: [16,16], popupAnchor: [0,-16] });
            const paraderoBajadaIcon = L.icon({ iconUrl: svgToDataUrl(paraderoBajadaSvg), iconSize: [32,32], iconAnchor: [16,16], popupAnchor: [0,-16] });
            const originIcon = L.icon({ iconUrl: svgToDataUrl(originSvg), iconSize: [42,42], iconAnchor: [21,42], popupAnchor: [0,-42] });
            const destIcon = L.icon({ iconUrl: svgToDataUrl(destSvg), iconSize: [42,42], iconAnchor: [21,42], popupAnchor: [0,-42] });
            
            function initMap() {
                // Centrar el mapa en Arequipa, Perú
                map = L.map('map').setView([-16.4090, -71.5375], 12);
                
                // Agregar capa de OpenStreetMap
                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                }).addTo(map);
                
                // Instrucciones mínimas
                addHelpControl();

                // (icons are defined in upper scope)

                // Click para elegir origen/destino DESHABILITADO
                // map.on('click', onMapClick);
            }
            
            // Buscar ruta usando los inputs de texto (origen/destino) o marcadores existentes
            async function buscarRuta() {
                const overlay = document.getElementById('loadingOverlay');
                if (overlay) overlay.style.display = 'flex';

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
                            alert('Proporcione origen y destino en el formulario.');
                        }
                        return;
                    }

                    // Crear o mover marcadores según corresponda
                    if (!originMarker) originMarker = createDraggableMarker(originLatLng, 'Origen'); else originMarker.setLatLng(originLatLng);
                    if (!destMarker) destMarker = createDraggableMarker(destLatLng, 'Destino'); else destMarker.setLatLng(destLatLng);

                    // Centrar vista entre ambos (mantener por ahora, bus plan ajustará también)
                    try {
                        const group = L.featureGroup([originMarker, destMarker]);
                        map.fitBounds(group.getBounds().pad(0.2));
                    } catch (e) {}

                    // Validación final por si vinieron de marcadores anteriores
                    if (!isInAqpBounds(originLatLng) || !isInAqpBounds(destLatLng)) {
                        alert('Alguno de los puntos esta fuera de los limites de Arequipa.');
                        return;
                    }
                    // Planificar con segmentos a pie por calles + línea de bus
                    planTransitRoute(true, false);
                } catch (err) {
                    console.error('Error en buscarRuta:', err);
                    alert('Ocurrio un error buscando la ruta. Reintenta.');
                } finally {
                    if (btn) btn.disabled = false;
                    const overlay = document.getElementById('loadingOverlay');
                    if (overlay) overlay.style.display = 'none';
                }
            }

            // Hacer la función accesible globalmente para que el onclick HTML la encuentre en Edge/IE
            try { window.buscarRuta = buscarRuta; } catch (e) { /* noop */ }
            
            // Función para usar ubicación actual
            async function usarUbicacionActual() {
                if (!navigator.geolocation) {
                    alert("Tu navegador no soporta geolocalización.");
                    return;
                }
                
                const overlay = document.getElementById('loadingOverlay');
                if (overlay) overlay.style.display = 'flex';

                navigator.geolocation.getCurrentPosition(
                    async (position) => {
                        const lat = position.coords.latitude;
                        const lng = position.coords.longitude;
                        const latlng = L.latLng(lat, lng);

                        if (!isInAqpBounds(latlng)) {
                            alert("Estás fuera del rango de Arequipa.");
                            if (overlay) overlay.style.display = 'none';
                            return;
                        }

                        // Set marker
                        if (originMarker) {
                            originMarker.setLatLng(latlng);
                        } else {
                            originMarker = createDraggableMarker(latlng, 'Origen');
                        }
                        
                        map.setView(latlng, 15);

                        // Reverse geocode to get address text
                        try {
                            const response = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}`);
                            const data = await response.json();
                            const address = data.display_name || `${lat.toFixed(5)}, ${lng.toFixed(5)}`;
                            const input = document.getElementById('origen');
                            if (input) input.value = "Tu ubicación actual"; 
                        } catch (e) {
                            console.error(e);
                            const input = document.getElementById('origen');
                            if (input) input.value = "Tu ubicación actual";
                        }
                        
                        if (overlay) overlay.style.display = 'none';
                    },
                    (error) => {
                        console.error(error);
                        alert("No se pudo obtener tu ubicación. Asegúrate de permitir el acceso.");
                        if (overlay) overlay.style.display = 'none';
                    }
                );
            }
            try { window.usarUbicacionActual = usarUbicacionActual; } catch (e) { /* noop */ }

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

            //DESHABILITADO por ahora
            function onMapClick(e) {
                // 1er clic: origen, 2do clic: destino; 3er clic: reinicia y pone nuevo origen
                if (!originMarker) {
                    originMarker = createDraggableMarker(e.latlng, 'Origen');
                } else if (!destMarker) {
                    destMarker = createDraggableMarker(e.latlng, 'Destino');
                    // Al definir destino, dibujar segmentos a pie (calles) + línea de bus
                    planTransitRoute(true, false);
                } else {
                    resetRoute();
                    originMarker = createDraggableMarker(e.latlng, 'Origen');
                }
            }

            function createDraggableMarker(latlng, label) {
                // Elegir icono según tipo
                let options = { draggable: true };
                if (label === 'Origen') options.icon = originIcon;
                else if (label === 'Destino') options.icon = destIcon;
                const marker = L.marker(latlng, options).addTo(map).bindPopup(label).openPopup();
                markers.push(marker);
                marker.on('dragend', function() {
                    if (originMarker && destMarker) {
                        // Recalcular incluyendo segmentos a pie por calles
                        planTransitRoute(true, false);
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
                    createMarker: function() { return null; }, // Desactivar marcadores por defecto
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
                // Ya no usamos OSRM para la vista principal; esta función queda para compatibilidad si se invoca manualmente.
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
                // Limpiar capas de transporte público
                try { clearTransitLayers(); } catch(_) {}
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
                // Control de información de ruta estilizado
                const RouteInfoControl = L.Control.extend({
                    options: { position: 'topright' },
                    onAdd: function() {
                        const div = L.DomUtil.create('div', 'leaflet-bar route-info-control');
                        div.id = 'routeInfoControl';
                        div.style.background = 'white';
                        div.style.padding = '0';
                        div.style.lineHeight = '1.5';
                        div.style.width = '350px';
                        div.style.borderRadius = '8px';
                        div.style.boxShadow = '0 2px 10px rgba(0,0,0,0.2)';
                        div.style.overflow = 'hidden';
                        div.style.transition = 'all 0.3s ease';
                        div.style.display = 'flex';
                        div.style.flexDirection = 'column';
                        
                        div.innerHTML = `
                            <div id="routeInfoHeader" style="background: linear-gradient(135deg, #DD6B20 0%, #C45A16 100%); color: white; padding: 14px 16px; cursor: pointer;">
                                <div style="display: flex; align-items: center; justify-content: space-between;">
                                    <div style="display: flex; align-items: center;">
                                        <i class="bi bi-bus-front" style="font-size: 24px; margin-right: 10px;"></i>
                                        <strong style="font-size: 16px;">Información del Viaje</strong>
                                    </div>
                                    <i id="expandIcon" class="bi bi-chevron-down" style="font-size: 18px; transition: transform 0.3s;"></i>
                                </div>
                            </div>
                            <div id="routeInfoBody" style="display: none; padding: 18px; overflow-y: auto; flex: 1;">
                                <div id="routeDetails" style="display: none;">
                                    <div style="background: #E8F5E9; border-left: 4px solid #4CAF50; padding: 12px; margin-bottom: 15px; border-radius: 4px;">
                                        <i class="bi bi-check-circle-fill" style="color: #4CAF50; margin-right: 8px; font-size: 18px;"></i>
                                        <strong style="color: #2E7D32; font-size: 15px;">¡Ruta encontrada exitosamente!</strong>
                                    </div>
                                    
                                    <!-- Contenedor de segmentos de líneas -->
                                    <div id="routeSegmentsContainer" style="margin-bottom: 15px;">
                                        <!-- Los segmentos se insertarán dinámicamente aquí -->
                                    </div>
                                    
                                    <div style="background: #F5F5F5; border-radius: 6px; padding: 12px; margin-bottom: 15px;">
                                        <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
                                            <span style="color: #666; font-size: 14px;"><i class="bi bi-clock" style="margin-right: 6px; font-size: 16px;"></i>Tiempo estimado:</span>
                                            <strong id="tiempoEstimadoTotal" style="color: #333; font-size: 14px;">-</strong>
                                        </div>
                                        <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
                                            <span style="color: #666; font-size: 14px;"><i class="bi bi-shuffle" style="margin-right: 6px; font-size: 16px;"></i>Transbordos:</span>
                                            <strong id="numTransbordos" style="color: #666; font-size: 14px;">-</strong>
                                        </div>
                                        <div style="display: flex; justify-content: space-between;">
                                            <span style="color: #666; font-size: 14px;"><i class="bi bi-cash" style="margin-right: 6px; font-size: 16px;"></i>Precio total:</span>
                                            <strong id="precioTotal" style="color: #4CAF50; font-size: 14px;">-</strong>
                                        </div>
                                    </div>
                                    
                                    <button id="btnConfirmarLlegada" onclick="confirmarLlegada()" style="width: 100%; background: #DD6B20; color: white; border: none; padding: 12px; border-radius: 6px; font-weight: bold; cursor: pointer; font-size: 15px; transition: background 0.3s;" onmouseover="this.style.background='#C45A16'" onmouseout="this.style.background='#DD6B20'">
                                        <i class="bi bi-check-circle" style="margin-right: 8px; font-size: 16px;"></i>Confirmar Llegada
                                    </button>
                                </div>
                                
                                <div id="noRouteMessage" style="text-align: center; padding: 18px; color: #666; font-size: 15px;">
                                    <i class="bi bi-info-circle" style="font-size: 32px; color: #DD6B20; display: block; margin-bottom: 10px;"></i>
                                    Ingrese origen y destino<br/>para buscar una ruta
                                </div>
                            </div>
                            <div style="padding: 12px; border-top: 1px solid #eee;">
                                <button id="btn-clear-route" class="btn btn-sm btn-outline-secondary w-100" style="font-size: 14px;">
                                    <i class="bi bi-trash" style="margin-right: 6px; font-size: 14px;"></i>Limpiar Ruta
                                </button>
                            </div>
                        `;
                        
                        L.DomEvent.disableClickPropagation(div);
                        
                        setTimeout(() => {
                            const header = document.getElementById('routeInfoHeader');
                            const body = document.getElementById('routeInfoBody');
                            const icon = document.getElementById('expandIcon');
                            const control = document.getElementById('routeInfoControl');
                            
                            // Función para ajustar la altura del panel al 95% del contenedor del mapa
                            const adjustPanelHeight = () => {
                                if (control && body.style.display !== 'none') {
                                    const mapContainer = document.getElementById('map');
                                    if (mapContainer) {
                                        const mapHeight = mapContainer.offsetHeight;
                                        const maxHeight = mapHeight * 0.95;
                                        control.style.maxHeight = maxHeight + 'px';
                                    }
                                }
                            };
                            
                            if (header && body && icon) {
                                header.addEventListener('click', () => {
                                    const isExpanded = body.style.display !== 'none';
                                    body.style.display = isExpanded ? 'none' : 'block';
                                    icon.style.transform = isExpanded ? 'rotate(0deg)' : 'rotate(180deg)';
                                    
                                    // Ajustar altura cuando se expande
                                    if (!isExpanded) {
                                        adjustPanelHeight();
                                    } else {
                                        control.style.maxHeight = 'none';
                                    }
                                });
                            }
                            
                            // Ajustar altura cuando cambia el tamaño de la ventana
                            window.addEventListener('resize', adjustPanelHeight);
                            
                            const btn = document.getElementById('btn-clear-route');
                            if (btn) btn.addEventListener('click', () => {
                                resetRoute();
                                hideRouteDetails();
                            });
                        }, 0);
                        
                        return div;
                    }
                });
                map.addControl(new RouteInfoControl());
            }

            // ========================= LÓGICA DE TRANSPORTE PÚBLICO =========================
            let transitLayers = []; // capas dibujadas (walking(caminando) + bus)

            //limpiar capas de transporte
            function clearTransitLayers(){
                transitLayers.forEach(l => { try { map.removeLayer(l); } catch(_){} });
                transitLayers = [];
            }

            // Agrega ruta caminando siguiendo calles entre dos puntos usando OSRM
            function addWalkingRouteViaStreets(startLatLng, endLatLng){
                if (!startLatLng || !endLatLng) return;
                try {
                    const router = L.Routing.osrmv1({ 
                        serviceUrl: 'https://router.project-osrm.org/route/v1', 
                        profile: 'foot' 
                    });
                    
                    // Asegurar que sean objetos L.latLng
                    const start = L.latLng(startLatLng);
                    const end = L.latLng(endLatLng);
                    
                    // Crear waypoints con la estructura correcta
                    const waypoints = [
                        L.Routing.waypoint(start),
                        L.Routing.waypoint(end)
                    ];
                    
                    router.route(waypoints, function(err, routes) {
                        if (err || !routes || routes.length === 0) {
                            // Fallback: línea recta si OSRM caminando no está disponible
                            const fallback = L.polyline([start, end], {
                                color:'#666', 
                                weight:4, 
                                dashArray:'4,6'
                            }).addTo(map);
                            transitLayers.push(fallback);
                        } else {
                            // Dibujar la ruta caminando siguiendo las calles
                            const coords = routes[0].coordinates;
                            const walkingLine = L.polyline(coords, {
                                color: '#666666',
                                weight: 4,
                                opacity: 1.0,
                                dashArray: '4,6'
                            }).addTo(map);
                            transitLayers.push(walkingLine);
                        }
                    });
                } catch (e) {
                    console.warn('Error en ruta caminando:', e);
                    // Fallback ante cualquier error
                    const fallback = L.polyline([startLatLng, endLatLng], {
                        color:'#666', 
                        weight:4, 
                        dashArray:'4,6'
                    }).addTo(map);
                    transitLayers.push(fallback);
                }
            }
            
            /**
             * ALGORITMO DE HAVERSINE
             * Calcula la distancia entre dos puntos geográficos usando la fórmula de Haversine.
             * 
             * La fórmula de Haversine determina la distancia del gran círculo entre dos puntos
             * en una esfera (la Tierra) dadas sus latitudes y longitudes. Esta es la distancia
             * más corta sobre la superficie de la esfera.
             * 
             * @param {number} lat1 - Latitud del primer punto en grados
             * @param {number} lon1 - Longitud del primer punto en grados
             * @param {number} lat2 - Latitud del segundo punto en grados
             * @param {number} lon2 - Longitud del segundo punto en grados
             * @returns {number} Distancia entre los dos puntos en metros
             * 
             * Funcionamiento:
             * 1. Convierte las coordenadas de grados a radianes (requerido por las funciones trigonométricas)
             * 2. Usa el radio de la Tierra (6,371 km) como constante
             * 3. Calcula las diferencias de latitud y longitud en radianes
             * 4. Aplica la fórmula de Haversine:
             *    - 'a' representa el cuadrado de la mitad de la longitud de la cuerda entre los puntos
             *    - 'c' es la distancia angular en radianes
             * 5. Multiplica la distancia angular por el radio terrestre para obtener metros
             * 
             * Nota: Asume que la Tierra es una esfera perfecta (simplificación con ~0.5% de error)
             */
            function haversine(lat1, lon1, lat2, lon2){
                // Función auxiliar para convertir grados a radianes
                function toRad(d){ return d * Math.PI / 180; }
                
                const R = 6371000; // Radio de la Tierra en metros
                
                // Calcular las diferencias en latitud y longitud (en radianes)
                const dLat = toRad(lat2 - lat1);
                const dLon = toRad(lon2 - lon1);
                
                // Aplicar la fórmula de Haversine
                // a = sin²(Δlat/2) + cos(lat1) × cos(lat2) × sin²(Δlon/2)
                const a = Math.sin(dLat/2)*Math.sin(dLat/2) + 
                         Math.cos(toRad(lat1))*Math.cos(toRad(lat2))*
                         Math.sin(dLon/2)*Math.sin(dLon/2);
                
                // c = 2 × atan2(√a, √(1−a)) - distancia angular en radianes
                const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
                
                // Distancia = radio × distancia angular
                return R * c; // resultado en metros
            }

            // Encuentra la paradero más cercano a un punto para una línea dada
            // Para hallar el paradero más cercano, se itera sobre todos los paraderos de la línea
            // y se calcula la distancia usando la fórmula de Haversine. Se mantiene el paradero con
            // la distancia mínima encontrada.
            function nearestParadero(linea, point){
                if (!linea || !linea.paraderos || linea.paraderos.length === 0) return null;
                let best = null; let bestDist = Infinity;
                for (const p of linea.paraderos){
                    const dist = haversine(point.lat, point.lng, p.lat, p.lng);
                    if (dist < bestDist){ bestDist = dist; best = { paradero: p, dist }; }
                }
                return best; // {paradero, dist}
            }

            // Calcula distancia sobre la secuencia de paraderos entre dos índices (inclusive)
            function busSegmentDistance(paraderos, idxA, idxB){
                if (idxA === idxB) return 0;
                let dist = 0;
                const step = idxA < idxB ? 1 : -1;
                for (let i = idxA; i !== idxB; i += step){
                    const p1 = paraderos[i];
                    const p2 = paraderos[i+step];
                    dist += haversine(p1.lat, p1.lng, p2.lat, p2.lng);
                }
                return dist;
            }

            // Construye array de paraderos que forman el trayecto en bus
            // entre dos índices (inclusive -> idxA hacia idxB)
            function buildBusSegment(paraderos, idxA, idxB){
                const segment = [];
                const step = idxA < idxB ? 1 : -1;
                for (let i = idxA; ; i += step){
                    segment.push(paraderos[i]);
                    if (i === idxB) break;
                }
                return segment;
            }

            /**
             * Planifica ruta de transporte público con soporte para múltiples transferencias.
             * Busca la mejor ruta considerando: ruta directa, una transferencia, o dos transferencias.
             * Prioriza minimizar la caminata total del usuario.
             */
            function planTransitRoute(forceButton, busOnly){
                if (!originMarker || !destMarker) return;
                if (!window.ROUTES || !Array.isArray(window.ROUTES.lineas)) return;
                const origin = originMarker.getLatLng();
                const dest = destMarker.getLatLng();
                const lineas = window.ROUTES.lineas;

                //Mejor plan encontrado
                let bestPlan = null;
                
                // 1. RUTA DIRECTA (sin transferencias)
                for (const linea of lineas){
                    const nOrigin = nearestParadero(linea, origin);
                    const nDest = nearestParadero(linea, dest);
                    if (!nOrigin || !nDest) continue;
                    
                    const paraderos = linea.paraderos;
                    const idxO = paraderos.findIndex(p => p.id === nOrigin.paradero.id);
                    const idxD = paraderos.findIndex(p => p.id === nDest.paradero.id);
                    if (idxO === -1 || idxD === -1) continue;
                    
                    const busDist = busSegmentDistance(paraderos, idxO, idxD);
                    const walkingDist = nOrigin.dist + nDest.dist;
                    const cost = walkingDist * 2 + busDist * 0.3; // Priorizar menos caminata
                    
                    if (!bestPlan || cost < bestPlan.cost){
                        bestPlan = {
                            type: 'direct',
                            segments: [{
                                linea,
                                originParadero: nOrigin.paradero,
                                destParadero: nDest.paradero,
                                busSegment: buildBusSegment(paraderos, idxO, idxD),
                                busDist
                            }],
                            walkingDist,
                            totalBusDist: busDist,
                            cost,
                            numTransfers: 0
                        };
                    }
                }
                
                // 2. RUTA CON UNA TRANSFERENCIA
                for (let i = 0; i < lineas.length; i++){
                    for (let j = 0; j < lineas.length; j++){
                        if (i === j) continue;
                        
                        const linea1 = lineas[i];
                        const linea2 = lineas[j];
                        
                        const nOrigin = nearestParadero(linea1, origin);
                        if (!nOrigin) continue;
                        
                        const nDest = nearestParadero(linea2, dest);
                        if (!nDest) continue;
                        
                        // Buscar punto de transferencia (paradero común o cercano)
                        for (const p1 of linea1.paraderos){
                            for (const p2 of linea2.paraderos){
                                const transferDist = haversine(p1.lat, p1.lng, p2.lat, p2.lng);
                                if (transferDist > 300) continue; // Máximo 300m para transferencia
                                
                                const idxO1 = linea1.paraderos.findIndex(p => p.id === nOrigin.paradero.id);
                                const idxD1 = linea1.paraderos.findIndex(p => p.id === p1.id);
                                const idxO2 = linea2.paraderos.findIndex(p => p.id === p2.id);
                                const idxD2 = linea2.paraderos.findIndex(p => p.id === nDest.paradero.id);
                                
                                if (idxO1 === -1 || idxD1 === -1 || idxO2 === -1 || idxD2 === -1) continue;
                                
                                const bus1Dist = busSegmentDistance(linea1.paraderos, idxO1, idxD1);
                                const bus2Dist = busSegmentDistance(linea2.paraderos, idxO2, idxD2);
                                const walkingDist = nOrigin.dist + transferDist + nDest.dist;
                                const totalBusDist = bus1Dist + bus2Dist;
                                const cost = walkingDist * 2 + totalBusDist * 0.3 + 500; // Penalizar transferencia
                                
                                if (!bestPlan || cost < bestPlan.cost){
                                    bestPlan = {
                                        type: 'one-transfer',
                                        segments: [
                                            {
                                                linea: linea1,
                                                originParadero: nOrigin.paradero,
                                                destParadero: p1,
                                                busSegment: buildBusSegment(linea1.paraderos, idxO1, idxD1),
                                                busDist: bus1Dist
                                            },
                                            {
                                                linea: linea2,
                                                originParadero: p2,
                                                destParadero: nDest.paradero,
                                                busSegment: buildBusSegment(linea2.paraderos, idxO2, idxD2),
                                                busDist: bus2Dist
                                            }
                                        ],
                                        walkingDist,
                                        totalBusDist,
                                        transferDist,
                                        cost,
                                        numTransfers: 1
                                    };
                                }
                            }
                        }
                    }
                }

                if (!bestPlan){
                    if (forceButton){
                        alert('No se encontró ninguna ruta disponible. Intenta con otros puntos.');
                    }
                    return;
                }

                // Validar si la caminata es razonable
                if (!forceButton && !busOnly && bestPlan.walkingDist > 2000) {
                    alert('La ruta requiere caminar más de 2km. Considera buscar puntos más cercanos a paraderos.');
                    return;
                }
                
                // Dibujar la mejor ruta encontrada -> bestPlan(Mejor Plan) / !!busOnly (solo bus)
                drawMultiSegmentPlan(bestPlan, !!busOnly);
            }

            /**
             * Dibuja en el mapa una ruta que puede tener múltiples segmentos (con transferencias)
             */
            function drawMultiSegmentPlan(plan, busOnly){
                clearTransitLayers();
                
                const origin = originMarker.getLatLng();
                const dest = destMarker.getLatLng();
                
                // Calcular tiempo total estimado (20 km/h para bus, 3 minutos por transferencia)
                const tiempoEstimadoMinutos = Math.round((plan.totalBusDist / 1000) / 20 * 60) + (plan.numTransfers * 3);
                
                // Preparar información para el panel
                const routeInfo = {
                    segments: plan.segments,
                    numTransfers: plan.numTransfers,
                    tiempoTotal: tiempoEstimadoMinutos,
                    precioTotal: (plan.segments.length * 1.0).toFixed(2), // S/. 1.00 por línea
                    walkingDist: plan.walkingDist
                };
                
                // Guardar para confirmar llegada (usar primer segmento para compatibilidad)
                window.currentRouteInfo = {
                    idLinea: plan.segments[0].linea.id,
                    nombreLinea: plan.segments[0].linea.nombre,
                    paraderoSubida: plan.segments[0].originParadero.nombre || 'Paradero',
                    paraderoBajada: plan.segments[plan.segments.length - 1].destParadero.nombre || 'Paradero',
                    tiempoEstimado: tiempoEstimadoMinutos,
                    allSegments: plan.segments // Guardar todos los segmentos
                };
                
                // Mostrar información en el panel
                mostrarRutaMultiSegmento(routeInfo);
                
                const allParaderoMarkers = [];
                
                // Dibujar cada segmento
                for (let i = 0; i < plan.segments.length; i++){
                    const segment = plan.segments[i];
                    const isFirst = i === 0;
                    const isLast = i === plan.segments.length - 1;
                    
                    // Colores diferentes para cada línea
                    const colors = ['#1E88E5', '#43A047', '#FB8C00', '#E53935', '#8E24AA'];
                    const lineColor = colors[i % colors.length];
                    
                    // Dibujar ruta del bus
                    try {
                        const coords = segment.busSegment.map(p => L.latLng(p.lat, p.lng));
                        const waypoints = coords.map(c => L.Routing.waypoint(c));
                        const router = L.Routing.osrmv1({ serviceUrl: 'https://router.project-osrm.org/route/v1' });
                        
                        router.route(waypoints, function(err, routes) {
                            if (err || !routes || routes.length === 0) {
                                // Fallback: línea directa entre paraderos
                                const fallback = L.polyline(coords, {color: lineColor, weight:6}).addTo(map);
                                transitLayers.push(fallback);
                            } else {
                                // Dibujar la ruta siguiendo las calles
                                const routeCoords = routes[0].coordinates;
                                const routeLine = L.polyline(routeCoords, {
                                    color: lineColor,
                                    weight: 6,
                                    opacity: 1.0
                                }).addTo(map);
                                transitLayers.push(routeLine);
                            }
                        });
                    } catch (e) {
                        const fallback = L.polyline(segment.busSegment.map(p => [p.lat, p.lng]), {color: lineColor, weight:6}).addTo(map);
                        transitLayers.push(fallback);
                    }
                    
                    // Marcadores de subida y bajada de este segmento
                    const pStart = segment.busSegment[0];
                    const mStart = L.marker([pStart.lat, pStart.lng], { icon: paraderoSubidaIcon }).addTo(map);
                    mStart.bindTooltip(segment.originParadero.nombre || 'Subida', {permanent:true, direction:'top', offset:[0,-16]});
                    allParaderoMarkers.push(mStart);
                    transitLayers.push(mStart);
                    
                    if (segment.busSegment.length > 1) {
                        const pEnd = segment.busSegment[segment.busSegment.length - 1];
                        const mEnd = L.marker([pEnd.lat, pEnd.lng], { icon: paraderoBajadaIcon }).addTo(map);
                        mEnd.bindTooltip(segment.destParadero.nombre || 'Bajada', {permanent:true, direction:'top', offset:[0,-16]});
                        allParaderoMarkers.push(mEnd);
                        transitLayers.push(mEnd);
                    }
                }
                
                // Rutas caminando
                if (!busOnly) {
                    // Origen -> primer paradero
                    const firstParadero = L.latLng(plan.segments[0].originParadero.lat, plan.segments[0].originParadero.lng);
                    addWalkingRouteViaStreets(origin, firstParadero);
                    
                    // Último paradero -> destino
                    const lastSegment = plan.segments[plan.segments.length - 1];
                    const lastParadero = L.latLng(lastSegment.destParadero.lat, lastSegment.destParadero.lng);
                    addWalkingRouteViaStreets(lastParadero, dest);
                    
                    // Transferencias (caminata entre líneas)
                    for (let i = 0; i < plan.segments.length - 1; i++){
                        const from = L.latLng(plan.segments[i].destParadero.lat, plan.segments[i].destParadero.lng);
                        const to = L.latLng(plan.segments[i + 1].originParadero.lat, plan.segments[i + 1].originParadero.lng);
                        if (from.distanceTo(to) > 10) { // Solo si hay distancia significativa
                            addWalkingRouteViaStreets(from, to);
                        }
                    }
                }
                
                // Ajustar vista del mapa
                try {
                    const bounds = L.latLngBounds([origin, dest]);
                    allParaderoMarkers.forEach(m => bounds.extend(m.getLatLng()));
                    map.fitBounds(bounds.pad(0.2));
                } catch(e) { /* noop */ }
            }

            function drawTransitPlan(plan, busOnly){
                clearTransitLayers();
                const infoEl = document.getElementById('route-info');
                const walkKm = (plan.walkingDist / 1000).toFixed(2);
                const busKm = (plan.busDist / 1000).toFixed(2);
                if (infoEl){
                    infoEl.innerText = 'Linea sugerida: ' + plan.linea.nombre + (busOnly ? '' : (' | Caminata: ' + walkKm + ' km')) + ' | Bus: ' + busKm + ' km | Paraderos: ' + plan.busSegment.length;
                }

                // Calcular tiempo estimado (asumiendo velocidad promedio de 20 km/h para el bus)
                const tiempoEstimadoMinutos = Math.round((plan.busDist / 1000) / 20 * 60);
                
                // Guardar información de la ruta para usarla después
                window.currentRouteInfo = {
                    idLinea: plan.linea.id,
                    nombreLinea: plan.linea.nombre,
                    paraderoSubida: plan.originParadero.nombre || 'Paradero',
                    paraderoBajada: plan.destParadero.nombre || 'Paradero',
                    tiempoEstimado: tiempoEstimadoMinutos
                };
                
                // Mostrar modal arriba a la derecha con la información
                mostrarModalRuta(window.currentRouteInfo);

                // Segmento caminando origen -> paradero origen
                const o = originMarker.getLatLng();
                const pO = L.latLng(plan.originParadero.lat, plan.originParadero.lng);
                const d = destMarker.getLatLng();
                const pD = L.latLng(plan.destParadero.lat, plan.destParadero.lng);

                if (!busOnly){
                    // Rutas caminando por calles usando OSRM perfil 'foot'
                    addWalkingRouteViaStreets(o, pO);
                    addWalkingRouteViaStreets(pD, d);
                }

                // Trayecto en bus (intentar seguir calles via OSRM; fallback a polilínea entre paraderos)
                try {
                    const coords = plan.busSegment.map(p => L.latLng(p.lat, p.lng));
                    const waypoints = coords.map(c => L.Routing.waypoint(c));
                    const router = L.Routing.osrmv1({ serviceUrl: 'https://router.project-osrm.org/route/v1' });
                    
                    router.route(waypoints, function(err, routes) {
                        if (err || !routes || routes.length === 0) {
                            // Fallback: línea directa entre paraderos
                            const fallback = L.polyline(coords, {color:'#1E88E5', weight:6}).addTo(map);
                            transitLayers.push(fallback);
                        } else {
                            // Dibujar la ruta siguiendo las calles
                            const routeCoords = routes[0].coordinates;
                            const routeLine = L.polyline(routeCoords, {
                                color: '#1E88E5',
                                weight: 6,
                                opacity: 1.0
                            }).addTo(map);
                            transitLayers.push(routeLine);
                        }
                    });
                } catch (e) {
                    // fallback inmediato
                    const fallback = L.polyline(plan.busSegment.map(p => [p.lat, p.lng]), {color:'#1E88E5', weight:6}).addTo(map);
                    transitLayers.push(fallback);
                }

                // Marcadores de paraderos: usar iconos específicos para subida (verde) y bajada (rojo)
                const paraderoMarkers = [];
                try {
                    if (plan.busSegment.length > 0) {
                        // Primer paradero (Subida) - Icono verde con flecha hacia arriba
                        const pStart = plan.busSegment[0];
                        const mStart = L.marker([pStart.lat, pStart.lng], { icon: paraderoSubidaIcon }).addTo(map);
                        mStart.bindTooltip(plan.originParadero.nombre || 'Paradero Subida', {permanent:true, direction:'top', offset:[0,-16]});
                        paraderoMarkers.push(mStart);
                        transitLayers.push(mStart);

                        // Último paradero (Bajada) - Icono rojo con flecha hacia abajo - solo si es diferente al primero
                        if (plan.busSegment.length > 1) {
                            const pEnd = plan.busSegment[plan.busSegment.length - 1];
                            const mEnd = L.marker([pEnd.lat, pEnd.lng], { icon: paraderoBajadaIcon }).addTo(map);
                            mEnd.bindTooltip(plan.destParadero.nombre || 'Paradero Bajada', {permanent:true, direction:'top', offset:[0,-16]});
                            paraderoMarkers.push(mEnd);
                            transitLayers.push(mEnd);
                        }
                    }
                } catch(e){ console.warn('paradero markers error', e); }

                // Asegurar que todo se vea
                try {
                    // Ajustar usando puntos clave y la línea de bus; los tramos a pie se trazan como controles aparte
                    const tempMarkers = [o, pO, pD, d].map(ll => L.marker(ll));
                    // Usar marcadores de paradero creados para bounds si existen
                    const groupItems = [originMarker, destMarker];
                    if (typeof paraderoMarkers !== 'undefined' && paraderoMarkers.length > 0) groupItems.push(...paraderoMarkers);
                    groupItems.push(...tempMarkers);
                    const g = L.featureGroup(groupItems);
                    map.fitBounds(g.getBounds().pad(0.2));
                    tempMarkers.forEach(m => { try { map.removeLayer(m); } catch(_){ } });
                } catch(e){ /* noop */ }
            }

            // Exponer para depuración (walking + bus)
            try { window.planTransitRoute = () => planTransitRoute(true, false); } catch(_){ }
            
            // ========================= MODAL DE INFORMACIÓN DE RUTA =========================
            
            /**
             * Muestra información de ruta con múltiples segmentos en el panel Leaflet
             */
            function mostrarRutaMultiSegmento(routeInfo) {
                try {
                    const container = document.getElementById('routeSegmentsContainer');
                    if (!container) return;
                    
                    // Limpiar contenedor
                    container.innerHTML = '';
                    
                    // Crear HTML para cada segmento
                    routeInfo.segments.forEach((segment, index) => {
                        const isFirst = index === 0;
                        const isLast = index === routeInfo.segments.length - 1;
                        
                        const segmentHTML = `
                            <div style="margin-bottom: 12px; padding: 12px; background: white; border: 1px solid #e0e0e0; border-radius: 6px;">
                                <div style="display: flex; align-items: center; margin-bottom: 10px;">
                                    <span style="background: #DD6B20; color: white; padding: 4px 10px; border-radius: 12px; font-size: 13px; font-weight: bold; margin-right: 10px;">
                                        ${index + 1}
                                    </span>
                                    <strong style="color: #DD6B20; font-size: 14px;">
                                        <i class="bi bi-bus-front" style="margin-right: 6px; font-size: 16px;"></i>${segment.linea.nombre}
                                    </strong>
                                </div>
                                <div style="display: flex; align-items: start; margin-bottom: 8px;">
                                    <div style="width: 28px; height: 28px; display: flex; align-items: center; justify-content: center; flex-shrink: 0;">
                                        <svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' width='28' height='28'><circle cx='12' cy='12' r='10' fill='#4CAF50'/><path d='M12 8l-4 4h2.5v4h3v-4H16z' fill='#ffffff'/></svg>
                                    </div>
                                    <div style="margin-left: 10px; flex: 1;">
                                        <small style="color: #999; font-size: 12px;">Subir en:</small><br/>
                                        <span style="color: #333; font-size: 13px;">${segment.originParadero.nombre || 'Paradero'}</span>
                                    </div>
                                </div>
                                <div style="display: flex; align-items: start;">
                                    <div style="width: 28px; height: 28px; display: flex; align-items: center; justify-content: center; flex-shrink: 0;">
                                        <svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' width='28' height='28'><circle cx='12' cy='12' r='10' fill='#F44336'/><path d='M12 16l4-4h-2.5V8h-3v4H8z' fill='#ffffff'/></svg>
                                    </div>
                                    <div style="margin-left: 10px; flex: 1;">
                                        <small style="color: #999; font-size: 12px;">Bajar en:</small><br/>
                                        <span style="color: #333; font-size: 13px;">${segment.destParadero.nombre || 'Paradero'}</span>
                                    </div>
                                </div>
                            </div>
                            ${!isLast ? '<div style="text-align: center; margin: 10px 0;"><i class="bi bi-arrow-down" style="color: #999; font-size: 20px;"></i><br/><small style="color: #666; font-size: 12px;">Transferencia</small></div>' : ''}
                        `;
                        
                        container.innerHTML += segmentHTML;
                    });
                    
                    // Actualizar información general
                    document.getElementById('tiempoEstimadoTotal').textContent = routeInfo.tiempoTotal + ' minutos';
                    document.getElementById('numTransbordos').textContent = routeInfo.numTransfers;
                    document.getElementById('precioTotal').textContent = 'S/. ' + routeInfo.precioTotal;
                    
                    // Mostrar el panel
                    const body = document.getElementById('routeInfoBody');
                    const details = document.getElementById('routeDetails');
                    const noMessage = document.getElementById('noRouteMessage');
                    const icon = document.getElementById('expandIcon');
                    const control = document.getElementById('routeInfoControl');
                    
                    if (body && details && noMessage) {
                        noMessage.style.display = 'none';
                        details.style.display = 'block';
                        body.style.display = 'block';
                        if (icon) icon.style.transform = 'rotate(180deg)';
                        
                        // Ajustar altura del panel al 90% del mapa
                        if (control) {
                            const mapContainer = document.getElementById('map');
                            if (mapContainer) {
                                const mapHeight = mapContainer.offsetHeight;
                                const maxHeight = mapHeight * 0.9;
                                control.style.maxHeight = maxHeight + 'px';
                            }
                        }
                    }
                } catch (e) {
                    console.error('Error mostrando ruta multi-segmento:', e);
                }
            }
            
            // ========================= MODAL DE INFORMACIÓN DE RUTA =========================
            
            function mostrarModalRuta(routeInfo) {
                try {
                    // Actualizar contenido del control Leaflet
                    document.getElementById('paraderoSubida').textContent = routeInfo.paraderoSubida;
                    document.getElementById('paraderoBajada').textContent = routeInfo.paraderoBajada;
                    document.getElementById('nombreLinea').textContent = routeInfo.nombreLinea;
                    document.getElementById('tiempoEstimado').textContent = routeInfo.tiempoEstimado + ' minutos';
                    document.getElementById('precioViaje').textContent = 'S/. 1.00';
                    
                    // Mostrar el panel de detalles y expandir
                    const body = document.getElementById('routeInfoBody');
                    const details = document.getElementById('routeDetails');
                    const noMessage = document.getElementById('noRouteMessage');
                    const icon = document.getElementById('expandIcon');
                    
                    if (body && details && noMessage) {
                        noMessage.style.display = 'none';
                        details.style.display = 'block';
                        body.style.display = 'block';
                        if (icon) icon.style.transform = 'rotate(180deg)';
                    }
                } catch (e) {
                    console.error('Error mostrando información de ruta:', e);
                }
            }
            
            function hideRouteDetails() {
                try {
                    const details = document.getElementById('routeDetails');
                    const noMessage = document.getElementById('noRouteMessage');
                    
                    if (details && noMessage) {
                        details.style.display = 'none';
                        noMessage.style.display = 'block';
                    }
                } catch (e) {
                    console.error('Error ocultando detalles:', e);
                }
            }
            
            function confirmarLlegada() {
                if (!window.currentRouteInfo) {
                    alert('No hay información de ruta disponible');
                    return;
                }
                
                // Guardar el viaje usando JSF
                guardarViajeEnHistorial(
                    window.currentRouteInfo.idLinea,
                    window.currentRouteInfo.paraderoSubida,
                    window.currentRouteInfo.paraderoBajada,
                    window.currentRouteInfo.nombreLinea,
                    window.currentRouteInfo.tiempoEstimado
                );
                
                // Colapsar el panel
                const body = document.getElementById('routeInfoBody');
                const icon = document.getElementById('expandIcon');
                if (body && icon) {
                    body.style.display = 'none';
                    icon.style.transform = 'rotate(0deg)';
                }
                
                // Mostrar mensaje de confirmación
                alert('¡Viaje guardado en tu historial!');
            }
            
            function guardarViajeEnHistorial(idLinea, paraderoSubida, paraderoBajada, nombreLinea, tiempoEstimado) {
                try {
                    // Actualizar los campos ocultos del formulario JSF
                    document.getElementById('saveTripForm:idLinea').value = idLinea;
                    document.getElementById('saveTripForm:paraderoSubida').value = paraderoSubida;
                    document.getElementById('saveTripForm:paraderoBajada').value = paraderoBajada;
                    document.getElementById('saveTripForm:nombreLinea').value = nombreLinea;
                    document.getElementById('saveTripForm:tiempoEstimado').value = tiempoEstimado;
                    
                    // Hacer clic en el botón oculto para ejecutar el action del bean
                    document.getElementById('saveTripForm:btnSaveTrip').click();
                } catch (e) {
                    console.error('Error en guardarViajeEnHistorial:', e);
                }
            }
            
            // Hacer funciones accesibles globalmente
            try { 
                window.confirmarLlegada = confirmarLlegada;
                window.mostrarModalRuta = mostrarModalRuta;
            } catch(_) { }
            
            // ========================= FIN MODAL =========================
            
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