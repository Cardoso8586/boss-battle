
// Lista de textos épicos
const loadingMessages = [
  "Indo pra arena...",
  "Preparando os guerreiros...",
  "Aquecendo as espadas...",
  "O combate vai começar...",
  "Entrando na batalha...",
  "Forjando heróis...",
  "Se preparando para a glória...",
  "Carregando ação épica..."
];

// Mostra imediatamente um texto aleatório
function setRandomLoadingText() {
    const textEl = document.getElementById('loading-text');
    const message = loadingMessages[Math.floor(Math.random() * loadingMessages.length)];
    textEl.textContent = message;
}

// Mostrar o texto **imediatamente** ao iniciar
setRandomLoadingText();

// Espera todas as imagens carregarem
window.addEventListener('load', () => {
    const loading = document.getElementById('loading');
    if (loading) {
      
        setTimeout(() => {
            loading.style.opacity = '0';
            setTimeout(() => loading.remove(), 3000);
        }, 7000);
    }
});
