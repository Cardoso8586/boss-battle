
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

    // Espera boss + guerreiro aparecerem
    await esperarImagensPrincipais(15000);

    esconderLoading();
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
/*
// ===============================
// 🔢 Formata números
// ===============================
function formatarNumero(numero) {
    return new Intl.NumberFormat('pt-BR').format(numero);
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
// 💾 Cache helpers (RAM + localStorage)
// ===============================
function getBossFromCache() {

    // 🧠 Primeiro tenta memória (mais rápido)
    if (bossCache && Date.now() - bossCache.time < CACHE_TTL) {
        return bossCache.data;
    }

    // 💾 Depois tenta localStorage
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
// ⚡ Placeholder (só se não houver cache)
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
// 🖼️ Pré-load de imagem
// ===============================
function preloadBossImage(url) {
    if (!url) return;
    const img = new Image();
    img.src = url;
}

// ===============================
// 🎨 Render do Boss (otimizado)
// ===============================
function renderBoss(boss) {

    const nameEl   = document.getElementById("boss-name");
    const imgEl    = document.getElementById("boss-image");
    const hpBarEl  = document.getElementById("boss-hp-bar");
    const hpTextEl = document.getElementById("boss-hp-text");
    const rewardEl = document.getElementById("boss-reward");
    const xpEl     = document.getElementById("boss-xp");
	const ataque     = document.getElementById("boss-ataque");
	const tempoEntreAtaques     = document.getElementById("boss-tempo-ataque");

    if (!boss || boss.alive === false) {
        nameEl.innerText = "Nenhum boss ativo!";
        imgEl.style.display = "none";
        hpBarEl.style.width = "0%";
        hpTextEl.innerText = "";
        rewardEl.innerText = "0";
        xpEl.innerText = "0";
		ataque.innerText = "0";
		tempoEntreAtaques.innerText = "0";
        return;
    }

    nameEl.innerText = boss.bossName;
    rewardEl.innerText = formatarNumero(boss.rewardBoss);
    xpEl.innerText = formatarNumero(boss.rewardExp);
	ataque.innerText = formatarNumero(boss.attackPower);
	tempoEntreAtaques.innerText = formatarNumero(boss.attackIntervalSeconds ?? 0) + "s";


	
    // 🧠 Atualiza HP apenas se mudar
    if (hpBarEl.dataset.lastHp !== String(boss.currentHp)) {

        const percent = (boss.currentHp / boss.maxHp) * 100;
        hpBarEl.style.width = percent + "%";

        hpTextEl.innerText =
            `${formatarNumero(boss.currentHp)} / ${formatarNumero(boss.maxHp)}`;

        hpBarEl.dataset.lastHp = boss.currentHp;
    }

    // 🖼️ Troca imagem apenas se mudar
    if (bossImagemAtual !== boss.imageUrl) {

        bossImagemAtual = boss.imageUrl;

        const img = new Image();
        img.src = boss.imageUrl;

        img.onload = () => {
            imgEl.src = boss.imageUrl;
            imgEl.style.display = "block";
        };
    }
}

// ===============================
// 🧠 Render seguro (mais suave)
// ===============================
function renderBossSafe(boss) {
    requestAnimationFrame(() => renderBoss(boss));
}

// ===============================
// 🚀 Carrega boss (inicial)
// ===============================
async function carregarBossInicial() {

    const cached = getBossFromCache();

    if (cached) {
        renderBossSafe(cached);
    } else {
        renderBossPlaceholder();
    }

    await fetchBoss();
}

// ===============================
// 🌐 Fetch real do servidor
// ===============================
async function fetchBoss() {
    try {
        const response = await fetch("/api/boss/active");
        if (!response.ok) return;

        const boss = await response.json();

        saveBossToCache(boss);
        preloadBossImage(boss.imageUrl);
        renderBossSafe(boss);

    } catch (e) {
        console.error("Erro ao buscar boss:", e);
    }
}

// ===============================
// 🔄 Loop inteligente (sem spam)
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
document.addEventListener("DOMContentLoaded", carregarBossInicial);


/**
 * 
 * 
 * 
// ===============================
// 🔢 Formata números
// ===============================
function formatarNumero(numero) {
    return new Intl.NumberFormat('pt-BR').format(numero);
}

// ===============================
// 🌐 Variáveis globais
// ===============================
let bossImagemAtual = null;
let bossEstavaVivo = false;
let bossCache = null;

const CACHE_KEY = "boss_active_cache";
const CACHE_TTL = 3000; // 3 segundos

// ===============================
// 💾 Cache helpers
// ===============================
function getBossFromCache() {
    const cached = localStorage.getItem(CACHE_KEY);
    if (!cached) return null;

    try {
        const parsed = JSON.parse(cached);
        if (Date.now() - parsed.time > CACHE_TTL) return null;
        return parsed.data;
    } catch {
        return null;
    }
}

function saveBossToCache(boss) {
    bossCache = boss;
    localStorage.setItem(CACHE_KEY, JSON.stringify({
        data: boss,
        time: Date.now()
    }));
}

// ===============================
// 🎨 Renderização do Boss
// ===============================
function renderBoss(boss) {
    const nameEl   = document.getElementById("boss-name");
    const imgEl    = document.getElementById("boss-image");
    const hpBarEl  = document.getElementById("boss-hp-bar");
    const hpTextEl = document.getElementById("boss-hp-text");
    const reward   = document.getElementById("boss-reward");
    const bossXp   = document.getElementById("boss-xp");

    // ❌ Sem boss ativo
    if (!boss || boss.alive === false) {
        if (bossEstavaVivo) {
            bossEstavaVivo = false;
            setTimeout(() => location.reload(), 600);
        }

        nameEl.innerText = "Nenhum boss ativo no momento!";
        imgEl.style.display = "none";
        hpBarEl.style.width = "0%";
        hpTextEl.innerText = "";
        reward.innerText = "0";
        bossXp.innerText = "0";
        return;
    }

    bossEstavaVivo = true;

    // ✅ Textos
    nameEl.innerText = boss.bossName;
    reward.innerText = formatarNumero(boss.rewardBoss);
    bossXp.innerText = formatarNumero(boss.rewardExp);

    // ❤️ HP
    const percent = (boss.currentHp / boss.maxHp) * 100;
    hpBarEl.style.width = percent + "%";
    hpTextEl.innerText =
        `${formatarNumero(boss.currentHp)} / ${formatarNumero(boss.maxHp)}`;

    // 🖼️ Imagem (só se mudar)
    if (bossImagemAtual !== boss.imageUrl) {
        bossImagemAtual = boss.imageUrl;
        imgEl.style.display = "none";

        const img = new Image();
        img.src = boss.imageUrl;
        img.onload = () => {
            imgEl.src = boss.imageUrl;
            imgEl.style.display = "block";
        };
    }
}

// ===============================
// 🔄 Carrega boss (cache + API)
// ===============================
async function carregarBossAtivo() {
    try {
        // 1️⃣ Cache imediato
        const cachedBoss = getBossFromCache();
        if (cachedBoss) {
            bossCache = cachedBoss;
            renderBoss(cachedBoss);
        }

        // 2️⃣ Busca servidor
        const response = await fetch("/api/boss/active");
        if (!response.ok) return;

        const boss = await response.json();

        // 3️⃣ Atualiza só se mudou
        if (
            !bossCache ||
            boss.id !== bossCache.id ||
            boss.currentHp !== bossCache.currentHp
        ) {
            saveBossToCache(boss);
            renderBoss(boss);
        }

    } catch (e) {
        console.error("Erro ao carregar boss ativo:", e);
    }
}

// ===============================
// 🚀 Inicialização
// ===============================
carregarBossAtivo();
setInterval(carregarBossAtivo, 3000);


/***
 *  // Função para formatar números com separadores de milhar
 function formatarNumero(numero) {
     return new Intl.NumberFormat('pt-BR').format(numero);
 }




 async function carregarBossAtivo() {
     try {
         const response = await fetch("/api/boss/active");
         if (!response.ok) return;

         const boss = await response.json();

         const nameEl = document.getElementById("boss-name");
         const imgEl = document.getElementById("boss-image");
         const hpBarEl = document.getElementById("boss-hp-bar");
         const hpTextEl = document.getElementById("boss-hp-text");
         const reward = document.getElementById("boss-reward");
         const bossXp = document.getElementById("boss-xp");

         if (!boss || boss.alive === false) {
             setTimeout(() => {
                 location.reload();
             }, 600);
             if (bossEstavaVivo) bossEstavaVivo = false;

             nameEl.innerText = "Nenhum boss ativo no momento!";
             imgEl.style.display = "none";
             hpBarEl.style.width = "0%";
             hpTextEl.innerText = "";
             reward.innerText = "0";
             bossXp.innerText = "0";
             return;
         }

         bossEstavaVivo = true;

         // Carrega a imagem antes de mostrar
         const img = new Image();
         img.src = boss.imageUrl;
         img.onload = () => {
             imgEl.src = boss.imageUrl;
             imgEl.style.display = "block";

             // Atualiza os outros elementos só depois que a imagem carregou
             nameEl.innerText = boss.bossName;
             reward.innerText = formatarNumero(boss.rewardBoss);
             bossXp.innerText = formatarNumero(boss.rewardExp);

             const percent = (boss.currentHp / boss.maxHp) * 100;
             hpBarEl.style.width = percent + "%";
             hpTextEl.innerText = `${formatarNumero(boss.currentHp)} / ${formatarNumero(boss.maxHp)}`;
         };

     } catch (e) {
         console.error("Erro ao carregar boss ativo:", e);
     }
 }


 carregarBossAtivo(); // ⚡ carrega imediatamente
 setInterval(carregarBossAtivo, 3000);



 * 
 */


 /**
  *  function tocarSom() {
  	
  	let ultimoAudio = "";

  	
  	    const checkbox = document.getElementById("audio-toggle");
  	    if (!checkbox.checked) return; // ❌ usuário desligou o áudio

  	    const audiosScary = [
  	        "audio/scary1.mp3",
  	        "audio/scary2.mp3",
  	        "audio/scary3.mp3",
  	        "audio/scary4.mp3",
  	        "audio/scary5.mp3",
  			"audio/scary6.mp3"
  	    ];

  	    const audio = document.getElementById("super-scary");

  	    // escolhe áudio sem repetir
  	    let src;
  	    do {
  	        src = audiosScary[Math.floor(Math.random() * audiosScary.length)];
  	    } while (src === ultimoAudio);

  	    ultimoAudio = src;

  	    // reset seguro
  	    audio.pause();
  	    audio.currentTime = 0;
  	    audio.src = src;

  	    // autoplay seguro
  	    audio.muted = true;
  	    audio.volume = 0;

  	    audio.play().then(() => {
  	        audio.muted = false;

  	        let vol = 0;
  	        const alvo = 0.4 + Math.random() * 0.2;

  	        const fade = setInterval(() => {
  	            if (!checkbox.checked) {
  	                audio.pause();
  	                clearInterval(fade);
  	                return;
  	            }

  	            if (vol < alvo) {
  	                vol += 0.02;
  	                audio.volume = vol;
  	            } else {
  	                clearInterval(fade);
  	            }
  	        }, 80);
  	    }).catch(() => {});
  	}

  	
  */





