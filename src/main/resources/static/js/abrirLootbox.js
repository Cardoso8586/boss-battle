document.addEventListener('DOMContentLoaded', () => {
    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;
    if (!usuarioId) return;

    const bossCoinErroImg = "icones/erro_img/boss_coin_erro.webp";

    const lootboxesImg = {
        basica: "/icones/lootbox_basica_aberta.webp",
        avancada: "/icones/lootbox_avancada_aberta.webp",
        especial: "/icones/lootbox_especial_aberta.webp",
        lendaria: "/icones/lootbox_lendaria_aberta.webp"
    };

    function gerarHtmlAnuncio(texto) {
        return `
            <div style="margin-bottom: 12px;">${texto}</div>
            <div class="modal-anuncio">
                <iframe
                    src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                    width="468"
                    height="60"
                    scrolling="no"
                    frameborder="0">
                </iframe>
            </div>
        `;
    }

    function travarLootbox(botao, texto = 'Abrindo...') {
        botao.disabled = true;
        botao.dataset.estado = "processando";
        botao.dataset.textoOriginal = botao.innerHTML;
        botao.innerHTML = `<span class="btn-lootbox-loader"></span> ${texto}`;
        botao.classList.add('btn-lootbox-processando', 'animar');
        botao.style.pointerEvents = 'none';
    }

    function destravarLootbox(botao) {
        const textoOriginal = botao.dataset.textoOriginal || 'Abrir';
        botao.disabled = false;
        botao.dataset.estado = "";
        botao.innerHTML = textoOriginal;
        botao.classList.remove('btn-lootbox-processando', 'animar');
        botao.style.pointerEvents = 'auto';
    }

    function sucessoLootbox(botao) {
        botao.classList.remove('btn-lootbox-erro');
        botao.classList.add('btn-lootbox-sucesso');
        botao.innerHTML = '🎁 Aberta!';

        setTimeout(() => {
            botao.classList.remove('btn-lootbox-sucesso', 'animar');
            destravarLootbox(botao);
        }, 1500);
    }

    function erroLootbox(botao) {
        botao.classList.remove('btn-lootbox-sucesso');
        botao.classList.add('btn-lootbox-erro');
        botao.innerHTML = 'Tentar novamente';

        setTimeout(() => {
            botao.classList.remove('btn-lootbox-erro', 'animar');
            destravarLootbox(botao);
        }, 1300);
    }

    document.querySelectorAll('.btn-lootbox').forEach(botao => {
        botao.addEventListener('click', async () => {
            if (botao.dataset.estado === "processando") return;

            const card = botao.closest('.lootbox-card');
            if (!card) return;

            const tipo = card.dataset.tipo;
            if (!tipo) return;

            travarLootbox(botao, 'Abrindo...');

            try {
                const response = await fetch(`/lootbox/abrir/${usuarioId}`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        tipoLootbox: tipo,
                        quantidade: 1
                    })
                });

                const mensagem = await response.text();
                const tipoLower = tipo.toLowerCase();
                const imagem = lootboxesImg[tipoLower] || lootboxesImg.basica;

                if (!response.ok) {
                    erroLootbox(botao);

                    await Swal.fire({
                        customClass: {
                            title: 'swal-game-error'
                        },
                        title: 'Saldo insuficiente',
                        imageUrl: bossCoinErroImg,
                        imageWidth: 120,
                        imageHeight: 120,
                        html: gerarHtmlAnuncio(
                            mensagem || 'Você não tem saldo suficiente para abrir esta lootbox.'
                        ),
                        background: 'transparent',
                        color: '#ff3b3b'
                    });

                    return;
                }

                sucessoLootbox(botao);

                await Swal.fire({
                    customClass: {
                        title: 'swal-game-text'
                    },
                    title: mensagem || `Lootbox ${tipo.charAt(0).toUpperCase() + tipo.slice(1)} aberta!`,
                    imageUrl: imagem,
                    imageWidth: 120,
                    imageHeight: 120,
                    html: gerarHtmlAnuncio(
                        `Lootbox ${tipo.charAt(0).toUpperCase() + tipo.slice(1)} aberta!`
                    ),
                    background: 'transparent',
                    color: '#ffb400'
                });

                if (typeof atualizarUsuario === 'function') {
                    await atualizarUsuario();
                }

            } catch (err) {
                console.error('Erro ao abrir lootbox:', err);

                erroLootbox(botao);

                await Swal.fire({
                    customClass: {
                        title: 'swal-game-error'
                    },
                    icon: 'error',
                    title: 'Erro inesperado',
                    html: gerarHtmlAnuncio('Não foi possível abrir a lootbox agora.'),
                    background: 'transparent',
                    color: '#ff3b3b'
                });
            }
        });
    });
});

/*

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
						html: `
										  		      <div class="modal-anuncio">
										  		        <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
										  		          width="468"
										  		          height="60"
										  		          scrolling="no"
										  		          frameborder="0">
										  		        </iframe>
										  		      </div>
										  		    `,
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
					html: `
									  		      <div class="modal-anuncio">
									  		        <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
									  		          width="468"
									  		          height="60"
									  		          scrolling="no"
									  		          frameborder="0">
									  		        </iframe>
									  		      </div>
									  		    `,
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
					html: `
									  		      <div class="modal-anuncio">
									  		        <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
									  		          width="468"
									  		          height="60"
									  		          scrolling="no"
									  		          frameborder="0">
									  		        </iframe>
									  		      </div>
									  		    `,
					background: 'transparent',
                  //  background: 'rgba(0,0,0,0.85)',
                    color: '#ff3b3b'
                });
            }
        });
    });
});

*/
