const mensagensLoader = {

    desafios: [
        "Carregando desafios disponíveis...",
        "Preparando novas missões...",
        "Analisando objetivos da Arena...",
        "Calculando recompensas dos desafios...",
        "Verificando progresso das missões...",
        "Atualizando desafios diários...",
        "Sincronizando sistema de missões...",
        "Preparando metas do guerreiro...",
        "Buscando novos objetivos...",
        "Processando recompensas disponíveis...",
        "Carregando contratos de desafio...",
        "Ativando protocolos de missão...",
        "Monitorando progresso das tarefas...",
        "Preparando jornada de conquistas..."
    ],
	
	"recarregar-vigor": [
	    "Recarregando vigor dos guerreiros...",
	    "Restaurando energia de combate...",
	    "Preparando força para a Arena...",
	    "Sincronizando fôlego dos guerreiros...",
	    "Ativando núcleo de vigor...",
	    "Recuperando resistência de batalha...",
	    "Carregando poder físico...",
	    "Estabilizando energia vital...",
	    "Revigorando tropas da Arena...",
	    "Preparando novo avanço de combate..."
	],

    default: [
        "Carregando sistema...",
        "Preparando ambiente...",
        "Sincronizando dados...",
        "Conectando ao servidor...",
        "Finalizando carregamento..."
    ]
};
const TEMPO_MINIMO_LOADING = 4200;

function iniciarLoadingPagina() {

    const loading = document.getElementById("loading");
    if (!loading) return;

    const inicio = Date.now();

    function fecharLoader() {

        const tempoDecorrido = Date.now() - inicio;
        const restante = Math.max(0, TEMPO_MINIMO_LOADING - tempoDecorrido);

        setTimeout(() => {

            loading.style.opacity = "0";
            loading.style.pointerEvents = "none";

            setTimeout(() => {
                loading.remove();
            }, 300);

        }, restante);
    }

    fecharLoader();
}

document.addEventListener(
    "DOMContentLoaded",
    iniciarLoadingPagina
);
/*

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
*/