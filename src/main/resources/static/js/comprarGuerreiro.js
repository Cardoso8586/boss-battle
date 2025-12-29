document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute('content')) : null;
    if (!usuarioId) return;

    const botoesComprar = document.querySelectorAll('.btn-comprar');

    botoesComprar.forEach(botao => {

        let emCooldownCompra = false;
        const tempoCompra = 3; // ⏱️ segundos

        botao.addEventListener('click', async () => {

            // 🔒 trava por botão
            if (emCooldownCompra) return;

            const card = botao.closest('.loja-card');

            const quantidadeInput = card.querySelector('#quantidade-guerreiros');
            const quantidade = parseInt(quantidadeInput.value);

            // ✅ validação
            if (!quantidade || quantidade <= 0) {
                Swal.fire({
                    icon: 'warning',
                    title: 'Quantidade inválida',
                    text: 'Informe uma quantidade válida.',
                    confirmButtonText: 'Ok',
					// background: '#0f0f0f',
									   background: `
									     linear-gradient(
									       rgba(0, 0, 0, 0.7),
									       rgba(0, 0, 0, 0.7)
									     ),
									     url('/icones/bg-alert.webp')
									     center / cover
									     no-repeat
									   `,
		            color: '#ffb400'   
                });
                return;
            }

            emCooldownCompra = true;
            botao.disabled = true;

            const textoOriginal = botao.innerText;
            let restante = tempoCompra;

            // ⏳ texto no botão
            botao.innerText = `Comprando... (${restante}s)`;

            const timer = setInterval(() => {
                restante--;
                botao.innerText = `Comprando... (${restante}s)`;
                if (restante <= 0) clearInterval(timer);
            }, 1000);

            // ⚠️ preço apenas visual
            const precoText = card.querySelector('.preco')
                .textContent.replace(/[^\d]/g, '');
            const precoUnitario = parseFloat(precoText);

            try {
                const response = await fetch(`/comprar/guerreiro/${usuarioId}`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ quantidade })
                });

                if (response.ok) {

					if (quantidade <= 1) {
						Swal.fire({
										    title: 'Compra realizada!',
										    html: `Você comprou ${quantidade} guerreiro.`,
										    imageUrl: '/icones/guerreiro.webp', 
										    imageWidth: 80,  
										    imageHeight: 80,
										    imageAlt: 'Guerreiro',
										    timer: 4000,
										    showConfirmButton: false,
											// background: '#0f0f0f',
															   background: `
															     linear-gradient(
															       rgba(0, 0, 0, 0.7),
															       rgba(0, 0, 0, 0.7)
															     ),
															     url('/icones/bg-alert.webp')
															     center / cover
															     no-repeat
															   `,
										    color: '#ffb400'       
										});

					} else{
						Swal.fire({
					    title: 'Compra realizada!',
					    html: `Você comprou ${quantidade} guerreiros.`,
					    imageUrl: '/icones/guerreiro.webp', 
					    imageWidth: 80,   
					    imageHeight: 80,
					    imageAlt: 'Guerreiro',
					    timer: 4000,
					    showConfirmButton: false,
						// background: '#0f0f0f',
										   background: `
										     linear-gradient(
										       rgba(0, 0, 0, 0.7),
										       rgba(0, 0, 0, 0.7)
										     ),
										     url('/icones/bg-alert.webp')
										     center / cover
										     no-repeat
										   `,
					    color: '#ffb400' 
					});
						
					}
					
					


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
                        text: `Custo estimado: ${custoEstimado.toLocaleString('pt-BR')} | Saldo: ${saldo.toLocaleString('pt-BR')}`,
                        timer: 4000,
                        showConfirmButton: false,
						// background: '#0f0f0f',
										   background: `
										     linear-gradient(
										       rgba(0, 0, 0, 0.7),
										       rgba(0, 0, 0, 0.7)
										     ),
										     url('/icones/bg-alert.webp')
										     center / cover
										     no-repeat
										   `,
	                    color: '#ffb400'       
						
                    });
                }

            } catch (err) {
                console.error(err);

                Swal.fire({
                    icon: 'error',
                    title: 'Erro',
                    text: 'Erro ao tentar comprar.',
                    timer: 4000,
                    showConfirmButton: false,
					// background: '#0f0f0f',
									   background: `
									     linear-gradient(
									       rgba(0, 0, 0, 0.7),
									       rgba(0, 0, 0, 0.7)
									     ),
									     url('/icones/bg-alert.webp')
									     center / cover
									     no-repeat
									   `,
					color: '#ffb400'     
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


/**
 * 
 *document.addEventListener('DOMContentLoaded', () => {
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
 * 
 */

