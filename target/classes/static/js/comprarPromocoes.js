document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;
    if (!usuarioId) return;
	const bossCoinErroImg ="icones/erro_img/boss_coin_erro.webp";
	
    document.querySelectorAll('.btn-promo').forEach(botao => {

        botao.addEventListener('click', async () => {

            const card = botao.closest('.promo-card');
            const tipo = card.dataset.tipo; // normal | avancada | especial | lendaria

            botao.classList.add('animar');
            setTimeout(() => botao.classList.remove('animar'), 1300);

            try {
                const response = await fetch(
                    `/promo/comprar/${usuarioId}/${tipo.toUpperCase()}`,
                    {
                        method: 'POST',
                        credentials: 'include'
                    }
                );

                const mensagem = await response.text(); // 🔥 sempre texto

                // ❌ ERRO (ex: saldo insuficiente)
                if (!response.ok) {
                    Swal.fire({
                        customClass: { title: 'swal-game-error' },
                      
                        title: mensagem || 'Saldo insuficiente.',
						imageUrl: bossCoinErroImg,							  
						imageWidth: 120,											   
						imageHeight: 120,
                        text: 'Não foi possível comprar',
                        background: 'transparent',
                        color: '#ff3b3b'
                    });
                    return;
                }

                const promoImg = {
                    normal: "/icones/promo_normal.webp",
                    avancada: "/icones/promo_avancada.webp",
                    especial: "/icones/promo_especial.webp",
                    lendaria: "/icones/promo_lendaria.webp"
                };

                Swal.fire({
                    customClass: { title: 'swal-game-text' },
                    title: mensagem,
                    html: 'Promoção comprada!',
                    imageUrl: promoImg[tipo],
                    imageWidth: 120,
                    imageHeight: 120,
                    background: 'transparent',
                    color: '#ffb400'
                });

                if (typeof atualizarUsuario === 'function') {
                    atualizarUsuario();
                }

            } catch (err) {
                Swal.fire({
					customClass: { title: 'swal-game-error' },
                    icon: 'error',
                    title: 'Erro inesperado',
                    text: 'Não foi possível comprar a promoção agora.',
                    background: 'transparent',
                    color: '#ff3b3b'
                });
            }
        });
    });
});
