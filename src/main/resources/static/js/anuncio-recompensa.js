
let tempoAnuncio = 20;
let intervaloAnuncio = null;
let intervaloCooldown = null;

let anuncioLiberado = false;
let recompensaRecebida = false;

const STATUS_CACHE_KEY = "anuncio_status_cache";
const STATUS_CACHE_TTL = 30000;

const btnAbrirAnuncio = document.getElementById("btnAbrirAnuncio");
const contadorAnuncio = document.getElementById("contadorAnuncio");
const btnReceberRecompensa = document.getElementById("btnReceberRecompensaAnuncio");

const resultadoBox = document.getElementById("resultadoRecompensa");
const imgRecompensa = document.getElementById("imgRecompensa");
const textoRecompensa = document.getElementById("textoRecompensa");
const textoStreak = document.getElementById("textoStreak");

document.addEventListener("DOMContentLoaded", () => {

    if (btnAbrirAnuncio) {
        aplicarStatusCacheAnuncio();
        verificarLimiteDiarioAnuncio();

        btnAbrirAnuncio.addEventListener("click", () => {
            if (btnAbrirAnuncio.disabled) return;

            const usuarioId = document.querySelector('meta[name="user-id"]')?.content;

            if (!usuarioId) {
                alert("Usuário não encontrado.");
                return;
            }

            localStorage.setItem("paginaAntesDoAnuncio", window.location.href);

            window.location.href = `/anuncio-recompensa?usuarioId=${usuarioId}`;
        });
    }

    if (contadorAnuncio && btnReceberRecompensa) {
        iniciarContadorPagina();
        btnReceberRecompensa.addEventListener("click", receberRecompensaAnuncio);
    }
});

function salvarStatusCacheAnuncio(dados) {
    if (!dados) return;

    localStorage.setItem(STATUS_CACHE_KEY, JSON.stringify({
        time: Date.now(),
        data: dados
    }));
}

function pegarStatusCacheAnuncio() {
    try {
        const cache = JSON.parse(localStorage.getItem(STATUS_CACHE_KEY));

        if (!cache) return null;

        if (Date.now() - cache.time > STATUS_CACHE_TTL) {
            localStorage.removeItem(STATUS_CACHE_KEY);
            return null;
        }

        return cache.data;

    } catch {
        return null;
    }
}

function aplicarStatusCacheAnuncio() {
    const cache = pegarStatusCacheAnuncio();

    if (!cache || !btnAbrirAnuncio) {
        iniciarControleAnuncio();
        return;
    }

    if (cache.limiteAtingido) {
        clearInterval(intervaloCooldown);
        clearInterval(intervaloAnuncio);
        btnAbrirAnuncio.style.display = "none";
        return;
    }

    iniciarControleAnuncio();
}

function gerarCooldownAleatorio() {
    const min = 300;
    const max = 480;
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function salvarNovoCooldown() {
    const cooldownTempo = gerarCooldownAleatorio();

    localStorage.setItem("cooldownTempo", cooldownTempo);
    localStorage.setItem("ultimoAnuncioTempo", Date.now());
}

function iniciarControleAnuncio() {
    if (!btnAbrirAnuncio) return;

    clearInterval(intervaloCooldown);

    btnAbrirAnuncio.style.display = "block";
    btnAbrirAnuncio.classList.remove("bloqueado");

    let tempoSalvo = parseInt(localStorage.getItem("cooldownTempo"));

    if (!tempoSalvo) {
        tempoSalvo = gerarCooldownAleatorio();
        localStorage.setItem("cooldownTempo", tempoSalvo);
    }

    const ultimoUso = localStorage.getItem("ultimoAnuncioTempo");

    if (!ultimoUso) {
        liberarBotaoAnuncio();
        return;
    }

    const agora = Date.now();
    const diff = Math.floor((agora - parseInt(ultimoUso)) / 1000);

    if (diff >= tempoSalvo) {
        liberarBotaoAnuncio();
    } else {
        iniciarCooldownAnuncio(tempoSalvo - diff);
    }
}

function iniciarCooldownAnuncio(segundosRestantes) {
    if (!btnAbrirAnuncio) return;

    btnAbrirAnuncio.style.display = "block";
    btnAbrirAnuncio.disabled = true;
    btnAbrirAnuncio.classList.add("bloqueado");

    clearInterval(intervaloCooldown);

    atualizarTextoCooldown(segundosRestantes);

    intervaloCooldown = setInterval(() => {
        segundosRestantes--;

        atualizarTextoCooldown(segundosRestantes);

        if (segundosRestantes <= 0) {
            clearInterval(intervaloCooldown);
            liberarBotaoAnuncio();
        }
    }, 1000);
}

function atualizarTextoCooldown(segundosRestantes) {
    if (!btnAbrirAnuncio) return;

    const min = Math.floor(segundosRestantes / 60);
    const seg = segundosRestantes % 60;

    btnAbrirAnuncio.textContent = `⏳ ${min}m ${seg.toString().padStart(2, "0")}s`;
}

function liberarBotaoAnuncio() {
    if (!btnAbrirAnuncio) return;

    btnAbrirAnuncio.style.display = "block";
    btnAbrirAnuncio.disabled = false;
    btnAbrirAnuncio.classList.remove("bloqueado");
    btnAbrirAnuncio.textContent = "🎁 Ver anúncio e ganhar recompensa";
}

async function verificarLimiteDiarioAnuncio() {
    const usuarioId = document.querySelector('meta[name="user-id"]')?.content;

    if (!usuarioId || !btnAbrirAnuncio) return;

    try {
        const res = await fetch(`/api/anuncio-recompensa/status/${usuarioId}`, {
            cache: "no-store"
        });

        const dados = await res.json();

        salvarStatusCacheAnuncio(dados);

        if (dados.limiteAtingido) {
            clearInterval(intervaloCooldown);
            clearInterval(intervaloAnuncio);

            btnAbrirAnuncio.style.display = "none";
            return;
        }

        iniciarControleAnuncio();

    } catch (e) {
        console.error("Erro ao verificar status:", e);
        iniciarControleAnuncio();
    }
}

function iniciarContadorPagina() {
    tempoAnuncio = 20;
    anuncioLiberado = false;
    recompensaRecebida = false;

    contadorAnuncio.textContent = tempoAnuncio;

    btnReceberRecompensa.disabled = true;
    btnReceberRecompensa.textContent = "Aguarde...";

    if (resultadoBox) {
        resultadoBox.style.display = "none";
    }

    clearInterval(intervaloAnuncio);

    intervaloAnuncio = setInterval(() => {
        tempoAnuncio--;
        contadorAnuncio.textContent = tempoAnuncio;

        if (tempoAnuncio <= 0) {
            clearInterval(intervaloAnuncio);

            anuncioLiberado = true;
            btnReceberRecompensa.disabled = false;
            btnReceberRecompensa.textContent = "🎁 Receber recompensa";
        }
    }, 1000);
}

async function receberRecompensaAnuncio() {
    if (!anuncioLiberado) {
        alert("Aguarde o anúncio.");
        return;
    }

    if (recompensaRecebida) {
        alert("Já recebeu.");
        return;
    }

    const usuarioId = document.querySelector('meta[name="user-id"]')?.content;

    if (!usuarioId) {
        alert("Usuário não encontrado.");
        return;
    }

    btnReceberRecompensa.disabled = true;
    btnReceberRecompensa.textContent = "Processando...";

    try {
        const resposta = await fetch(`/api/anuncio-recompensa/receber/${usuarioId}`, {
            method: "POST"
        });

        const dados = await resposta.json();

        if (dados.success) {
            recompensaRecebida = true;

            salvarNovoCooldown();
            localStorage.removeItem(STATUS_CACHE_KEY);

            if (resultadoBox) resultadoBox.style.display = "block";

            if (imgRecompensa) {
                imgRecompensa.src = dados.ganhouItem && dados.imagemItem
                    ? "/" + dados.imagemItem
                    : "/icones/boss_coin.webp";
            }

            if (textoRecompensa) textoRecompensa.textContent = dados.descricao;

            if (textoStreak) {
                textoStreak.textContent = `🔥 Streak: ${dados.streakAtual}/15`;
            }

            btnReceberRecompensa.textContent = "✅ Recebido";

            mostrarValorRecebidoAnimado(`+${dados.descricao} `);

            if (dados.ganhouItem && dados.streakAtual >= 15) {
                animacaoBonusEspecial15();
            }

            setTimeout(() => {
                voltarPaginaAnterior();
            }, 8000);

        } else {
            mostrarErroAnuncio(dados.message || "Não foi possível resgatar a recompensa.");
        }

    } catch (e) {
        console.error(e);
        mostrarErroAnuncio("Erro ao conectar com o servidor.");
    }
}

function voltarPaginaAnterior() {
    const paginaAnterior = localStorage.getItem("paginaAntesDoAnuncio");

    if (paginaAnterior) {
        localStorage.removeItem("paginaAntesDoAnuncio");
        window.location.href = paginaAnterior;
        return;
    }

    window.location.href = "/dashboard";
}

function voltarJogo() {
    voltarPaginaAnterior();
}

function mostrarErroAnuncio(mensagem) {
    if (typeof Swal !== "undefined") {
        Swal.fire({
            icon: "error",
            title: mensagem,
            timer: 8000,
            showConfirmButton: false,
            background: "#111827",
            color: "#fff"
        });
    } else {
        alert(mensagem);
    }

    btnReceberRecompensa.disabled = false;
    btnReceberRecompensa.textContent = "Tentar novamente";
}

function mostrarValorRecebidoAnimado(valor) {
    const animacao = document.createElement("div");

    animacao.className = "animacao-recompensa-flutuante";
    animacao.textContent = `${valor}`;

    document.body.appendChild(animacao);

    setTimeout(() => {
        animacao.remove();
    }, 2600);
}

function animacaoBonusEspecial15() {
    if (typeof Swal === "undefined") return;

    Swal.fire({
        title: "🎁 BÔNUS ESPECIAL!",
        html: `
            <div class="bonus-especial-15">
                <div class="bau-bonus">🎁</div>
                <p>Você completou os 15 anúncios!</p>
            </div>
        `,
        background: "#000",
        color: "#ffd700",
        showConfirmButton: false,
        timer: 5000
    });
}

/**
 * 
 * let tempoAnuncio = 20;
let intervaloAnuncio = null;
let intervaloCooldown = null;

let anuncioLiberado = false;
let recompensaRecebida = false;

const btnAbrirAnuncio = document.getElementById("btnAbrirAnuncio");
const contadorAnuncio = document.getElementById("contadorAnuncio");
const btnReceberRecompensa = document.getElementById("btnReceberRecompensaAnuncio");

const resultadoBox = document.getElementById("resultadoRecompensa");
const imgRecompensa = document.getElementById("imgRecompensa");
const textoRecompensa = document.getElementById("textoRecompensa");
const textoStreak = document.getElementById("textoStreak");

// ===============================
// INIT
// ===============================

document.addEventListener("DOMContentLoaded", () => {

    // DASHBOARD / PÁGINA ORIGINAL
    if (btnAbrirAnuncio) {
        verificarLimiteDiarioAnuncio();

        btnAbrirAnuncio.addEventListener("click", () => {
            if (btnAbrirAnuncio.disabled) return;

            const usuarioId = document.querySelector('meta[name="user-id"]')?.content;

            if (!usuarioId) {
                alert("Usuário não encontrado.");
                return;
            }

            localStorage.setItem("paginaAntesDoAnuncio", window.location.href);

            window.location.href = `/anuncio-recompensa?usuarioId=${usuarioId}`;
        });
    }

    // PÁGINA DO ANÚNCIO
    if (contadorAnuncio && btnReceberRecompensa) {
        iniciarContadorPagina();

        btnReceberRecompensa.addEventListener("click", receberRecompensaAnuncio);
    }
});

// ===============================
// COOLDOWN DO BOTÃO PRINCIPAL
// ===============================

function gerarCooldownAleatorio() {
    const min = 300;
    const max = 480;
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function salvarNovoCooldown() {
    const cooldownTempo = gerarCooldownAleatorio();
    localStorage.setItem("cooldownTempo", cooldownTempo);
    localStorage.setItem("ultimoAnuncioTempo", Date.now());
}

function iniciarControleAnuncio() {
    if (!btnAbrirAnuncio) return;

    clearInterval(intervaloCooldown);

    btnAbrirAnuncio.style.display = "block";
    btnAbrirAnuncio.classList.remove("bloqueado");

    let tempoSalvo = parseInt(localStorage.getItem("cooldownTempo"));

    if (!tempoSalvo) {
        tempoSalvo = gerarCooldownAleatorio();
        localStorage.setItem("cooldownTempo", tempoSalvo);
    }

    const ultimoUso = localStorage.getItem("ultimoAnuncioTempo");

    if (!ultimoUso) {
        liberarBotaoAnuncio();
        return;
    }

    const agora = Date.now();
    const diff = Math.floor((agora - parseInt(ultimoUso)) / 1000);

    if (diff >= tempoSalvo) {
        liberarBotaoAnuncio();
    } else {
        iniciarCooldownAnuncio(tempoSalvo - diff);
    }
}

function iniciarCooldownAnuncio(segundosRestantes) {
    if (!btnAbrirAnuncio) return;

    btnAbrirAnuncio.style.display = "block";
    btnAbrirAnuncio.disabled = true;
    btnAbrirAnuncio.classList.add("bloqueado");

    clearInterval(intervaloCooldown);

    intervaloCooldown = setInterval(() => {
        segundosRestantes--;

        const min = Math.floor(segundosRestantes / 60);
        const seg = segundosRestantes % 60;

        btnAbrirAnuncio.textContent = `⏳ ${min}m ${seg.toString().padStart(2, "0")}s`;

        if (segundosRestantes <= 0) {
            clearInterval(intervaloCooldown);
            liberarBotaoAnuncio();
        }
    }, 1000);
}

function liberarBotaoAnuncio() {
    if (!btnAbrirAnuncio) return;

    btnAbrirAnuncio.style.display = "block";
    btnAbrirAnuncio.disabled = false;
    btnAbrirAnuncio.classList.remove("bloqueado");
    btnAbrirAnuncio.textContent = "🎁 Ver anúncio e ganhar recompensa";
}

// ===============================
// LIMITE DIÁRIO
// ===============================

async function verificarLimiteDiarioAnuncio() {
    const usuarioId = document.querySelector('meta[name="user-id"]')?.content;

    if (!usuarioId || !btnAbrirAnuncio) return;

    try {
        const res = await fetch(`/api/anuncio-recompensa/status/${usuarioId}`);
        const dados = await res.json();

        if (dados.limiteAtingido) {
            clearInterval(intervaloCooldown);
            clearInterval(intervaloAnuncio);

            btnAbrirAnuncio.style.display = "none";
            return;
        }

        iniciarControleAnuncio();

    } catch (e) {
        console.error("Erro ao verificar status:", e);
        iniciarControleAnuncio();
    }
}

// ===============================
// CONTADOR DA PÁGINA
// ===============================

function iniciarContadorPagina() {
    tempoAnuncio = 20;
    anuncioLiberado = false;
    recompensaRecebida = false;

    contadorAnuncio.textContent = tempoAnuncio;

    btnReceberRecompensa.disabled = true;
    btnReceberRecompensa.textContent = "Aguarde...";

    if (resultadoBox) {
        resultadoBox.style.display = "none";
    }

    clearInterval(intervaloAnuncio);

    intervaloAnuncio = setInterval(() => {
        tempoAnuncio--;
        contadorAnuncio.textContent = tempoAnuncio;

        if (tempoAnuncio <= 0) {
            clearInterval(intervaloAnuncio);

            anuncioLiberado = true;
            btnReceberRecompensa.disabled = false;
            btnReceberRecompensa.textContent = "🎁 Receber recompensa";
        }
    }, 1000);
}

// ===============================
// RECEBER RECOMPENSA
// ===============================

async function receberRecompensaAnuncio() {
    if (!anuncioLiberado) {
        alert("Aguarde o anúncio.");
        return;
    }

    if (recompensaRecebida) {
        alert("Já recebeu.");
        return;
    }

    const usuarioId = document.querySelector('meta[name="user-id"]')?.content;

    if (!usuarioId) {
        alert("Usuário não encontrado.");
        return;
    }

    btnReceberRecompensa.disabled = true;
    btnReceberRecompensa.textContent = "Processando...";

    try {
        const resposta = await fetch(`/api/anuncio-recompensa/receber/${usuarioId}`, {
            method: "POST"
        });

        const dados = await resposta.json();

        if (dados.success) {
            recompensaRecebida = true;

            salvarNovoCooldown();

            if (resultadoBox) {
                resultadoBox.style.display = "block";
            }

            if (imgRecompensa) {
                imgRecompensa.src = dados.ganhouItem && dados.imagemItem
                    ? "/" + dados.imagemItem
                    : "/icones/boss_coin.webp";
            }

            if (textoRecompensa) {
                textoRecompensa.textContent = dados.descricao;
            }

            if (textoStreak) {
                textoStreak.textContent = `🔥 Streak: ${dados.streakAtual}/15`;
            }

            btnReceberRecompensa.textContent = "✅ Recebido";
			mostrarValorRecebidoAnimado(`+${dados.descricao} `);

            if (dados.ganhouItem && dados.streakAtual >= 15) {
                animacaoBonusEspecial15();
            }

            setTimeout(() => {
                voltarPaginaAnterior();
            }, 8000);

        } else {
            mostrarErroAnuncio(dados.message || "Não foi possível resgatar a recompensa.");
        }

    } catch (e) {
        console.error(e);
        mostrarErroAnuncio("Erro ao conectar com o servidor.");
    }
}

// ===============================
// VOLTAR
// ===============================

function voltarPaginaAnterior() {
    const paginaAnterior = localStorage.getItem("paginaAntesDoAnuncio");

    if (paginaAnterior) {
        localStorage.removeItem("paginaAntesDoAnuncio");
        window.location.href = paginaAnterior;
        return;
    }

    window.location.href = "/dashboard";
}

function voltarJogo() {
    voltarPaginaAnterior();
}

// ===============================
// ERRO
// ===============================

function mostrarErroAnuncio(mensagem) {
    if (typeof Swal !== "undefined") {
        Swal.fire({
            icon: "error",
            title: mensagem,
            timer: 8000,
            showConfirmButton: false,
            background: "#111827",
            color: "#fff"
        });
    } else {
        alert(mensagem);
    }

    btnReceberRecompensa.disabled = false;
    btnReceberRecompensa.textContent = "Tentar novamente";
}

// ANIMAÇÃO
function mostrarValorRecebidoAnimado(valor) {

    const animacao = document.createElement("div");

    animacao.className = "animacao-recompensa-flutuante";

    animacao.textContent = `${valor}`;

    document.body.appendChild(animacao);

    setTimeout(() => {
        animacao.remove();
    }, 2600);
}


// ===============================
// ANIMAÇÃO ESPECIAL
// ===============================

function animacaoBonusEspecial15() {
    if (typeof Swal === "undefined") return;

    Swal.fire({
        title: "🎁 BÔNUS ESPECIAL!",
        html: `
            <div class="bonus-especial-15">
                <div class="bau-bonus">🎁</div>
                <p>Você completou os 15 anúncios!</p>
            </div>
        `,
        background: "#000",
        color: "#ffd700",
        showConfirmButton: false,
        timer: 5000
    });
}
 */