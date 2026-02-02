
document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? Number(meta.content) : null;
    if (!usuarioId) return;

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
                    icon: 'warning',
                    title: 'Quantidade inválida',
                    text: 'Informe uma quantidade válida.',
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
                    title: 'Compra realizada!',
					html: `Você comprou ${quantidade} ${quantidade > 1 ? 'Arcos Celestiais' : 'Arco Celestial'}`,
                    imageUrl: '/icones/arco.webp',
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
                    icon: 'error',
                    title: 'Erro na compra',
                    text: 'Saldo insuficiente ou erro no servidor.',
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
