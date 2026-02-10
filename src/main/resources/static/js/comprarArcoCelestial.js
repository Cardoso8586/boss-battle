
document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? Number(meta.content) : null;
    if (!usuarioId) return;

	const arcoCelestialErroImg ="icones/erro_img/arco_celestial_erro.webp";
    const arcoCelestialOkImg ="icones/ok_img/arco_celestial_ok.webp";
    const bossCoinErroImg ="icones/erro_img/boss_coin_erro.webp";
		
    document.querySelectorAll('.btn-comprar').forEach(botao => {

        let emCooldown = false;
        const tempoCooldown = 5; // segundos

        botao.addEventListener('click', async () => {

            if (emCooldown) return;

            const card = botao.closest('.loja-card');
            if (!card) return;

			const quantidadeInput = card.querySelector('.quantidade-arco');

            if (!quantidadeInput) return;

            const quantidade = Number(quantidadeInput.value);

            if (quantidade <= 0 || isNaN(quantidade)) {
                Swal.fire({
					customClass: {
				    title: 'swal-game-error'
					},
                   
                    title: 'Quantidade inválida',
                    text: 'Informe uma quantidade válida.',
					imageUrl: arcoCelestialErroImg,							  
					imageWidth: 60,											   
					imageHeight: 140,
                    background: 'transparent',
                    color: '#ff3b3b'
                });
                return;
            }

            emCooldown = true;
            botao.disabled = true;

            const textoOriginal = botao.textContent;
            botao.textContent = 'Comprando Arco Celestial...';

            try {
                const response = await fetch(`/comprar/arco/celestial/${usuarioId}`,
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
					title: `Você comprou ${quantidade} ${quantidade > 1 ? 'Arcos Celestiais' : 'Arco Celestial'}`,
					html: 'Compra realizada!',
					imageUrl: arcoCelestialOkImg,							  
					imageWidth: 60,											   
					imageHeight: 140,
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
