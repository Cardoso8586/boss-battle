document.addEventListener("DOMContentLoaded", function () {

    const botoesLocais = document.querySelectorAll(".btn-local");

    const bossCoinsAtual = document.getElementById("bossCoinsAtual");
    const tentativasRestantes = document.getElementById("tentativasRestantes");
    const ataqueEspecialBonus = document.getElementById("ataqueEspecialBonus");

    let explorando = false;

    botoesLocais.forEach(botao => {
        botao.addEventListener("click", function () {

            if (explorando) {
                return;
            }

            const card = botao.closest(".local-card");
            const local = card ? card.dataset.local : null;

            explorarRuinas(local, botao);
        });
    });

    function explorarRuinas(local, botaoClicado) {

        explorando = true;

        bloquearTodosBotoes("Aguarde...");
        botaoClicado.innerText = "Explorando...";

        fetch("/ruinas-perdidas/explorar", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                local: local
            })
        })
        .then(response => response.json())
        .then(data => {

            if (!data.sucesso) {

                if (data.tipoPremio === "LIMITE_DIARIO") {
                    bloquearTodosBotoes("Limite diário atingido");
                } else if (data.emCooldown) {
                    bloquearTodosBotoes("Em descanso");
                } else {
                    bloquearTodosBotoes("Indisponível");
                }

                mostrarAlertaAviso(data.mensagem).then(() => {
                     window.location.replace("/cacador-recompensas");
                });

                return;
            }

            atualizarTela(data);

            if (data.tentativasRestantesHoje <= 0) {
                limparCooldownRuinas();

                bloquearTodosBotoes("Limite diário atingido");

                mostrarAlertaSucesso(data, local).then(() => {
                     window.location.replace("/cacador-recompensas");
                });

                return;
            }

            criarCooldownVisualRuinas();

            bloquearTodosBotoes("Em descanso");

            mostrarAlertaSucesso(data, local).then(() => {
             window.location.replace("/cacador-recompensas");            });
        })
        .catch(() => {

            bloquearTodosBotoes("Erro");

            mostrarAlertaErro().then(() => {
                 window.location.replace("/cacador-recompensas");
            });
        });
    }

    function atualizarTela(data) {

        if (bossCoinsAtual) {
            bossCoinsAtual.innerText = formatarNumero(data.bossCoinsAtual);
        }

        if (tentativasRestantes) {
            tentativasRestantes.innerText = data.tentativasRestantesHoje;
        }

        if (ataqueEspecialBonus) {
            ataqueEspecialBonus.innerText = data.ataqueEspecialBonus;
        }
    }

    function criarCooldownVisualRuinas() {

        const cooldownVisual = sortearNumero(300, 600);

        localStorage.setItem("ruinasCooldownVisual", cooldownVisual);
        localStorage.setItem("ruinasCooldownInicio", Date.now());
    }

    function limparCooldownRuinas() {
        localStorage.removeItem("ruinasCooldownVisual");
        localStorage.removeItem("ruinasCooldownInicio");
    }

    function mostrarAlertaSucesso(data, local) {

        const valorGanhoHtml = montarValorGanhoHtml(data);
        const imagemRuina = obterImagemRuina(local);

        return Swal.fire({

            customClass: {
                title: 'swal-game-text'
            },

            icon:
                data.tipoPremio === "ATAQUE_ESPECIAL"
                    ? "success"
                    : "info",

            title:
                data.premio || "Exploração concluída",

            html: `
                <div style="margin-bottom:15px;">
                    <img
                        src="${imagemRuina}"
                        alt="Ruína explorada"
                        style="
                            width:100%;
                            max-width:340px;
                            height:190px;
                            object-fit:cover;
                            border-radius:14px;
                            border:2px solid rgba(255,180,0,.45);
                            box-shadow:0 0 18px rgba(255,180,0,.25);
                        ">
                </div>

                <div style="margin-bottom:10px;">
                    ${data.mensagem || ""}
                </div>

                ${valorGanhoHtml}

                <div class="modal-anuncio" style="margin-top:15px;">
                    <iframe
                        src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                        width="468"
                        height="60"
                        scrolling="no"
                        frameborder="0">
                    </iframe>
                </div>
            `,

            timer: 7000,
            showConfirmButton: false,
            background: 'transparent',
            color: '#ffb400'
        });
    }

    function mostrarAlertaAviso(mensagem) {

        return Swal.fire({

            customClass: {
                title: 'swal-game-error'
            },

            icon: 'warning',

            title: 'Atenção',

            html: `
                <div style="margin-bottom:15px;">
                    ${mensagem || "Não foi possível explorar agora."}
                </div>

                <div class="modal-anuncio">
                    <iframe
                        src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                        width="468"
                        height="60"
                        scrolling="no"
                        frameborder="0">
                    </iframe>
                </div>
            `,

            timer: 10000,
            showConfirmButton: false,
            background: 'transparent',
            color: '#ffb400'
        }).then(() => {
		 window.location.replace("/cacador-recompensas");
		});
    }

    function mostrarAlertaErro() {

        return Swal.fire({

            customClass: {
                title: 'swal-game-error'
            },

            icon: 'error',

            title: 'Erro',

            html: `
                <div style="margin-bottom:15px;">
                    Não foi possível explorar as ruínas.
                </div>

                <div class="modal-anuncio">
                    <iframe
                        src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                        width="468"
                        height="60"
                        scrolling="no"
                        frameborder="0">
                    </iframe>
                </div>
            `,

            timer: 7000,
            showConfirmButton: false,
            background: 'transparent',
            color: '#ff3b3b'
			
        }).then(() => {
		window.location.replace("/cacador-recompensas");
	    });
		
    }

	function montarValorGanhoHtml(data) {

	    const imagemPremio = obterImagemPremioRuinas(data);

	    if (!data.valorGanho || data.valorGanho <= 0) {
	        return `
	            <div class="premio-ruinas-box">
	                <img src="${imagemPremio}"
	                     alt="Prêmio"
	                     class="premio-ruinas-img">

	                <div class="valor-ganho valor-ganho-vazio">
	                    Nada encontrado
	                </div>
	            </div>
	        `;
	    }

	    let sufixo = "";

	    if (data.tipoPremio === "BOSS_COINS") {
	        sufixo = " Boss Coins";
	    } else if (data.tipoPremio === "XP") {
	        sufixo = " XP";
	    } else if (data.tipoPremio === "ATAQUE_ESPECIAL") {
	        sufixo = " Ataque Base";
	    }

	    return `
	        <div class="premio-ruinas-box">
	            <img src="${imagemPremio}"
	                 alt="Prêmio"
	                 class="premio-ruinas-img">

	            <div class="valor-ganho">
	                +${formatarNumero(data.valorGanho)}${sufixo}
	            </div>
	        </div>
	    `;
	}

    function obterImagemRuina(local) {

        const imagens = {
            PORTAO_ANTIGO:
                "/images/cacador-recompensas/ruinas/portao-antigo.webp",

            TEMPLO_QUEBRADO:
                "/images/cacador-recompensas/ruinas/templo-quebrado.webp",

            SALA_SECRETA:
                "/images/cacador-recompensas/ruinas/sala-secreta.webp",

            ALTAR_PERDIDO:
                "/images/cacador-recompensas/ruinas/altar-perdido.webp",

            CRIPTA_ANTIGA:
                "/images/cacador-recompensas/ruinas/cripta-antiga.webp",

            BIBLIOTECA_PROIBIDA:
                "/images/cacador-recompensas/ruinas/biblioteca-proibida.webp",

            CAMARA_DOURADA:
                "/images/cacador-recompensas/ruinas/camara-dourada.webp",

            POCO_DAS_ALMAS:
                "/images/cacador-recompensas/ruinas/poco-das-almas.webp"
        };

        return imagens[local] ||
            "/images/cacador-recompensas/ruinas/portao-antigo.webp";
    }

    function bloquearTodosBotoes(texto) {
        botoesLocais.forEach(btn => {
            btn.disabled = true;
            btn.innerText = texto;
            btn.classList.add("btn-local-bloqueado");
        });
    }

    function sortearNumero(min, max) {
        return Math.floor(
            Math.random() * (max - min + 1)
        ) + min;
    }

    function formatarNumero(valor) {

        if (valor === null || valor === undefined) {
            return "0";
        }

        return Number(valor).toLocaleString("pt-BR");
    }
	
	function obterImagemPremioRuinas(data) {

	    if (!data || !data.tipoPremio) {
	        return "/images/cacador-recompensas/recompensas/nada.webp";
	    }

	    switch (data.tipoPremio) {

	        case "BOSS_COINS":
	            return "/icones/boss_coin.webp";

	        case "XP":
	            return "/images/cacador-recompensas/recompensas/exp.webp";

	        case "ATAQUE_ESPECIAL":
	            return "/images/cacador-recompensas/recompensas/ataque-base.webp";

	        case "NADA":
	            return "/images/cacador-recompensas/recompensas/nada.webp";

	        default:
	            return "/images/cacador-recompensas/recompensas/bau-recompensa.webp";
	    }
	}
	
});