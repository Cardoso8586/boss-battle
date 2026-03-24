const loadingMessages = [
  "Indo pra arena...",
  "Preparando os guerreiros...",
  "Aquecendo as espadas...",
  "O combate vai começar...",
  "Entrando na batalha...",
  "Forjando heróis...",
  "Se preparando para a glória...",
  "Carregando ação épica...",
  "Afiando lâminas...",
  "Reunindo tropas...",
  "Chamando reforços...",
  "Os tambores de guerra ecoam...",
  "Preparando o ataque final...",
  "Sentindo a fúria do campo de batalha...",
  "Erguendo escudos...",
  "Invocando coragem...",
  "A batalha se aproxima...",
  "Os inimigos estão se posicionando...",
  "Definindo estratégias...",
  "Testando o poder dos guerreiros...",
  "Preparando habilidades especiais...",
  "Concentrando energia...",
  "A tensão está no ar...",
  "Tudo pronto para o confronto...",
  "Os portões da arena estão se abrindo...",
  "A multidão aguarda o combate...",
  "O destino será decidido...",
  "Forças sendo reunidas...",
  "A guerra está prestes a começar...",
  "Carregando poder máximo...",
  "Preparando o golpe decisivo...",
  "Heróis em posição...",
  "O desafio está lançado...",
  "Entrando em modo de combate...",
  "O campo de batalha chama...",
  "A adrenalina está subindo...",
  "Preparando a investida...",
  "A vitória está em jogo...",
  "Sincronizando ataques...",
  "Despertando habilidades ocultas...",
  "O caos está prestes a começar...",
  "Os guerreiros estão prontos...",
  "Respirando antes da batalha...",
  "A glória espera..."
];
function setRandomLoadingText() {
    const textEl = document.getElementById('loading-text');
    if (textEl) {
        const message = loadingMessages[Math.floor(Math.random() * loadingMessages.length)];
        textEl.textContent = message;
    }
}

setRandomLoadingText();

document.addEventListener('DOMContentLoaded', () => {
    const loading = document.getElementById('loading');
    if (loading) {
        setTimeout(() => {
            loading.style.opacity = '0';
            setTimeout(() => loading.remove(), 300);
        }, 1000); //10 segundos só
    }
});
/*
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
            setTimeout(() => loading.remove(), 1000);
        }, 5000);
    }
});

*/
