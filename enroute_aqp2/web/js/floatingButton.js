document.addEventListener('DOMContentLoaded', function() {
    const button = document.getElementById('tryAppBtn');
    
    if (!button) return;

    // Crear un placeholder para evitar cambios en el diseño cuando el botón se vuelve fijo
    const placeholder = document.createElement('div');
    placeholder.style.display = 'none';
    // Copiar dimensiones y márgenes para que coincida exactamente con el botón
    const computedStyle = window.getComputedStyle(button);
    placeholder.style.width = button.offsetWidth + 'px';
    placeholder.style.height = button.offsetHeight + 'px';
    placeholder.style.margin = computedStyle.margin;
    
    button.parentNode.insertBefore(placeholder, button);

    const handleScroll = () => {
        // Cuando el usuario se desplaza más allá de la sección hero o un umbral
        if (window.scrollY > 300) {
            if (!button.classList.contains('floating-btn')) {
                button.classList.add('floating-btn');
                placeholder.style.display = 'inline-block';
            }
        } else {
            if (button.classList.contains('floating-btn')) {
                button.classList.remove('floating-btn');
                placeholder.style.display = 'none';
            }
        }
    };

    window.addEventListener('scroll', handleScroll);
});
