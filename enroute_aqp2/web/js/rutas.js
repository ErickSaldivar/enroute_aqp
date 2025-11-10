// rutas.js - Renderiza líneas y paraderos en el mapa de rutas
let rutasMap;
let rutasLayers = {};

function initRutasMap() {
  if (!document.getElementById('routesLeafletMap')) return;
  rutasMap = L.map('routesLeafletMap').setView([-16.4090, -71.5375], 12);
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
  }).addTo(rutasMap);
}

function mostrarRuta(idLinea) {
  if (!window.ROUTES || !rutasMap) return;
  // Limpiar capa previa
  Object.values(rutasLayers).forEach(layer => {
    try { rutasMap.removeLayer(layer); } catch(e) {}
  });
  rutasLayers = {};
  const linea = window.ROUTES.lineas.find(l => l.id === idLinea);
  if (!linea) return;
  const coords = linea.paraderos.sort((a,b)=> a.orden - b.orden).map(p => [p.lat, p.lng]);
  const poly = L.polyline(coords, { color: '#DD6B20', weight: 5 }).addTo(rutasMap);
  rutasLayers['poly'] = poly;
  // markers
  coords.forEach((c, idx) => {
    const p = linea.paraderos[idx];
    const m = L.marker(c).addTo(rutasMap).bindPopup(`<strong>${p.nombre}</strong><br/>Orden: ${p.orden}`);
    rutasLayers['m'+idx] = m;
  });
  try { rutasMap.fitBounds(poly.getBounds().pad(0.2)); } catch(e) {}
}

document.addEventListener('DOMContentLoaded', function(){
  initRutasMap();
});
