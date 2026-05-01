
let indiceAnuncioAtual = Number(localStorage.getItem("indiceAnuncioAtual") || 0);

const anunciosDisponiveis = [
    { key: "d594127d103d7640e87b4b11c19bbb17", width: 320, height: 50 },
    { key: "0a3b52a54a7a4d065b759366194e5a20", width: 468, height: 60 }
];

let tempoAnuncio = 20;
let intervaloAnuncio = null;
let intervaloCooldown = null;

let anuncioLiberado = false;
let recompensaRecebida = false;

// cooldown inicial
let cooldownTempo = parseInt(localStorage.getItem("cooldownTempo"));
if (!cooldownTempo) {
    cooldownTempo = gerarCooldownAleatorio();
    localStorage.setItem("cooldownTempo", cooldownTempo);
}

// elementos
const btnAbrirAnuncio = document.getElementById("btnAbrirAnuncio");
const modalAnuncio = document.getElementById("modalAnuncioRecompensa");
const fecharModalAnuncio = document.getElementById("fecharModalAnuncio");
const contadorAnuncio = document.getElementById("contadorAnuncio");
const btnReceberRecompensa = document.getElementById("btnReceberRecompensaAnuncio");

const resultadoBox = document.getElementById("resultadoRecompensa");
const imgRecompensa = document.getElementById("imgRecompensa");
const textoRecompensa = document.getElementById("textoRecompensa");
const textoStreak = document.getElementById("textoStreak");

btnAbrirAnuncio?.addEventListener("click", abrirModalAnuncio);
fecharModalAnuncio?.addEventListener("click", fecharModal);

// =====================================
// COOLDOWN
// =====================================

function gerarCooldownAleatorio() {
    const min = 300;
    const max = 480;
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function salvarNovoCooldown() {
    cooldownTempo = gerarCooldownAleatorio();
    localStorage.setItem("cooldownTempo", cooldownTempo);
    localStorage.setItem("ultimoAnuncioTempo", Date.now());
}

function iniciarControleAnuncio() {
    if (!btnAbrirAnuncio) return;

	
    clearInterval(intervaloCooldown);
     // 🔥 IMPORTANTE: reset visual antes de aplicar cooldown
	btnAbrirAnuncio.classList.remove("bloqueado");
    let tempoSalvo = parseInt(localStorage.getItem("cooldownTempo"));

    if (!tempoSalvo) {
        tempoSalvo = gerarCooldownAleatorio();
        localStorage.setItem("cooldownTempo", tempoSalvo);
    }

    cooldownTempo = tempoSalvo;

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

    btnAbrirAnuncio.style.display = "block"; // 🔥 GARANTE VISIBILIDADE
    btnAbrirAnuncio.disabled = true;

    clearInterval(intervaloCooldown);

    intervaloCooldown = setInterval(() => {
        segundosRestantes--;

        const min = Math.floor(segundosRestantes / 60);
        const seg = segundosRestantes % 60;

        btnAbrirAnuncio.textContent = `⏳ ${min}m ${seg.toString().padStart(2, '0')}s`;

        if (segundosRestantes <= 0) {
            clearInterval(intervaloCooldown);
            liberarBotaoAnuncio();
        }
    }, 1000);
}
function liberarBotaoAnuncio() {
    if (!btnAbrirAnuncio) return;

    btnAbrirAnuncio.style.display = "block"; // 🔥 ESSENCIAL
    btnAbrirAnuncio.disabled = false;
    btnAbrirAnuncio.textContent = "🎁 Ver anúncio e ganhar recompensa";
}

// =====================================
// MODAL / ANÚNCIO
// =====================================
function carregarAnuncioAlternado() {

    const areaBanner = document.getElementById("areaBannerAnuncio");
    if (!areaBanner) return;

    const anuncio = anunciosDisponiveis[indiceAnuncioAtual];

    // limpa tudo
    areaBanner.innerHTML = "";

    // cria container
    const container = document.createElement("div");
    container.style.width = anuncio.width + "px";
    container.style.height = anuncio.height + "px";

    areaBanner.appendChild(container);

    // define config GLOBAL
    window.atOptions = {
        key: anuncio.key,
        format: "iframe",
        height: anuncio.height,
        width: anuncio.width,
        params: {}
    };

    // 🔥 IMPORTANTE: usar setTimeout
    setTimeout(() => {
        const script = document.createElement("script");
        script.src = `https://www.highperformanceformat.com/${anuncio.key}/invoke.js`;
        script.async = true;

        container.appendChild(script);
    }, 100);

    // alterna
    indiceAnuncioAtual = (indiceAnuncioAtual + 1) % anunciosDisponiveis.length;
    localStorage.setItem("indiceAnuncioAtual", indiceAnuncioAtual);
}

function abrirModalAnuncio() {
	abrirPopunderRecompensa(); // 🔥 dinheiro vem daqui
    if (btnAbrirAnuncio.disabled) return;

    modalAnuncio.style.display = "flex";

    carregarAnuncioAlternado();

    tempoAnuncio = 20;
    anuncioLiberado = false;
    recompensaRecebida = false;

    contadorAnuncio.textContent = tempoAnuncio;

    btnReceberRecompensa.disabled = true;
    btnReceberRecompensa.textContent = "Aguarde...";

    resultadoBox.style.display = "none";

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

function fecharModal() {
    modalAnuncio.style.display = "none";
    clearInterval(intervaloAnuncio);
    anuncioLiberado = false;
    recompensaRecebida = false;
}

// =====================================
// RECEBER RECOMPENSA
// =====================================

btnReceberRecompensa?.addEventListener("click", async () => {
	abrirPopunderRecompensa(); // 🔥 dinheiro vem daqui
    if (!anuncioLiberado) return alert("Aguarde o anúncio.");
    if (recompensaRecebida) return alert("Já recebeu.");

    const usuarioId = document.querySelector('meta[name="user-id"]')?.content;
    if (!usuarioId) return alert("Usuário não encontrado.");

    abrirPopunderRecompensa();

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
			await verificarLimiteDiarioAnuncio(); // ✔ CORRETO
			
            resultadoBox.style.display = "block";

            imgRecompensa.src = dados.ganhouItem && dados.imagemItem
                ? "/" + dados.imagemItem
                : "/icones/boss_coin.webp";

            textoRecompensa.textContent = dados.descricao;
            textoStreak.textContent = `🔥 Streak: ${dados.streakAtual}/15`;

          //  atualizarSaldoBossCoins?.();
            //
            btnReceberRecompensa.textContent = "✅ Recebido";

            // 🎁 animação especial
            if (dados.ganhouItem && dados.streakAtual >= 15) {
                animacaoBonusEspecial15();
            }

            setTimeout(fecharModal, 8000);

        } else {
          //  alert(dados.message);
			Swal.fire({
			           customClass: {
			               title: 'swal-game-error'
			           },
			           icon: 'error',
			           title: dados.message,
			           html: `
			               <div style="margin-bottom:10px;">
			                   Não foi possível resgatar a recompensa de ataques.
			               </div>

			               <div class="modal-anuncio">
			                   <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
			                       width="468"
			                       height="60"
			                       scrolling="no"
			                       frameborder="0"
			                       style="max-width:100%; border:0;">
			                   </iframe>
			               </div>
			           `,
			           timer: 8000,
			           showConfirmButton: false,
			           background: 'transparent',
			           color: '#ff3b3b'
			       });
            fecharModal();
            iniciarControleAnuncio();
        }

    } catch (e) {
        console.error(e);
       // alert("Erro servidor");
		
		Swal.fire({
		           customClass: {
		               title: 'swal-game-error'
		           },
		           icon: 'error',
		           title: 'Erro',
		           html: `
		               <div style="margin-bottom:10px;">
		                   Não foi possível resgatar a recompensa.
		               </div>

		               <div class="modal-anuncio">
		                   <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
		                       width="468"
		                       height="60"
		                       scrolling="no"
		                       frameborder="0"
		                       style="max-width:100%; border:0;">
		                   </iframe>
		               </div>
		           `,
		           timer: 8000,
		           showConfirmButton: false,
		           background: 'transparent',
		           color: '#ff3b3b'
		       });
        fecharModal();
        iniciarControleAnuncio();
    }
});

// =====================================
// POPUNDER + ANIMAÇÃO
// =====================================

function abrirPopunderRecompensa() {

    const script = document.createElement("script");
    script.src = "https://pl29305331.profitablecpmratenetwork.com/ae/8d/d0/ae8dd0216346a9ae71a7abd7fae5d760.js";
    script.async = true;

    document.body.appendChild(script);
}
function animacaoBonusEspecial15() {
    Swal.fire({
        title: '🎁 BÔNUS ESPECIAL!',
        html: `<div class="bonus-especial-15">
                <div class="bau-bonus">🎁</div>
                <p>Você completou os 15 anúncios!</p>
              </div>`,
        background: '#000',
        color: '#ffd700',
        showConfirmButton: false,
        timer: 5000
    });
}


// =====================================
// INIT
// =====================================
let intervaloResetDiario = null;

async function verificarLimiteDiarioAnuncio() {

    const usuarioId = document.querySelector('meta[name="user-id"]')?.content;

    if (!usuarioId || !btnAbrirAnuncio) return;

    try {
        const res = await fetch(`/api/anuncio-recompensa/status/${usuarioId}`);
        const dados = await res.json();

        if (dados.limiteAtingido) {

            // 🔥 mata TODOS os intervalos
            clearInterval(intervaloCooldown);
            clearInterval(intervaloAnuncio);
            clearInterval(intervaloResetDiario);

            // 🔥 some com o botão
            btnAbrirAnuncio.style.display = "none";

            return;
        }

        // 🔥 normal
        btnAbrirAnuncio.style.display = "block";
        btnAbrirAnuncio.disabled = false;

        iniciarControleAnuncio();

    } catch (e) {
        console.error("Erro ao verificar status:", e);
        iniciarControleAnuncio();
    }
}

//iniciarControleAnuncio();
verificarLimiteDiarioAnuncio();

/*
let indiceAnuncioAtual = Number(localStorage.getItem("indiceAnuncioAtual") || 0);

const anunciosDisponiveis = [
    {
        key: "d594127d103d7640e87b4b11c19bbb17", // 320x50
        width: 320,
        height: 50
    },
    {
        key: "0a3b52a54a7a4d065b759366194e5a20", // 468x60
        width: 468,
        height: 60
    }
];
let tempoAnuncio = 20;
let intervaloAnuncio = null;
let anuncioLiberado = false;
let recompensaRecebida = false;

//let cooldownTempo = 300; // 5 minutos

let intervaloCooldown = null;
//let ultimoUso = localStorage.getItem("ultimoAnuncioTempo");

let cooldownTempo = parseInt(localStorage.getItem("cooldownTempo")) || gerarCooldownAleatorio();

const btnAbrirAnuncio = document.getElementById("btnAbrirAnuncio");
const modalAnuncio = document.getElementById("modalAnuncioRecompensa");
const fecharModalAnuncio = document.getElementById("fecharModalAnuncio");
const contadorAnuncio = document.getElementById("contadorAnuncio");
const btnReceberRecompensa = document.getElementById("btnReceberRecompensaAnuncio");

const resultadoBox = document.getElementById("resultadoRecompensa");
const imgRecompensa = document.getElementById("imgRecompensa");
const textoRecompensa = document.getElementById("textoRecompensa");
const textoStreak = document.getElementById("textoStreak");

btnAbrirAnuncio?.addEventListener("click", abrirModalAnuncio);
fecharModalAnuncio?.addEventListener("click", fecharModal);

function iniciarControleAnuncio() {
    if (!btnAbrirAnuncio) return;

    clearInterval(intervaloCooldown);

   // ultimoUso = localStorage.getItem("ultimoAnuncioTempo");
	const ultimoUso = localStorage.getItem("ultimoAnuncioTempo");

    if (!ultimoUso) {
        liberarBotaoAnuncio();
        return;
    }

    const agora = Date.now();
    const diff = Math.floor((agora - parseInt(ultimoUso)) / 1000);

    if (diff >= cooldownTempo) {
        liberarBotaoAnuncio();
    } else {
        iniciarCooldownAnuncio(cooldownTempo - diff);
    }
}

//-----------------------------------

function gerarCooldownAleatorio() {
    const min = 300; // 5 min
    const max = 480; // 8 min

    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function salvarNovoCooldown() {
    cooldownTempo = gerarCooldownAleatorio();

    localStorage.setItem("cooldownTempo", cooldownTempo);
    localStorage.setItem("ultimoAnuncioTempo", Date.now());

    return cooldownTempo;
}

function iniciarCooldownAnuncio(segundosRestantes) {
    if (!btnAbrirAnuncio) return;

    btnAbrirAnuncio.style.display = "block";
    btnAbrirAnuncio.disabled = true;

    clearInterval(intervaloCooldown);

    intervaloCooldown = setInterval(() => {
        segundosRestantes--;

        const min = Math.floor(segundosRestantes / 60);
        const seg = segundosRestantes % 60;

        btnAbrirAnuncio.textContent = `⏳ ${min}:${seg.toString().padStart(2, '0')}`;

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
    btnAbrirAnuncio.textContent = "🎁 Ver anúncio e ganhar recompensa";
}

function carregarAnuncioAlternado() {
    const areaBanner = document.getElementById("areaBannerAnuncio");
    if (!areaBanner) return;

    areaBanner.innerHTML = "";

    const anuncio = anunciosDisponiveis[indiceAnuncioAtual];

    areaBanner.className = "area-banner-anuncio";

    if (anuncio.width === 320 && anuncio.height === 50) {
        areaBanner.classList.add("banner-320x50");
    }

    if (anuncio.width === 468 && anuncio.height === 60) {
        areaBanner.classList.add("banner-468x60");
    }

    window.atOptions = {
        key: anuncio.key,
        format: "iframe",
        height: anuncio.height,
        width: anuncio.width,
        params: {}
    };

    const script = document.createElement("script");
    script.src = `https://www.highperformanceformat.com/${anuncio.key}/invoke.js`;
    script.async = false;

    areaBanner.appendChild(script);

    indiceAnuncioAtual++;

    if (indiceAnuncioAtual >= anunciosDisponiveis.length) {
        indiceAnuncioAtual = 0;
    }

    localStorage.setItem("indiceAnuncioAtual", indiceAnuncioAtual);
}
function abrirModalAnuncio() {
    if (!modalAnuncio || !btnAbrirAnuncio || btnAbrirAnuncio.disabled) return;

    modalAnuncio.style.display = "flex";

    carregarAnuncioAlternado();

    tempoAnuncio = 20;
    anuncioLiberado = false;
    recompensaRecebida = false;

    if (contadorAnuncio) contadorAnuncio.textContent = tempoAnuncio;

    if (btnReceberRecompensa) {
        btnReceberRecompensa.disabled = true;
        btnReceberRecompensa.textContent = "Aguarde...";
    }

    if (resultadoBox) resultadoBox.style.display = "none";
    if (imgRecompensa) imgRecompensa.src = "";
    if (textoRecompensa) textoRecompensa.textContent = "";
    if (textoStreak) textoStreak.textContent = "";

    clearInterval(intervaloAnuncio);

    intervaloAnuncio = setInterval(() => {
        tempoAnuncio--;

        if (contadorAnuncio) contadorAnuncio.textContent = tempoAnuncio;

        if (tempoAnuncio <= 0) {
            clearInterval(intervaloAnuncio);
            anuncioLiberado = true;

            if (btnReceberRecompensa) {
                btnReceberRecompensa.disabled = false;
                btnReceberRecompensa.textContent = "🎁 Receber recompensa";
            }
        }
    }, 1000);
}

function fecharModal() {
    if (modalAnuncio) {
        modalAnuncio.style.display = "none";
    }

    clearInterval(intervaloAnuncio);

    anuncioLiberado = false;
    recompensaRecebida = false;
}

btnReceberRecompensa?.addEventListener("click", async () => {

    if (!anuncioLiberado) {
        alert("Aguarde o tempo do anúncio terminar.");
        return;
    }

    if (recompensaRecebida) {
        alert("Você já recebeu essa recompensa.");
        fecharModal();
        return;
    }

    const usuarioId = document.querySelector('meta[name="user-id"]')?.content;

    if (!usuarioId) {
        alert("Usuário não encontrado.");
        fecharModal();
        return;
    }

    abrirPopunderRecompensa();

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
		    iniciarControleAnuncio();

		    if (resultadoBox) resultadoBox.style.display = "block";

		    if (imgRecompensa) {
		        imgRecompensa.src = dados.ganhouItem && dados.imagemItem
		            ? "/" + dados.imagemItem
		            : "/icones/boss_coin.webp";
		    }

		    if (textoRecompensa) {
		        textoRecompensa.textContent = dados.descricao || "Recompensa recebida!";
		    }

		    if (textoStreak) {
		        textoStreak.textContent = `🔥 Streak: ${dados.streakAtual}/15`;
		    }

		    if (typeof atualizarSaldoBossCoins === "function") {
		        atualizarSaldoBossCoins();
		    }

		    if (typeof carregarSaldo === "function") {
		        carregarSaldo();
		    }

		    btnReceberRecompensa.textContent = "✅ Recebido";

		    if (dados.ganhouItem && dados.streakAtual >= 15) {
		        animacaoBonusEspecial15();
		    }

		    setTimeout(fecharModal, 8000);
		}else {
            alert(dados.message || "Erro ao receber recompensa.");
            fecharModal();
            iniciarControleAnuncio();
        }

    } catch (erro) {
        console.error("Erro:", erro);
        alert("Erro ao conectar com servidor.");
        fecharModal();
        iniciarControleAnuncio();
    }
});


function abrirPopunderRecompensa() {
    const script = document.createElement("script");
    script.src = "https://pl29305331.profitablecpmratenetwork.com/ae/8d/d0/ae8dd0216346a9ae71a7abd7fae5d760.js";
    script.async = true;

    document.body.appendChild(script);
}
function animacaoBonusEspecial15() {

  Swal.fire({
    title: '🎁 BÔNUS ESPECIAL!',
    html: `
      <div class="bonus-especial-15">
        <div class="bau-bonus">🎁</div>
        <p>Você completou os 15 anúncios de hoje!</p>
        <p class="texto-premio">Recompensa especial desbloqueada!</p>
      </div>
    `,
    background: 'rgba(0, 0, 0, 0.85)',
    color: '#ffd700',
    showConfirmButton: false,
    timer: 5000,
    customClass: {
      popup: 'popup-bonus-especial',
      title: 'titulo-bonus-especial'
    }
  });

}

iniciarControleAnuncio();
*/