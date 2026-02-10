/**
 * Comprar machado dilacerador
 */
document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? Number(meta.content) : null;
    if (!usuarioId) return;

	const machadoDilaceradorErroImg ="icones/erro_img/machado_dilacerador_erro.webp";
	const machadoDilaceradorOkImg ="icones/ok_img/machado_dilacerador_ok.webp";
	const bossCoinErroImg ="icones/erro_img/boss_coin_erro.webp";
		
    document.querySelectorAll('.btn-comprar').forEach(botao => {

        let emCooldown = false;
        const tempoCooldown = 5; // segundos

        botao.addEventListener('click', async () => {

            if (emCooldown) return;

            const card = botao.closest('.loja-card');
            if (!card) return;

            const quantidadeInput = card.querySelector('.quantidade-machado');
            if (!quantidadeInput) return;

            const quantidade = Number(quantidadeInput.value);

            if (quantidade <= 0 || isNaN(quantidade)) {
                Swal.fire({
					customClass: {
					title: 'swal-game-error'
					},
                    title: 'Quantidade inválida',
                    text: 'Informe uma quantidade válida.',
					imageUrl: machadoDilaceradorErroImg,							  
					imageWidth: 120,											   
					imageHeight: 160,
                    background: 'transparent',
                    color: '#ff3b3b'
                });
                return;
            }

            emCooldown = true;
            botao.disabled = true;

            const textoOriginal = botao.textContent;
            botao.textContent = 'Comprando Machado Dilacerador...';

            try {
                const response = await fetch(
                    `/comprar/machado/dilacerador/${usuarioId}`,
                    {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ quantidade })
                    }
                );

                if (!response.ok) throw new Error('Saldo insuficiente');

                Swal.fire({
					customClass: {      
					title: 'swal-game-text'
					},
					title: `Você comprou ${quantidade} machado${quantidade > 1 ? 's' : ''} dilacerador.`,
					html: 'Compra realizada!',
					imageUrl: machadoDilaceradorOkImg,							  
					imageWidth: 120,											   
					imageHeight: 160,
                    timer: 5000,
                    showConfirmButton: false,
                    background: 'transparent',
                    color: '#ffb400'
                });

                if (typeof atualizarUsuario === 'function') {
                    atualizarUsuario();
                }

            } catch (err) {
                Swal.fire({
					customClass: {
				    title: 'swal-game-error'
					},
                   
                    title: 'Erro na compra',
                    text: 'Saldo insuficiente ou erro no servidor.',
					imageUrl: bossCoinErroImg,							  
					imageWidth: 120,											   
					imageHeight: 120,
                    background: 'transparent',
                    color: '#ff3b3b'
                });
            } finally {
                setTimeout(() => {
                    emCooldown = false;
                    botao.disabled = false;
                    botao.textContent = textoOriginal;
                }, tempoCooldown * 1000);
            }
        });
    });
});
