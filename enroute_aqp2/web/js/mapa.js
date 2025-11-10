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
            const busSvg = `<?xml version="1.0" encoding="UTF-8"?><svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' width='20' height='20'><rect x='2' y='6' width='20' height='10' rx='2' ry='2' fill='%231E88E5'/><circle cx='7' cy='17' r='1.5' fill='%23ffffff'/><circle cx='17' cy='17' r='1.5' fill='%23ffffff'/><rect x='4' y='8' width='4' height='4' fill='%23ffffff'/><rect x='16' y='8' width='2' height='4' fill='%23ffffff'/></svg>`;
            const originSvg = `<?xml version="1.0" encoding="UTF-8"?><svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' width='28' height='28'><path d='M12 2C8 2 5 5 5 9c0 5 7 13 7 13s7-8 7-13c0-4-3-7-7-7z' fill='%2334A853'/><circle cx='12' cy='9' r='2.2' fill='%23ffffff'/></svg>`;
            const destSvg = `<?xml version="1.0" encoding="UTF-8"?><svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' width='28' height='28'><path d='M12 2C8 2 5 5 5 9c0 5 7 13 7 13s7-8 7-13c0-4-3-7-7-7z' fill='%23EA4335'/><circle cx='12' cy='9' r='2.2' fill='%23ffffff'/></svg>`;
            function svgToDataUrl(svg){ return 'data:image/svg+xml;charset=UTF-8,' + encodeURIComponent(svg); }

            const busIconSmall = L.icon({ iconUrl: svgToDataUrl(busSvg), iconSize: [20,20], iconAnchor: [10,10], popupAnchor: [0,-10] });
            const originIcon = L.icon({ iconUrl: svgToDataUrl(originSvg), iconSize: [28,28], iconAnchor: [14,28], popupAnchor: [0,-28] });
            const destIcon = L.icon({ iconUrl: svgToDataUrl(destSvg), iconSize: [28,28], iconAnchor: [14,28], popupAnchor: [0,-28] });
            
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
                            <button id="btn-transit-route" class="btn btn-sm btn-outline-primary w-100">Transporte público</button>
                        `;
                        // Evitar que el mapa capture clics sobre el control
                        L.DomEvent.disableClickPropagation(div);
                        setTimeout(() => {
                            const btn = document.getElementById('btn-clear-route');
                            if (btn) btn.addEventListener('click', resetRoute);
                            const transitBtn = document.getElementById('btn-transit-route');
                            if (transitBtn) transitBtn.addEventListener('click', () => { try { planTransitRoute(true, false); } catch(e){ console.error(e); } });
                        }, 0);
                        return div;
                    }
                });
                map.addControl(new HelpControl());
            }

            // ========================= LÓGICA DE TRANSPORTE PÚBLICO =========================
            let transitLayers = []; // capas dibujadas (walking fallback + bus)
            let transitRoutingControls = []; // controles de rutas a pie (OSRM)

            function clearTransitLayers(){
                transitLayers.forEach(l => { try { map.removeLayer(l); } catch(_){} });
                transitLayers = [];
                transitRoutingControls.forEach(c => { try { map.removeControl(c); } catch(_){} });
                transitRoutingControls = [];
            }

            function addWalkingRouteViaStreets(startLatLng, endLatLng){
                if (!startLatLng || !endLatLng) return;
                try {
                    const ctrl = L.Routing.control({
                        waypoints: [startLatLng, endLatLng],
                        router: L.Routing.osrmv1({ serviceUrl: 'https://router.project-osrm.org/route/v1', profile: 'foot' }),
                        show: false,
                        addWaypoints: false,
                        draggableWaypoints: false,
                        routeWhileDragging: false,
                        fitSelectedRoutes: false,
                        lineOptions: {
                            styles: [
                                { color: '#000000', opacity: 0.10, weight: 10 },
                                { color: '#666666', opacity: 1.0, weight: 4, dashArray: '4,6' }
                            ]
                        }
                    });
                    ctrl.on('routingerror', () => {
                        // Fallback: línea recta si OSRM caminando no está disponible
                        const fallback = L.polyline([startLatLng, endLatLng], {color:'#666', weight:4, dashArray:'4,6'}).addTo(map);
                        transitLayers.push(fallback);
                    });
                    ctrl.addTo(map);
                    transitRoutingControls.push(ctrl);
                } catch (e) {
                    // Fallback ante cualquier error
                    const fallback = L.polyline([startLatLng, endLatLng], {color:'#666', weight:4, dashArray:'4,6'}).addTo(map);
                    transitLayers.push(fallback);
                }
            }

            function haversine(lat1, lon1, lat2, lon2){
                function toRad(d){ return d * Math.PI / 180; }
                const R = 6371000; // m
                const dLat = toRad(lat2 - lat1);
                const dLon = toRad(lon2 - lon1);
                const a = Math.sin(dLat/2)*Math.sin(dLat/2) + Math.cos(toRad(lat1))*Math.cos(toRad(lat2))*Math.sin(dLon/2)*Math.sin(dLon/2);
                const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
                return R * c; // metros
            }

            // Encuentra la paradero más cercano a un punto para una línea dada
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
            function buildBusSegment(paraderos, idxA, idxB){
                const segment = [];
                const step = idxA < idxB ? 1 : -1;
                for (let i = idxA; ; i += step){
                    segment.push(paraderos[i]);
                    if (i === idxB) break;
                }
                return segment;
            }

            // Planificar ruta de transporte público.
            // forceButton: mostrar feedback aunque la caminata sea grande.
            // busOnly: cuando true dibuja solo el segmento de la línea (sin tramos caminando ni paraderos extra).
            function planTransitRoute(forceButton, busOnly){
                if (!originMarker || !destMarker) return;
                if (!window.ROUTES || !Array.isArray(window.ROUTES.lineas)) return;
                const origin = originMarker.getLatLng();
                const dest = destMarker.getLatLng();
                const lineas = window.ROUTES.lineas;

                let bestPlan = null;
                for (const linea of lineas){
                    const nOrigin = nearestParadero(linea, origin);
                    const nDest = nearestParadero(linea, dest);
                    if (!nOrigin || !nDest) continue;
                    // Indices dentro del array de paraderos (según orden ya viene preordenado del backend)
                    const paraderos = linea.paraderos;
                    const idxO = paraderos.findIndex(p => p.id === nOrigin.paradero.id);
                    const idxD = paraderos.findIndex(p => p.id === nDest.paradero.id);
                    if (idxO === -1 || idxD === -1) continue;
                    const busDist = busSegmentDistance(paraderos, idxO, idxD); // metros
                    const walkingDist = nOrigin.dist + nDest.dist; // metros
                    // Costo simple: priorizar menor caminata, penalizar bus menos
                    const cost = walkingDist + busDist * 0.3;
                    if (!bestPlan || cost < bestPlan.cost){
                        bestPlan = {
                            linea,
                            originParadero: nOrigin.paradero,
                            destParadero: nDest.paradero,
                            walkingDist,
                            busDist,
                            cost,
                            busSegment: buildBusSegment(paraderos, idxO, idxD)
                        };
                    }
                }

                if (!bestPlan){
                    if (forceButton){
                        const infoEl = document.getElementById('route-info');
                        if (infoEl) infoEl.innerText = 'Sin coincidencia de lineas cercanas.';
                    }
                    return;
                }

                // Si no es llamada forzada y la caminata supera 2km podemos ignorar (excepto busOnly que siempre se muestra si hay coincidencia)
                if (!forceButton && !busOnly && bestPlan.walkingDist > 2000) return;
                drawTransitPlan(bestPlan, !!busOnly);
            }

            function drawTransitPlan(plan, busOnly){
                clearTransitLayers();
                const infoEl = document.getElementById('route-info');
                const walkKm = (plan.walkingDist / 1000).toFixed(2);
                const busKm = (plan.busDist / 1000).toFixed(2);
                if (infoEl){
                    infoEl.innerText = 'Linea sugerida: ' + plan.linea.nombre + (busOnly ? '' : (' | Caminata: ' + walkKm + ' km')) + ' | Bus: ' + busKm + ' km | Paraderos: ' + plan.busSegment.length;
                }

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
                let busControl = null;
                try {
                    const waypoints = plan.busSegment.map(p => L.latLng(p.lat, p.lng));
                    busControl = L.Routing.control({
                        waypoints: waypoints,
                        router: L.Routing.osrmv1({ serviceUrl: 'https://router.project-osrm.org/route/v1' }),
                        show: false,
                        addWaypoints: false,
                        draggableWaypoints: false,
                        routeWhileDragging: false,
                        fitSelectedRoutes: false,
                        lineOptions: {
                            styles: [
                                { color: '#000000', opacity: 0.12, weight: 10 },
                                { color: '#1E88E5', opacity: 1.0, weight: 6 }
                            ]
                        }
                    });
                    busControl.on('routingerror', function() {
                        // fallback: trazo directo entre paraderos
                        const fallback = L.polyline(waypoints.map(w => [w.lat, w.lng]), {color:'#1E88E5', weight:6}).addTo(map);
                        transitLayers.push(fallback);
                    });
                    busControl.addTo(map);
                    transitRoutingControls.push(busControl);
                } catch (e) {
                    // fallback inmediato
                    const fallback = L.polyline(plan.busSegment.map(p => [p.lat, p.lng]), {color:'#1E88E5', weight:6}).addTo(map);
                    transitLayers.push(fallback);
                }

                // Marcadores de paraderos: marcador pequeño (icono bus) para cada paradero
                const paraderoMarkers = [];
                try {
                    for (let i = 0; i < plan.busSegment.length; i++){
                        const p = plan.busSegment[i];
                        const m = L.marker([p.lat, p.lng], { icon: busIconSmall }).addTo(map);
                        paraderoMarkers.push(m);
                        transitLayers.push(m);
                    }
                    // Etiquetas permanentes sobre el paradero de inicio y fin
                    if (paraderoMarkers.length > 0){
                        const first = paraderoMarkers[0];
                        const last = paraderoMarkers[paraderoMarkers.length - 1];
                        first.bindTooltip(plan.originParadero.nombre || 'Paradero', {permanent:true, direction:'top', offset:[0,-8]});
                        last.bindTooltip(plan.destParadero.nombre || 'Paradero', {permanent:true, direction:'top', offset:[0,-8]});
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