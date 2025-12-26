document.addEventListener('DOMContentLoaded', () => {
    const usuarioId = parseInt(
        document.querySelector('meta[name="user-id"]').getAttribute('content')
    );

    const botoesComprar = document.querySelectorAll('.btn-comprar');

    botoesComprar.forEach(botao => {
        botao.addEventListener('click', async () => {
            const card = botao.closest('.loja-card');

            const quantidadeInput = card.querySelector('#quantidade-guerreiros');
            const quantidade = parseInt(quantidadeInput.value);

            // ✅ validação básica
            if (!quantidade || quantidade <= 0) {
                Swal.fire({
                    icon: 'warning',
                    title: 'Quantidade inválida',
                    text: 'Informe uma quantidade válida.',
                    confirmButtonText: 'Ok'
                });
                return;
            }

            // ⚠️ preço APENAS para exibição
            const precoText = card.querySelector('.preco')
                .textContent.replace(/[^\d]/g, '');
            const precoUnitario = parseFloat(precoText);

            const data = { quantidade };

            try {
                const response = await fetch(`/comprar/guerreiro/${usuarioId}`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(data)
                });

                if (response.ok) {
                    const message = await response.text();

                    Swal.fire({
                        icon: 'success',
                        title: 'Compra realizada!',
                        text: `Você comprou ${quantidade} guerreiro(s).`,
                        confirmButtonText: 'Ok'
                    });

                    if (typeof atualizarUsuario === 'function') {
                        atualizarUsuario();
                    }

                } else {
                    // ⚠️ só feedback visual
                    const custoEstimado = quantidade * precoUnitario;
                    const saldoElement = document.getElementById('boss_coins');
                    const saldo = parseFloat(
                        saldoElement.textContent.replace(/\D/g, '')
                    );

                    Swal.fire({
                        icon: 'warning',
                        title: 'Saldo insuficiente',
                        text: `Custo estimado: ${custoEstimado.toLocaleString('pt-BR')} | Saldo: ${saldo.toLocaleString('pt-BR')}`,
                        confirmButtonText: 'Ok'
                    });
                }

            } catch (err) {
                console.error(err);
                Swal.fire({
                    icon: 'error',
                    title: 'Erro',
                    text: 'Erro ao tentar comprar.',
                    confirmButtonText: 'Ok'
                });
            }
        });
    });
});

/**
 * 
 * document.addEventListener('DOMContentLoaded', () => {
    const usuarioId = parseInt(document.querySelector('meta[name="user-id"]').getAttribute('content'));
    const respostaDiv = document.getElementById('resposta');

    // Seleciona todos os botões de compra
    const botoesComprar = document.querySelectorAll('.btn-comprar');

    botoesComprar.forEach(botao => {
        botao.addEventListener('click', () => {
            // Pega o card pai
            const card = botao.closest('.loja-card');

            // Pega a quantidade digitada
            const quantidadeInput = card.querySelector('#quantidade-guerreiros');
            const quantidade = parseInt(quantidadeInput.value);

            // Pega o preço unitário do card (remove texto não numérico)
            const precoText = card.querySelector('.preco').textContent.replace(/[^\d]/g, '');
            const precoUnitario = parseFloat(precoText);

            // Monta o corpo da requisição
            const data = {
                quantidade: quantidade,
                precoUnitario: precoUnitario
            };

            // Faz a requisição POST para o backend
            fetch(`/comprar/guerreiro/${usuarioId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
            .then(response => {
                if (response.ok) {
                    return response.text();
					
					
                } else {
                    return Promise.reject('Saldo insuficiente');
                }
            })
            .then(message => {
                respostaDiv.textContent = message;
                respostaDiv.style.color = 'green';
            })
            .catch(error => {
                respostaDiv.textContent = error;
                respostaDiv.style.color = 'red';
            });
        });
    });
});
 * 
 */

