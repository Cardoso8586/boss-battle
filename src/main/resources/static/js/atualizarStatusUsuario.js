document.addEventListener('DOMContentLoaded', () => {
    const btnRecarregar = document.getElementById('btnRecarregar');
    const energiaAtual = document.getElementById('energiaAtual');
    const energiaBar = document.getElementById('energiaBar');
    const energiaMaxima = document.getElementById('energiaMaxima');
	const ganhosRef = document.getElementById('ganhosPendentesSpan');
	const ataqueBase = document.getElementById('damage');
	const bossCoin = document.getElementById('boss_coins');
    const guerreiros = document.getElementById('guerreiros');
	const ataquePorMinuto  = document.getElementById('ataquePorMinuto');
	const xp = document.getElementById('xp');
	const guerreirosRetaguarda  = document.getElementById('guerreirosRetaguarda');
	const espadaFlanejante  = document.getElementById('espadaFlanejante');
    const machadoDilacerador = document.getElementById('machadoDilacerador');
	const generatingImageAtaque =  document.getElementById('generating-image-ataque');
	const generatingImage =  document.getElementById('generating-image');
	const btnAtivarEspada = document.getElementById('btnAtivarEspadaFlanejante');
	const btnAtivarMachado = document.getElementById('btnAtivarMachadoDilacerador'); 
    const btnEquiparArco = document.getElementById('btnEquiparArco');
	
	//Atualizr Central de Comando
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
	const xpComando = document.getElementById('xp_comando');
	const nivelComando = document.getElementById('nivel_comando');
	const bossCoinComando = document.getElementById('boss_coins_comando');
   const ataqueEspecial  = document.getElementById('damage');
	
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

	function mostrarGanho(valor) {
	  if (!saldoBox || valor <= 0) return;

	  const el = document.createElement("span");
	  el.className = "ganho-boss";
	  el.textContent = `+${formatarNumeroBR(valor)}`;

	  saldoBox.appendChild(el);

	  setTimeout(() => el.remove(), 1500);
	}

	function animarSaldo() {
	  bossCoinsEl.classList.remove("animar-saldo");
	  void bossCoinsEl.offsetWidth;
	  bossCoinsEl.classList.add("animar-saldo");
	}

	function animarNumero(inicio, fim, duracao = 1600) {
	  const start = performance.now();

	  function update(time) {
	    const progress = Math.min((time - start) / duracao, 1);
	    const valor = Math.floor(inicio + (fim - inicio) * progress);

	    bossCoinsEl.textContent = formatarNumeroBR(valor);

	    if (progress < 1) {
	      requestAnimationFrame(update);
	    }
	  }

	  requestAnimationFrame(update);
	}

	function atualizarBossCoins(novoValor) {
	  const novo = Number(novoValor || 0);

	  // PRIMEIRA VEZ → NÃO ANIMA
	  if (valorAtualBoss === null) {
	    valorAtualBoss = novo;
	    bossCoinsEl.textContent = formatarNumeroBR(novo);
	    return;
	  }

	  const diff = novo - valorAtualBoss;

	  if (diff > 0) {
	    mostrarGanho(diff);
	    animarSaldo();
	    animarNumero(valorAtualBoss, novo);
	  } else {
	    bossCoinsEl.textContent = formatarNumeroBR(novo);
	  }

	  valorAtualBoss = novo;
	}
	
	/**
	 * function animarMoedaSubindo(quantidade = 1) {
	  const container = document.querySelector(".saldo-box");

	  if (!container) return;

	  for (let i = 0; i < quantidade; i++) {
	    const moeda = document.createElement("img");
	    moeda.src = "/icones/boss_coin.webp";
	    moeda.className = "moeda-animada";

	    // leve variação lateral
	    const offset = (Math.random() - 0.5) * 40;
	    moeda.style.left = `calc(50% + ${offset}px)`;

	    container.appendChild(moeda);

	    setTimeout(() => {
	      moeda.remove();
	    }, 1400);
	  }
	}
	
	====== css do mesmo.. 
	.moeda-animada {
	  position: absolute;
	  left: 50%;
	  top: 0;
	  transform: translateX(-50%) scale(0.8);
	  width: 28px;
	  height: 28px;
	  pointer-events: none;
	  opacity: 0;

	  animation: subirMoeda 1.4s ease-out forwards;
	  filter: drop-shadow(0 0 6px rgba(255, 215, 0, 0.7));
	}

	@keyframes subirMoeda {
	  0% {
	    opacity: 0;
	    transform: translateX(-50%) translateY(10px) scale(0.7) rotate(0deg);
	  }

	  20% {
	    opacity: 1;
	    transform: translateX(-50%) translateY(0) scale(1) rotate(10deg);
	  }

	  60% {
	    transform: translateX(-50%) translateY(-20px) scale(1.1) rotate(-10deg);
	  }

	  100% {
	    opacity: 0;
	    transform: translateX(-50%) translateY(-45px) scale(0.9) rotate(15deg);
	  }
	}

	 */
	

    async function atualizarUsuario() {
        try {
            const res = await fetch(`/api/atualizar/status/usuario/${usuarioId}`);
            const data = await res.json();

            const energia = Math.max(0, data.energiaGuerreiros);
            const energiaMax = data.energiaGuerreirosPadrao;
			const damageContainer = document.getElementById("damageContainer");
			const verificarEnergia = data.energiaGuerreiros;
			
			ganhosRef.textContent = formatarNumero(data.ganhosRef);
			ataqueBase.textContent = formatarNumero(data.ataqueBase);
			
			//bossCoin.textContent = formatarNumero(data.bossCoin);
			atualizarBossCoins(data.bossCoin);
			
			
			guerreiros.textContent = formatarNumero(data.guerreiros);
			ataquePorMinuto.textContent = formatarNumero(data.ataquePorMinuto);
			xp.textContent = formatarNumero(data.xp);
			guerreirosRetaguarda.textContent  = formatarNumero(data.guerreirosRetaguarda);
			espadaFlanejante.textContent = formatarNumero(data.espadaflanejante);
			machadoDilacerador.textContent = formatarNumero(data.machadoDilacerador);
            // Atualiza valores
            energiaAtual.textContent = formatarNumero(energia);
            energiaMaxima.textContent = formatarNumero(energiaMax);

			
			
			  //Atualizar Central de Comando
			
			    xpComando.textContent = formatarNumero(data.xp);
			    nivelComando.textContent = formatarNumero(data.nivel);
	            bossCoinComando.textContent = formatarNumero(data.bossCoin);
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
				
				
				//--- limiye diario de saques
				const limite = 3;

				// valor que veio da API
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
				//--- limiye diario de saques
			
				// FIM Atualizar Central de Comando
			
            const percentualEnergia = Math.max(0, (energia / energiaMax) * 100);
            energiaBar.style.width = percentualEnergia + '%';

			//verificar se usuario esta ativo e esconder/ou bloquear o dano recebido
						if (verificarEnergia <= 0) {
						    damageContainer.classList.add("hidden");
						} else {
						    damageContainer.classList.remove("hidden");
						}

            // Liberar botão quando energia < 20%
            if (energia / energiaMax < 0.2) {
                btnRecarregar.disabled = false;
            } else {
                btnRecarregar.disabled = true;
            }
		
			//==================================================================================
			const container = document.getElementById('desgasteContainer');
			const barra = document.getElementById('barradesgaste');
			const texto = document.getElementById('desgasteTexto');

			const ativa = data.ativaEspadaFlanejante;        // ex: 0 ou 1
			const desgaste = data.desgasteEspadaFlanejante; // ex: 100 → 0
           
			
			const ativaMachado = data.ativarMachadoDilacerador;        // ex: 0 ou 1
		    const desgasteMachado = data.desgasteMachadoDilacerador; // ex: 100 → 0
			       
			
			//status arco
		    const arcoAtivo = data.arcoAtivo;
		    const durabilidade = data.durabilidadeArco;
		    const tipoAtivo = data.tipoFlecha || null;
            const arcoEquipado =  arcoAtivo > 0;
			
			              //debug JSON
							/*
									Swal.fire({
									    title: "JSON RECEBIDO",
									    html: `<pre style="text-align:left">${JSON.stringify(data, null, 2)}</pre>`,
									    width: 600
									});
								
								*/
						
		

			const guerreiroImage = document.getElementById('guerreiro-image');

			const quantGuerreirosPadrao = data.guerreiros;
			const quantGuerreirosRetaguarda = data.guerreirosRetaguarda;

			// ================================
			// ❌ NÃO EXISTE NENHUM GUERREIRO
			// ================================
			if (quantGuerreirosPadrao <= 0 && quantGuerreirosRetaguarda <= 0) {
			    guerreiroImage.style.display = "none";
			    generatingImageAtaque.style.display = "none";
			    generatingImage.style.display = "none";
			    container.classList.add("hidden");

			    barra.value = 0;
			    texto.textContent = "";
			    return;
			}

			// ================================
			// ✅ EXISTE PELO MENOS UM GUERREIRO
			// ================================
			guerreiroImage.style.display = "block";
			generatingImageAtaque.style.display = "block";
			generatingImage.style.display = "block";

			// ================================
			// ⚔️ PRIORIDADE: ESPADA > MACHADO
			// ================================
			if (ativa !== null && ativa > 0) {
			    // 🗡️ ESPADA ATIVA
			    container.classList.remove("hidden");
				btnAtivarMachado.style.display = "none";
			    const desgasteMax = 100;
			    barra.max = desgasteMax;
			    barra.value = desgaste;

			    texto.textContent = `${Math.round((desgaste / desgasteMax) * 100)}%`;

			    guerreiroImage.src = "/icones/guerreiroPadrao_espadaFlanejante.webp";

			} else if (ativaMachado !== null && ativaMachado > 0) {
			    // 🪓 MACHADO ATIVO
			    container.classList.remove("hidden");
				btnAtivarEspada.style.display = "none";
			    const desgasteMax = 200;
			    barra.max = desgasteMax;
			    barra.value = desgasteMachado;

			    texto.textContent = `${Math.round((desgasteMachado / desgasteMax) * 100)}%`;

			    guerreiroImage.src = "/icones/guerreiroPadrao_machadoDilacerador.webp";

			} 
			
			else if (arcoEquipado) {
			    // 🏹 Arco equipado
			    container.classList.remove("hidden");
			    btnEquiparArco.classList.add("hidden");

			    const desgasteMax = 200;
			    barra.max = desgasteMax;
			    barra.value = durabilidade;

			    texto.textContent = `${Math.round((durabilidade / desgasteMax) * 100)}%`;

			    if (tipoAtivo) {
			        const imagens = {
			            FERRO: "icones/guerreiro_arco_flecha_ferro.webp",
			            FOGO: "icones/guerreiro_arco_flecha_fogo.webp",
			            VENENO: "icones/guerreiro_arco_flecha_veneno.webp",
			            DIAMANTE: "icones/guerreiro_arco_flecha_diamante.webp"
			        };

			        guerreiroImage.src = imagens[tipoAtivo] ?? "icones/guerreiro_arco_padrao.webp";
			    }
			}

			else {
			    // ⚔️ SEM ARMA ATIVA → IMAGEM PADRÃO
			    container.classList.add("hidden");

			    barra.value = 0;
			    barra.max = 100;
			    texto.textContent = "";

			    guerreiroImage.src = "/images/guerreiro_padrao.webp";
			}


			
		

        } catch (err) {
            console.error(err);
        }
  
}



    // Atualiza ao carregar e a cada 10s
    atualizarUsuario();
    setInterval(atualizarUsuario, 10000);
});


