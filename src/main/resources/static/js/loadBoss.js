
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
    }, 3700);
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
	    const imagemAtualNoHtml = imgEl.getAttribute("src");

	    if (imagemAtualNoHtml !== boss.imageUrl) {
	        imgEl.src = boss.imageUrl;
	    }

	    imgEl.style.display = "block";
	    bossImagemAtual = boss.imageUrl;
	} else {
	    imgEl.style.display = "none";
	}
}

function renderBossSafe(boss) {
    requestAnimationFrame(() => renderBoss(boss));
}

async function fetchBoss() {
    try {
        const response = await fetch("/api/boss/active", {
            cache: "no-store"
        });

        if (!response.ok) return null;

        const boss = await response.json();

        saveBossToCache(boss);
        renderBossSafe(boss);

        return boss;

    } catch (e) {
        console.error("Erro ao buscar boss:", e);
        return null;
    }
}

function carregarBossInicial() {
    const cached = getBossFromCache();

    if (cached) {
        renderBossSafe(cached);
    } else {
        renderBossPlaceholder();
    }

    fetchBoss();
}

setInterval(() => {
    fetchBoss();
}, UPDATE_INTERVAL);

document.addEventListener("DOMContentLoaded", () => {

    const jaEntrouArena =
        sessionStorage.getItem("arena_loading_ok");

    // PRIMEIRA entrada após login
    if (!jaEntrouArena) {

        iniciarLoading();

        setTimeout(() => {
            esconderLoading();
        }, 1500);

        sessionStorage.setItem("arena_loading_ok", "1");

    } else {

        // remove loading instantaneamente
        const loading = document.getElementById("loading");

        if (loading) {
            loading.remove();
        }
    }

    carregarBossInicial();
});

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

// ===============================
// 🔢 Formata números
// ===============================
function formatarNumero(numero) {
    return new Intl.NumberFormat("pt-BR").format(numero);
}

// ===============================
// 🌐 Estado global
// ===============================
let bossImagemAtual = null;
let bossCache = null;

const CACHE_KEY = "boss_active_cache";
const CACHE_TTL = 10000;
const UPDATE_INTERVAL = 15000;

// ===============================
// 🌀 Loading
// ===============================
function setRandomLoadingText() {
    const textEl = document.getElementById("loading-text");
    if (!textEl) return;

    const message = loadingMessages[Math.floor(Math.random() * loadingMessages.length)];
    textEl.textContent = message;
}

function iniciarLoading() {
    // escolhe uma frase só por carregamento
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
    }, 650);
}
// Espera a imagem realmente aparecer
function esperarImagemAparecer(imgEl, timeout = 15000) {
    return new Promise(resolve => {
        if (!imgEl) {
            resolve();
            return;
        }

        const inicio = Date.now();

        const verificar = () => {
            const apareceu =
                imgEl.src &&
                imgEl.src.trim() !== "" &&
                imgEl.complete &&
                imgEl.naturalWidth > 0 &&
                imgEl.style.display !== "none";

            if (apareceu) {
                resolve();
                return;
            }

            if (Date.now() - inicio >= timeout) {
                resolve();
                return;
            }

            requestAnimationFrame(verificar);
        };

        verificar();
    });
}

// Espera várias imagens
function esperarImagensPrincipais(timeout = 15000) {
    const imgBoss = document.getElementById("boss-image");
    const imgGuerreiro = document.getElementById("guerreiro-image");

    return Promise.all([
        esperarImagemAparecer(imgBoss, timeout),
        esperarImagemAparecer(imgGuerreiro, timeout)
    ]);
}

// ===============================
// 💾 Cache helpers
// ===============================
function getBossFromCache() {
    if (bossCache && Date.now() - bossCache.time < CACHE_TTL) {
        return bossCache.data;
    }

    try {
        const cached = JSON.parse(localStorage.getItem(CACHE_KEY));
        if (!cached) return null;
        if (Date.now() - cached.time > CACHE_TTL) return null;

        bossCache = cached;
        return cached.data;
    } catch {
        return null;
    }
}

function saveBossToCache(boss) {
    bossCache = {
        data: boss,
        time: Date.now()
    };

    localStorage.setItem(CACHE_KEY, JSON.stringify(bossCache));
}

// ===============================
// ⚡ Placeholder
// ===============================
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

    document.getElementById("boss-name").innerText =
        frases[Math.floor(Math.random() * frases.length)];

    document.getElementById("boss-hp-bar").style.width = "100%";
    document.getElementById("boss-hp-text").innerText = "???? / ????";
    document.getElementById("boss-reward").innerText = "?";
    document.getElementById("boss-xp").innerText = "?";
    document.getElementById("boss-ataque").innerText = "?";
    document.getElementById("boss-tempo-ataque").innerText = "?";
}

// ===============================
// 🖼️ Pré-load
// ===============================
function preloadBossImage(url) {
    if (!url) return;
    const img = new Image();
    img.src = url;
}

// ===============================
// 🎨 Render do Boss
// ===============================
function renderBoss(boss) {
    const nameEl = document.getElementById("boss-name");
    const imgEl = document.getElementById("boss-image");
    const hpBarEl = document.getElementById("boss-hp-bar");
    const hpTextEl = document.getElementById("boss-hp-text");
    const rewardEl = document.getElementById("boss-reward");
    const xpEl = document.getElementById("boss-xp");
    const ataque = document.getElementById("boss-ataque");
    const tempoEntreAtaques = document.getElementById("boss-tempo-ataque");

    if (!boss || boss.alive === false) {
        nameEl.innerText = "Nenhum boss ativo!";
        imgEl.style.display = "none";
        hpBarEl.style.width = "0%";
        hpTextEl.innerText = "";
        rewardEl.innerText = "0";
        xpEl.innerText = "0";
        ataque.innerText = "0";
        tempoEntreAtaques.innerText = "0";
        esconderLoading();
        return;
    }

    nameEl.innerText = boss.bossName;
    rewardEl.innerText = formatarNumero(boss.rewardBoss);
    xpEl.innerText = formatarNumero(boss.rewardExp);
    ataque.innerText = formatarNumero(boss.attackPower);
    tempoEntreAtaques.innerText = formatarNumero(boss.attackIntervalSeconds ?? 0) + "s";

    if (hpBarEl.dataset.lastHp !== String(boss.currentHp)) {
        const percent = (boss.currentHp / boss.maxHp) * 100;
        hpBarEl.style.width = percent + "%";
        hpTextEl.innerText = `${formatarNumero(boss.currentHp)} / ${formatarNumero(boss.maxHp)}`;
        hpBarEl.dataset.lastHp = boss.currentHp;
    }

    if (bossImagemAtual !== boss.imageUrl) {
        bossImagemAtual = boss.imageUrl;

        const img = new Image();
        img.src = boss.imageUrl;

        img.onload = () => {
            imgEl.src = boss.imageUrl;
            imgEl.style.display = "block";
        };

        img.onerror = () => {
            imgEl.style.display = "none";
        };
    }
}

// ===============================
// 🧠 Render seguro
// ===============================
function renderBossSafe(boss) {
    requestAnimationFrame(() => renderBoss(boss));
}

// ===============================
// 🚀 Carrega boss inicial
// ===============================
async function carregarBossInicial() {
    const cached = getBossFromCache();

    if (cached) {
        renderBossSafe(cached);
    } else {
        renderBossPlaceholder();
    }

    await fetchBoss();
    await esperarImagensPrincipais(15000);
    await new Promise(resolve => setTimeout(resolve, 300));

    esconderLoading();
}

function esconderLoading() {
    if (loadingFechado) return;
    loadingFechado = true;

    const loading = document.getElementById("loading");
    if (!loading) return;

    loading.classList.add("fade-out");

    setTimeout(() => {
        loading.remove();
    }, 700);
}

// ===============================
// 🌐 Fetch real do servidor
// ===============================
async function fetchBoss() {
    try {
        const response = await fetch("/api/boss/active");
        if (!response.ok) return null;

        const boss = await response.json();

        saveBossToCache(boss);
        preloadBossImage(boss.imageUrl);
        renderBossSafe(boss);

        return boss;
    } catch (e) {
        console.error("Erro ao buscar boss:", e);
        return null;
    }
}

// ===============================
// 🔄 Loop inteligente
// ===============================
setInterval(() => {
    const cached = getBossFromCache();

    if (cached) {
        renderBossSafe(cached);
    } else {
        fetchBoss();
    }
}, UPDATE_INTERVAL);

// ===============================
// 🧠 Inicialização
// ===============================
document.addEventListener("DOMContentLoaded", async () => {
    iniciarLoading();
    await carregarBossInicial();
});
  	
  */





