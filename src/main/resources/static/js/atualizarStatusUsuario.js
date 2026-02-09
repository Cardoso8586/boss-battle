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
	
	
    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;

    function formatarNumero(numero) {
        return new Intl.NumberFormat('pt-BR').format(numero);
    }

    async function atualizarUsuario() {
        try {
            const res = await fetch(`/api/atualizar/status/usuario/${usuarioId}`);
            const data = await res.json();

            const energia = Math.max(0, data.energiaGuerreiros);
            const energiaMax = data.energiaGuerreirosPadrao;
			
			
			
			ganhosRef.textContent = formatarNumero(data.ganhosRef);
			ataqueBase.textContent = formatarNumero(data.ataqueBase);
			bossCoin.textContent = formatarNumero(data.bossCoin);
			guerreiros.textContent = formatarNumero(data.guerreiros);
			ataquePorMinuto.textContent = formatarNumero(data.ataquePorMinuto);
			xp.textContent = formatarNumero(data.xp);
			guerreirosRetaguarda.textContent  = formatarNumero(data.guerreirosRetaguarda);
			espadaFlanejante.textContent = formatarNumero(data.espadaflanejante);
			machadoDilacerador.textContent = formatarNumero(data.machadoDilacerador);
            // Atualiza valores
            energiaAtual.textContent = formatarNumero(energia);
            energiaMaxima.textContent = formatarNumero(energiaMax);

            const percentualEnergia = Math.max(0, (energia / energiaMax) * 100);
            energiaBar.style.width = percentualEnergia + '%';

			
			
            // Liberar botão quando energia < 50%
            if (energia / energiaMax < 0.2) {
                btnRecarregar.disabled = false;
            } else {
                btnRecarregar.disabled = true;
            }
			
			//======================================= status do arco =============================
			
			
			 
			
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


