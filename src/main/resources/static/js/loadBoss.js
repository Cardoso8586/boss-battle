
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

let loadingFechado = false;
let bossCache = null;
let imagemAtual = null;

// ===========================================
// HELPERS
// ===========================================

function formatarNumero(numero) {
    return new Intl.NumberFormat("pt-BR")
        .format(Number(numero || 0));
}

function setRandomLoadingText() {

    const textEl =
        document.getElementById("loading-text");

    if (!textEl) return;

    const message =
        loadingMessages[
            Math.floor(Math.random() * loadingMessages.length)
        ];

    textEl.textContent = message;
}

function esconderLoading() {

    if (loadingFechado) return;

    loadingFechado = true;

    const loading =
        document.getElementById("loading");

    if (!loading) return;

    loading.style.opacity = "0";

    setTimeout(() => {
        loading.remove();
    }, 300);
}

// ===========================================
// CACHE
// ===========================================

function getBossFromCache() {

    if (
        bossCache &&
        Date.now() - bossCache.time < CACHE_TTL
    ) {

        return bossCache.data;
    }

    try {

        const cached =
            JSON.parse(
                localStorage.getItem(CACHE_KEY)
            );

        if (!cached)
            return null;

        if (
            Date.now() - cached.time > CACHE_TTL
        ) {

            localStorage.removeItem(CACHE_KEY);

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

    img.src = src;
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
        document.getElementById("boss-name");

    const hpBarEl =
        document.getElementById("boss-hp-bar");

    const hpTextEl =
        document.getElementById("boss-hp-text");

    if (nameEl) {

        nameEl.innerText =
            frases[
                Math.floor(Math.random() * frases.length)
            ];
    }

    if (hpBarEl)
        hpBarEl.style.width = "100%";

    if (hpTextEl)
        hpTextEl.innerText = "???? / ????";
}

// ===========================================
// RENDER
// ===========================================

function renderBoss(boss) {

    const nameEl =
        document.getElementById("boss-name");

    const imgEl =
        document.getElementById("boss-image");

    const hpBarEl =
        document.getElementById("boss-hp-bar");

    const hpTextEl =
        document.getElementById("boss-hp-text");

    const rewardEl =
        document.getElementById("boss-reward");

    const xpEl =
        document.getElementById("boss-xp");

    const ataqueEl =
        document.getElementById("boss-ataque");

    const tempoEl =
        document.getElementById("boss-tempo-ataque");

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

        imgEl.style.display = "none";

        hpBarEl.style.width = "0%";

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
            formatarNumero(boss.rewardBoss);

    if (xpEl)
        xpEl.innerText =
            formatarNumero(boss.rewardExp);

    if (ataqueEl)
        ataqueEl.innerText =
            formatarNumero(boss.attackPower);

    if (tempoEl)
        tempoEl.innerText =
            formatarNumero(
                boss.attackIntervalSeconds ?? 0
            ) + "s";

    // ===========================================
    // HP
    // ===========================================

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
                    (currentHp / maxHp) * 100
                )
            );

        hpBarEl.style.width =
            percent + "%";

        hpTextEl.innerText =
            `${formatarNumero(currentHp)} / ${formatarNumero(maxHp)}`;
    }

    // ===========================================
    // IMAGEM SUPER OTIMIZADA
    // ===========================================

    if (boss.imageUrl) {

        if (imagemAtual !== boss.imageUrl) {

            preloadImagem(boss.imageUrl);

            imgEl.src = boss.imageUrl;

            imagemAtual = boss.imageUrl;
        }

        imgEl.style.display = "block";

    } else {

        imgEl.style.display = "none";
    }
}

// ===========================================
// FETCH
// ===========================================

async function fetchBoss() {

    try {

        const response =
            await fetch("/api/boss/active", {
                cache: "force-cache"
            });

        if (!response.ok)
            return null;

        const boss =
            await response.json();

        saveBossToCache(boss);

        // renderiza imediatamente
        renderBoss(boss);

        // preload futuro
        preloadImagem(boss.imageUrl);

        return boss;

    } catch (e) {

        console.error(
            "Erro ao buscar boss:",
            e
        );

        return null;
    }
}

// ===========================================
// LOAD INICIAL
// ===========================================

async function carregarBossInicial() {

    const cached =
        getBossFromCache();

    // mostra cache instantâneo
    if (cached) {

        renderBoss(cached);

        preloadImagem(cached.imageUrl);

    } else {

        renderBossPlaceholder();
    }

    // busca atualização sem travar
    fetchBoss();
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

setRandomLoadingText();

document.addEventListener(
    "DOMContentLoaded",
    async () => {

        const jaEntrouArena =
            sessionStorage.getItem(
                "arena_loading_ok"
            );

        // mostra cache imediatamente
        carregarBossInicial();

        if (!jaEntrouArena) {

            setTimeout(() => {

                esconderLoading();

            }, 1200);

            sessionStorage.setItem(
                "arena_loading_ok",
                "1"
            );

        } else {

            const loading =
                document.getElementById(
                    "loading"
                );

            if (loading)
                loading.remove();
        }
    }
);
/*
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

let loadingFechado = false;
let bossImagemAtual = null;
let bossCache = null;

const CACHE_KEY = "boss_active_cache";
const CACHE_TTL = 30000;
const UPDATE_INTERVAL = 15000;

function formatarNumero(numero) {
    return new Intl.NumberFormat("pt-BR").format(Number(numero || 0));
}

function setRandomLoadingText() {
    const textEl = document.getElementById("loading-text");
    if (!textEl) return;

    const message = loadingMessages[Math.floor(Math.random() * loadingMessages.length)];
    textEl.textContent = message;
}

function iniciarLoading() {
    setRandomLoadingText();
}

function esconderLoading() {
    if (loadingFechado) return;
    loadingFechado = true;

    const loading = document.getElementById("loading");
    if (!loading) return;

    loading.classList.add("fade-out");

    setTimeout(() => {
        loading.remove();
    }, 5700);
}

function preloadImagem(src) {
    return new Promise((resolve, reject) => {
        if (!src) {
            resolve(null);
            return;
        }

        const img = new Image();

        img.onload = () => resolve(src);
        img.onerror = () => reject(src);

        img.src = src;
    });
}

async function prepararImagemBoss(boss) {
    if (!boss || !boss.imageUrl) return;

    if (bossImagemAtual === boss.imageUrl) return;

    await preloadImagem(boss.imageUrl);

    bossImagemAtual = boss.imageUrl;
}

function getBossFromCache() {
    if (bossCache && Date.now() - bossCache.time < CACHE_TTL) {
        return bossCache.data;
    }

    try {
        const cached = JSON.parse(localStorage.getItem(CACHE_KEY));

        if (!cached) return null;

        if (Date.now() - cached.time > CACHE_TTL) {
            localStorage.removeItem(CACHE_KEY);
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

    localStorage.setItem(CACHE_KEY, JSON.stringify(bossCache));
}

function renderBossPlaceholder() {
    const frases = [
        "O chefe está se aproximando...",
        "Um chefe poderoso se aproxima...",
        "Prepare-se... o chefe está chegando.",
        "Você sente uma presença poderosa se aproximando...",
        "Algo terrível está prestes a aparecer...",
        "Um inimigo lendário se aproxima...",
        "O ar fica pesado... algo poderoso desperta.",
        "Você sente um arrepio. Um chefe se aproxima.",
        "O silêncio é quebrado por uma presença aterradora...",
        "Prepare-se. Esta batalha não será comum."
    ];

    const nameEl = document.getElementById("boss-name");
    const hpBarEl = document.getElementById("boss-hp-bar");
    const hpTextEl = document.getElementById("boss-hp-text");
    const rewardEl = document.getElementById("boss-reward");
    const xpEl = document.getElementById("boss-xp");
    const ataqueEl = document.getElementById("boss-ataque");
    const tempoEl = document.getElementById("boss-tempo-ataque");

    if (nameEl) {
        nameEl.innerText = frases[Math.floor(Math.random() * frases.length)];
    }

    if (hpBarEl) hpBarEl.style.width = "100%";
    if (hpTextEl) hpTextEl.innerText = "???? / ????";
    if (rewardEl) rewardEl.innerText = "?";
    if (xpEl) xpEl.innerText = "?";
    if (ataqueEl) ataqueEl.innerText = "?";
    if (tempoEl) tempoEl.innerText = "?";
}

function renderBoss(boss) {
    const nameEl = document.getElementById("boss-name");
    const imgEl = document.getElementById("boss-image");
    const hpBarEl = document.getElementById("boss-hp-bar");
    const hpTextEl = document.getElementById("boss-hp-text");
    const rewardEl = document.getElementById("boss-reward");
    const xpEl = document.getElementById("boss-xp");
    const ataqueEl = document.getElementById("boss-ataque");
    const tempoEl = document.getElementById("boss-tempo-ataque");

    if (!nameEl || !imgEl || !hpBarEl || !hpTextEl) return;

    if (!boss || boss.alive === false) {
        nameEl.innerText = "Nenhum boss ativo!";
        imgEl.style.display = "none";
        hpBarEl.style.width = "0%";
        hpTextEl.innerText = "";

        if (rewardEl) rewardEl.innerText = "0";
        if (xpEl) xpEl.innerText = "0";
        if (ataqueEl) ataqueEl.innerText = "0";
        if (tempoEl) tempoEl.innerText = "0";

        return;
    }

    nameEl.innerText = boss.bossName || "Boss";

    if (rewardEl) rewardEl.innerText = formatarNumero(boss.rewardBoss);
    if (xpEl) xpEl.innerText = formatarNumero(boss.rewardExp);
    if (ataqueEl) ataqueEl.innerText = formatarNumero(boss.attackPower);
    if (tempoEl) tempoEl.innerText = formatarNumero(boss.attackIntervalSeconds ?? 0) + "s";

    const maxHp = Number(boss.maxHp || 0);
    const currentHp = Number(boss.currentHp || 0);

    if (maxHp > 0) {
        const percent = Math.max(0, Math.min(100, (currentHp / maxHp) * 100));

        hpBarEl.style.width = percent + "%";
        hpTextEl.innerText = `${formatarNumero(currentHp)} / ${formatarNumero(maxHp)}`;
    }

    if (boss.imageUrl) {
        if (imgEl.getAttribute("src") !== boss.imageUrl) {
            imgEl.src = boss.imageUrl;
        }

        imgEl.style.display = "block";
    } else {
        imgEl.style.display = "none";
    }
}

function renderBossSafe(boss) {
    requestAnimationFrame(() => renderBoss(boss));
}

async function fetchBoss(apenasBuscar = false) {
    try {
        const response = await fetch("/api/boss/active", {
            cache: "no-store"
        });

        if (!response.ok) return null;

        const boss = await response.json();

        saveBossToCache(boss);

        if (!apenasBuscar) {
            await prepararImagemBoss(boss);
            renderBossSafe(boss);
        }

        return boss;

    } catch (e) {
        console.error("Erro ao buscar boss:", e);
        return null;
    }
}

async function carregarBossInicial() {
    const cached = getBossFromCache();

    if (cached) {
        await prepararImagemBoss(cached);
        renderBossSafe(cached);
    } else {
        renderBossPlaceholder();
    }

    const boss = await fetchBoss(true);

    if (boss) {
        await prepararImagemBoss(boss);
        renderBossSafe(boss);
    }
}

setInterval(() => {
    fetchBoss(false);
}, UPDATE_INTERVAL);

document.addEventListener("DOMContentLoaded", async () => {

    const jaEntrouArena = sessionStorage.getItem("arena_loading_ok");

    if (!jaEntrouArena) {

        iniciarLoading();

        await carregarBossInicial();

        esconderLoading();

        sessionStorage.setItem("arena_loading_ok", "1");

    } else {

        const loading = document.getElementById("loading");

        if (loading) {
            loading.remove();
        }

        carregarBossInicial();
    }
});

  */





