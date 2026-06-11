document.addEventListener("DOMContentLoaded", () => {
    iniciarPaginaCacadorRecompensas();
});

let intervaloAnuncioRecompensa = null;
let intervaloRuinasPerdidas = null;

async function iniciarPaginaCacadorRecompensas() {
  //  iniciarCardAnuncioRecompensa();
    iniciarCardRuinasPerdidas();
    iniciarCardMasmorraSombria();
    iniciarCardFonteVigor();
}

/* =========================================================
   CARD 1 - ANÚNCIO RECOMPENSA


async function iniciarCardAnuncioRecompensa() {
    const textoStatus = document.getElementById("textoStatusRecompensa");
    const contador = document.getElementById("contadorCooldownRecompensa");
    const barra = document.getElementById("barraCooldownRecompensa");
    const btnIr = document.getElementById("btnIrAnuncio");
    const btnBloqueado = document.getElementById("btnBloqueadoAnuncio");
    const usuarioId = document.querySelector('meta[name="user-id"]')?.content;

    if (!textoStatus || !contador || !barra || !btnIr || !btnBloqueado) {
        return;
    }

	
    limparIntervaloAnuncio();

    if (usuarioId) {
        try {
            const res = await fetch(`/api/anuncio-recompensa/status/${usuarioId}`);
            const dados = await res.json();

            if (dados.limiteAtingido) {
                mostrarEventoConcluido(
                    textoStatus,
                    contador,
                    barra,
                    btnIr,
                    btnBloqueado,
                    "🏆 Recompensas diárias concluídas!",
                    "Você já coletou todas as 20 recompensas de hoje.",
                    "✅ 20/20 concluídas"
                );
                return;
            }
        } catch (e) {
            console.error("Erro ao verificar limite diário do anúncio:", e);
        }
    }

    const cooldownTempo = parseInt(localStorage.getItem("cooldownTempo"));
    const ultimoUso = parseInt(localStorage.getItem("ultimoAnuncioTempo"));

	if (!cooldownTempo || !ultimoUso) {
	    liberarEvento(
	        textoStatus,
	        contador,
	        barra,
	        btnIr,
	        btnBloqueado,
	        "🎁 Recompensa disponível agora!",
	        "Clique abaixo para assistir e receber sua recompensa.",
	        "🎬 Assistir anúncio e ganhar"
	    );

	    btnIr.onclick = function () {
	        mostrarSpinnerBotao(
	            btnIr,
	            "Abrindo..."
	        );
	    };

	    return;
	}

    function atualizar() {
        const restante = calcularRestante(ultimoUso, cooldownTempo);

        if (restante <= 0) {
            limparIntervaloAnuncio();

			liberarEvento(
			    textoStatus,
			    contador,
			    barra,
			    btnIr,
			    btnBloqueado,
			    "🎁 Recompensa disponível agora!",
			    "Clique abaixo para assistir e receber sua recompensa.",
			    "🎬 Assistir anúncio e ganhar"
			);

			btnIr.onclick = function () {
			    mostrarSpinnerBotao(
			        btnIr,
			        "Abrindo..."
			    );
			};

			return;
        }

        bloquearEvento(
            textoStatus,
            contador,
            barra,
            btnIr,
            btnBloqueado,
            restante,
            cooldownTempo,
            "⏳ Próxima recompensa em:"
        );
    }

    atualizar();
    intervaloAnuncioRecompensa = setInterval(atualizar, 1000);
}
========================================================= */
/* =========================================================
   CARD 2 - RUÍNAS PERDIDAS
========================================================= */

async function iniciarCardRuinasPerdidas() {
    const textoStatus = document.getElementById("textoStatusRuinas");
    const contador = document.getElementById("contadorCooldownRuinas");
    const barra = document.getElementById("barraCooldownRuinas");
    const btnIr = document.getElementById("btnIrRuinas");
    const btnBloqueado = document.getElementById("btnBloqueadoRuinas");
    const usuarioId = document.querySelector('meta[name="user-id"]')?.content;

    if (!textoStatus || !contador || !barra || !btnIr || !btnBloqueado || !usuarioId) {
        return;
    }

    limparIntervaloRuinas();

    try {
        const res = await fetch(`/ruinas-perdidas/status/${usuarioId}`);
        const dados = await res.json();

        if (dados.limiteAtingido) {
            localStorage.removeItem("ruinasCooldownVisual");

            mostrarEventoConcluido(
                textoStatus,
                contador,
                barra,
                btnIr,
                btnBloqueado,
                "🏆 Ruínas exploradas por hoje!",
                `Você já usou todas as ${dados.limite} explorações diárias.`,
                `✅ ${dados.tentativasHoje}/${dados.limite} concluídas`
            );
            return;
        }

        if (dados.emCooldown) {
            iniciarCooldownEventoRuinas(
                textoStatus,
                contador,
                barra,
                btnIr,
                btnBloqueado,
                dados.segundosRestantes,
                dados.tentativasHoje,
                dados.limite
            );
            return;
        }

		localStorage.removeItem("ruinasCooldownVisual");

		liberarEvento(
		    textoStatus,
		    contador,
		    barra,
		    btnIr,
		    btnBloqueado,
		    "🏛️ Ruínas disponíveis!",
		    `Explorações: ${dados.tentativasHoje}/${dados.limite}`,
		    "🏛️ Entrar nas Ruínas"
		);

		btnIr.href = "/ruinas-perdidas";

		btnIr.onclick = function (e) {
		    e.preventDefault();

		    mostrarSpinnerBotao(
		        btnIr,
		        "Entrando..."
		    );

		    setTimeout(() => {
		        window.location.href = "/ruinas-perdidas";
		    }, 300);
		};
    } catch (e) {
        console.error("Erro ao verificar status das Ruínas:", e);

        mostrarEventoBloqueado(
            textoStatus,
            contador,
            barra,
            btnIr,
            btnBloqueado,
            "⚠️ Ruínas Perdidas",
            "Não foi possível verificar o status agora.",
            "Tente novamente"
        );
    }
}

function iniciarCooldownEventoRuinas(
    textoStatus,
    contador,
    barra,
    btnIr,
    btnBloqueado,
    segundosRestantes,
    tentativasHoje,
    limite
) {

    let cooldownVisual = parseInt(
        localStorage.getItem("ruinasCooldownVisual")
    );

    let inicioCooldown = parseInt(
        localStorage.getItem("ruinasCooldownInicio")
    );

    if (!cooldownVisual || !inicioCooldown) {

        cooldownVisual = sortearNumero(300, 600);

        inicioCooldown = Date.now();

        localStorage.setItem(
            "ruinasCooldownVisual",
            cooldownVisual
        );

        localStorage.setItem(
            "ruinasCooldownInicio",
            inicioCooldown
        );
    }

    function atualizar() {

        const agora = Date.now();

        const decorrido =
            Math.floor((agora - inicioCooldown) / 1000);

        const restante =
            cooldownVisual - decorrido;

        if (restante <= 0) {

            localStorage.removeItem(
                "ruinasCooldownVisual"
            );

            localStorage.removeItem(
                "ruinasCooldownInicio"
            );

            limparIntervaloRuinas();

            iniciarCardRuinasPerdidas();

            return;
        }

        bloquearEvento(
            textoStatus,
            contador,
            barra,
            btnIr,
            btnBloqueado,
            restante,
            cooldownVisual,
            `⏳ Explorações: ${tentativasHoje}/${limite}`
        );
    }

    atualizar();

    intervaloRuinasPerdidas =
        setInterval(atualizar, 1000);
}

/* =========================================================
   CARD 3 - MASMORRA SOMBRIA
========================================================= */

let intervaloMasmorraSombria = null;

async function iniciarCardMasmorraSombria() {
    const textoStatus = document.getElementById("textoStatusMasmorra");
    const contador = document.getElementById("contadorCooldownMasmorra");
    const barra = document.getElementById("barraCooldownMasmorra");
    const btnIr = document.getElementById("btnIrMasmorra");
    const btnBloqueado = document.getElementById("btnBloqueadoMasmorra");

    if (!textoStatus || !contador || !barra || !btnIr || !btnBloqueado) {
        return;
    }


	
    limparIntervaloMasmorra();

    const cooldownVisual = parseInt(localStorage.getItem("masmorraCooldownVisual"));
    const inicioCooldown = parseInt(localStorage.getItem("masmorraCooldownInicio"));

    if (cooldownVisual && inicioCooldown) {
        const decorrido = Math.floor((Date.now() - inicioCooldown) / 1000);
        const restante = cooldownVisual - decorrido;

        if (restante > 0) {
            iniciarCooldownMasmorra(
                textoStatus,
                contador,
                barra,
                btnIr,
                btnBloqueado,
                restante,
                cooldownVisual
            );
            return;
        }

        limparCooldownVisualMasmorra();
    }

    try {
        const res = await fetch("/masmorra-sombria/status");
        const dados = await res.json();

        if (!dados.sucesso) {
            mostrarEventoBloqueado(
                textoStatus,
                contador,
                barra,
                btnIr,
                btnBloqueado,
                "⚠️ Masmorra Sombria",
                dados.mensagem || "Não foi possível verificar a masmorra.",
                "Indisponível"
            );
            return;
        }

        if (dados.emCombate) {
            textoStatus.textContent = "⚔️ Combate em andamento!";
            contador.textContent = `${dados.inimigoNome} - HP ${formatarNumero(dados.inimigoHpAtual)}/${formatarNumero(dados.inimigoHpMax)}`;

            const percentualHp = dados.inimigoHpMax > 0
                ? Math.max(0, Math.min((dados.inimigoHpAtual / dados.inimigoHpMax) * 100, 100))
                : 0;

            barra.style.width = `${percentualHp}%`;

            btnIr.style.display = "block";
            btnIr.href = "/masmorra-sombria";
            btnIr.textContent = "⚔️ Continuar Combate";

            btnBloqueado.style.display = "none";
            return;
        }

        if (dados.tentativasHoje >= dados.limite) {
            mostrarEventoConcluido(
                textoStatus,
                contador,
                barra,
                btnIr,
                btnBloqueado,
                "🏆 Masmorra concluída por hoje!",
                `Entradas: ${dados.tentativasHoje}/${dados.limite}`,
                `✅ ${dados.tentativasHoje}/${dados.limite} concluídas`
            );
            return;
        }

		if (dados.vigorAtual < 10) {

		    textoStatus.textContent = "🕸️ Masmorra Sombria";
		    contador.textContent = `Vigor insuficiente: ${dados.vigorAtual}/10`;

		    barra.style.width = "100%";

		    btnBloqueado.style.display = "none";

		    btnIr.style.display = "block";
		    btnIr.href = "/recarregar-vigor";
		    btnIr.textContent = "⚡ Recarregar Vigor";

		    btnIr.onclick = function (e) {
		        e.preventDefault();

		        mostrarSpinnerBotao(
		            btnIr,
		            "Abrindo..."
		        );

		        setTimeout(() => {
		            window.location.href = "/recarregar-vigor";
		        }, 300);
		    };

		    return;
		}
		
		liberarEvento(
		    textoStatus,
		    contador,
		    barra,
		    btnIr,
		    btnBloqueado,
		    "🕸️ Masmorra disponível!",
		    `Entradas: ${dados.tentativasHoje}/${dados.limite} | Vigor: ${dados.vigorAtual}`,
		    "🕸️ Entrar na Masmorra"
		);

		btnIr.href = "/masmorra-sombria";

		btnIr.onclick = function (e) {
		    e.preventDefault();

		    mostrarSpinnerBotao(
		        btnIr,
		        "Entrando..."
		    );

		    setTimeout(() => {
		        window.location.href = "/masmorra-sombria";
		    }, 300);
		};
    } catch (e) {
        console.error("Erro ao verificar status da Masmorra:", e);

        mostrarEventoBloqueado(
            textoStatus,
            contador,
            barra,
            btnIr,
            btnBloqueado,
            "⚠️ Masmorra Sombria",
            "Não foi possível verificar o status agora.",
            "Tente novamente"
        );
    }
}

function iniciarCooldownVisualMasmorra() {
    const cooldownVisual = sortearNumero(600, 900);

    localStorage.setItem("masmorraCooldownVisual", cooldownVisual);
    localStorage.setItem("masmorraCooldownInicio", Date.now());
}

function iniciarCooldownMasmorra(
    textoStatus,
    contador,
    barra,
    btnIr,
    btnBloqueado,
    restante,
    total
) {
    limparIntervaloMasmorra();

    function atualizar() {
        if (restante <= 0) {
            limparCooldownVisualMasmorra();
            limparIntervaloMasmorra();
            iniciarCardMasmorraSombria();
            return;
        }

        bloquearEvento(
            textoStatus,
            contador,
            barra,
            btnIr,
            btnBloqueado,
            restante,
            total,
            "🕸️ Masmorra em recuperação"
        );

        restante--;
    }

    atualizar();

    intervaloMasmorraSombria = setInterval(atualizar, 1000);
}

function limparIntervaloMasmorra() {
    if (intervaloMasmorraSombria) {
        clearInterval(intervaloMasmorraSombria);
        intervaloMasmorraSombria = null;
    }
}

function limparCooldownVisualMasmorra() {
    localStorage.removeItem("masmorraCooldownVisual");
    localStorage.removeItem("masmorraCooldownInicio");
}
/* =========================================================
   CARD 4 - FONTE DO VIGOR
========================================================= */

function iniciarCardFonteVigor() {
    const textoStatus = document.getElementById("textoStatusFonteVigor");
    const contador = document.getElementById("contadorCooldownFonteVigor");
    const barra = document.getElementById("barraCooldownFonteVigor");
    const btnIr = document.getElementById("btnIrFonteVigor");
    const btnBloqueado = document.getElementById("btnBloqueadoFonteVigor");

    if (!textoStatus || !contador || !barra || !btnIr || !btnBloqueado) {
        return;
    }

    mostrarEventoBloqueado(
        textoStatus,
        contador,
        barra,
        btnIr,
        btnBloqueado,
        "💧 Fonte do Vigor",
        "Esse evento ainda será liberado.",
        "🔒 Em breve"
    );
}

/* =========================================================
   FUNÇÕES GENÉRICAS
========================================================= */

function liberarEvento(
    textoStatus,
    contador,
    barra,
    btnIr,
    btnBloqueado,
    texto,
    descricao,
    textoBotao
) {
    textoStatus.textContent = texto;
    contador.textContent = descricao;

    barra.style.width = "100%";

    btnIr.style.display = "block";
    btnIr.textContent = textoBotao;

    btnBloqueado.style.display = "none";
}

function bloquearEvento(
    textoStatus,
    contador,
    barra,
    btnIr,
    btnBloqueado,
    restante,
    cooldownTempo,
    textoStatusBloqueado
) {
    const tempoFormatado = formatarTempo(restante);

    textoStatus.textContent = textoStatusBloqueado;
    contador.textContent = tempoFormatado;

    const passado = cooldownTempo - restante;
    const percentual = Math.min((passado / cooldownTempo) * 100, 100);

    barra.style.width = `${percentual}%`;

    btnIr.style.display = "none";

    btnBloqueado.style.display = "block";
    btnBloqueado.textContent = `Aguarde ${tempoFormatado}`;
}

function mostrarEventoConcluido(
    textoStatus,
    contador,
    barra,
    btnIr,
    btnBloqueado,
    titulo,
    descricao,
    textoBotao
) {
    textoStatus.textContent = titulo;
    contador.textContent = descricao;

    barra.style.width = "100%";

    btnIr.style.display = "none";

    btnBloqueado.style.display = "block";
    btnBloqueado.textContent = textoBotao;
}

function mostrarEventoBloqueado(
    textoStatus,
    contador,
    barra,
    btnIr,
    btnBloqueado,
    titulo,
    descricao,
    textoBotao
) {
    textoStatus.textContent = titulo;
    contador.textContent = descricao;

    barra.style.width = "100%";

    btnIr.style.display = "none";

    btnBloqueado.style.display = "block";
    btnBloqueado.textContent = textoBotao;
}

function calcularRestante(ultimoUso, cooldownTempo) {
    const agora = Date.now();
    const diff = Math.floor((agora - ultimoUso) / 1000);

    return cooldownTempo - diff;
}

function formatarTempo(segundos) {
    const min = Math.floor(segundos / 60);
    const seg = segundos % 60;

    return `${min}m ${seg.toString().padStart(2, "0")}s`;
}

function limparIntervaloAnuncio() {
    if (intervaloAnuncioRecompensa) {
        clearInterval(intervaloAnuncioRecompensa);
        intervaloAnuncioRecompensa = null;
    }
}

function limparIntervaloRuinas() {
    if (intervaloRuinasPerdidas) {
        clearInterval(intervaloRuinasPerdidas);
        intervaloRuinasPerdidas = null;
    }
}

function sortearNumero(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function formatarNumero(valor) {
    if (valor === null || valor === undefined) {
        return "0";
    }

    return Number(valor).toLocaleString("pt-BR");
}

function mostrarSpinnerBotao(botao, texto = "Carregando...") {

    if (!botao) return;

    botao.disabled = true;

    botao.innerHTML = `
        <span class="spinner-btn"></span>
        <span>${texto}</span>
    `;
}

function restaurarBotao(botao, texto) {

    if (!botao) return;

    botao.disabled = false;
    botao.textContent = texto;
}