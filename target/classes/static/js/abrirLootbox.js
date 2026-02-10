document.addEventListener('DOMContentLoaded', () => {
    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;
    if (!usuarioId) return;

	const bossCoinErroImg ="icones/erro_img/boss_coin_erro.webp";
	
	
    document.querySelectorAll('.btn-lootbox').forEach(botao => {
        botao.addEventListener('click', async () => {
            const card = botao.closest('.lootbox-card');
            const tipo = card.dataset.tipo;

            botao.classList.add('animar');
            setTimeout(() => botao.classList.remove('animar'), 3000);

            try {
                const response = await fetch(`/lootbox/abrir/${usuarioId}`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ tipoLootbox: tipo, quantidade: 1 })
                });

                const mensagem = await response.text(); // 🔴 sempre leia o texto

				const lootboxesImg = {
				    basica: "/icones/lootbox_basica_aberta.webp",
				    avancada: "/icones/lootbox_avancada_aberta.webp",
				    especial: "/icones/lootbox_especial_aberta.webp",
				    lendaria: "/icones/lootbox_lendaria_aberta.webp"
				};

				   
				   
                // ❌ ERRO DO BACKEND (ex: sem saldo)
                if (!response.ok) {        
                    Swal.fire({
						customClass: {
					title: 'swal-game-error'
					},
                       
                        title: 'Saldo insuficiente',
                        text: mensagem || 'Você não tem saldo suficiente para abrir esta lootbox.',
						imageUrl: bossCoinErroImg,							  
						imageWidth: 120,											   
						imageHeight: 120,
					    background: 'transparent',
                        color: '#ff3b3b' 
                    });
                    return; // ⛔ impede o Swal de sucesso
                }

				const tipoLower = tipo?.toLowerCase();
				const imagem = lootboxesImg[tipoLower];

				Swal.fire({
					customClass: {      
					title: 'swal-game-text'
					},
					title:  mensagem,
					html:`Lootbox ${tipo.charAt(0).toUpperCase() + tipo.slice(1)} aberta!`,
				   // title: `Lootbox ${tipo.charAt(0).toUpperCase() + tipo.slice(1)} aberta!`,
				   // html: mensagem,
				    imageUrl: imagem,
				    imageWidth: 120,
				    imageHeight: 120,
					background: 'transparent',
				    //background: 'rgba(0,0,0,0.85)',
				    color: '#ffb400'
				});

                if (typeof atualizarUsuario === 'function') atualizarUsuario();

            } catch (err) {
                Swal.fire({
                    icon: 'error',
                    title: 'Erro inesperado',
                    text: 'Não foi possível abrir a lootbox agora.',
					background: 'transparent',
                  //  background: 'rgba(0,0,0,0.85)',
                    color: '#ff3b3b'
                });
            }
        });
    });
});
