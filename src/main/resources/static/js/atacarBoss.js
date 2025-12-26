const attackBtn = document.getElementById("attackBtn");
const usuarioId = getUsuarioLogadoId();

let cooldownInterval = null;
let bossVivo = true;
let reloadDisparado = false; // 🔥 evita reload múltiplo

let ataqueEmAndamento = false; // 🔒 trava real de ataque

// ---------------------------------------------------

function getUsuarioLogadoId() {
    const meta = document.querySelector('meta[name="user-id"]');
    return meta ? parseInt(meta.getAttribute("content")) : null;
}

// ---------------------------------------------------

function showDamageFloating(value) {
    const container = document.getElementById("damageFloatContainer");
    const floatText = document.createElement("div");

    floatText.className = "damage-float";
    floatText.textContent = `-${value}`;

    container.appendChild(floatText);

    setTimeout(() => floatText.remove(), 1000);
}

// ---------------------------------------------------
// ⏱️ COOLDOWN
function iniciarCooldown(segundos) {

    if (!bossVivo) return;

    let restante = segundos;

    attackBtn.disabled = true;
    attackBtn.innerText = `Aguarde (${restante}s)`;

    if (cooldownInterval) {
        clearInterval(cooldownInterval);
        cooldownInterval = null;
    }

    cooldownInterval = setInterval(() => {
        restante--;

        if (restante <= 0) {
            clearInterval(cooldownInterval);
            cooldownInterval = null;

            if (bossVivo) {
                attackBtn.disabled = false;
                attackBtn.innerText = "Atacar Boss";

                // 🔓 LIBERA O LOCK AQUI
                ataqueEmAndamento = false;
            }
            return;
        }

        attackBtn.innerText = `Aguarde (${restante}s)`;
    }, 1000);
}


// ---------------------------------------------------
// 💀 MORTE DO BOSS
function tratarMorteBoss() {

    if (reloadDisparado) return;

    reloadDisparado = true;
    bossVivo = false;

    if (cooldownInterval) {
        clearInterval(cooldownInterval);
        cooldownInterval = null;
    }

    attackBtn.disabled = true;
    attackBtn.innerText = "Boss derrotado";
    attackBtn.style.pointerEvents = "none";

    setTimeout(() => {
        location.reload();
    }, 600);
}

// ---------------------------------------------------
// ⚔️ CLICK ATAQUE
attackBtn.addEventListener("click", async () => {

    // 🔒 trava absoluta
    if (ataqueEmAndamento || !bossVivo) return;

    ataqueEmAndamento = true;
    attackBtn.disabled = true;

    if (!usuarioId) {
        alert("Usuário não identificado.");
        ataqueEmAndamento = false;
        return;
    }

    try {
        const response = await fetch(`/api/boss/hit?usuarioId=${usuarioId}`, {
            method: "POST"
        });

        const data = await response.json();

        // 💀 boss morreu
        if (data.status === "BOSS_DEAD") {
            tratarMorteBoss();
            return;
        }

        // ⏱️ cooldown
        if (data.status === "COOLDOWN") {
            iniciarCooldown(data.segundosRestantes || 300);
            return;
        }

        // ✅ ataque válido
        if (data.damage !== undefined) {
            showDamageFloating(data.damage);
            iniciarCooldown(data.segundosRestantes || 300);
        }

    } catch (err) {
        console.error(err);

        if (bossVivo) {
            attackBtn.disabled = false;
            attackBtn.innerText = "Atacar Boss";
        }

    } finally {
        // ⚠️ libera SOMENTE se cooldown não iniciou
        if (!cooldownInterval && bossVivo) {
            ataqueEmAndamento = false;
        }
    }
});

// ---------------------------------------------------
// 🔄 COOLDOWN INICIAL
async function verificarCooldownInicial() {

    if (!usuarioId) return;

    try {
        const res = await fetch(`/api/boss/cooldown?usuarioId=${usuarioId}`);
        const data = await res.json();

        if (!bossVivo) return;

        if (!data.podeAtacar) {
            iniciarCooldown(data.segundosRestantes || 300);
        } else {
            attackBtn.disabled = false;
            attackBtn.innerText = "Atacar Boss";
           // attackBtn.style.backgroundColor = "#f39c12";
        }

    } catch (e) {
        console.error("Erro ao verificar cooldown", e);
    }
}

// 🚀 INICIALIZA
verificarCooldownInicial();


/**
 * 

const attackBtn = document.getElementById("attackBtn");
const usuarioId = getUsuarioLogadoId();

let cooldownInterval = null;

function getUsuarioLogadoId() {
    const meta = document.querySelector('meta[name="user-id"]');
    return meta ? parseInt(meta.getAttribute("content")) : null;
}

function showDamageFloating(value) {
    const container = document.getElementById("damageFloatContainer");
    const floatText = document.createElement("div");

    floatText.className = "damage-float";
    floatText.textContent = `-${value}`;

    container.appendChild(floatText);

    setTimeout(() => floatText.remove(), 1000);
}

// 🔄 Atualiza o texto do botão com contador e cor
function iniciarCooldown(segundos) {
    let restante = segundos;

    attackBtn.disabled = true;
    attackBtn.style.backgroundColor = "#ccc"; // cinza para cooldown
    attackBtn.innerText = `Aguarde (${restante}s)`;

    if (cooldownInterval) clearInterval(cooldownInterval);

    cooldownInterval = setInterval(() => {
        restante--;

        if (restante <= 0) {
            clearInterval(cooldownInterval);
            attackBtn.disabled = false;
            attackBtn.innerText = "Atacar Boss";
            attackBtn.style.backgroundColor = "#f39c12"; 
            return;
        }

        attackBtn.innerText = `Aguarde (${restante}s)`;
    }, 1000);
}


attackBtn.addEventListener("click", async () => {
    if (!usuarioId) {
        alert("Usuário não identificado.");
        return;
    }

	attackBtn.disabled = true;
	attackBtn.style.backgroundColor = "#ccc"; // cinza para cooldown
    try {
        const response = await fetch(`/api/boss/hit?usuarioId=${usuarioId}`, {
            method: "POST"
        });

        const data = await response.json();
        console.log("Resposta do servidor:", data);

        // ⏱️ Se estiver em cooldown
        if (data.status === "COOLDOWN") {
            iniciarCooldown(data.segundosRestantes || 300);
            return;
        }

        // ✅ Ataque OK
        if (data.damage !== undefined) {
            document.getElementById("damage").innerText =
                'Dano - ' + data.damage;

            showDamageFloating(data.damage);

            // Inicia cooldown com o valor do backend (ou 300s)
            iniciarCooldown(data.segundosRestantes || 300);
        }

        if (data.message) {
            console.log("Boss:", data.message);
        }

    } catch (err) {
        console.error(err);

        // Em caso de erro, libera o botão
        attackBtn.disabled = false;
        attackBtn.style.backgroundColor = "#f39c12";
    }
});

async function verificarCooldownInicial() {
    if (!usuarioId) return;

    try {
        const res = await fetch(`/api/boss/cooldown?usuarioId=${usuarioId}`);
        const data = await res.json();

        if (!data.podeAtacar) {
           // iniciarCooldown(data.segundosRestantes);
			iniciarCooldown(data.segundosRestantes || 300);
        } else {
            attackBtn.disabled = false;
            attackBtn.innerText = "Atacar Boss";
            attackBtn.style.backgroundColor = "#f39c12"; 
        }

    } catch (e) {
        console.error("Erro ao verificar cooldown", e);
    }
}

// 🚀 roda assim que a página carrega
verificarCooldownInicial();
 */

