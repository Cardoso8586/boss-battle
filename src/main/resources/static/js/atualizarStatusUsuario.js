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