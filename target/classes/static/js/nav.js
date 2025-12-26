const mobileMenu = document.getElementById('mobile-menu');
const navList = document.querySelector('.nav-list');
const links = document.querySelectorAll('.nav-list a[data-section]');
const sections = document.querySelectorAll('main section');

// Toggle menu mobile
mobileMenu.addEventListener('click', (e) => {
    e.stopPropagation(); // impede fechar imediatamente
    navList.classList.toggle('show');
});

// Fecha menu ao clicar fora (somente mobile)
document.addEventListener('click', () => {
    if (window.innerWidth <= 768 && navList.classList.contains('show')) {
        navList.classList.remove('show');
    }
});

// Evita fechar menu ao clicar dentro do menu
navList.addEventListener('click', (e) => e.stopPropagation());

// Navegação das sections
links.forEach(link => {
    link.addEventListener('click', (e) => {
        e.preventDefault();
        const target = link.dataset.section;

        // Esconde todas as sections e mostra a selecionada
        sections.forEach(sec => sec.classList.remove('active'));
        document.getElementById(target).classList.add('active');

        // Geolocalização se for Perfil
        if (target === 'perfil') {
            const locElem = document.getElementById('user-location');
            if (locElem && navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(
                    pos => locElem.textContent = `Lat: ${pos.coords.latitude.toFixed(4)}, Lon: ${pos.coords.longitude.toFixed(4)}`,
                    () => locElem.textContent = "Não foi possível obter sua localização."
                );
            } else if (locElem) {
                locElem.textContent = "Geolocalização não suportada pelo navegador.";
            }
        }

        // Fecha menu mobile ao clicar no link (somente se estiver aberto e mobile)
        if (window.innerWidth <= 768 && navList.classList.contains('show')) {
            navList.classList.remove('show');
        }
    });
});

// Opcional: ajusta menu se redimensionar a tela
window.addEventListener('resize', () => {
    if (window.innerWidth > 768) {
        navList.classList.remove('show'); // garante que não fica escondido no desktop
    }
});
