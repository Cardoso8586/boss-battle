document.addEventListener('DOMContentLoaded', () => {

	
	
    const CACHE_KEY_STATUS = "usuario_status_cache";
    const CACHE_IMAGEM_GUERREIRO = "cache_imagem_guerreiro";
    const CACHE_TTL = 1000 * 60 * 60 * 24;
    const UPDATE_INTERVAL = 60000;

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;

    let ultimoStatus = null;
    let valorAtualBoss = null;

    function el(id) {
        return document.getElementById(id);
    }

    function formatarNumero(numero) {
        return new Intl.NumberFormat('pt-BR').format(Number(numero || 0));
    }

    function setText(id, valor) {
        const elemento = el(id);
        if (!elemento) return;

        const texto = String(valor ?? "");

        if (elemento.textContent !== texto) {
            elemento.textContent = texto;
        }
    }

    function setDisplay(elemento, display) {
        if (!elemento) return;

        if (elemento.style.display !== display) {
            elemento.style.display = display;
        }
    }

    function setHidden(elemento, esconder) {
        if (!elemento) return;

        esconder
            ? elemento.classList.add("hidden")
            : elemento.classList.remove("hidden");
    }

    function salvarImagemCache(src) {
        if (!src) return;
        localStorage.setItem(CACHE_IMAGEM_GUERREIRO, src);
    }

    function pegarImagemCache() {
        return localStorage.getItem(CACHE_IMAGEM_GUERREIRO);
    }

    function preloadImagem(src) {
        return new Promise(resolve => {
            if (!src) {
                resolve();
                return;
            }

            const img = new Image();
            img.decoding = "async";
            img.loading = "eager";
            img.fetchPriority = "high";
            img.onload = () => resolve();
            img.onerror = () => resolve();
            img.src = src;
        });
    }

    async function trocarImagemSemPiscar(imgEl, novaSrc) {
        if (!imgEl || !novaSrc) return;

        const atual = imgEl.getAttribute("src");

        if (atual && atual.includes(novaSrc)) return;

        await preloadImagem(novaSrc);

        const tempImg = new Image();
        tempImg.src = novaSrc;

        try {
            await tempImg.decode();
        } catch {}

        requestAnimationFrame(() => {
            imgEl.decoding = "async";
            imgEl.fetchPriority = "high";
            imgEl.loading = "eager";
            imgEl.src = novaSrc;
            salvarImagemCache(novaSrc);
        });
    }

    function salvarCache(data) {
        if (!data) return;

        localStorage.setItem(CACHE_KEY_STATUS, JSON.stringify({
            time: Date.now(),
            data
        }));
    }

    function pegarCache() {
        try {
            const cache = JSON.parse(localStorage.getItem(CACHE_KEY_STATUS));

            if (!cache) return null;

            if (Date.now() - cache.time > CACHE_TTL) {
                localStorage.removeItem(CACHE_KEY_STATUS);
                return null;
            }

            return cache.data;

        } catch {
            return null;
        }
    }

    function dadosMudaram(novo) {
        return JSON.stringify(ultimoStatus) !== JSON.stringify(novo);
    }

    const bossCoinsEl = el("boss_coins");
    const saldoBox = document.querySelector(".saldo-box");

    function formatarNumeroBR(valor) {
        return Number(valor || 0).toLocaleString("pt-BR");
    }

    function mostrarVariacao(valor, tipo = "ganho") {
        if (!saldoBox || valor <= 0) return;

        const span = document.createElement("span");
        span.className = tipo === "perda" ? "perda-boss" : "ganho-boss";
        span.textContent = `${tipo === "perda" ? "-" : "+"}${formatarNumeroBR(valor)}`;

        saldoBox.appendChild(span);

        setTimeout(() => {
            span.remove();
        }, 1500);
    }

    function animarSaldo(tipo = "ganho") {
        if (!bossCoinsEl) return;

        bossCoinsEl.classList.remove("animar-saldo", "animar-perda");
        void bossCoinsEl.offsetWidth;

        bossCoinsEl.classList.add(
            tipo === "perda" ? "animar-perda" : "animar-saldo"
        );
    }

    function animarNumero(inicio, fim, duracao = 1600) {
        if (!bossCoinsEl) return;

        const start = performance.now();

        function update(time) {
            const progress = Math.min((time - start) / duracao, 1);
            const valor = Math.floor(inicio + (fim - inicio) * progress);

            bossCoinsEl.textContent = formatarNumeroBR(valor);

            if (progress < 1) {
                requestAnimationFrame(update);
            } else {
                bossCoinsEl.textContent = formatarNumeroBR(fim);
            }
        }

        requestAnimationFrame(update);
    }

    function atualizarBossCoins(novoValor) {
        if (!bossCoinsEl) return;

        const novo = Number(novoValor || 0);

        if (valorAtualBoss === null) {
            valorAtualBoss = novo;
            bossCoinsEl.textContent = formatarNumeroBR(novo);
            return;
        }

        const diff = novo - valorAtualBoss;

        if (diff > 0) {
            mostrarVariacao(diff, "ganho");
            animarSaldo("ganho");
            animarNumero(valorAtualBoss, novo);

        } else if (diff < 0) {
            mostrarVariacao(Math.abs(diff), "perda");
            animarSaldo("perda");
            animarNumero(valorAtualBoss, novo);

        } else {
            bossCoinsEl.textContent = formatarNumeroBR(novo);
        }

        valorAtualBoss = novo;
    }

    async function renderizarUsuario(data) {
        if (!data) return;

        const energia = Math.max(0, data.energiaGuerreiros || 0);
        const energiaMax = data.energiaGuerreirosPadrao || 1;

        setText("ganhosPendentesSpan", formatarNumero(data.ganhosRef));
        setText("damage", formatarNumero(data.ataqueBase));
        setText("guerreiros", formatarNumero(data.guerreiros));
        setText("ataquePorMinuto", formatarNumero(data.ataquePorMinuto));
        setText("xp", formatarNumero(data.xp));

        const valor = data.ultimoValorRecebido;
        setText("ganho", valor > 0 ? "+" + formatarNumero(valor) : "0");

        setText("guerreirosRetaguarda", formatarNumero(data.guerreirosRetaguarda));
        setText("espadaFlanejante", formatarNumero(data.espadaflanejante));
        setText("machadoDilacerador", formatarNumero(data.machadoDilacerador));
        setText("escudoPrimordial", formatarNumero(data.escudoPrimordial));

        setText("energiaAtual", formatarNumero(energia));
        setText("energiaMaxima", formatarNumero(energiaMax));

        setText("quantidade_guerreiros", formatarNumero(data.totalGuerreiros));
        setText("capacidade_vigor", formatarNumero(data.capacidadeVigor));
        setText("ataques_minutos", formatarNumero(data.ataquePorMinuto));
        setText("ataque_especial", formatarNumero(data.ataqueBase));
        setText("pocao_vigor", formatarNumero(data.pocaoVigor));
        setText("espada_flanejante", formatarNumero(data.espadaflanejante));
        setText("machado_dilacerador", formatarNumero(data.machadoDilacerador));
        setText("arco_celestial", formatarNumero(data.arcoInventario));
        setText("flecha_diamante", formatarNumero(data.flechaDiamante));
        setText("flecha_fogo", formatarNumero(data.flechaFogo));
        setText("flecha_veneno", formatarNumero(data.flechaVeneno));
        setText("flecha_ferro", formatarNumero(data.flechaFerro));

        atualizarBossCoins(data.bossCoin);

        const limite = 3;
        const quantidadeSaquesHoje = data.quantidadeSaquesHoje ?? 0;
        const restantes = limite - quantidadeSaquesHoje;

        const mensagem = restantes <= 0
            ? `Limite diário de saques atingido. (${quantidadeSaquesHoje}/${limite})`
            : `Você ainda pode fazer ${restantes} ${restantes === 1 ? "saque" : "saques"} hoje. (${quantidadeSaquesHoje}/${limite})`;

        setText("infoSaques", mensagem);

        const energiaBar = el("energiaBar");
        if (energiaBar) {
            const percentualEnergia = Math.max(0, Math.min(100, (energia / energiaMax) * 100));
            energiaBar.style.width = percentualEnergia + "%";
        }

        const btnRecarregar = el("btnRecarregar");
        if (btnRecarregar) {
            btnRecarregar.disabled = energia / energiaMax >= 0.2;
        }

        const damageContainer = el("damageContainer");
        setHidden(damageContainer, data.energiaGuerreiros <= 0);

        await renderizarImagemGuerreiro(data);

        ultimoStatus = data;
    }

    async function renderizarImagemGuerreiro(data) {
		const guerreiroImage =
		    el("userGuerreiroImagem") || el("guerreiro-image");

		if (!guerreiroImage) return;
		
		
        const generatingImageAtaque = el("generating-image-ataque");
        const generatingImage = el("generating-image");

        const container = el("desgasteContainer");
        const barra = el("barradesgaste");
        const texto = el("desgasteTexto");

        const barraEscudo = el("barradesgasteEscudo");
        const textoEscudo = el("desgasteTextoEscudo");
        const wrapperEscudo = document.querySelector('.barra-wrapper-escudo');
        const tituloEscudo = document.querySelector('.titulo-durabilidade-escudo');

        const btnAtivarEspada = el("btnAtivarEspadaFlanejante");
        const btnAtivarMachado = el("btnAtivarMachadoDilacerador");
        const btnEquiparArco = el("btnEquiparArco");

      

        const quantGuerreirosPadrao = data.guerreiros ?? 0;
        const quantGuerreirosRetaguarda = data.guerreirosRetaguarda ?? 0;

        if (quantGuerreirosPadrao <= 0 && quantGuerreirosRetaguarda <= 0) {
            setDisplay(guerreiroImage, "none");
            setDisplay(generatingImageAtaque, "none");
            setDisplay(generatingImage, "none");

            setHidden(container, true);

            if (barra) barra.value = 0;
            if (texto) texto.textContent = "";

            setHidden(wrapperEscudo, true);
            setHidden(tituloEscudo, true);

            if (barraEscudo) barraEscudo.value = 0;
            if (textoEscudo) textoEscudo.textContent = "";

            return;
        }

        setDisplay(guerreiroImage, "block");
        setDisplay(generatingImageAtaque, "block");
        setDisplay(generatingImage, "block");

        const espadaAtiva = (data.ativaEspadaFlanejante ?? 0) > 0;
        const machadoAtivo = (data.ativarMachadoDilacerador ?? 0) > 0;
        const escudoAtivo = (data.ativarEscudoPrimordial ?? 0) > 0;
        const arcoEquipado = (data.arcoAtivo ?? 0) > 0;

        const desgaste = data.desgasteEspadaFlanejante ?? 0;
        const desgasteMachado = data.desgasteMachadoDilacerador ?? 0;
        const desgasteEscudo = data.desgasteEscudoPrimordial ?? 0;
        const durabilidade = data.durabilidadeArco ?? 0;
        const tipoAtivo = data.tipoFlecha || null;

        if (btnAtivarEspada) btnAtivarEspada.style.display = "block";
        if (btnAtivarMachado) btnAtivarMachado.style.display = "block";

        setHidden(container, true);

        if (barra) {
            barra.value = 0;
            barra.max = 100;
        }

        if (texto) texto.textContent = "";

        setHidden(wrapperEscudo, true);
        setHidden(tituloEscudo, true);

        if (barraEscudo) {
            barraEscudo.value = 0;
            barraEscudo.max = 200;
        }

        if (textoEscudo) textoEscudo.textContent = "";

        let novaImagem = "/images/guerreiro_padrao.webp";

        if (escudoAtivo && espadaAtiva) {
            setHidden(container, false);

            if (barra) {
                barra.max = 100;
                barra.value = desgaste;
            }

            if (texto) {
                texto.textContent = `${Math.round((desgaste / 100) * 100)}%`;
            }

            setHidden(wrapperEscudo, false);
            setHidden(tituloEscudo, false);

            if (barraEscudo) {
                barraEscudo.value = desgasteEscudo;
            }

            if (textoEscudo) {
                textoEscudo.textContent = `${Math.round((desgasteEscudo / 200) * 100)}%`;
            }

            if (btnAtivarEspada) btnAtivarEspada.style.display = "none";
            if (btnAtivarMachado) btnAtivarMachado.style.display = "none";
            if (btnEquiparArco) btnEquiparArco.classList.add("hidden");

            novaImagem = "/icones/guerreiro_escudo_espada.webp";

        } else if (escudoAtivo && machadoAtivo) {
            setHidden(container, false);

            if (barra) {
                barra.max = 200;
                barra.value = desgasteMachado;
            }

            if (texto) {
                texto.textContent = `${Math.round((desgasteMachado / 200) * 100)}%`;
            }

            setHidden(wrapperEscudo, false);
            setHidden(tituloEscudo, false);

            if (barraEscudo) {
                barraEscudo.value = desgasteEscudo;
            }

            if (textoEscudo) {
                textoEscudo.textContent = `${Math.round((desgasteEscudo / 200) * 100)}%`;
            }

            if (btnAtivarEspada) btnAtivarEspada.style.display = "none";
            if (btnAtivarMachado) btnAtivarMachado.style.display = "none";
            if (btnEquiparArco) btnEquiparArco.classList.add("hidden");

            novaImagem = "/icones/guerreiro_escudo_machado.webp";

        } else if (escudoAtivo) {
            setHidden(wrapperEscudo, false);
            setHidden(tituloEscudo, false);

            if (barraEscudo) {
                barraEscudo.max = 200;
                barraEscudo.value = desgasteEscudo;
            }

            if (textoEscudo) {
                textoEscudo.textContent = `${Math.round((desgasteEscudo / 200) * 100)}%`;
            }

            setHidden(container, true);

            if (barra) {
                barra.value = 0;
                barra.max = 100;
            }

            if (texto) texto.textContent = "";

            if (btnEquiparArco) btnEquiparArco.classList.add("hidden");

            novaImagem = "/icones/guerreiro_escudo_ativo.webp";

        } else if (espadaAtiva) {
            setHidden(container, false);

            if (barra) {
                barra.max = 100;
                barra.value = desgaste;
            }

            if (texto) {
                texto.textContent = `${Math.round((desgaste / 100) * 100)}%`;
            }

            if (btnAtivarMachado) btnAtivarMachado.style.display = "none";
            if (btnEquiparArco) btnEquiparArco.classList.add("hidden");

            novaImagem = "/icones/guerreiroPadrao_espadaFlanejante.webp";

        } else if (machadoAtivo) {
            setHidden(container, false);

            if (barra) {
                barra.max = 200;
                barra.value = desgasteMachado;
            }

            if (texto) {
                texto.textContent = `${Math.round((desgasteMachado / 200) * 100)}%`;
            }

            if (btnAtivarEspada) btnAtivarEspada.style.display = "none";
            if (btnEquiparArco) btnEquiparArco.classList.add("hidden");

            novaImagem = "/icones/guerreiroPadrao_machadoDilacerador.webp";

        } else if (arcoEquipado) {
            setHidden(container, false);

            if (barra) {
                barra.max = 200;
                barra.value = durabilidade;
            }

            if (texto) {
                texto.textContent = `${Math.round((durabilidade / 200) * 100)}%`;
            }

            if (btnEquiparArco) btnEquiparArco.classList.add("hidden");

            const imagens = {
                FERRO: "/icones/guerreiro_arco_flecha_ferro.webp",
                FOGO: "/icones/guerreiro_arco_flecha_fogo.webp",
                VENENO: "/icones/guerreiro_arco_flecha_veneno.webp",
                DIAMANTE: "/icones/guerreiro_arco_flecha_diamante.webp"
            };

            novaImagem = imagens[tipoAtivo] ?? "/icones/guerreiro_arco_padrao.webp";

        } else {
            novaImagem = "/images/guerreiro_padrao.webp";
        }

        await trocarImagemSemPiscar(guerreiroImage, novaImagem);
    }

    async function buscarUsuario() {
        if (!usuarioId) return;

        try {
            const res = await fetch(`/api/atualizar/status/usuario/${usuarioId}`, {
                cache: "no-store"
            });

            if (!res.ok) return;

            const data = await res.json();

            salvarCache(data);

            if (dadosMudaram(data)) {
                await renderizarUsuario(data);
            }

        } catch (err) {
            console.error("Erro ao atualizar usuário:", err);
        }
    }


//------------------------------------------------------
    async function iniciarTela() {
        const guerreiroImage = el("guerreiro-image");
        const imagemCache = pegarImagemCache();

        if (guerreiroImage && imagemCache) {
            guerreiroImage.src = imagemCache;
        }

        const cache = pegarCache();

        if (cache) {
            await renderizarUsuario(cache);
        }

        buscarUsuario();

        setInterval(() => {
            if (document.hidden) return;

            buscarUsuario();

        }, UPDATE_INTERVAL);
    }

    iniciarTela();
});

