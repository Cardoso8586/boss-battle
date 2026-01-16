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
			
			
			//==================================================================================
			const container = document.getElementById('desgasteContainer');
			const barra = document.getElementById('barradesgaste');
			const texto = document.getElementById('desgasteTexto');

			const ativa = data.ativaEspadaFlanejante;        // ex: 0 ou 1
			const desgaste = data.desgasteEspadaFlanejante; // ex: 100 → 0
           
			
			const ativaMachado = data.ativarMachadoDilacerador;        // ex: 0 ou 1
		    const desgasteMachado = data.desgasteMachadoDilacerador; // ex: 100 → 0
			       
			
			
		

			
			const guerreiroImage = document.getElementById('guerreiro-image');

			// PRIORIDADE: ESPADA > MACHADO
			if (ativa !== null && ativa > 0) {
			    // 🗡️ ESPADA ATIVA
			    container.classList.remove('hidden');

			    const desgasteAtual = desgaste;
			    const desgasteMax = 100;

			    barra.max = desgasteMax;
			    barra.value = desgasteAtual;

			    const porcentagem = Math.round(
			        (desgasteAtual / desgasteMax) * 100
			    );

			    texto.textContent = `${porcentagem}%`;

			    guerreiroImage.src = "/icones/guerreiroPadrao_espadaFlanejante.webp";

			} else if (ativaMachado !== null && ativaMachado > 0) {
			    // 🪓 MACHADO ATIVO
			    container.classList.remove('hidden');

			    const desgasteAtual = desgasteMachado;
			    const desgasteMax = 200; // 🔥 aqui está o segredo

			    barra.max = desgasteMax;
			    barra.value = desgasteAtual;

			    const porcentagem = Math.round(
			        (desgasteAtual / desgasteMax) * 100
			    );

			    texto.textContent = `${porcentagem}%`;

			    guerreiroImage.src = "/icones/guerreiroPadrao_machadoDilacerador.webp";

			} else {
			    // ❌ NENHUMA ARMA ATIVA
			    container.classList.add('hidden');

			    barra.value = 0;
			    barra.max = 100;
			    texto.textContent = '';

			    guerreiroImage.src = "/images/guerreiro_padrao.webp";
			}

			
			
/**
 * 	// PRIORIDADE: ESPADA > MACHADO
			if (ativa !== null && ativa > 0) {
			    // ✅ ESPADA ATIVA
			    container.classList.remove('hidden');

			    barra.value = desgaste;
			    texto.textContent = `${desgaste}%`;

			    guerreiroImage.src = "/icones/guerreiroPadrao_espadaFlanejante.webp";

			} else if (ativaMachado !== null && ativaMachado > 0) {
			    // ✅ MACHADO ATIVO
			    container.classList.remove('hidden');

			    barra.value = desgasteMachado;
			    texto.textContent = `${desgasteMachado}%`;

			    guerreiroImage.src = "/icones/guerreiroPadrao_machadoDilacerador.webp";

			} else {
			    // ❌ NENHUMA ARMA ATIVA
			    container.classList.add('hidden');

			    barra.value = 0;
			    texto.textContent = '';

			    guerreiroImage.src = "/images/guerreiro_padrao.webp";
			}

				
 * 
 */
			
			
			

        } catch (err) {
            console.error(err);
        }
  
}



    // Atualiza ao carregar e a cada 10s
    atualizarUsuario();
    setInterval(atualizarUsuario, 10000);
});


