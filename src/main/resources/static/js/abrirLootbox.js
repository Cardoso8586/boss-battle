document.addEventListener('DOMContentLoaded', () => {
    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;
    if (!usuarioId) return;

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

                // ❌ ERRO DO BACKEND (ex: sem saldo)
                if (!response.ok) {        
                    Swal.fire({
						customClass: {
					title: 'swal-game-error'
					},
                        icon: 'warning',
                        title: 'Saldo insuficiente',
                        text: mensagem || 'Você não tem saldo suficiente para abrir esta lootbox.',
                        background: 'rgba(0,0,0,0.85)',
                        color: '#ff3b3b' 
                    });
                    return; // ⛔ impede o Swal de sucesso
                }

                // ✅ SUCESSO
                Swal.fire({
                    title: `Lootbox ${tipo.charAt(0).toUpperCase() + tipo.slice(1)} aberta!`,
                    html: mensagem,
                    imageUrl: card.querySelector('.lootbox-img').src,
                    imageWidth: 120,
                    imageHeight: 120,
                    background: 'rgba(0,0,0,0.85)',
                    color: '#ffb400'
                });

                if (typeof atualizarUsuario === 'function') atualizarUsuario();

            } catch (err) {
                Swal.fire({
                    icon: 'error',
                    title: 'Erro inesperado',
                    text: 'Não foi possível abrir a lootbox agora.',
                    background: 'rgba(0,0,0,0.85)',
                    color: '#ff3b3b'
                });
            }
        });
    });
});
