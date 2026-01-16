/**
 * Comprar Espada Flanejante
 */

document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute('content')) : null;
    if (!usuarioId) return;

    const botoesComprar = document.querySelectorAll('.btn-comprar');

    botoesComprar.forEach(botao => {

        let emCooldownCompra = false;
        const tempoCompra = 5; // segundos

        botao.addEventListener('click', async () => {

            // 🔒 trava por botão
            if (emCooldownCompra) return;

            const card = botao.closest('.loja-card');
            if (!card) return;

            // 📦 quantidade (ID — exatamente como no HTML)
            const quantidadeInput = card.querySelector('.quantidade-espadas');
            if (!quantidadeInput) return;

            const quantidade = parseInt(quantidadeInput.value);

            // ✅ validação
            if (!quantidade || quantidade <= 0) {
                Swal.fire({
                    customClass: { title: 'swal-game-error' },
                    icon: 'warning',
                    title: 'Quantidade inválida',
                    text: 'Informe uma quantidade válida.',
                    confirmButtonText: 'Ok',
                    background: 'transparent',
                    color: '#ff3b3b'
                });
                return;
            }

            emCooldownCompra = true;
            botao.disabled = true;

            const textoOriginal = botao.innerText;
           // let restante = tempoCompra;

            // ⏳ texto no botão
            botao.innerText = `Comprando espada flanejante...`;
		
            // ⚠️ preço apenas visual
            const precoSpan = card.querySelector('.preco');
            const precoText = precoSpan
                ? precoSpan.textContent.replace(/[^\d]/g, '')
                : '0';

            const precoUnitario = parseFloat(precoText);

            try {
                const response = await fetch(`/comprar/espada/flanejante/${usuarioId}`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ quantidade })
                });

                if (response.ok) {

                    if (quantidade === 1) {
                        Swal.fire({
                            customClass: { title: 'swal-game-text' },
                            title: 'Compra realizada!',
                            html: `Você comprou ${quantidade} espada Flanejante.`,
                            imageUrl: '/icones/espada_flanejante.webp',
                            imageWidth: 40,
                            imageHeight: 140,
                            imageAlt: 'Espada Flanejante',
                            timer: 5000,
                            showConfirmButton: false,
                            background: 'transparent',
                            color: '#ffb400'
                        });
                    } else {
                        Swal.fire({
                            customClass: { title: 'swal-game-text' },
                            title: 'Compra realizada!',
                            html: `Você comprou ${quantidade} espadas Flanejante.`,
                            imageUrl: '/icones/espada_flanejante.webp',
                            imageWidth: 40,
                            imageHeight: 140,
                            imageAlt: 'Espada Flanejante',
                            timer: 5000,
                            showConfirmButton: false,
                            background: 'transparent',
                            color: '#ffb400'
                        });
                    }

                    if (typeof atualizarUsuario === 'function') {
                        atualizarUsuario();
                    }

                } else {
                    // ❌ saldo insuficiente (backend decide)
                    const custoEstimado = quantidade * precoUnitario;

                    const saldoElement = document.getElementById('boss_coins');
                    const saldo = saldoElement
                        ? parseFloat(saldoElement.textContent.replace(/\D/g, ''))
                        : 0;

                    Swal.fire({
                        customClass: { title: 'swal-game-error' },
                        icon: 'warning',
                        title: 'Saldo insuficiente',
                        text: `Custo estimado: ${custoEstimado.toLocaleString('pt-BR')} | Saldo: ${saldo.toLocaleString('pt-BR')}`,
                        timer: 5000,
                        showConfirmButton: false,
                        background: 'transparent',
                        color: '#ff3b3b'
                    });
                }

            } catch (err) {
                console.error(err);

                Swal.fire({
                    customClass: { title: 'swal-game-error' },
                    icon: 'error',
                    title: 'Erro',
                    text: 'Erro ao tentar comprar.',
                    timer: 5000,
                    showConfirmButton: false,
                    background: 'transparent',
                    color: '#ff3b3b'
                });

            } finally {
                setTimeout(() => {
                    emCooldownCompra = false;
                    botao.disabled = false;
                    botao.innerText = textoOriginal;
                }, tempoCompra * 1000);
            }
        });
    });
});

