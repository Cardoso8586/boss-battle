document.addEventListener('DOMContentLoaded', () => {

    const usuarioId = parseInt(
        document.querySelector('meta[name="user-id"]').getAttribute('content')
    );

    if (!usuarioId) {
        console.error('Usuário não encontrado');
        return;
    }

    /* ===============================
       FUNÇÃO: ATUALIZAR PREÇOS DA LOJA
    =============================== */
    async function atualizarPrecosLoja() {
        try {
            const response = await fetch(`/api/loja/${usuarioId}`);

            if (!response.ok) {
                console.error('Erro ao buscar preços da loja');
                return;
            }

            const dadosLoja = await response.json();

            document.getElementById('preco-guerreiros').textContent =
                `${dadosLoja.precoGuerreiros.toLocaleString('pt-BR')} Boss Coins`;

            document.getElementById('preco-energia').textContent =
                `${dadosLoja.precoEnergia.toLocaleString('pt-BR')} Boss Coins`;

            document.getElementById('preco-ataque-especial').textContent =
                `${dadosLoja.precoAtaqueEspecial.toLocaleString('pt-BR')} Boss Coins`;
				
			document.getElementById('preco-auto-vigor').textContent =
		   `${dadosLoja.PrecoPocaoAutomaticaVigor.toLocaleString('pt-BR')} Boss Coins`;
							  
					
		   document.getElementById('preco-espada-flanejante').textContent =
		   		   `${dadosLoja.PrecoEspadaFlanejante.toLocaleString('pt-BR')} Boss Coins`;		  

		   document.getElementById('preco-machado-dilacerador').textContent =
				    `${dadosLoja.PrecoMachadoDilacerador.toLocaleString('pt-BR')} Boss Coins`;		  
	   
				   
        } catch (error) {
            console.error('Erro ao carregar preços da loja:', error);
        }
    }

    /* ===============================
       CHAMADAS
    =============================== */

    // Atualiza ao entrar na página
    atualizarPrecosLoja();

    // Atualiza automaticamente a cada 10 segundos
    setInterval(atualizarPrecosLoja, 10000);

});


