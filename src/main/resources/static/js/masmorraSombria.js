document.addEventListener("DOMContentLoaded", function () {

    const estadoSemCombate = document.getElementById("estadoSemCombate");

    const btnEntrar = document.getElementById("btnEntrarMasmorra");
    const btnAtacarModal = document.getElementById("btnAtacarModal");
    const btnFecharModal = document.getElementById("btnFecharModalCombate");
    const btnUsarPocaoModal = document.getElementById("btnUsarPocaoModal");
    const btnFugirModal = document.getElementById("btnFugirModal");

    const textoStatus = document.getElementById("textoStatusMasmorra");

    const bossCoinsAtual = document.getElementById("bossCoinsAtual");
    const vigorAtual = document.getElementById("vigorAtual");
    const ataqueBase = document.getElementById("ataqueBase");

    const modal = document.getElementById("modalCombateMasmorra");

    let processando = false;
    let combateEncerrado = false;
    let timerAtaqueInimigo = null;
    let segundosAtaqueInimigo = 20;

    carregarStatus();

    if (btnEntrar) {
        btnEntrar.addEventListener("click", entrarMasmorra);
    }

    if (btnAtacarModal) {
        btnAtacarModal.addEventListener("click", atacarMasmorra);
    }

    if (btnUsarPocaoModal) {
        btnUsarPocaoModal.addEventListener("click", usarPocaoMasmorra);
    }

    if (btnFugirModal) {
        btnFugirModal.addEventListener("click", fugirMasmorra);
    }

    if (btnFecharModal) {
        btnFecharModal.addEventListener("click", fecharModalCombate);
    }

    if (modal) {
        modal.addEventListener("click", function (e) {
            if (e.target === modal) {
                e.preventDefault();
                e.stopPropagation();
            }
        });
    }

    async function carregarStatus() {
        try {
            const res = await fetch("/masmorra-sombria/status", {
                cache: "no-store"
            });

            const dados = await res.json();

            if (!dados.sucesso) {
                mostrarSemCombate(
                    dados.mensagem || "Não foi possível carregar a masmorra.",
                    false
                );
                return;
            }

            atualizarStatusJogador(dados);

            if (dados.emCombate) {
                if (textoStatus) {
                    textoStatus.textContent =
                        "Você possui um combate em andamento. Clique para continuar.";
                }

                liberarBotao(btnEntrar, "⚔️ Continuar Combate");
                return;
            }

            if (dados.cooldownAtivo) {
                const minutos = calcularMinutosCooldown(dados);

                mostrarSemCombate(
                    `A Masmorra está descansando. Retorne em aproximadamente ${minutos} minuto(s).`,
                    true
                );

                return;
            }

            mostrarSemCombate(
                `Entradas utilizadas: ${dados.tentativasHoje || 0}/${dados.limite || 30}`,
                false
            );

        } catch (e) {
            console.error("Erro ao carregar status da masmorra:", e);

            mostrarSemCombate(
                "Erro ao carregar a Masmorra Sombria.",
                false
            );
        }
    }

    async function entrarMasmorra() {
        if (processando) return;

        processando = true;
        combateEncerrado = false;

        limparTimerAtaqueInimigo();
        bloquearBotao(btnEntrar, "Entrando...");

        try {
            const res = await fetch("/masmorra-sombria/entrar", {
                method: "POST"
            });

            const dados = await res.json();

            if (!dados.sucesso) {

                atualizarStatusJogador(dados);

                if (dados.status === "COOLDOWN" || dados.cooldownAtivo) {
                    const minutos = calcularMinutosCooldown(dados);

                    await mostrarAvisoSemRedirect(
                        dados.mensagem ||
                        `Você completou ${dados.limite || 30} explorações. Aguarde aproximadamente ${minutos} minuto(s).`
                    );

                    finalizarFluxoMasmorra(dados);
                    return;
                }

                await mostrarAvisoSemRedirect(
                    dados.mensagem || "Não foi possível entrar na masmorra."
                );

                liberarBotao(btnEntrar, "🕸️ Entrar na Masmorra");
                processando = false;
                return;
            }

            atualizarStatusJogador(dados);
            abrirModalCombate(dados);

            if (textoStatus) {
                textoStatus.textContent = "Combate iniciado na Masmorra Sombria.";
            }

            processando = false;

        } catch (e) {
            console.error("Erro ao entrar na masmorra:", e);

            await mostrarErroSemRedirect("Não foi possível entrar na masmorra.");
            fecharModalEAtualizarStatus();

            processando = false;
        }
    }

    async function atacarMasmorra() {
        if (processando || combateEncerrado) return;

        processando = true;

        limparTimerAtaqueInimigo();
        bloquearBotao(btnAtacarModal, "Atacando...");

        try {
            const res = await fetch("/masmorra-sombria/atacar", {
                method: "POST"
            });

            const dados = await res.json();

            if (!dados.sucesso) {
                await mostrarAvisoSemRedirect(
                    dados.mensagem || "Não foi possível atacar."
                );

                finalizarFluxoMasmorra(dados);
                return;
            }

            atualizarStatusJogador(dados);
            atualizarModalCombate(dados);

            animarDano("areaImagemInimigo", dados.danoUsuario);
            animarDano("areaImagemUsuario", dados.danoRecebido);

            if (dados.status === "VITORIA") {
                combateEncerrado = true;
                limparTimerAtaqueInimigo();

                bloquearBotao(btnAtacarModal, "Vitória");

                await mostrarResultadoVitoria(dados);

                finalizarFluxoMasmorra(dados);
                return;
            }

            if (dados.status === "DERROTA") {
                combateEncerrado = true;
                limparTimerAtaqueInimigo();

                bloquearBotao(btnAtacarModal, "Derrotado");

                await mostrarResultadoDerrota(dados);

                finalizarFluxoMasmorra(dados);
                return;
            }

            liberarBotao(btnAtacarModal, "⚔️ Atacar");
            processando = false;
            iniciarTimerAtaqueInimigo();

        } catch (e) {
            console.error("Erro ao atacar na masmorra:", e);

            await mostrarErroSemRedirect("Erro ao atacar na masmorra.");
            fecharModalEAtualizarStatus();

            processando = false;
        }
    }

    async function inimigoAtacarAutomatico() {
        if (processando || combateEncerrado) return;

        processando = true;
        bloquearBotao(btnAtacarModal, "Inimigo atacando...");

        try {
            const res = await fetch("/masmorra-sombria/inimigo-atacar", {
                method: "POST"
            });

            const dados = await res.json();

            if (!dados.sucesso) {
                await mostrarAvisoSemRedirect(
                    dados.mensagem || "Não foi possível processar o ataque do inimigo."
                );

                finalizarFluxoMasmorra(dados);
                return;
            }

            atualizarStatusJogador(dados);
            atualizarModalCombate(dados);

            animarDano("areaImagemInimigo", dados.danoUsuario);
            animarDano("areaImagemUsuario", dados.danoRecebido);

            if (dados.status === "DERROTA") {
                combateEncerrado = true;
                limparTimerAtaqueInimigo();

                bloquearBotao(btnAtacarModal, "Derrotado");

                await mostrarResultadoDerrota(dados);

                finalizarFluxoMasmorra(dados);
                return;
            }

            liberarBotao(btnAtacarModal, "⚔️ Atacar");
            processando = false;
            iniciarTimerAtaqueInimigo();

        } catch (e) {
            console.error("Erro no ataque automático do inimigo:", e);

            await mostrarErroSemRedirect("Erro no ataque do inimigo.");
            fecharModalEAtualizarStatus();

            processando = false;
        }
    }
	function finalizarFluxoMasmorra(dados) {
	    limparTimerAtaqueInimigo();

	    if (deveRedirecionarFimDoCiclo(dados)) {
	        iniciarCooldownVisualMasmorraFimCiclo(dados);
	        window.location.replace("/cacador-recompensas");
	        return;
	    }

	    fecharModalEAtualizarStatus();
	    processando = false;
	}
	
	
    function deveRedirecionarFimDoCiclo(dados) {
        return Boolean(dados && dados.cooldownAtivo)
            && Number(dados.tentativasHoje || 0) >= Number(dados.limite || 10);
    }

    function fecharModalEAtualizarStatus() {
        limparTimerAtaqueInimigo();

        if (modal) {
            modal.style.display = "none";
        }

        combateEncerrado = false;

        liberarBotao(btnEntrar, "🕸️ Entrar na Masmorra");
        carregarStatus();
    }

    function iniciarTimerAtaqueInimigo() {
        limparTimerAtaqueInimigo();

        if (combateEncerrado) return;

        segundosAtaqueInimigo = 30;

        timerAtaqueInimigo = setInterval(async function () {
            segundosAtaqueInimigo--;

            const mensagemCombateModal =
                document.getElementById("mensagemCombateModal");

            if (mensagemCombateModal && !combateEncerrado) {
                mensagemCombateModal.textContent =
                    "O inimigo atacará em " + segundosAtaqueInimigo + "s. Ataque antes!";
            }

            if (segundosAtaqueInimigo <= 0) {
                limparTimerAtaqueInimigo();
                await inimigoAtacarAutomatico();
            }

        }, 1000);
    }

    function limparTimerAtaqueInimigo() {
        if (timerAtaqueInimigo) {
            clearInterval(timerAtaqueInimigo);
            timerAtaqueInimigo = null;
        }
    }

    function mostrarSemCombate(mensagem, emCooldown = false) {
        limparTimerAtaqueInimigo();

        if (estadoSemCombate) {
            estadoSemCombate.style.display = "block";
        }

        if (textoStatus) {
            textoStatus.textContent = mensagem;
        }

        if (emCooldown) {
            bloquearBotao(btnEntrar, "⏳ Em Cooldown");
        } else {
            liberarBotao(btnEntrar, "🕸️ Entrar na Masmorra");
        }
    }

    function abrirModalCombate(dados) {
        if (!modal) return;

        combateEncerrado = false;
        limparTimerAtaqueInimigo();

        const imagemGuerreiroUsuario =
            document.getElementById("imagemGuerreiroUsuario");

        if (imagemGuerreiroUsuario) {
            imagemGuerreiroUsuario.dataset.carregado = "";
        }

        atualizarModalCombate(dados);

        if (btnAtacarModal) {
            btnAtacarModal.style.display = "inline-flex";
            liberarBotao(btnAtacarModal, "⚔️ Atacar");
        }

        modal.style.display = "flex";

        iniciarTimerAtaqueInimigo();
    }

    function atualizarModalCombate(dados) {
        const imagemGuerreiroUsuario =
            document.getElementById("imagemGuerreiroUsuario");

        const ataqueGuerreiroModal =
            document.getElementById("ataqueGuerreiroModal");

        const vigorGuerreiroModal =
            document.getElementById("vigorGuerreiroModal");

        const vigorMaximoModal =
            document.getElementById("vigorMaximoModal");

        const imagemInimigoModal =
            document.getElementById("imagemInimigoModal");

        const nomeInimigoModal =
            document.getElementById("nomeInimigoModal");

        const hpAtualInimigoModal =
            document.getElementById("hpAtualInimigoModal");

        const hpMaxInimigoModal =
            document.getElementById("hpMaxInimigoModal");

        const ataqueInimigoModal =
            document.getElementById("ataqueInimigoModal");

        const mensagemCombateModal =
            document.getElementById("mensagemCombateModal");

        const nomeGuerreiroUsuario =
            document.getElementById("nomeGuerreiroUsuario");

        atualizarBotaoPocao(
            dados.pocoesVida || dados.pocoesRestantes || 0
        );

        if (
            imagemGuerreiroUsuario &&
            !imagemGuerreiroUsuario.dataset.carregado
        ) {
            obterImagemGuerreiroAleatorioMasmorra()
                .then(guerreiro => {
                    imagemGuerreiroUsuario.onerror = function () {
                        console.error(
                            "Imagem não encontrada:",
                            guerreiro.imagem
                        );

                        this.src =
                            "/images/cacador-recompensas/masmorra/guerreiro-masmorra.webp";
                    };

                    imagemGuerreiroUsuario.src = guerreiro.imagem;

                    if (nomeGuerreiroUsuario) {
                        nomeGuerreiroUsuario.textContent = guerreiro.nome;
                    }

                    imagemGuerreiroUsuario.dataset.carregado = "true";
                });
        }

        if (ataqueGuerreiroModal) {
            ataqueGuerreiroModal.textContent =
                formatarNumero(dados.ataqueBase || dados.danoUsuario || 0);
        }

        if (vigorGuerreiroModal) {
            vigorGuerreiroModal.textContent =
                formatarNumero(dados.vigorAtual || 0);
        }

        const vigorMaximo =
            Number(
                dados.vigorMaximo ||
                dados.energiaGuerreirosPadrao ||
                obterVigorMaximo()
            );

        if (vigorMaximoModal) {
            vigorMaximoModal.textContent = formatarNumero(vigorMaximo);
        }

        atualizarBarraHp(
            "barraVigorModal",
            dados.vigorAtual || 0,
            vigorMaximo || 1
        );

        if (imagemInimigoModal) {
            imagemInimigoModal.src =
                dados.inimigoImagem ||
                "/images/cacador-recompensas/masmorra/goblin-das-sombras.webp";
        }

        if (nomeInimigoModal) {
            nomeInimigoModal.textContent =
                dados.inimigoNome || "Inimigo";
        }

        if (hpAtualInimigoModal) {
            hpAtualInimigoModal.textContent =
                formatarNumero(dados.inimigoHpAtual || 0);
        }

        if (hpMaxInimigoModal) {
            hpMaxInimigoModal.textContent =
                formatarNumero(dados.inimigoHpMax || 0);
        }

        if (ataqueInimigoModal) {
            ataqueInimigoModal.textContent =
                formatarNumero(dados.inimigoAtaque || 0);
        }

        if (mensagemCombateModal) {
            mensagemCombateModal.textContent =
                dados.mensagem || "Combate em andamento.";
        }

        atualizarBarraHp(
            "barraHpInimigoModal",
            dados.inimigoHpAtual || 0,
            dados.inimigoHpMax || 1
        );
    }

    async function fecharModalCombate() {
        if (!modal) return;

        limparTimerAtaqueInimigo();

        if (combateEncerrado) {
            fecharModalEAtualizarStatus();
            return;
        }

        try {
            const res = await fetch("/masmorra-sombria/fugir", {
                method: "POST"
            });

            const dados = await res.json();

            finalizarFluxoMasmorra(dados);

        } catch (e) {
            console.error("Erro ao fechar/fugir da masmorra:", e);

            await mostrarErroSemRedirect("Erro ao fechar o combate.");
            fecharModalEAtualizarStatus();
            processando = false;
        }
    }

    function atualizarStatusJogador(dados) {
        if (bossCoinsAtual && dados.bossCoinsAtual !== undefined) {
            bossCoinsAtual.textContent =
                formatarNumero(dados.bossCoinsAtual);
        }

        if (vigorAtual && dados.vigorAtual !== undefined) {
            vigorAtual.textContent =
                formatarNumero(dados.vigorAtual);
        }

        if (ataqueBase && dados.ataqueBase !== undefined) {
            ataqueBase.textContent =
                formatarNumero(dados.ataqueBase);
        }
    }

    function obterVigorMaximo() {
        const vigorMaximoModal =
            document.getElementById("vigorMaximoModal");

        if (vigorMaximoModal) {
            const valor = Number(
                vigorMaximoModal.textContent.replace(/\D/g, "")
            );

            if (valor > 0) return valor;
        }

        const vigorAtualNumero = Number(
            (vigorAtual?.textContent || "0").replace(/\D/g, "")
        );

        return Math.max(1, vigorAtualNumero);
    }

    function atualizarBarraHp(id, hpAtual, hpMax) {
        const barra = document.getElementById(id);

        if (!barra) return;

        const percentual = hpMax > 0
            ? Math.max(0, Math.min((hpAtual / hpMax) * 100, 100))
            : 0;

        barra.style.width = percentual + "%";
    }

    function montarTextoRecompensa(recompensa) {
        if (!recompensa) {
            return "Nenhuma recompensa recebida.";
        }

        if (recompensa.tipo === "BOSS_COINS") {
            return `+${formatarNumero(recompensa.valor)} Boss Coins`;
        }

        if (recompensa.tipo === "XP") {
            return `+${formatarNumero(recompensa.valor)} XP`;
        }

        if (recompensa.tipo === "ATAQUE_BASE") {
            return `+${formatarNumero(recompensa.valor)} Ataque Base`;
        }

        if (recompensa.tipo === "ESPADA_FANEJANTE") {
            return `+${formatarNumero(recompensa.valor)} Espada Flanejante`;
        }

        if (recompensa.tipo === "MACHADO_DILACERADOR") {
            return `+${formatarNumero(recompensa.valor)} Machado Dilacerador`;
        }

        if (recompensa.tipo === "ESCUDO_PRIMORDIAL") {
            return `+${formatarNumero(recompensa.valor)} Escudo Primordial`;
        }

        if (recompensa.tipo === "POCAO_VIGOR") {
            return `+${formatarNumero(recompensa.valor)} Poção de Vigor`;
        }

        if (recompensa.tipo === "NADA") {
            return "Nada ganho";
        }

        return recompensa.nome || "Recompensa recebida.";
    }

    function mostrarResultadoVitoria(dados) {
        const imagemRecompensa = obterImagemRecompensa(dados.recompensa);

        return Swal.fire({
            target: document.body,
            heightAuto: false,
            customClass: {
                title: "swal-game-text"
            },
            icon: "success",
            title: "Vitória!",
            html: `
                <div style="margin-bottom:12px;">
                    ${dados.mensagem || "Você venceu o combate."}
                </div>

                <div class="recompensa-vitoria-box">
                    <img src="${imagemRecompensa}"
                         alt="Recompensa"
                         class="recompensa-vitoria-img">

                    <div class="valor-ganho">
                        ${montarTextoRecompensa(dados.recompensa)}
                    </div>
                </div>

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
            background: "transparent",
            color: "#ffb400"
        });
    }

    function mostrarResultadoDerrota(dados) {
        return Swal.fire({
            customClass: {
                title: "swal-game-error"
            },
            icon: "error",
            title: "Derrota",
            html: `
                <div style="margin-bottom:12px;">
                    ${dados.mensagem || "Você foi derrotado."}
                </div>

                <div>
                    Dano recebido:
                    <strong>${formatarNumero(dados.danoRecebido || 0)}</strong>
                </div>

                <div style="margin-top:10px;">
                    Vigor atual:
                    <strong>${formatarNumero(dados.vigorAtual || 0)}</strong>
                </div>

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
            background: "transparent",
            color: "#ff3b3b"
        });
    }

    function mostrarErroSemRedirect(mensagem) {
        return Swal.fire({
            customClass: {
                title: "swal-game-error"
            },
            icon: "error",
            title: "Erro",
            text: mensagem,
            timer: 5000,
            showConfirmButton: false,
            background: "transparent",
            color: "#ff3b3b"
        });
    }

    function mostrarAvisoSemRedirect(mensagem) {
        return Swal.fire({
            customClass: {
                title: "swal-game-error"
            },
            icon: "warning",
            title: "Atenção",
            text: mensagem,
            timer: 5000,
            showConfirmButton: false,
            background: "transparent",
            color: "#ffb400"
        });
    }

    function bloquearBotao(botao, texto) {
        if (!botao) return;

        botao.disabled = true;
        botao.textContent = texto;
    }

    function liberarBotao(botao, texto) {
        if (!botao) return;

        botao.disabled = false;
        botao.textContent = texto;
    }

    function formatarNumero(valor) {
        if (valor === null || valor === undefined) {
            return "0";
        }

        return Number(valor).toLocaleString("pt-BR");
    }

    function calcularMinutosCooldown(dados) {
        return Math.max(
            1,
            Math.ceil((dados.cooldownSegundosRestantes || 0) / 60)
        );
    }

    function animarDano(areaId, valorDano) {
        const area = document.getElementById(areaId);

        if (!area || !valorDano || valorDano <= 0) return;

        const x = Math.floor(Math.random() * 60) + 20;
        const y = Math.floor(Math.random() * 55) + 20;
        const rotacao = Math.random() > 0.5 ? "35deg" : "-35deg";

        const textoDano = document.createElement("div");
        textoDano.className = "dano-flutuante";
        textoDano.textContent = "-" + formatarNumero(valorDano);
        textoDano.style.left = x + "%";
        textoDano.style.top = y + "%";

        const corte = document.createElement("div");
        corte.className = "corte-ataque";
        corte.style.left = x + "%";
        corte.style.top = y + "%";
        corte.style.setProperty("--rotacao", rotacao);

        area.appendChild(corte);
        area.appendChild(textoDano);

        setTimeout(() => {
            corte.remove();
            textoDano.remove();
        }, 4000);
    }

    async function obterImagemGuerreiroAleatorioMasmorra() {
        const usuarioId =
            document.querySelector('meta[name="user-id"]')?.content;

        if (!usuarioId) {
            return {
                imagem: "/images/guerreiro_padrao.webp",
                nome: "Guerreiro Tradicional"
            };
        }

        const chave = `cache_guerreiros_usuario_${usuarioId}`;

        let guerreiros = [];

        try {
            const cache = localStorage.getItem(chave);

            if (cache) {
                const cacheObj = JSON.parse(cache);
                guerreiros = cacheObj.dados || [];
            }

            if (!guerreiros || guerreiros.length === 0) {
                const response =
                    await fetch(`/guerreiros/usuario/${usuarioId}`, {
                        cache: "no-store"
                    });

                if (response.ok) {
                    guerreiros = await response.json();

                    localStorage.setItem(
                        chave,
                        JSON.stringify({
                            atualizadoEm: Date.now(),
                            dados: guerreiros
                        })
                    );
                }
            }

        } catch (e) {
            console.error(
                "Erro ao buscar guerreiro da masmorra:",
                e
            );
        }

        if (!guerreiros || guerreiros.length === 0) {
            return {
                imagem: "/images/guerreiro_padrao.webp",
                nome: "Guerreiro Tradicional"
            };
        }

        const guerreiro =
            guerreiros[Math.floor(Math.random() * guerreiros.length)];

        return {
            imagem: obterImagemDoGuerreiroMasmorra(guerreiro),
            nome: guerreiro.nome || "Guerreiro"
        };
    }

    function obterImagemDoGuerreiroMasmorra(guerreiro) {
        if (!guerreiro) {
            return "/images/guerreiro_padrao.webp";
        }

        if (
            guerreiro.elite === false ||
            guerreiro.nome === "Guerreiro Tradicional" ||
            guerreiro.id === 0
        ) {
            return "/images/guerreiro_padrao.webp";
        }

        const pasta = obterPastaGuerreiroMasmorra(guerreiro);
        const prefixo = obterPrefixoArquivoMasmorra(guerreiro);

        return `${pasta}/${prefixo}.webp`;
    }

    function obterPastaGuerreiroMasmorra(guerreiro) {
        const nome = guerreiro?.nome || "Guerreira Mística";

        switch (nome) {
            case "Guerreira Mística":
                return "/images/guerreiros_elite/guerreira_mistica";

            case "Valgard":
                return "/images/guerreiros_elite/guerreiro_valgard";

            case "Caçador de Boss":
                return "/images/guerreiros_elite/cacador_boss";

            case "Arqueira Real":
                return "/images/guerreiros_elite/arqueira_real";

            case "Guerreiro Guardião":
                return "/images/guerreiros_elite/guerreiro_guardiao";

            case "Ninja das Sombras":
                return "/images/guerreiros_elite/ninja_das_sombras";

            case "Sentinela Nyara":
                return "/images/guerreiros_elite/sentinela_nyara";

            default:
                return `/images/guerreiros_elite/${normalizarParaPastaMasmorra(nome)}`;
        }
    }

    function obterPrefixoArquivoMasmorra(guerreiro) {
        const nome = guerreiro?.nome || "Guerreira Mística";

        switch (nome) {
            case "Guerreira Mística":
                return "guerreira-mistica";

            case "Valgard":
                return "valgard";

            case "Caçador de Boss":
                return "cacador-boss";

            case "Arqueira Real":
                return "arqueira-real";

            case "Guerreiro Guardião":
                return "guerreiro-guardiao";

            case "Sentinela Nyara":
                return "sentinela-nyara";

            case "Ninja das Sombras":
                return "ninja-das-sombras";

            default:
                return normalizarParaArquivoMasmorra(nome);
        }
    }

    function normalizarParaPastaMasmorra(nome) {
        return nome
            .toLowerCase()
            .normalize("NFD")
            .replace(/[\u0300-\u036f]/g, "")
            .replace(/ç/g, "c")
            .replace(/[^a-z0-9]+/g, "_")
            .replace(/^_+|_+$/g, "");
    }

    function normalizarParaArquivoMasmorra(nome) {
        return nome
            .toLowerCase()
            .normalize("NFD")
            .replace(/[\u0300-\u036f]/g, "")
            .replace(/ç/g, "c")
            .replace(/[^a-z0-9]+/g, "-")
            .replace(/^-+|-+$/g, "");
    }

    async function preloadImagensGuerreiros() {
        const usuarioId =
            document.querySelector('meta[name="user-id"]')?.content;

        if (!usuarioId) return;

        const chave =
            `cache_guerreiros_usuario_${usuarioId}`;

        const cache =
            localStorage.getItem(chave);

        if (!cache) return;

        const guerreiros =
            JSON.parse(cache).dados || [];

        guerreiros.forEach(g => {
            const img = new Image();
            img.src = obterImagemDoGuerreiroMasmorra(g);
        });
    }

    async function usarPocaoMasmorra() {
        if (processando || combateEncerrado) return;

        processando = true;

        if (btnUsarPocaoModal) {
            btnUsarPocaoModal.disabled = true;
            btnUsarPocaoModal.innerHTML = "Usando...";
        }

        try {
            const res = await fetch("/masmorra-sombria/usar-pocao", {
                method: "POST"
            });

            const dados = await res.json();

            if (!dados.sucesso) {
                await mostrarAvisoSemRedirect(
                    dados.mensagem || "Não foi possível usar a poção."
                );

                const restantes = Number(
                    dados.pocoesRestantes ||
                    dados.pocoesVida ||
                    0
                );

                atualizarBotaoPocao(restantes);

                processando = false;
                return;
            }

            if (vigorAtual && dados.vigorAtual !== undefined) {
                vigorAtual.textContent =
                    formatarNumero(dados.vigorAtual);
            }

            const vigorGuerreiroModal =
                document.getElementById("vigorGuerreiroModal");

            if (vigorGuerreiroModal) {
                vigorGuerreiroModal.textContent =
                    formatarNumero(dados.vigorAtual);
            }

            const vigorMaximoAtual =
                Number(
                    dados.vigorMaximo ||
                    dados.energiaGuerreirosPadrao ||
                    obterVigorMaximo()
                );

            atualizarBarraHp(
                "barraVigorModal",
                dados.vigorAtual || 0,
                vigorMaximoAtual || 1
            );

            const pocoesRestantes =
                Number(
                    dados.pocoesRestantes ||
                    dados.pocoesVida ||
                    0
                );

            atualizarBotaoPocao(pocoesRestantes);

            const mensagemCombateModal =
                document.getElementById("mensagemCombateModal");

            if (mensagemCombateModal) {
                mensagemCombateModal.textContent = dados.mensagem;
            }

            processando = false;

        } catch (e) {
            console.error("Erro ao usar poção:", e);

            await mostrarErroSemRedirect("Não foi possível usar a poção.");
            processando = false;
        }
    }

    function atualizarBotaoPocao(quantidade) {
        if (!btnUsarPocaoModal) return;

        quantidade = Number(quantidade || 0);

        if (quantidade > 0) {
            btnUsarPocaoModal.disabled = false;
            btnUsarPocaoModal.style.display = "flex";
            btnUsarPocaoModal.innerHTML = `
                <img src="/icones/pocao_vigor.webp"
                     alt="Poção"
                     class="icone-pocao">
                Usar Poção (${quantidade})
            `;
        } else {
            btnUsarPocaoModal.disabled = true;
            btnUsarPocaoModal.style.display = "none";
        }
    }

    async function fugirMasmorra() {
        if (processando || combateEncerrado) return;

        const confirmar = await Swal.fire({
            customClass: {
                popup: "swal-game-popup",
                title: "swal-game-text",
                confirmButton: "swal-game-confirm",
                cancelButton: "swal-game-cancel"
            },

            title: "Fugir da Masmorra?",

            html: `
                <p style="color:#ffb400;">
                    Você abandonará este combate.
                </p>

                <p style="color:#ffb400;">
                    Você perderá apenas este combate atual.
                </p>

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

            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "🏃 Fugir",
            cancelButtonText: "Continuar Lutando",
            buttonsStyling: false,
            background: "transparent",
            color: "#ffb400"
        });

        if (!confirmar.isConfirmed) {
            return;
        }

        try {
            processando = true;
            limparTimerAtaqueInimigo();

            const res = await fetch("/masmorra-sombria/fugir", {
                method: "POST"
            });

            const dados = await res.json();

            finalizarFluxoMasmorra(dados);

        } catch (e) {
            console.error("Erro ao fugir:", e);

            await mostrarErroSemRedirect("Não foi possível fugir.");
            fecharModalEAtualizarStatus();
            processando = false;
        }
    }

    function obterImagemRecompensa(recompensa) {
        if (!recompensa || !recompensa.tipo) {
            return "/images/cacador-recompensas/recompensas/nada.webp";
        }

        switch (recompensa.tipo) {
            case "BOSS_COINS":
                return "/images/cacador-recompensas/recompensas/boss_coin.webp";

            case "XP":
                return "/images/cacador-recompensas/recompensas/exp.webp";

            case "ATAQUE_BASE":
                return "/images/cacador-recompensas/recompensas/ataque-base.webp";

            case "POCAO_VIGOR":
                return "/images/cacador-recompensas/recompensas/pocao_vigor.webp";

            case "MACHADO_DILACERADOR":
                return "/images/cacador-recompensas/recompensas/machado_dilacerador.webp";

            case "ESPADA_FANEJANTE":
                return "/images/cacador-recompensas/recompensas/espada_flanejante.webp";

            case "ESCUDO_PRIMORDIAL":
                return "/images/cacador-recompensas/recompensas/escudo_primordial.webp";

            case "NADA":
                return "/images/cacador-recompensas/recompensas/nada.webp";

            default:
                return "/images/cacador-recompensas/recompensas/nada.webp";
        }
    }

	function iniciarCooldownVisualMasmorraFimCiclo(dados) {
	    if (!deveRedirecionarFimDoCiclo(dados)) return;

		//const cooldownVisual = sortearNumero(0, 0);
		
	    const cooldownVisual = sortearNumero(3900, 4200);
	    // 3900 = 1h05
	    // 4200 = 1h10

	    localStorage.setItem("masmorraCooldownVisual", cooldownVisual);
	    localStorage.setItem("masmorraCooldownInicio", Date.now());
	}

	function sortearNumero(min, max) {
	    return Math.floor(
	        Math.random() * (max - min + 1)
	    ) + min;
	}
    preloadImagensGuerreiros();
});

/**
 * 
 * document.addEventListener("DOMContentLoaded", function () {

    const estadoSemCombate = document.getElementById("estadoSemCombate");

    const btnEntrar = document.getElementById("btnEntrarMasmorra");
    const btnAtacarModal = document.getElementById("btnAtacarModal");
    const btnFecharModal = document.getElementById("btnFecharModalCombate");
    const btnUsarPocaoModal = document.getElementById("btnUsarPocaoModal");
    const btnFugirModal = document.getElementById("btnFugirModal");

    const textoStatus = document.getElementById("textoStatusMasmorra");

    const bossCoinsAtual = document.getElementById("bossCoinsAtual");
    const vigorAtual = document.getElementById("vigorAtual");
    const ataqueBase = document.getElementById("ataqueBase");

    const modal = document.getElementById("modalCombateMasmorra");

    let processando = false;
    let combateEncerrado = false;
    let timerAtaqueInimigo = null;
    let segundosAtaqueInimigo = 30;

    carregarStatus();

    if (btnEntrar) {
        btnEntrar.addEventListener("click", entrarMasmorra);
    }

    if (btnAtacarModal) {
        btnAtacarModal.addEventListener("click", atacarMasmorra);
    }

    if (btnUsarPocaoModal) {
        btnUsarPocaoModal.addEventListener("click", usarPocaoMasmorra);
    }

    if (btnFugirModal) {
        btnFugirModal.addEventListener("click", fugirMasmorra);
    }

    if (btnFecharModal) {
        btnFecharModal.addEventListener("click", fecharModalCombate);
    }

    if (modal) {
        modal.addEventListener("click", function (e) {
            if (e.target === modal) {
                e.preventDefault();
                e.stopPropagation();
            }
        });
    }

    async function carregarStatus() {
        try {
            const res = await fetch("/masmorra-sombria/status", {
                cache: "no-store"
            });

            const dados = await res.json();

            if (!dados.sucesso) {
                mostrarSemCombate(
                    dados.mensagem || "Não foi possível carregar a masmorra.",
                    false
                );
                return;
            }

            atualizarStatusJogador(dados);

            if (dados.emCombate) {
                if (textoStatus) {
                    textoStatus.textContent =
                        "Você possui um combate em andamento. Clique para continuar.";
                }

                liberarBotao(btnEntrar, "⚔️ Continuar Combate");
                return;
            }

            if (dados.cooldownAtivo) {
                const minutos = calcularMinutosCooldown(dados);

                mostrarSemCombate(
                    `A Masmorra está descansando. Retorne em aproximadamente ${minutos} minuto(s).`,
                    true
                );

                return;
            }

            mostrarSemCombate(
                `Entradas utilizadas: ${dados.tentativasHoje || 0}/${dados.limite || 30}`,
                false
            );

        } catch (e) {
            console.error("Erro ao carregar status da masmorra:", e);

            mostrarSemCombate(
                "Erro ao carregar a Masmorra Sombria.",
                false
            );
        }
    }

    async function entrarMasmorra() {
        if (processando) return;

        processando = true;
        combateEncerrado = false;

        limparTimerAtaqueInimigo();
        bloquearBotao(btnEntrar, "Entrando...");

        try {
            const res = await fetch("/masmorra-sombria/entrar", {
                method: "POST"
            });

            const dados = await res.json();

            if (!dados.sucesso) {

                atualizarStatusJogador(dados);

                if (dados.status === "COOLDOWN" || dados.cooldownAtivo) {
                    const minutos = calcularMinutosCooldown(dados);

                    await mostrarAvisoSemRedirect(
                        dados.mensagem ||
                        `Você completou ${dados.limite || 30} explorações. Aguarde aproximadamente ${minutos} minuto(s).`
                    );

                    mostrarSemCombate(
                        `A Masmorra está descansando. Retorne em aproximadamente ${minutos} minuto(s).`,
                        true
                    );

                    processando = false;
                    return;
                }

                await mostrarAvisoSemRedirect(
                    dados.mensagem || "Não foi possível entrar na masmorra."
                );

                liberarBotao(btnEntrar, "🕸️ Entrar na Masmorra");
                processando = false;
                return;
            }

            atualizarStatusJogador(dados);
            abrirModalCombate(dados);

            if (textoStatus) {
                textoStatus.textContent = "Combate iniciado na Masmorra Sombria.";
            }

        } catch (e) {
            console.error("Erro ao entrar na masmorra:", e);

            await mostrarErroSemRedirect("Não foi possível entrar na masmorra.");
            liberarBotao(btnEntrar, "🕸️ Entrar na Masmorra");
        }

        processando = false;
    }

    async function atacarMasmorra() {
        if (processando || combateEncerrado) return;

        processando = true;

        limparTimerAtaqueInimigo();
        bloquearBotao(btnAtacarModal, "Atacando...");

        try {
            const res = await fetch("/masmorra-sombria/atacar", {
                method: "POST"
            });

            const dados = await res.json();

            if (!dados.sucesso) {
                await mostrarAvisoSemRedirect(
                    dados.mensagem || "Não foi possível atacar."
                );

                liberarBotao(btnAtacarModal, "⚔️ Atacar");
                processando = false;
                iniciarTimerAtaqueInimigo();
                return;
            }

            atualizarStatusJogador(dados);
            atualizarModalCombate(dados);

            animarDano("areaImagemInimigo", dados.danoUsuario);
            animarDano("areaImagemUsuario", dados.danoRecebido);

            if (dados.status === "VITORIA") {
                combateEncerrado = true;
                limparTimerAtaqueInimigo();

                bloquearBotao(btnAtacarModal, "Vitória");

                await mostrarResultadoVitoria(dados);

                window.location.replace("/cacador-recompensas");
                return;
            }

            if (dados.status === "DERROTA") {
                combateEncerrado = true;
                limparTimerAtaqueInimigo();

                bloquearBotao(btnAtacarModal, "Derrotado");

                await mostrarResultadoDerrota(dados);

                window.location.replace("/cacador-recompensas");
                return;
            }

            liberarBotao(btnAtacarModal, "⚔️ Atacar");
            processando = false;
            iniciarTimerAtaqueInimigo();

        } catch (e) {
            console.error("Erro ao atacar na masmorra:", e);

            await mostrarErroSemRedirect("Erro ao atacar na masmorra.");
            liberarBotao(btnAtacarModal, "⚔️ Atacar");

            processando = false;
            iniciarTimerAtaqueInimigo();
        }
    }

    async function inimigoAtacarAutomatico() {
        if (processando || combateEncerrado) return;

        processando = true;
        bloquearBotao(btnAtacarModal, "Inimigo atacando...");

        try {
            const res = await fetch("/masmorra-sombria/inimigo-atacar", {
                method: "POST"
            });

            const dados = await res.json();

            if (!dados.sucesso) {
                liberarBotao(btnAtacarModal, "⚔️ Atacar");
                processando = false;
                return;
            }

            atualizarStatusJogador(dados);
            atualizarModalCombate(dados);

            animarDano("areaImagemInimigo", dados.danoUsuario);
            animarDano("areaImagemUsuario", dados.danoRecebido);

            if (dados.status === "DERROTA") {
                combateEncerrado = true;
                limparTimerAtaqueInimigo();

                bloquearBotao(btnAtacarModal, "Derrotado");

                await mostrarResultadoDerrota(dados);

                fecharModalSemFugir();
                processando = false;
                return;
            }

            liberarBotao(btnAtacarModal, "⚔️ Atacar");
            processando = false;
            iniciarTimerAtaqueInimigo();

        } catch (e) {
            console.error("Erro no ataque automático do inimigo:", e);

            await mostrarErroSemRedirect("Erro no ataque do inimigo.");
            liberarBotao(btnAtacarModal, "⚔️ Atacar");

            processando = false;
            iniciarTimerAtaqueInimigo();
        }
    }

    function iniciarTimerAtaqueInimigo() {
        limparTimerAtaqueInimigo();

        if (combateEncerrado) return;

        segundosAtaqueInimigo = 30;

        timerAtaqueInimigo = setInterval(async function () {
            segundosAtaqueInimigo--;

            const mensagemCombateModal =
                document.getElementById("mensagemCombateModal");

            if (mensagemCombateModal && !combateEncerrado) {
                mensagemCombateModal.textContent =
                    "O inimigo atacará em " + segundosAtaqueInimigo + "s. Ataque antes!";
            }

            if (segundosAtaqueInimigo <= 0) {
                limparTimerAtaqueInimigo();
                await inimigoAtacarAutomatico();
            }

        }, 1000);
    }

    function limparTimerAtaqueInimigo() {
        if (timerAtaqueInimigo) {
            clearInterval(timerAtaqueInimigo);
            timerAtaqueInimigo = null;
        }
    }

    function mostrarSemCombate(mensagem, emCooldown = false) {
        limparTimerAtaqueInimigo();

        if (estadoSemCombate) {
            estadoSemCombate.style.display = "block";
        }

        if (textoStatus) {
            textoStatus.textContent = mensagem;
        }

        if (emCooldown) {
            bloquearBotao(btnEntrar, "⏳ Em Cooldown");
        } else {
            liberarBotao(btnEntrar, "🕸️ Entrar na Masmorra");
        }
    }

    function abrirModalCombate(dados) {
        if (!modal) return;

        combateEncerrado = false;
        limparTimerAtaqueInimigo();

        const imagemGuerreiroUsuario =
            document.getElementById("imagemGuerreiroUsuario");

        if (imagemGuerreiroUsuario) {
            imagemGuerreiroUsuario.dataset.carregado = "";
        }

        atualizarModalCombate(dados);

        if (btnAtacarModal) {
            btnAtacarModal.style.display = "inline-flex";
            liberarBotao(btnAtacarModal, "⚔️ Atacar");
        }

        modal.style.display = "flex";

        iniciarTimerAtaqueInimigo();
    }

    function atualizarModalCombate(dados) {
        const imagemGuerreiroUsuario =
            document.getElementById("imagemGuerreiroUsuario");

        const ataqueGuerreiroModal =
            document.getElementById("ataqueGuerreiroModal");

        const vigorGuerreiroModal =
            document.getElementById("vigorGuerreiroModal");

        const vigorMaximoModal =
            document.getElementById("vigorMaximoModal");

        const imagemInimigoModal =
            document.getElementById("imagemInimigoModal");

        const nomeInimigoModal =
            document.getElementById("nomeInimigoModal");

        const hpAtualInimigoModal =
            document.getElementById("hpAtualInimigoModal");

        const hpMaxInimigoModal =
            document.getElementById("hpMaxInimigoModal");

        const ataqueInimigoModal =
            document.getElementById("ataqueInimigoModal");

        const mensagemCombateModal =
            document.getElementById("mensagemCombateModal");

        const nomeGuerreiroUsuario =
            document.getElementById("nomeGuerreiroUsuario");

        atualizarBotaoPocao(
            dados.pocoesVida || dados.pocoesRestantes || 0
        );

        if (
            imagemGuerreiroUsuario &&
            !imagemGuerreiroUsuario.dataset.carregado
        ) {
            obterImagemGuerreiroAleatorioMasmorra()
                .then(guerreiro => {
                    imagemGuerreiroUsuario.onerror = function () {
                        console.error(
                            "Imagem não encontrada:",
                            guerreiro.imagem
                        );

                        this.src =
                            "/images/cacador-recompensas/masmorra/guerreiro-masmorra.webp";
                    };

                    imagemGuerreiroUsuario.src = guerreiro.imagem;

                    if (nomeGuerreiroUsuario) {
                        nomeGuerreiroUsuario.textContent = guerreiro.nome;
                    }

                    imagemGuerreiroUsuario.dataset.carregado = "true";
                });
        }

        if (ataqueGuerreiroModal) {
            ataqueGuerreiroModal.textContent =
                formatarNumero(dados.ataqueBase || dados.danoUsuario || 0);
        }

        if (vigorGuerreiroModal) {
            vigorGuerreiroModal.textContent =
                formatarNumero(dados.vigorAtual || 0);
        }

        const vigorMaximo =
            Number(
                dados.vigorMaximo ||
                dados.energiaGuerreirosPadrao ||
                obterVigorMaximo()
            );

        if (vigorMaximoModal) {
            vigorMaximoModal.textContent = formatarNumero(vigorMaximo);
        }

        atualizarBarraHp(
            "barraVigorModal",
            dados.vigorAtual || 0,
            vigorMaximo || 1
        );

        if (imagemInimigoModal) {
            imagemInimigoModal.src =
                dados.inimigoImagem ||
                "/images/cacador-recompensas/masmorra/goblin-das-sombras.webp";
        }

        if (nomeInimigoModal) {
            nomeInimigoModal.textContent =
                dados.inimigoNome || "Inimigo";
        }

        if (hpAtualInimigoModal) {
            hpAtualInimigoModal.textContent =
                formatarNumero(dados.inimigoHpAtual || 0);
        }

        if (hpMaxInimigoModal) {
            hpMaxInimigoModal.textContent =
                formatarNumero(dados.inimigoHpMax || 0);
        }

        if (ataqueInimigoModal) {
            ataqueInimigoModal.textContent =
                formatarNumero(dados.inimigoAtaque || 0);
        }

        if (mensagemCombateModal) {
            mensagemCombateModal.textContent =
                dados.mensagem || "Combate em andamento.";
        }

        atualizarBarraHp(
            "barraHpInimigoModal",
            dados.inimigoHpAtual || 0,
            dados.inimigoHpMax || 1
        );
    }

    async function fecharModalCombate() {
        if (!modal) return;

        limparTimerAtaqueInimigo();

        if (combateEncerrado) {
            fecharModalSemFugir();
            return;
        }

        try {
            await fetch("/masmorra-sombria/fugir", {
                method: "POST"
            });
        } catch (e) {
            console.error("Erro ao fugir da masmorra:", e);
        }

        fecharModalSemFugir();
    }

    function fecharModalSemFugir() {
        limparTimerAtaqueInimigo();

        if (modal) {
            modal.style.display = "none";
        }

        window.location.replace("/cacador-recompensas");
    }

    function atualizarStatusJogador(dados) {
        if (bossCoinsAtual && dados.bossCoinsAtual !== undefined) {
            bossCoinsAtual.textContent =
                formatarNumero(dados.bossCoinsAtual);
        }

        if (vigorAtual && dados.vigorAtual !== undefined) {
            vigorAtual.textContent =
                formatarNumero(dados.vigorAtual);
        }

        if (ataqueBase && dados.ataqueBase !== undefined) {
            ataqueBase.textContent =
                formatarNumero(dados.ataqueBase);
        }
    }

    function obterVigorMaximo() {
        const vigorMaximoModal =
            document.getElementById("vigorMaximoModal");

        if (vigorMaximoModal) {
            const valor = Number(
                vigorMaximoModal.textContent.replace(/\D/g, "")
            );

            if (valor > 0) return valor;
        }

        const vigorAtualNumero = Number(
            (vigorAtual?.textContent || "0").replace(/\D/g, "")
        );

        return Math.max(1, vigorAtualNumero);
    }

    function atualizarBarraHp(id, hpAtual, hpMax) {
        const barra = document.getElementById(id);

        if (!barra) return;

        const percentual = hpMax > 0
            ? Math.max(0, Math.min((hpAtual / hpMax) * 100, 100))
            : 0;

        barra.style.width = percentual + "%";
    }

    function montarTextoRecompensa(recompensa) {
        if (!recompensa) {
            return "Nenhuma recompensa recebida.";
        }

        if (recompensa.tipo === "BOSS_COINS") {
            return `+${formatarNumero(recompensa.valor)} Boss Coins`;
        }

        if (recompensa.tipo === "XP") {
            return `+${formatarNumero(recompensa.valor)} XP`;
        }

        if (recompensa.tipo === "ATAQUE_BASE") {
            return `+${formatarNumero(recompensa.valor)} Ataque Base`;
        }

        if (recompensa.tipo === "ESPADA_FANEJANTE") {
            return `+${formatarNumero(recompensa.valor)} Espada Flanejante`;
        }

        if (recompensa.tipo === "MACHADO_DILACERADOR") {
            return `+${formatarNumero(recompensa.valor)} Machado Dilacerador`;
        }

        if (recompensa.tipo === "ESCUDO_PRIMORDIAL") {
            return `+${formatarNumero(recompensa.valor)} Escudo Primordial`;
        }

        if (recompensa.tipo === "POCAO_VIGOR") {
            return `+${formatarNumero(recompensa.valor)} Poção de Vigor`;
        }

        if (recompensa.tipo === "NADA") {
            return "Nada ganho";
        }

        return recompensa.nome || "Recompensa recebida.";
    }

    function mostrarResultadoVitoria(dados) {
        const imagemRecompensa = obterImagemRecompensa(dados.recompensa);

        return Swal.fire({
            target: document.body,
            heightAuto: false,
            customClass: {
                title: "swal-game-text"
            },
            icon: "success",
            title: "Vitória!",
            html: `
                <div style="margin-bottom:12px;">
                    ${dados.mensagem || "Você venceu o combate."}
                </div>

                <div class="recompensa-vitoria-box">
                    <img src="${imagemRecompensa}"
                         alt="Recompensa"
                         class="recompensa-vitoria-img">

                    <div class="valor-ganho">
                        ${montarTextoRecompensa(dados.recompensa)}
                    </div>
                </div>

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
            background: "transparent",
            color: "#ffb400"
        });
    }

    function mostrarResultadoDerrota(dados) {
        return Swal.fire({
            customClass: {
                title: "swal-game-error"
            },
            icon: "error",
            title: "Derrota",
            html: `
                <div style="margin-bottom:12px;">
                    ${dados.mensagem || "Você foi derrotado."}
                </div>

                <div>
                    Dano recebido:
                    <strong>${formatarNumero(dados.danoRecebido || 0)}</strong>
                </div>

                <div style="margin-top:10px;">
                    Vigor atual:
                    <strong>${formatarNumero(dados.vigorAtual || 0)}</strong>
                </div>

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
            background: "transparent",
            color: "#ff3b3b"
        });
    }

    function mostrarErro(mensagem) {
        return Swal.fire({
            customClass: {
                title: "swal-game-error"
            },
            icon: "error",
            title: "Erro",
            text: mensagem,
            timer: 5000,
            showConfirmButton: false,
            background: "transparent",
            color: "#ff3b3b"
        }).then(() => {
            window.location.replace("/cacador-recompensas");
        });
    }


    function mostrarAviso(mensagem) {
        return Swal.fire({
            customClass: {
                title: "swal-game-error"
            },
            icon: "warning",
            title: "Atenção",
            text: mensagem,
            timer: 5000,
            showConfirmButton: false,
            background: "transparent",
            color: "#ffb400"
        }).then(() => {
            window.location.replace("/cacador-recompensas");
        });
    }

    function mostrarErroSemRedirect(mensagem) {
        return Swal.fire({
            customClass: {
                title: "swal-game-error"
            },
            icon: "error",
            title: "Erro",
            text: mensagem,
            timer: 5000,
            showConfirmButton: false,
            background: "transparent",
            color: "#ff3b3b"
			}).then(() => {
			         window.location.replace("/cacador-recompensas");
			     });
    }

    function mostrarAvisoSemRedirect(mensagem) {
        return Swal.fire({
            customClass: {
                title: "swal-game-error"
            },
            icon: "warning",
            title: "Atenção",
            text: mensagem,
            timer: 5000,
            showConfirmButton: false,
            background: "transparent",
            color: "#ffb400"
			}).then(() => {
			         window.location.replace("/cacador-recompensas");
			     });
    }

    function bloquearBotao(botao, texto) {
        if (!botao) return;

        botao.disabled = true;
        botao.textContent = texto;
    }

    function liberarBotao(botao, texto) {
        if (!botao) return;

        botao.disabled = false;
        botao.textContent = texto;
    }

    function formatarNumero(valor) {
        if (valor === null || valor === undefined) {
            return "0";
        }

        return Number(valor).toLocaleString("pt-BR");
    }

    function calcularMinutosCooldown(dados) {
        return Math.max(
            1,
            Math.ceil((dados.cooldownSegundosRestantes || 0) / 60)
        );
    }

    function animarDano(areaId, valorDano) {
        const area = document.getElementById(areaId);

        if (!area || !valorDano || valorDano <= 0) return;

        const x = Math.floor(Math.random() * 60) + 20;
        const y = Math.floor(Math.random() * 55) + 20;
        const rotacao = Math.random() > 0.5 ? "35deg" : "-35deg";

        const textoDano = document.createElement("div");
        textoDano.className = "dano-flutuante";
        textoDano.textContent = "-" + formatarNumero(valorDano);
        textoDano.style.left = x + "%";
        textoDano.style.top = y + "%";

        const corte = document.createElement("div");
        corte.className = "corte-ataque";
        corte.style.left = x + "%";
        corte.style.top = y + "%";
        corte.style.setProperty("--rotacao", rotacao);

        area.appendChild(corte);
        area.appendChild(textoDano);

        setTimeout(() => {
            corte.remove();
            textoDano.remove();
        }, 4000);
    }

    function animarAtaqueNoInimigo(dados) {
        const area = document.getElementById("areaImagemInimigo");

        if (!area) return;

        const dano =
            dados.danoCausado ||
            dados.danoUsuario ||
            dados.ataqueBase ||
            0;

        animarDano("areaImagemInimigo", dano);
    }

    async function obterImagemGuerreiroAleatorioMasmorra() {
        const usuarioId =
            document.querySelector('meta[name="user-id"]')?.content;

        if (!usuarioId) {
            return {
                imagem: "/images/guerreiro_padrao.webp",
                nome: "Guerreiro Tradicional"
            };
        }

        const chave = `cache_guerreiros_usuario_${usuarioId}`;

        let guerreiros = [];

        try {
            const cache = localStorage.getItem(chave);

            if (cache) {
                const cacheObj = JSON.parse(cache);
                guerreiros = cacheObj.dados || [];
            }

            if (!guerreiros || guerreiros.length === 0) {
                const response =
                    await fetch(`/guerreiros/usuario/${usuarioId}`, {
                        cache: "no-store"
                    });

                if (response.ok) {
                    guerreiros = await response.json();

                    localStorage.setItem(
                        chave,
                        JSON.stringify({
                            atualizadoEm: Date.now(),
                            dados: guerreiros
                        })
                    );
                }
            }

        } catch (e) {
            console.error(
                "Erro ao buscar guerreiro da masmorra:",
                e
            );
        }

        if (!guerreiros || guerreiros.length === 0) {
            return {
                imagem: "/images/guerreiro_padrao.webp",
                nome: "Guerreiro Tradicional"
            };
        }

        const guerreiro =
            guerreiros[Math.floor(Math.random() * guerreiros.length)];

        return {
            imagem: obterImagemDoGuerreiroMasmorra(guerreiro),
            nome: guerreiro.nome || "Guerreiro"
        };
    }

    function obterImagemDoGuerreiroMasmorra(guerreiro) {
        if (!guerreiro) {
            return "/images/guerreiro_padrao.webp";
        }

        if (
            guerreiro.elite === false ||
            guerreiro.nome === "Guerreiro Tradicional" ||
            guerreiro.id === 0
        ) {
            return "/images/guerreiro_padrao.webp";
        }

        const pasta = obterPastaGuerreiroMasmorra(guerreiro);
        const prefixo = obterPrefixoArquivoMasmorra(guerreiro);

        return `${pasta}/${prefixo}.webp`;
    }

    function obterPastaGuerreiroMasmorra(guerreiro) {
        const nome = guerreiro?.nome || "Guerreira Mística";

        switch (nome) {
            case "Guerreira Mística":
                return "/images/guerreiros_elite/guerreira_mistica";

            case "Valgard":
                return "/images/guerreiros_elite/guerreiro_valgard";

            case "Caçador de Boss":
                return "/images/guerreiros_elite/cacador_boss";

            case "Arqueira Real":
                return "/images/guerreiros_elite/arqueira_real";

            case "Guerreiro Guardião":
                return "/images/guerreiros_elite/guerreiro_guardiao";

            case "Ninja das Sombras":
                return "/images/guerreiros_elite/ninja_das_sombras";

            case "Sentinela Nyara":
                return "/images/guerreiros_elite/sentinela_nyara";

            default:
                return `/images/guerreiros_elite/${normalizarParaPastaMasmorra(nome)}`;
        }
    }

    function obterPrefixoArquivoMasmorra(guerreiro) {
        const nome = guerreiro?.nome || "Guerreira Mística";

        switch (nome) {
            case "Guerreira Mística":
                return "guerreira-mistica";

            case "Valgard":
                return "valgard";

            case "Caçador de Boss":
                return "cacador-boss";

            case "Arqueira Real":
                return "arqueira-real";

            case "Guerreiro Guardião":
                return "guerreiro-guardiao";

            case "Sentinela Nyara":
                return "sentinela-nyara";

            case "Ninja das Sombras":
                return "ninja-das-sombras";

            default:
                return normalizarParaArquivoMasmorra(nome);
        }
    }

    function normalizarParaPastaMasmorra(nome) {
        return nome
            .toLowerCase()
            .normalize("NFD")
            .replace(/[\u0300-\u036f]/g, "")
            .replace(/ç/g, "c")
            .replace(/[^a-z0-9]+/g, "_")
            .replace(/^_+|_+$/g, "");
    }

    function normalizarParaArquivoMasmorra(nome) {
        return nome
            .toLowerCase()
            .normalize("NFD")
            .replace(/[\u0300-\u036f]/g, "")
            .replace(/ç/g, "c")
            .replace(/[^a-z0-9]+/g, "-")
            .replace(/^-+|-+$/g, "");
    }

    async function preloadImagensGuerreiros() {
        const usuarioId =
            document.querySelector('meta[name="user-id"]')?.content;

        if (!usuarioId) return;

        const chave =
            `cache_guerreiros_usuario_${usuarioId}`;

        const cache =
            localStorage.getItem(chave);

        if (!cache) return;

        const guerreiros =
            JSON.parse(cache).dados || [];

        guerreiros.forEach(g => {
            const img = new Image();
            img.src = obterImagemDoGuerreiroMasmorra(g);
        });
    }

    async function usarPocaoMasmorra() {
        if (processando || combateEncerrado) return;

        processando = true;

        if (btnUsarPocaoModal) {
            btnUsarPocaoModal.disabled = true;
            btnUsarPocaoModal.innerHTML = "Usando...";
        }

        try {
            const res = await fetch("/masmorra-sombria/usar-pocao", {
                method: "POST"
            });

            const dados = await res.json();

            if (!dados.sucesso) {
                await mostrarAvisoSemRedirect(
                    dados.mensagem || "Não foi possível usar a poção."
                );

                const restantes = Number(
                    dados.pocoesRestantes ||
                    dados.pocoesVida ||
                    0
                );

                atualizarBotaoPocao(restantes);

                processando = false;
                return;
            }

            if (vigorAtual && dados.vigorAtual !== undefined) {
                vigorAtual.textContent =
                    formatarNumero(dados.vigorAtual);
            }

            const vigorGuerreiroModal =
                document.getElementById("vigorGuerreiroModal");

            if (vigorGuerreiroModal) {
                vigorGuerreiroModal.textContent =
                    formatarNumero(dados.vigorAtual);
            }

            const vigorMaximoAtual =
                Number(
                    dados.vigorMaximo ||
                    dados.energiaGuerreirosPadrao ||
                    obterVigorMaximo()
                );

            atualizarBarraHp(
                "barraVigorModal",
                dados.vigorAtual || 0,
                vigorMaximoAtual || 1
            );

            const pocoesRestantes =
                Number(
                    dados.pocoesRestantes ||
                    dados.pocoesVida ||
                    0
                );

            atualizarBotaoPocao(pocoesRestantes);

            const mensagemCombateModal =
                document.getElementById("mensagemCombateModal");

            if (mensagemCombateModal) {
                mensagemCombateModal.textContent = dados.mensagem;
            }

            processando = false;

        } catch (e) {
            console.error("Erro ao usar poção:", e);

            await mostrarErroSemRedirect("Não foi possível usar a poção.");

            if (btnUsarPocaoModal) {
                btnUsarPocaoModal.disabled = false;
            }

            processando = false;
        }
    }

    function atualizarBotaoPocao(quantidade) {
        if (!btnUsarPocaoModal) return;

        quantidade = Number(quantidade || 0);

        if (quantidade > 0) {
            btnUsarPocaoModal.disabled = false;
            btnUsarPocaoModal.style.display = "flex";
            btnUsarPocaoModal.innerHTML = `
                <img src="/icones/pocao_vigor.webp"
                     alt="Poção"
                     class="icone-pocao">
                Usar Poção (${quantidade})
            `;
        } else {
            btnUsarPocaoModal.disabled = true;
            btnUsarPocaoModal.style.display = "none";
        }
    }

    async function fugirMasmorra() {
        if (processando || combateEncerrado) return;

        const confirmar = await Swal.fire({
            customClass: {
                popup: "swal-game-popup",
                title: "swal-game-text",
                confirmButton: "swal-game-confirm",
                cancelButton: "swal-game-cancel"
            },

            title: "Fugir da Masmorra?",

            html: `
                <p style="color:#ffb400;">
                    Você abandonará este combate.
                </p>

                <p style="color:#ffb400;">
                    Você perderá apenas este combate atual.
                </p>

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

            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "🏃 Fugir",
            cancelButtonText: "Continuar Lutando",
            buttonsStyling: false,
            background: "transparent",
            color: "#ffb400"
        });

        if (!confirmar.isConfirmed) {
            return;
        }

        try {
            processando = true;
            limparTimerAtaqueInimigo();

            await fetch("/masmorra-sombria/fugir", {
                method: "POST"
            });

            window.location.href = "/cacador-recompensas";

        } catch (e) {
            console.error("Erro ao fugir:", e);

            await mostrarErroSemRedirect("Não foi possível fugir.");
            processando = false;
            iniciarTimerAtaqueInimigo();
        }
    }

    function obterImagemRecompensa(recompensa) {
        if (!recompensa || !recompensa.tipo) {
            return "/images/cacador-recompensas/recompensas/nada.webp";
        }

        switch (recompensa.tipo) {
            case "BOSS_COINS":
                return "/images/cacador-recompensas/recompensas/boss_coin.webp";

            case "XP":
                return "/images/cacador-recompensas/recompensas/exp.webp";

            case "ATAQUE_BASE":
                return "/images/cacador-recompensas/recompensas/ataque-base.webp";

            case "POCAO_VIGOR":
                return "/images/cacador-recompensas/recompensas/pocao_vigor.webp";

            case "MACHADO_DILACERADOR":
                return "/images/cacador-recompensas/recompensas/machado_dilacerador.webp";

            case "ESPADA_FANEJANTE":
                return "/images/cacador-recompensas/recompensas/espada_flanejante.webp";

            case "ESCUDO_PRIMORDIAL":
                return "/images/cacador-recompensas/recompensas/escudo_primordial.webp";

            case "NADA":
                return "/images/cacador-recompensas/recompensas/nada.webp";

            default:
                return "/images/cacador-recompensas/recompensas/nada.webp";
        }
    }

    preloadImagensGuerreiros();
});

 */