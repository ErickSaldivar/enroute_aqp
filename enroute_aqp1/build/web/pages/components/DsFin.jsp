<%-- 
    Document   : DsFin
    Created on : 12 oct. 2025, 12:13:02
    Author     : User
--%>

  </div>
<!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Leaflet JS -->
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
    
    <script>
        // Inicializar el mapa
        let map;
        let markers = [];
        
        function initMap() {
            // Centrar el mapa en Arequipa, Perú
            map = L.map('map').setView([-16.4090, -71.5375], 12);
            
            // Agregar capa de OpenStreetMap
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            }).addTo(map);
            
            // Agregar marcador en el centro de Arequipa
            L.marker([-16.4090, -71.5375]).addTo(map)
                .bindPopup('Arequipa - Ciudad Blanca')
                .openPopup();
        }
        
        function buscarRuta() {
            const origen = document.getElementById('origen').value;
            const destino = document.getElementById('destino').value;
            
            if (origen && destino) {
                alert('Buscando ruta de: ' + origen + ' a: ' + destino);
                // Aquí se implementaría la lógica de búsqueda de rutas
            } else {
                alert('Por favor, completa ambos campos');
            }
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
            const map = document.getElementById('map');
            if (window.innerWidth < 992) { // Móviles
                const viewportHeight = window.innerHeight;
                const searchBarHeight = document.querySelector('.bg-white.shadow-sm').offsetHeight;
                const mobileHeaderHeight = document.querySelector('.d-lg-none.p-3') ? 
                    document.querySelector('.d-lg-none.p-3').offsetHeight : 0;
                const remainingHeight = viewportHeight - searchBarHeight - mobileHeaderHeight;
                map.style.height = Math.max(remainingHeight, 300) + 'px';
            }
        }

        // Ajustar altura al cargar y redimensionar
        window.addEventListener('load', adjustMapHeight);
        window.addEventListener('resize', adjustMapHeight);
    </script>
</body>
</html>
