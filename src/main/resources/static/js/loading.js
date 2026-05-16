const loadingMessagesLogin = [
    "Carregando login...",
    "Preparando acesso...",
    "Abrindo tela de entrada...",
    "Conectando ao reino...",
    "Preparando autenticação...",
    "Verificando credenciais...",
    "Acessando portal principal...",
    "Os guardiões estão verificando sua entrada...",
    "Carregando sistema de acesso...",
    "Preparando conexão com a Arena..."
];

function estaNaPaginaLogin() {
    return window.location.pathname
        .toLowerCase()
        .includes("login");
}

function mostrarMensagemLogin() {

    const textEl = document.getElementById("loading-text");

    if (!textEl) return;

    // somente login
    if (!estaNaPaginaLogin()) {
        textEl.textContent = "";
        return;
    }

    // já exibiu uma vez nesta sessão
    const loginJaExibido =
        sessionStorage.getItem("LOGIN_LOADING_EXIBIDO");

    if (loginJaExibido === "true") {
        textEl.textContent = "";
        return;
    }

    // escolhe SOMENTE UMA mensagem aleatória
    const indice =
        Math.floor(
            Math.random() * loadingMessagesLogin.length
        );

    const mensagem = loadingMessagesLogin[indice];

    textEl.textContent = mensagem;

    // salva que já mostrou
    sessionStorage.setItem(
        "LOGIN_LOADING_EXIBIDO",
        "true"
    );
}

document.addEventListener("DOMContentLoaded", () => {

    mostrarMensagemLogin();

    const loading = document.getElementById("loading");

    if (loading) {

        setTimeout(() => {

            loading.style.opacity = "0";

            setTimeout(() => {
                loading.remove();
            }, 300);

        }, 1000);
    }
});
/*
// Lista de textos épicos
const loadingMessagesLogin = [
    "Carregando login...",
    "Preparando acesso...",
    "Abrindo tela de entrada...",
    "Conectando ao reino...",
    "Preparando autenticação...",
    "Verificando credenciais...",
    "Acessando portal principal...",
    "Os guardiões estão verificando sua entrada...",
    "Carregando sistema de acesso...",
    "Preparando conexão com a Arena...",
    "Abrindo portal dos guerreiros...",
    "Sincronizando dados do jogador...",
    "Seu acesso está sendo preparado...",
    "O reino aguarda seu retorno...",
    "Os portões estão se abrindo...",
    "Preparando ambiente de batalha...",
    "Carregando informações da conta...",
    "Verificando permissões de acesso...",
    "Inicializando sistema do jogador...",
    "Seu herói está sendo localizado...",
    "A Arena está pronta para recebê-lo...",
    "Entrando no mundo da batalha...",
    "O portal de entrada foi ativado...",
    "Conectando aos servidores do reino...",
    "Preparando sessão do guerreiro...",
    "As muralhas do reino estão se abrindo...",
    "Seu caminho para a batalha está sendo liberado...",
    "A conexão com o reino foi iniciada...",
    "Tudo pronto para seu retorno...",
    "Carregando experiência épica..."
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
