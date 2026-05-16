
document.addEventListener('DOMContentLoaded', () => {

    // ======================================
    // CACHE
    // ======================================

    const CACHE_KEY_STATUS =
        "usuario_status_cache";

    const CACHE_IMAGEM_GUERREIRO =
        "cache_imagem_guerreiro";

    const CACHE_TTL =
        1000 * 60 * 60 * 24;

		// ======================================
		// UPDATE
		// ======================================

		// 1 minuto
		const UPDATE_INTERVAL =
		    60000;

    // ======================================
    // USER
    // ======================================

    const meta =
        document.querySelector(
            'meta[name="user-id"]'
        );

    const usuarioId =
        meta
            ? parseInt(
                meta.getAttribute(
                    "content"
                )
            )
            : null;

    let ultimoStatus =
        null;

    let valorAtualBoss =
        null;

    // ======================================
    // HELPERS
    // ======================================

    function el(id) {

        return document.getElementById(
            id
        );
    }

    function formatarNumero(numero) {

        return new Intl.NumberFormat(
            'pt-BR'
        ).format(
            Number(numero || 0)
        );
    }

    function setText(
        id,
        valor
    ) {

        const elemento =
            el(id);

        if (!elemento)
            return;

        const texto =
            String(valor ?? "");

        if (
            elemento.textContent !== texto
        ) {

            elemento.textContent =
                texto;
        }
    }

    function setDisplay(
        elemento,
        display
    ) {

        if (!elemento)
            return;

        if (
            elemento.style.display !== display
        ) {

            elemento.style.display =
                display;
        }
    }

    function setHidden(
        elemento,
        esconder
    ) {

        if (!elemento)
            return;

        if (esconder) {

            elemento.classList.add(
                "hidden"
            );

        } else {

            elemento.classList.remove(
                "hidden"
            );
        }
    }

    function setBarWidth(
        id,
        valor
    ) {

        const barra =
            el(id);

        if (!barra)
            return;

        const width =
            valor + "%";

        if (
            barra.style.width !== width
        ) {

            barra.style.width =
                width;
        }
    }

    // ======================================
    // CACHE IMAGEM
    // ======================================

    function salvarImagemCache(src) {

        if (!src)
            return;

        localStorage.setItem(

            CACHE_IMAGEM_GUERREIRO,

            src
        );
    }

    function pegarImagemCache() {

        return localStorage.getItem(
            CACHE_IMAGEM_GUERREIRO
        );
    }

    // ======================================
    // PRELOAD FORTE
    // ======================================

    function preloadImagem(src) {

        return new Promise(resolve => {

            if (!src) {

                resolve();
                return;
            }

            const img =
                new Image();

            img.decoding =
                "async";

            img.loading =
                "eager";

            img.fetchPriority =
                "high";

            img.onload =
                () => resolve();

            img.onerror =
                () => resolve();

            img.src =
                src;
        });
    }

    // ======================================
    // TROCA IMAGEM
    // ======================================

    async function trocarImagemSemPiscar(
        imgEl,
        novaSrc
    ) {

        if (
            !imgEl ||
            !novaSrc
        ) return;

        const atual =
            imgEl.getAttribute(
                "src"
            );

        // evita recarregar igual
        if (
            atual &&
            atual.includes(novaSrc)
        ) {

            return;
        }

        await preloadImagem(
            novaSrc
        );

        const tempImg =
            new Image();

        tempImg.src =
            novaSrc;

        try {

            await tempImg.decode();

        } catch {}

        requestAnimationFrame(() => {

            imgEl.decoding =
                "async";

            imgEl.fetchPriority =
                "high";

            imgEl.loading =
                "eager";

            imgEl.src =
                novaSrc;

            salvarImagemCache(
                novaSrc
            );
        });
    }

    // ======================================
    // CACHE STATUS
    // ======================================

    function salvarCache(data) {

        if (!data)
            return;

        localStorage.setItem(

            CACHE_KEY_STATUS,

            JSON.stringify({

                time: Date.now(),

                data: data
            })
        );
    }

    function pegarCache() {

        try {

            const cache =
                JSON.parse(

                    localStorage.getItem(
                        CACHE_KEY_STATUS
                    )
                );

            if (!cache)
                return null;

            if (

                Date.now() -
                cache.time >

                CACHE_TTL

            ) {

                localStorage.removeItem(
                    CACHE_KEY_STATUS
                );

                return null;
            }

            return cache.data;

        } catch {

            return null;
        }
    }

    // ======================================
    // COMPARAÇÃO
    // ======================================

    function dadosMudaram(
        novo
    ) {

        return JSON.stringify(
            ultimoStatus
        ) !== JSON.stringify(
            novo
        );
    }

    // ======================================
    // BOSS COINS
    // ======================================

    const bossCoinsEl =
        el("boss_coins");

    const saldoBox =
        document.querySelector(
            ".saldo-box"
        );

    function formatarNumeroBR(
        valor
    ) {

        return Number(
            valor || 0
        ).toLocaleString(
            "pt-BR"
        );
    }

    function mostrarVariacao(
        valor,
        tipo = "ganho"
    ) {

        if (
            !saldoBox ||
            valor <= 0
        ) return;

        const span =
            document.createElement(
                "span"
            );

        span.className =

            tipo === "perda"

                ? "perda-boss"

                : "ganho-boss";

        span.textContent =

            `${tipo === "perda" ? "-" : "+"}${formatarNumeroBR(valor)}`;

        saldoBox.appendChild(
            span
        );

        setTimeout(() => {

            span.remove();

        }, 1500);
    }

    function animarSaldo(
        tipo = "ganho"
    ) {

        if (!bossCoinsEl)
            return;

        bossCoinsEl.classList.remove(

            "animar-saldo",

            "animar-perda"
        );

        void bossCoinsEl.offsetWidth;

        bossCoinsEl.classList.add(

            tipo === "perda"

                ? "animar-perda"

                : "animar-saldo"
        );
    }

    function animarNumero(
        inicio,
        fim,
        duracao = 1600
    ) {

        if (!bossCoinsEl)
            return;

        const start =
            performance.now();

        function update(time) {

            const progress =
                Math.min(

                    (
                        time - start
                    ) / duracao,

                    1
                );

            const valor =
                Math.floor(

                    inicio +

                    (
                        fim - inicio
                    ) * progress
                );

            bossCoinsEl.textContent =

                formatarNumeroBR(
                    valor
                );

            if (progress < 1) {

                requestAnimationFrame(
                    update
                );

            } else {

                bossCoinsEl.textContent =

                    formatarNumeroBR(
                        fim
                    );
            }
        }

        requestAnimationFrame(
            update
        );
    }

    function atualizarBossCoins(
        novoValor
    ) {

        if (!bossCoinsEl)
            return;

        const novo =
            Number(
                novoValor || 0
            );

        if (
            valorAtualBoss === null
        ) {

            valorAtualBoss =
                novo;

            bossCoinsEl.textContent =

                formatarNumeroBR(
                    novo
                );

            return;
        }

        const diff =
            novo -
            valorAtualBoss;

        if (diff > 0) {

            mostrarVariacao(
                diff,
                "ganho"
            );

            animarSaldo(
                "ganho"
            );

            animarNumero(

                valorAtualBoss,

                novo
            );

        } else if (diff < 0) {

            mostrarVariacao(

                Math.abs(diff),

                "perda"
            );

            animarSaldo(
                "perda"
            );

            animarNumero(

                valorAtualBoss,

                novo
            );

        } else {

            bossCoinsEl.textContent =

                formatarNumeroBR(
                    novo
                );
        }

        valorAtualBoss =
            novo;
    }

    // ======================================
    // RENDER IMAGEM
    // ======================================

    async function renderizarImagemGuerreiro(
        data
    ) {

        const guerreiroImage =
            el("guerreiro-image");

        if (!guerreiroImage)
            return;

        let novaImagem =
            "/images/guerreiro_padrao.webp";

        const espadaAtiva =
            (data.ativaEspadaFlanejante ?? 0) > 0;

        const machadoAtivo =
            (data.ativarMachadoDilacerador ?? 0) > 0;

        const escudoAtivo =
            (data.ativarEscudoPrimordial ?? 0) > 0;

        const arcoEquipado =
            (data.arcoAtivo ?? 0) > 0;

        const tipoAtivo =
            data.tipoFlecha || null;

        if (
            escudoAtivo &&
            espadaAtiva
        ) {

            novaImagem =
                "/icones/guerreiro_escudo_espada.webp";

        } else if (
            escudoAtivo &&
            machadoAtivo
        ) {

            novaImagem =
                "/icones/guerreiro_escudo_machado.webp";

        } else if (
            escudoAtivo
        ) {

            novaImagem =
                "/icones/guerreiro_escudo_ativo.webp";

        } else if (
            espadaAtiva
        ) {

            novaImagem =
                "/icones/guerreiroPadrao_espadaFlanejante.webp";

        } else if (
            machadoAtivo
        ) {

            novaImagem =
                "/icones/guerreiroPadrao_machadoDilacerador.webp";

        } else if (
            arcoEquipado
        ) {

            const imagens = {

                FERRO:
                    "/icones/guerreiro_arco_flecha_ferro.webp",

                FOGO:
                    "/icones/guerreiro_arco_flecha_fogo.webp",

                VENENO:
                    "/icones/guerreiro_arco_flecha_veneno.webp",

                DIAMANTE:
                    "/icones/guerreiro_arco_flecha_diamante.webp"
            };

            novaImagem =

                imagens[tipoAtivo] ??

                "/icones/guerreiro_arco_padrao.webp";
        }

        await trocarImagemSemPiscar(

            guerreiroImage,

            novaImagem
        );
    }

    // ======================================
    // RENDER STATUS
    // ======================================

    async function renderizarUsuario(
        data
    ) {

        if (!data)
            return;

        setText(
            "guerreiros",
            formatarNumero(
                data.guerreiros
            )
        );

        setText(
            "xp",
            formatarNumero(
                data.xp
            )
        );

        atualizarBossCoins(
            data.bossCoin
        );

        await renderizarImagemGuerreiro(
            data
        );

        ultimoStatus =
            data;
    }

    // ======================================
    // FETCH
    // ======================================

    async function buscarUsuario() {

        if (!usuarioId)
            return;

        try {

            const res =
                await fetch(

                    `/api/atualizar/status/usuario/${usuarioId}`,

                    {
                        cache:
                            "force-cache"
                    }
                );

            if (!res.ok)
                return;

            const data =
                await res.json();

            salvarCache(
                data
            );

            if (
                dadosMudaram(data)
            ) {

                await renderizarUsuario(
                    data
                );
            }

        } catch (err) {

            console.error(

                "Erro ao atualizar usuário:",

                err
            );
        }
    }

    // ======================================
    // START
    // ======================================

    async function iniciarTela() {

        // imagem instantânea
        const guerreiroImage =
            el("guerreiro-image");

        const imagemCache =
            pegarImagemCache();

        if (
            guerreiroImage &&
            imagemCache
        ) {

            guerreiroImage.src =
                imagemCache;
        }

        // status cache
        const cache =
            pegarCache();

        if (cache) {

            await renderizarUsuario(
                cache
            );
        }

		// primeira carga imediata
		buscarUsuario();

		// atualizações leves
		setInterval(() => {

		    // evita request desnecessário
		    // quando aba estiver minimizada
		    if (document.hidden) {
		        return;
		    }

		    buscarUsuario();

		}, UPDATE_INTERVAL);
    }

    iniciarTela();

});

/*

document.addEventListener('DOMContentLoaded', () => {

    const CACHE_KEY_STATUS = "usuario_status_cache";
    const CACHE_TTL = 30000;
    const UPDATE_INTERVAL = 10000;

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

        if (esconder) {
            elemento.classList.add("hidden");
        } else {
            elemento.classList.remove("hidden");
        }
    }

    function setBarWidth(id, valor) {
        const barra = el(id);
        if (!barra) return;

        const width = valor + "%";

        if (barra.style.width !== width) {
            barra.style.width = width;
        }
    }

    function preloadImagem(src) {
        return new Promise(resolve => {
            if (!src) {
                resolve();
                return;
            }

            const img = new Image();

            img.onload = () => resolve();
            img.onerror = () => resolve();

            img.src = src;
        });
    }

    async function trocarImagemSemPiscar(imgEl, novaSrc) {
        if (!imgEl || !novaSrc) return;

        const atual = imgEl.getAttribute("src");

        if (atual === novaSrc) return;

        await preloadImagem(novaSrc);

        imgEl.src = novaSrc;
    }

    function salvarCache(data) {
        if (!data) return;

        localStorage.setItem(CACHE_KEY_STATUS, JSON.stringify({
            time: Date.now(),
            data: data
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

        bossCoinsEl.classList.add(tipo === "perda" ? "animar-perda" : "animar-saldo");
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

        const mensagem = restantes === 0
            ? `Limite diário de saques atingido. (${quantidadeSaquesHoje}/${limite})`
            : `Você ainda pode fazer ${restantes} ${restantes === 1 ? "saque" : "saques"} hoje. (${quantidadeSaquesHoje}/${limite})`;

        setText("infoSaques", mensagem);


        const damageContainer = el("damageContainer");
        setHidden(damageContainer, data.energiaGuerreiros <= 0);

        await renderizarImagemGuerreiro(data);

        ultimoStatus = data;
    }

    async function renderizarImagemGuerreiro(data) {
        const guerreiroImage = el("guerreiro-image");
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

        if (!guerreiroImage) return;

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
            if (texto) texto.textContent = `${Math.round((desgaste / 100) * 100)}%`;

            setHidden(wrapperEscudo, false);
            setHidden(tituloEscudo, false);
            if (barraEscudo) barraEscudo.value = desgasteEscudo;
            if (textoEscudo) textoEscudo.textContent = `${Math.round((desgasteEscudo / 200) * 100)}%`;

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
            if (texto) texto.textContent = `${Math.round((desgasteMachado / 200) * 100)}%`;

            setHidden(wrapperEscudo, false);
            setHidden(tituloEscudo, false);
            if (barraEscudo) barraEscudo.value = desgasteEscudo;
            if (textoEscudo) textoEscudo.textContent = `${Math.round((desgasteEscudo / 200) * 100)}%`;

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

            if (textoEscudo) textoEscudo.textContent = `${Math.round((desgasteEscudo / 200) * 100)}%`;

            if (btnEquiparArco) btnEquiparArco.classList.add("hidden");

            novaImagem = "/icones/guerreiro_escudo_ativo.webp";

        } else if (espadaAtiva) {
            setHidden(container, false);

            if (barra) {
                barra.max = 100;
                barra.value = desgaste;
            }

            if (texto) texto.textContent = `${Math.round((desgaste / 100) * 100)}%`;

            if (btnAtivarMachado) btnAtivarMachado.style.display = "none";
            if (btnEquiparArco) btnEquiparArco.classList.add("hidden");

            novaImagem = "/icones/guerreiroPadrao_espadaFlanejante.webp";

        } else if (machadoAtivo) {
            setHidden(container, false);

            if (barra) {
                barra.max = 200;
                barra.value = desgasteMachado;
            }

            if (texto) texto.textContent = `${Math.round((desgasteMachado / 200) * 100)}%`;

            if (btnAtivarEspada) btnAtivarEspada.style.display = "none";
            if (btnEquiparArco) btnEquiparArco.classList.add("hidden");

            novaImagem = "/icones/guerreiroPadrao_machadoDilacerador.webp";

        } else if (arcoEquipado) {
            setHidden(container, false);

            if (barra) {
                barra.max = 200;
                barra.value = durabilidade;
            }

            if (texto) texto.textContent = `${Math.round((durabilidade / 200) * 100)}%`;

            if (btnEquiparArco) btnEquiparArco.classList.add("hidden");

            const imagens = {
                FERRO: "/icones/guerreiro_arco_flecha_ferro.webp",
                FOGO: "/icones/guerreiro_arco_flecha_fogo.webp",
                VENENO: "/icones/guerreiro_arco_flecha_veneno.webp",
                DIAMANTE: "/icones/guerreiro_arco_flecha_diamante.webp"
            };

            novaImagem = imagens[tipoAtivo] ?? "/icones/guerreiro_arco_padrao.webp";
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

    async function iniciarTela() {
        const cache = pegarCache();

        if (cache) {
            await renderizarUsuario(cache);
        }

        buscarUsuario();

        setInterval(buscarUsuario, UPDATE_INTERVAL);
    }

    iniciarTela();
});
/*
document.addEventListener('DOMContentLoaded', () => {
    const btnRecarregar = document.getElementById('btnRecarregar');
    const energiaAtual = document.getElementById('energiaAtual');
    const energiaBar = document.getElementById('energiaBar');
    const energiaMaxima = document.getElementById('energiaMaxima');
    const ganhosRef = document.getElementById('ganhosPendentesSpan');
    const ataqueBase = document.getElementById('damage');
    const guerreiros = document.getElementById('guerreiros');
    const ataquePorMinuto = document.getElementById('ataquePorMinuto');
    const xp = document.getElementById('xp');
    const guerreirosRetaguarda = document.getElementById('guerreirosRetaguarda');
    const espadaFlanejante = document.getElementById('espadaFlanejante');
    const machadoDilacerador = document.getElementById('machadoDilacerador');
    const generatingImageAtaque = document.getElementById('generating-image-ataque');
    const generatingImage = document.getElementById('generating-image');
    const btnAtivarEspada = document.getElementById('btnAtivarEspadaFlanejante');
    const btnAtivarMachado = document.getElementById('btnAtivarMachadoDilacerador');
    const btnEquiparArco = document.getElementById('btnEquiparArco');
    const escudoPrimordial = document.getElementById('escudoPrimordial');
   // const btnAtivarEscudo = document.getElementById('btnAtivarEscudoPrimordial');

    // Atualizar Central de Comando
    const quantidade_guerreiros = document.getElementById('quantidade_guerreiros');
    const capacidade_vigor = document.getElementById('capacidade_vigor');
    const ataques_minutos = document.getElementById('ataques_minutos');
    const ataque_especial = document.getElementById('ataque_especial');
    const pocao_vigor = document.getElementById('pocao_vigor');
    const espada_flanejante = document.getElementById('espada_flanejante');
    const machado_dilacerador = document.getElementById('machado_dilacerador');
    const arco_celestial = document.getElementById('arco_celestial');
    const flecha_diamante = document.getElementById('flecha_diamante');
    const flecha_fogo = document.getElementById('flecha_fogo');
    const flecha_veneno = document.getElementById('flecha_veneno');
    const flecha_ferro = document.getElementById('flecha_ferro');
    const ataqueEspecial = document.getElementById('damage');
    const ultimoGanho = document.getElementById('ganho');

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;

    function formatarNumero(numero) {
        return new Intl.NumberFormat('pt-BR').format(numero);
    }

    const bossCoinsEl = document.getElementById("boss_coins");
    const saldoBox = document.querySelector(".saldo-box");
    let valorAtualBoss = null;

    function formatarNumeroBR(valor) {
        return Number(valor || 0).toLocaleString("pt-BR");
    }

    function mostrarVariacao(valor, tipo = "ganho") {
        if (!saldoBox || valor <= 0) return;

        const el = document.createElement("span");
        el.className = tipo === "perda" ? "perda-boss" : "ganho-boss";
        el.textContent = `${tipo === "perda" ? "-" : "+"}${formatarNumeroBR(valor)}`;

        saldoBox.appendChild(el);

        setTimeout(() => {
            el.remove();
        }, 1500);
    }

    function animarSaldo(tipo = "ganho") {
        if (!bossCoinsEl) return;

        bossCoinsEl.classList.remove("animar-saldo", "animar-perda");
        void bossCoinsEl.offsetWidth;

        if (tipo === "perda") {
            bossCoinsEl.classList.add("animar-perda");
        } else {
            bossCoinsEl.classList.add("animar-saldo");
        }
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

    async function atualizarUsuario() {
        try {
            const res = await fetch(`/api/atualizar/status/usuario/${usuarioId}`);
            const data = await res.json();

            const energia = Math.max(0, data.energiaGuerreiros);
            const energiaMax = data.energiaGuerreirosPadrao;
            const damageContainer = document.getElementById("damageContainer");
            const verificarEnergia = data.energiaGuerreiros;

            const ativoEscudo = data.ativarEscudoPrimordial ?? 0;
            const desgasteEscudo = data.desgasteEscudoPrimordial ?? 0;

            ganhosRef.textContent = formatarNumero(data.ganhosRef);
            ataqueBase.textContent = formatarNumero(data.ataqueBase);

            atualizarBossCoins(data.bossCoin);

            guerreiros.textContent = formatarNumero(data.guerreiros);
            ataquePorMinuto.textContent = formatarNumero(data.ataquePorMinuto);
            xp.textContent = formatarNumero(data.xp);

            const valor = data.ultimoValorRecebido;
            ultimoGanho.textContent = valor > 0 ? "+" + formatarNumero(valor) : "0";

            guerreirosRetaguarda.textContent = formatarNumero(data.guerreirosRetaguarda);
            espadaFlanejante.textContent = formatarNumero(data.espadaflanejante);
            machadoDilacerador.textContent = formatarNumero(data.machadoDilacerador);
            escudoPrimordial.textContent = formatarNumero(data.escudoPrimordial);

            energiaAtual.textContent = formatarNumero(energia);
            energiaMaxima.textContent = formatarNumero(energiaMax);

            // Central de Comando
            quantidade_guerreiros.textContent = formatarNumero(data.totalGuerreiros);
            capacidade_vigor.textContent = formatarNumero(data.capacidadeVigor);
            ataques_minutos.textContent = formatarNumero(data.ataquePorMinuto);
            ataque_especial.textContent = formatarNumero(data.ataqueBase);
            pocao_vigor.textContent = formatarNumero(data.pocaoVigor);
            espada_flanejante.textContent = formatarNumero(data.espadaflanejante);
            machado_dilacerador.textContent = formatarNumero(data.machadoDilacerador);
            arco_celestial.textContent = formatarNumero(data.arcoInventario);
            flecha_diamante.textContent = formatarNumero(data.flechaDiamante);
            flecha_fogo.textContent = formatarNumero(data.flechaFogo);
            flecha_veneno.textContent = formatarNumero(data.flechaVeneno);
            flecha_ferro.textContent = formatarNumero(data.flechaFerro);
            ataqueEspecial.textContent = formatarNumero(data.ataqueBase);

            const limite = 3;
            const quantidadeSaquesHoje = data.quantidadeSaquesHoje ?? 0;
            const restantes = limite - quantidadeSaquesHoje;

            let mensagem = "";
            if (restantes === 0) {
                mensagem = `Limite diário de saques atingido. (${quantidadeSaquesHoje}/${limite})`;
            } else {
                const palavra = restantes === 1 ? "saque" : "saques";
                mensagem = `Você ainda pode fazer ${restantes} ${palavra} hoje. (${quantidadeSaquesHoje}/${limite})`;
            }
            document.getElementById("infoSaques").innerText = mensagem;

            const percentualEnergia = Math.max(0, (energia / energiaMax) * 100);
            energiaBar.style.width = percentualEnergia + '%';

            if (verificarEnergia <= 0) {
                damageContainer.classList.add("hidden");
            } else {
                damageContainer.classList.remove("hidden");
            }

            if (energia / energiaMax < 0.2) {
                btnRecarregar.disabled = false;
            } else {
                btnRecarregar.disabled = true;
            }

          
            // Barra principal: espada / machado / arco
            const container = document.getElementById('desgasteContainer');
            const barra = document.getElementById('barradesgaste');
            const texto = document.getElementById('desgasteTexto');

            const ativa = data.ativaEspadaFlanejante ?? 0;
            const desgaste = data.desgasteEspadaFlanejante ?? 0;

            const ativaMachado = data.ativarMachadoDilacerador ?? 0;
            const desgasteMachado = data.desgasteMachadoDilacerador ?? 0;

            const arcoAtivo = data.arcoAtivo ?? 0;
            const durabilidade = data.durabilidadeArco ?? 0;
            const tipoAtivo = data.tipoFlecha || null;
            const arcoEquipado = arcoAtivo > 0;

            const guerreiroImage = document.getElementById('guerreiro-image');
            const quantGuerreirosPadrao = data.guerreiros ?? 0;
            const quantGuerreirosRetaguarda = data.guerreirosRetaguarda ?? 0;

            // Barra separada do escudo
            const barraEscudo = document.getElementById('barradesgasteEscudo');
            const textoEscudo = document.getElementById('desgasteTextoEscudo');
            const wrapperEscudo = document.querySelector('.barra-wrapper-escudo');
            const tituloEscudo = document.querySelector('.titulo-durabilidade-escudo');
           // const infoEscudo = document.querySelector('.escudo-primordial-ativo-info');

			// Sem guerreiros
			if (quantGuerreirosPadrao <= 0 && quantGuerreirosRetaguarda <= 0) {
			    guerreiroImage.style.display = "none";
			    generatingImageAtaque.style.display = "none";
			    generatingImage.style.display = "none";

			    container.classList.add("hidden");
			    barra.value = 0;
			    texto.textContent = "";

			    if (wrapperEscudo) wrapperEscudo.classList.add("hidden");
			    if (tituloEscudo) tituloEscudo.classList.add("hidden");
			   // if (infoEscudo) infoEscudo.classList.add("hidden");
			    if (barraEscudo) barraEscudo.value = 0;
			    if (textoEscudo) textoEscudo.textContent = "";

			    return;
			}

			// Com guerreiros
			guerreiroImage.style.display = "block";
			generatingImageAtaque.style.display = "block";
			generatingImage.style.display = "block";

			const espadaAtiva = ativa > 0;
			const machadoAtivo = ativaMachado > 0;
			const escudoAtivo = ativoEscudo > 0;


			// ===============================
			// PRIORIDADE VISUAL (ÚNICO CONTROLE)
			// ===============================

			// RESET GLOBAL
			btnAtivarEspada.style.display = "block";
			btnAtivarMachado.style.display = "block";
			//btnEquiparArco.classList.remove("hidden");

			//guerreiroImage.src = "/images/guerreiro_padrao.webp";

			// reset barra principal
			container.classList.add("hidden");
			barra.value = 0;
			barra.max = 100;
			texto.textContent = "";

			// reset barra escudo
			if (wrapperEscudo) wrapperEscudo.classList.add("hidden");
			if (tituloEscudo) tituloEscudo.classList.add("hidden");
			//if (infoEscudo) infoEscudo.classList.add("hidden");

			if (barraEscudo) {
			    barraEscudo.value = 0;
			    barraEscudo.max = 200;
			}
			if (textoEscudo) textoEscudo.textContent = "";


			// ===============================
			// COMBINAÇÕES
			// ===============================

			if (escudoAtivo && espadaAtiva) {

			    // espada
			    container.classList.remove("hidden");
			    barra.max = 100;
			    barra.value = desgaste;
			    texto.textContent = `${Math.round((desgaste / 100) * 100)}%`;

			    // escudo
			    wrapperEscudo.classList.remove("hidden");
			    tituloEscudo.classList.remove("hidden");
			   // infoEscudo.classList.remove("hidden");

			    barraEscudo.value = desgasteEscudo;
			    textoEscudo.textContent = `${Math.round((desgasteEscudo / 200) * 100)}%`;

			    btnAtivarEspada.style.display = "none";
			    btnAtivarMachado.style.display = "none";
			  //  btnAtivarEscudo.classList.add("hidden");
			    btnEquiparArco.classList.add("hidden");

			    guerreiroImage.src = "/icones/guerreiro_escudo_espada.webp";

			} else if (escudoAtivo && machadoAtivo) {

			    // machado
			    container.classList.remove("hidden");
			    barra.max = 200;
			    barra.value = desgasteMachado;
			    texto.textContent = `${Math.round((desgasteMachado / 200) * 100)}%`;

			    // escudo
			    wrapperEscudo.classList.remove("hidden");
			    tituloEscudo.classList.remove("hidden");
			   // infoEscudo.classList.remove("hidden");

			    barraEscudo.value = desgasteEscudo;
			    textoEscudo.textContent = `${Math.round((desgasteEscudo / 200) * 100)}%`;

			    btnAtivarEspada.style.display = "none";
			    btnAtivarMachado.style.display = "none";
			   // btnAtivarEscudo.classList.add("hidden");
			    btnEquiparArco.classList.add("hidden");

			    guerreiroImage.src = "/icones/guerreiro_escudo_machado.webp";

			}else if (escudoAtivo) {

			    // só escudo
			    if (wrapperEscudo) wrapperEscudo.classList.remove("hidden");
			    if (tituloEscudo) tituloEscudo.classList.remove("hidden");
			   // if (infoEscudo) infoEscudo.classList.remove("hidden");

			    if (barraEscudo) {
			        barraEscudo.max = 200;
			        barraEscudo.value = desgasteEscudo;
			    }

			    if (textoEscudo) {
			        textoEscudo.textContent = `${Math.round((desgasteEscudo / 200) * 100)}%`;
			    }

			    // barra principal continua escondida
			    container.classList.add("hidden");
			    barra.value = 0;
			    barra.max = 100;
			    texto.textContent = "";

			   // btnAtivarEscudo.classList.add("hidden");
			    btnEquiparArco.classList.add("hidden");

			    guerreiroImage.src = "/icones/guerreiro_escudo_ativo.webp";
				
				}
				else if (espadaAtiva) {

			    container.classList.remove("hidden");
			    barra.max = 100;
			    barra.value = desgaste;
			    texto.textContent = `${Math.round((desgaste / 100) * 100)}%`;

			    btnAtivarMachado.style.display = "none";
			   // btnAtivarEscudo.classList.add("hidden");
			    btnEquiparArco.classList.add("hidden");

			    guerreiroImage.src = "/icones/guerreiroPadrao_espadaFlanejante.webp";

			} else if (machadoAtivo) {

			    container.classList.remove("hidden");
			    barra.max = 200;
			    barra.value = desgasteMachado;
			    texto.textContent = `${Math.round((desgasteMachado / 200) * 100)}%`;

			    btnAtivarEspada.style.display = "none";
			   // btnAtivarEscudo.classList.add("hidden");
			    btnEquiparArco.classList.add("hidden");

			    guerreiroImage.src = "/icones/guerreiroPadrao_machadoDilacerador.webp";

			} else if (arcoEquipado) {

			    container.classList.remove("hidden");
			    barra.max = 200;
			    barra.value = durabilidade;
			    texto.textContent = `${Math.round((durabilidade / 200) * 100)}%`;

			    btnEquiparArco.classList.add("hidden");
			   // btnAtivarEscudo.classList.add("hidden");

			    if (tipoAtivo) {
			        const imagens = {
			            FERRO: "icones/guerreiro_arco_flecha_ferro.webp",
			            FOGO: "icones/guerreiro_arco_flecha_fogo.webp",
			            VENENO: "icones/guerreiro_arco_flecha_veneno.webp",
			            DIAMANTE: "icones/guerreiro_arco_flecha_diamante.webp"
			        };

			        guerreiroImage.src = imagens[tipoAtivo] ?? "icones/guerreiro_arco_padrao.webp";
			    } else {
			        guerreiroImage.src = "icones/guerreiro_arco_padrao.webp";
			    }

			} else {

			    guerreiroImage.src = "/images/guerreiro_padrao.webp";
			}

        } catch (err) {
            console.error(err);
        }
    }

    atualizarUsuario();
    setInterval(atualizarUsuario, 10000);
});

*/