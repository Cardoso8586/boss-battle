
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



