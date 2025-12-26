document.addEventListener('DOMContentLoaded', () => {
    const btn = document.querySelector('[data-item="ataqueEspecial"]');

    if (!btn) return;

    btn.addEventListener('click', async () => {
        const card = btn.closest('.loja-card');

        const quantidade = parseInt(
            card.querySelector('#quantidade-ataque').value
        );

        if (isNaN(quantidade) || quantidade <= 0) {
            Swal.fire({
                icon: 'warning',
                title: 'Quantidade inválida',
                text: 'Informe uma quantidade válida.',
                confirmButtonText: 'Ok'
            });
            return;
        }

        const precoUnitario = parseFloat(
            card.querySelector('.preco').textContent.replace(/[^\d]/g, '')
        );

        try {
            const res = await fetch(`/comprar/ataque-especial/${usuarioId}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    quantidade: quantidade,
                   
                })
            });

            if (res.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Ataque especial aprimorado!',
                    text: `Você comprou ${quantidade} unidade(s) de ataque especial!`,
                    confirmButtonText: 'Ok',
                    background: '#fff',
                    color: '#000'
                });

                // Atualiza os dados do usuário
                if (typeof atualizarUsuario === 'function') {
                    atualizarUsuario();
                }

            } else {
                const custoEstimado = quantidade * precoUnitario;
                const saldoElement = document.getElementById('boss_coins');
                const saldo = saldoElement
                    ? parseFloat(saldoElement.textContent.replace(/\D/g, ''))
                    : 0;

                Swal.fire({
                    icon: 'warning',
                    title: 'Saldo insuficiente',
                    text: `Você precisa de ${custoEstimado.toLocaleString('pt-BR')} Boss Coins, mas só tem ${saldo.toLocaleString('pt-BR')} Boss Coins.`,
                    confirmButtonText: 'Ok',
                    background: '#fff',
                    color: '#000'
                });
            }
        } catch (err) {
            console.error(err);
            Swal.fire({
                icon: 'error',
                title: 'Erro',
                text: 'Erro ao tentar comprar ataque especial.',
                confirmButtonText: 'Ok'
            });
        }
    });
});