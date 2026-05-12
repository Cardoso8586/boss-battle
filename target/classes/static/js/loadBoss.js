

// ===========================================
// LOADING FIX IMEDIATO
// ===========================================

function setLoadingTextSeguro(mensagem) {

    const aplicar = () => {

        const textEl =
            document.getElementById("loading-text");

        if (textEl) {
            textEl.textContent = mensagem;
        }
    };

    aplicar();

    if (document.readyState === "loading") {

        document.addEventListener(
            "DOMContentLoaded",
            aplicar
        );
    }
}

// ===========================================
// LOADING TEXTS
// ===========================================

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

// ===========================================
// MENSAGENS DEMORADAS
// ===========================================

const loadingMessagesDemorado = [
    "O boss está despertando...",
    "Os portões da arena estão pesados...",
    "Uma presença sombria se aproxima...",
    "O chefe está reunindo poder...",
    "As criaturas aguardam ordens...",
    "Algo poderoso está surgindo...",
    "O chão da arena está tremendo...",
    "As sombras estão se movendo...",
    "Os guerreiros aguardam o confronto...",
    "A energia do boss está aumentando...",
    "Uma batalha épica está prestes a começar...",
    "O inimigo está preparando seu ataque...",
    "A arena está sendo preparada...",
    "Os ecos da batalha podem ser ouvidos...",
    "O boss está quase pronto...",
    "As forças inimigas estão se organizando...",
    "A tensão cresce no campo de batalha...",
    "O confronto lendário está chegando...",
    "Os céus escurecem sobre a arena...",
    "A presença do boss domina o ambiente..."
];

// ===========================================
// CONFIG
// ===========================================

const CACHE_KEY = "boss_active_cache";
const CACHE_TTL = 30000;
const UPDATE_INTERVAL = 15000;

const LOADING_MIN_TIME = 2500;
const LOADING_FORCE_TIME = 1200;
const LOADING_MAX_TIME = 7000;

// ===========================================
// STATE
// ===========================================

let loadingFechado = false;
let bossCache = null;
let imagemAtual = null;
let iniciouLoadingEm = Date.now();
let loadingMessageInterval = null;

// ===========================================
// HELPERS
// ===========================================

function formatarNumero(numero) {

    return new Intl.NumberFormat("pt-BR")
        .format(Number(numero || 0));
}

function mostrarMensagemImediata(mensagem) {

    setLoadingTextSeguro(mensagem);
}

function pegarMensagemAleatoria(lista) {

    return lista[
        Math.floor(
            Math.random() * lista.length
        )
    ];
}

function setRandomLoadingText() {

    mostrarMensagemImediata(
        pegarMensagemAleatoria(
            loadingMessages
        )
    );
}

function setRandomLoadingDemoradoText() {

    mostrarMensagemImediata(
        pegarMensagemAleatoria(
            loadingMessagesDemorado
        )
    );
}

// ===========================================
// LOADING
// ===========================================

function pararRotacaoMensagens() {

    if (loadingMessageInterval) {

        clearInterval(
            loadingMessageInterval
        );

        loadingMessageInterval = null;
    }
}

function esconderLoading() {

    if (loadingFechado) return;

    loadingFechado = true;

    pararRotacaoMensagens();

    const loading =
        document.getElementById("loading");

    if (!loading) return;

    loading.style.transition =
        "opacity 0.3s ease";

    loading.style.opacity = "0";

    setTimeout(() => {

        loading.remove();

    }, 300);
}

function liberarLoadingSeguro() {

    const tempoPassado =
        Date.now() - iniciouLoadingEm;

    const tempoRestante =
        Math.max(
            0,
            LOADING_MIN_TIME - tempoPassado
        );

    setTimeout(() => {

        esconderLoading();

    }, tempoRestante);
}

// ===========================================
// FORCE LOADING
// ===========================================

function forcarLoadingSeDemorar() {

    setTimeout(() => {

        if (loadingFechado) return;

        setRandomLoadingDemoradoText();

        loadingMessageInterval =
            setInterval(() => {

                if (loadingFechado) {

                    pararRotacaoMensagens();

                    return;
                }

                setRandomLoadingDemoradoText();

            }, 2200);

    }, LOADING_FORCE_TIME);
}

// ===========================================
// SEGURANÇA
// ===========================================

setTimeout(() => {

    esconderLoading();

}, LOADING_MAX_TIME);

// ===========================================
// CACHE
// ===========================================

function getBossFromCache() {

    if (
        bossCache &&
        Date.now() - bossCache.time <
        CACHE_TTL
    ) {

        return bossCache.data;
    }

    try {

        const cached =
            JSON.parse(
                localStorage.getItem(
                    CACHE_KEY
                )
            );

        if (!cached)
            return null;

        if (
            Date.now() - cached.time >
            CACHE_TTL
        ) {

            localStorage.removeItem(
                CACHE_KEY
            );

            return null;
        }

        bossCache = cached;

        return cached.data;

    } catch {

        return null;
    }
}

function saveBossToCache(boss) {

    if (!boss) return;

    bossCache = {
        data: boss,
        time: Date.now()
    };

    localStorage.setItem(
        CACHE_KEY,
        JSON.stringify(bossCache)
    );
}

// ===========================================
// IMAGE PRELOAD
// ===========================================

function preloadImagem(src) {

    if (!src)
        return;

    const img = new Image();

    img.fetchPriority = "high";

    img.src = src;
}

function preloadImagemPromise(src) {

    return new Promise((resolve) => {

        if (!src) {

            resolve();
            return;
        }

        const img = new Image();

        img.onload = () => resolve();

        img.onerror = () => resolve();

        img.fetchPriority = "high";

        img.src = src;
    });
}

// ===========================================
// PLACEHOLDER
// ===========================================

function renderBossPlaceholder() {

    const frases = [
        "O chefe está se aproximando...",
        "Um chefe poderoso se aproxima...",
        "Prepare-se... o chefe está chegando.",
        "Algo terrível está prestes a aparecer...",
        "Um inimigo lendário se aproxima..."
    ];

    const nameEl =
        document.getElementById(
            "boss-name"
        );

    const hpBarEl =
        document.getElementById(
            "boss-hp-bar"
        );

    const hpTextEl =
        document.getElementById(
            "boss-hp-text"
        );

    if (nameEl) {

        nameEl.innerText =
            pegarMensagemAleatoria(frases);
    }

    if (hpBarEl)
        hpBarEl.style.width =
            "100%";

    if (hpTextEl)
        hpTextEl.innerText =
            "???? / ????";
}

// ===========================================
// RENDER
// ===========================================

function renderBoss(boss) {

    const nameEl =
        document.getElementById(
            "boss-name"
        );

    const imgEl =
        document.getElementById(
            "boss-image"
        );

    const hpBarEl =
        document.getElementById(
            "boss-hp-bar"
        );

    const hpTextEl =
        document.getElementById(
            "boss-hp-text"
        );

    const rewardEl =
        document.getElementById(
            "boss-reward"
        );

    const xpEl =
        document.getElementById(
            "boss-xp"
        );

    const ataqueEl =
        document.getElementById(
            "boss-ataque"
        );

    const tempoEl =
        document.getElementById(
            "boss-tempo-ataque"
        );

    if (
        !nameEl ||
        !imgEl ||
        !hpBarEl ||
        !hpTextEl
    ) return;

    // ===========================================
    // SEM BOSS
    // ===========================================

    if (!boss || boss.alive === false) {

        nameEl.innerText =
            "Nenhum boss ativo!";

        imgEl.style.display =
            "none";

        hpBarEl.style.width =
            "0%";

        hpTextEl.innerText = "";

        return;
    }

    // ===========================================
    // TEXTOS
    // ===========================================

    nameEl.innerText =
        boss.bossName || "Boss";

    if (rewardEl)
        rewardEl.innerText =
            formatarNumero(
                boss.rewardBoss
            );

    if (xpEl)
        xpEl.innerText =
            formatarNumero(
                boss.rewardExp
            );

    if (ataqueEl)
        ataqueEl.innerText =
            formatarNumero(
                boss.attackPower
            );

    if (tempoEl)
        tempoEl.innerText =
            formatarNumero(
                boss.attackIntervalSeconds ?? 0
            ) + "s";

    // ===========================================
    // HP
    // ===========================================

    const maxHp =
        Number(
            boss.maxHp || 0
        );

    const currentHp =
        Number(
            boss.currentHp || 0
        );

    if (maxHp > 0) {

        const percent =
            Math.max(
                0,
                Math.min(
                    100,
                    (currentHp / maxHp) * 100
                )
            );

        hpBarEl.style.width =
            percent + "%";

        hpTextEl.innerText =
            `${formatarNumero(currentHp)} / ${formatarNumero(maxHp)}`;
    }

    // ===========================================
    // IMAGEM
    // ===========================================

    if (boss.imageUrl) {

        if (
            imagemAtual !==
            boss.imageUrl
        ) {

            imgEl.src =
                boss.imageUrl;

            imagemAtual =
                boss.imageUrl;
        }

        imgEl.loading =
            "eager";

        imgEl.fetchPriority =
            "high";

        imgEl.style.display =
            "block";

    } else {

        imgEl.style.display =
            "none";
    }
}

// ===========================================
// FETCH
// ===========================================

async function fetchBossSemRenderizar() {

    try {

        const response =
            await fetch(
                "/api/boss/active",
                {
                    cache: "no-store"
                }
            );

        if (!response.ok)
            return null;

        const boss =
            await response.json();

        return boss;

    } catch (e) {

        console.error(
            "Erro ao buscar boss:",
            e
        );

        return null;
    }
}

async function fetchBoss() {

    const boss =
        await fetchBossSemRenderizar();

    if (!boss)
        return null;

    saveBossToCache(boss);

    renderBoss(boss);

    preloadImagem(
        boss.imageUrl
    );

    return boss;
}

// ===========================================
// LOAD INICIAL
// ===========================================

async function carregarBossInicial() {

    const cached =
        getBossFromCache();

    // ===========================================
    // JÁ LOGADO / CACHE / RELOAD
    // ===========================================

	if (cached) {

	    mostrarMensagemImediata(
	        "Preparando arena..."
	    );

	    renderBoss(cached);

	    preloadImagem(
	        cached.imageUrl
	    );

	    fetchBoss();

	    liberarLoadingSeguro();

	    return;
	}

    // ===========================================
    // LOGIN / PRIMEIRA VEZ
    // ===========================================

    setRandomLoadingText();

    renderBossPlaceholder();

    forcarLoadingSeDemorar();

    try {

        const boss =
            await fetchBossSemRenderizar();

        if (boss) {

            await preloadImagemPromise(
                boss.imageUrl
            );

            saveBossToCache(boss);

            renderBoss(boss);

        } else {

            renderBoss(null);
        }

    } finally {

        liberarLoadingSeguro();
    }
}

// ===========================================
// AUTO UPDATE
// ===========================================

setInterval(() => {

    fetchBoss();

}, UPDATE_INTERVAL);

// ===========================================
// START
// ===========================================

document.addEventListener(
    "DOMContentLoaded",
    async () => {

        iniciouLoadingEm =
            Date.now();

        mostrarMensagemImediata(
            "Preparando arena..."
        );

        await carregarBossInicial();
    }
);


/*

// ===========================================
// LOADING FIX IMEDIATO
// ===========================================

function setLoadingTextSeguro(mensagem) {

    const aplicar = () => {

        const textEl =
            document.getElementById("loading-text");

        if (textEl) {
            textEl.textContent = mensagem;
        }
    };

    aplicar();

    if (document.readyState === "loading") {

        document.addEventListener(
            "DOMContentLoaded",
            aplicar
        );
    }
}

// ===========================================
// LOADING TEXTS
// ===========================================

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

// ===========================================
// CONFIG
// ===========================================

const CACHE_KEY = "boss_active_cache";
const CACHE_TTL = 30000;
const UPDATE_INTERVAL = 15000;

const LOADING_MIN_TIME = 1000;
const LOADING_FORCE_TIME = 2500;
const LOADING_MAX_TIME = 7000;

// ===========================================
// STATE
// ===========================================

let loadingFechado = false;
let bossCache = null;
let imagemAtual = null;
let iniciouLoadingEm = Date.now();
let loadingMessageInterval = null;

// ===========================================
// HELPERS
// ===========================================

function formatarNumero(numero) {

    return new Intl.NumberFormat("pt-BR")
        .format(Number(numero || 0));
}

function mostrarMensagemImediata(mensagem) {

    setLoadingTextSeguro(mensagem);
}

function setRandomLoadingText() {

    const message =
        loadingMessages[
            Math.floor(
                Math.random() *
                loadingMessages.length
            )
        ];

    mostrarMensagemImediata(message);
}

// ===========================================
// ROTACIONAR MENSAGENS
// ===========================================

function iniciarRotacaoMensagens() {

    setRandomLoadingText();

    loadingMessageInterval =
        setInterval(() => {

            if (loadingFechado) {

                clearInterval(
                    loadingMessageInterval
                );

                return;
            }

            setRandomLoadingText();

        }, 1800);
}

function pararRotacaoMensagens() {

    if (loadingMessageInterval) {

        clearInterval(
            loadingMessageInterval
        );

        loadingMessageInterval = null;
    }
}

// ===========================================
// LOADING
// ===========================================

function esconderLoading() {

    if (loadingFechado) return;

    loadingFechado = true;

    pararRotacaoMensagens();

    const loading =
        document.getElementById("loading");

    if (!loading) return;

    loading.style.transition =
        "opacity 0.3s ease";

    loading.style.opacity = "0";

    setTimeout(() => {

        loading.remove();

    }, 300);
}

function liberarLoadingSeguro() {

    const tempoPassado =
        Date.now() - iniciouLoadingEm;

    const tempoRestante =
        Math.max(
            0,
            LOADING_MIN_TIME - tempoPassado
        );

    setTimeout(() => {

        esconderLoading();

    }, tempoRestante);
}

function forcarLoadingSeDemorar() {

    setTimeout(() => {

        if (loadingFechado) return;

        mostrarMensagemImediata(
            "Ainda carregando boss..."
        );

    }, LOADING_FORCE_TIME);
}

// ===========================================
// SEGURANÇA
// ===========================================

setTimeout(() => {

    esconderLoading();

}, LOADING_MAX_TIME);

// ===========================================
// CACHE
// ===========================================

function getBossFromCache() {

    if (
        bossCache &&
        Date.now() - bossCache.time <
        CACHE_TTL
    ) {

        return bossCache.data;
    }

    try {

        const cached =
            JSON.parse(
                localStorage.getItem(
                    CACHE_KEY
                )
            );

        if (!cached)
            return null;

        if (
            Date.now() - cached.time >
            CACHE_TTL
        ) {

            localStorage.removeItem(
                CACHE_KEY
            );

            return null;
        }

        bossCache = cached;

        return cached.data;

    } catch {

        return null;
    }
}

function saveBossToCache(boss) {

    if (!boss) return;

    bossCache = {
        data: boss,
        time: Date.now()
    };

    localStorage.setItem(
        CACHE_KEY,
        JSON.stringify(bossCache)
    );
}

// ===========================================
// IMAGE PRELOAD
// ===========================================

function preloadImagem(src) {

    if (!src)
        return;

    const img = new Image();

    img.fetchPriority = "high";

    img.src = src;
}

function preloadImagemPromise(src) {

    return new Promise((resolve) => {

        if (!src) {

            resolve();
            return;
        }

        const img = new Image();

        img.onload = () => resolve();

        img.onerror = () => resolve();

        img.fetchPriority = "high";

        img.src = src;
    });
}

// ===========================================
// PLACEHOLDER
// ===========================================

function renderBossPlaceholder() {

    const frases = [
        "O chefe está se aproximando...",
        "Um chefe poderoso se aproxima...",
        "Prepare-se... o chefe está chegando.",
        "Algo terrível está prestes a aparecer...",
        "Um inimigo lendário se aproxima..."
    ];

    const nameEl =
        document.getElementById(
            "boss-name"
        );

    const hpBarEl =
        document.getElementById(
            "boss-hp-bar"
        );

    const hpTextEl =
        document.getElementById(
            "boss-hp-text"
        );

    if (nameEl) {

        nameEl.innerText =
            frases[
                Math.floor(
                    Math.random() *
                    frases.length
                )
            ];
    }

    if (hpBarEl)
        hpBarEl.style.width =
            "100%";

    if (hpTextEl)
        hpTextEl.innerText =
            "???? / ????";
}

// ===========================================
// RENDER
// ===========================================

function renderBoss(boss) {

    const nameEl =
        document.getElementById(
            "boss-name"
        );

    const imgEl =
        document.getElementById(
            "boss-image"
        );

    const hpBarEl =
        document.getElementById(
            "boss-hp-bar"
        );

    const hpTextEl =
        document.getElementById(
            "boss-hp-text"
        );

    if (
        !nameEl ||
        !imgEl ||
        !hpBarEl ||
        !hpTextEl
    ) return;

    if (!boss || boss.alive === false) {

        nameEl.innerText =
            "Nenhum boss ativo!";

        imgEl.style.display =
            "none";

        hpBarEl.style.width =
            "0%";

        hpTextEl.innerText = "";

        return;
    }

    nameEl.innerText =
        boss.bossName || "Boss";

    const maxHp =
        Number(boss.maxHp || 0);

    const currentHp =
        Number(boss.currentHp || 0);

    if (maxHp > 0) {

        const percent =
            Math.max(
                0,
                Math.min(
                    100,
                    (currentHp / maxHp) *
                    100
                )
            );

        hpBarEl.style.width =
            percent + "%";

        hpTextEl.innerText =
            `${formatarNumero(currentHp)} / ${formatarNumero(maxHp)}`;
    }

    if (boss.imageUrl) {

        if (
            imagemAtual !==
            boss.imageUrl
        ) {

            imgEl.src =
                boss.imageUrl;

            imagemAtual =
                boss.imageUrl;
        }

        imgEl.loading = "eager";

        imgEl.fetchPriority =
            "high";

        imgEl.style.display =
            "block";

    } else {

        imgEl.style.display =
            "none";
    }
}

// ===========================================
// FETCH
// ===========================================

async function fetchBossSemRenderizar() {

    try {

        const response =
            await fetch(
                "/api/boss/active",
                {
                    cache: "no-store"
                }
            );

        if (!response.ok)
            return null;

        const boss =
            await response.json();

        return boss;

    } catch (e) {

        console.error(
            "Erro ao buscar boss:",
            e
        );

        return null;
    }
}

async function fetchBoss() {

    const boss =
        await fetchBossSemRenderizar();

    if (!boss)
        return null;

    saveBossToCache(boss);

    renderBoss(boss);

    preloadImagem(
        boss.imageUrl
    );

    return boss;
}

// ===========================================
// LOAD INICIAL
// ===========================================

async function carregarBossInicial() {

    mostrarMensagemImediata(
        "Entrando na arena..."
    );

    iniciarRotacaoMensagens();

    const cached =
        getBossFromCache();

    // ===========================================
    // CACHE
    // ===========================================

    if (cached) {

        renderBoss(cached);

        preloadImagem(
            cached.imageUrl
        );

        fetchBoss();

        liberarLoadingSeguro();

        return;
    }

    // ===========================================
    // SEM CACHE
    // ===========================================

    renderBossPlaceholder();

    forcarLoadingSeDemorar();

    try {

        const boss =
            await fetchBossSemRenderizar();

        if (boss) {

            mostrarMensagemImediata(
                "Carregando imagem do boss..."
            );

            await preloadImagemPromise(
                boss.imageUrl
            );

            saveBossToCache(boss);

            renderBoss(boss);

            mostrarMensagemImediata(
                "Boss carregado!"
            );

        } else {

            renderBoss(null);
        }

    } finally {

        liberarLoadingSeguro();
    }
}

// ===========================================
// AUTO UPDATE
// ===========================================

setInterval(() => {

    fetchBoss();

}, UPDATE_INTERVAL);

// ===========================================
// START
// ===========================================

document.addEventListener(
    "DOMContentLoaded",
    async () => {

        iniciouLoadingEm =
            Date.now();

        mostrarMensagemImediata(
            "Preparando arena..."
        );

        await carregarBossInicial();
    }
);

  */





