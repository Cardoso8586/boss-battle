document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? Number(meta.content) : null;

    if (!usuarioId) {
        console.error('Usuário não encontrado');
        return;
    }

    const botoes = document.querySelectorAll('.btn-comprar-guerreiro');

    botoes.forEach(botao => {

        let emCooldown = false;
        const tempoCooldown = 5;

        botao.addEventListener('click', async () => {

            if (emCooldown) return;

            const card = botao.closest('.card-guerreiro');

            if (!card) {
                console.error('Card do guerreiro não encontrado');
                return;
            }

            const inputQuantidade = card.querySelector('.quantidade-guerreiro');

            if (!inputQuantidade) {
                console.error('Input de quantidade não encontrado');
                return;
            }

            const guerreiroId = Number(botao.dataset.guerreiroId);
            const guerreiroNome = botao.dataset.guerreiroNome || 'guerreiro';
            let quantidade = Number(inputQuantidade.value);

            if (!guerreiroId) {
                Swal.fire({
                    customClass: {
                        title: 'swal-game-error'
                    },
                    title: 'Erro interno',
                    text: 'Guerreiro inválido.',
                    timer: 8000,
                    showConfirmButton: false,
                    background: 'transparent',
                    color: '#ff3b3b'
                });
                return;
            }

            if (!quantidade || quantidade < 1) {
                quantidade = 1;
            }

            if (quantidade > 5) {
                quantidade = 5;
            }

            inputQuantidade.value = quantidade;

            emCooldown = true;
            botao.disabled = true;

            const textoOriginal = botao.textContent;
            botao.textContent = 'Comprando...';

            try {

                const response = await fetch(
                    `/boss-battle/guerreiros-elite/comprar/${usuarioId}`,
                    {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            guerreiroId: guerreiroId,
                            quantidade: quantidade
                        })
                    }
                );

                const texto = await response.text();

                console.log('STATUS:', response.status);
                console.log('RESPOSTA:', texto);

                let data;

                try {
                    data = JSON.parse(texto);
                } catch (e) {
                    throw new Error('Resposta não é JSON: ' + texto);
                }

                if (data.sucesso) {
					
					if (typeof window.atualizarCacheGuerreiros === "function") {
					    await window.atualizarCacheGuerreiros();
					}else {
					    console.error("Função atualizarCacheGuerreiros não encontrada");
					 }
					
                    Swal.fire({
                        customClass: {
                            title: 'swal-game-text'
                        },
                        title: `Você comprou ${quantidade} ${guerreiroNome}`,
                        text: data.mensagem || 'Compra realizada com sucesso.',
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
                        timer: 8000,
                        showConfirmButton: false,
                        background: 'transparent',
                        color: '#ffb400'
                    });

                    inputQuantidade.value = 1;

                    const quantidadeElemento = card.querySelector('.quantidade-atual');

                    if (quantidadeElemento && data.quantidadeAtual !== undefined) {
                        quantidadeElemento.textContent =
                            Number(data.quantidadeAtual).toLocaleString('pt-BR');
                    }

                    const precoElemento = card.querySelector('.preco-atual');

                    if (precoElemento && data.precoAtual !== undefined) {
                        precoElemento.textContent =
                            Number(data.precoAtual).toLocaleString('pt-BR');
                    }

                    const bossCoinsElemento = document.querySelector('.boss-coins-atual');

                    if (bossCoinsElemento && data.bossCoinsAtual !== undefined) {
                        bossCoinsElemento.textContent =
                            Number(data.bossCoinsAtual).toLocaleString('pt-BR');
                    }

                } else {

                    Swal.fire({
                        customClass: {
                            title: 'swal-game-error'
                        },
                        title: 'Compra não realizada',
                        text: data.mensagem || 'BossCoins insuficientes.',
                      
                        timer: 8000,
                        showConfirmButton: false,
                        background: 'transparent',
                        color: '#ff3b3b'
                    });
                }

            } catch (error) {

                console.error('Erro ao comprar guerreiro elite:', error);

                Swal.fire({
                    customClass: {
                        title: 'swal-game-error'
                    },
                    title: 'Erro interno',
                    text: 'Não foi possível concluir a compra.',
                    timer: 8000,
                    showConfirmButton: false,
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

/*

document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? Number(meta.content) : null;

    if (!usuarioId) {
        console.error('Usuário não encontrado');
        return;
    }

    const botoes = document.querySelectorAll('.btn-comprar-guerreiro');

    botoes.forEach(botao => {

        let emCooldown = false;
        const tempoCooldown = 5;

        botao.addEventListener('click', async () => {

            if (emCooldown) return;

            const card = botao.closest('.card-guerreiro');
            const inputQuantidade = card.querySelector('.quantidade-guerreiro');

            const guerreiroId = Number(botao.dataset.guerreiroId);
            const guerreiroNome = botao.dataset.guerreiroNome;
            const quantidade = Number(inputQuantidade.value);

            if (!quantidade || quantidade <= 0 || quantidade > 5) {
                Swal.fire({
                    customClass: {
                        title: 'swal-game-error'
                    },
                    title: 'Quantidade inválida',
                    text: 'Você pode comprar de 1 até 5 guerreiros por vez.',
                    timer: 8000,
                    showConfirmButton: false,
                    background: 'transparent',
                    color: '#ff3b3b'
                });
                return;
            }

            emCooldown = true;
            botao.disabled = true;

            const textoOriginal = botao.textContent;
            botao.textContent = 'Comprando...';

            try {

                const response = await fetch(
                    `/boss-battle/guerreiros-elite/comprar/${usuarioId}`,
                    {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            guerreiroId: guerreiroId,
                            quantidade: quantidade
                        })
                    }
                );

                const texto = await response.text();

                console.log('STATUS:', response.status);
                console.log('RESPOSTA:', texto);

                let data;

                try {
                    data = JSON.parse(texto);
                } catch (e) {
                    throw new Error('Resposta não é JSON: ' + texto);
                }

                if (data.sucesso) {

                    Swal.fire({
                        customClass: {
                            title: 'swal-game-text'
                        },
                        title: `Você comprou ${quantidade} ${guerreiroNome}`,
                        text: data.mensagem || 'Compra realizada com sucesso.',
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
                        timer: 8000,
                        showConfirmButton: false,
                        background: 'transparent',
                        color: '#ffb400'
                    });

                    // Limpa quantidade
                    inputQuantidade.value = 1;

                    // Atualiza quantidade do guerreiro
                    const quantidadeElemento =
                        card.querySelector('.quantidade-atual');

                    if (quantidadeElemento && data.quantidadeAtual !== undefined) {
                        quantidadeElemento.textContent =
                            Number(data.quantidadeAtual).toLocaleString('pt-BR');
                    }

                    // Atualiza preço do guerreiro
                    const precoElemento =
                        card.querySelector('.preco-atual');

                    if (precoElemento && data.precoAtual !== undefined) {
                        precoElemento.textContent =
                            Number(data.precoAtual).toLocaleString('pt-BR');
                    }

                    // Atualiza saldo BossCoins
                    const bossCoinsElemento =
                        document.querySelector('.boss-coins-atual');

                    if (bossCoinsElemento && data.bossCoinsAtual !== undefined) {
                        bossCoinsElemento.textContent =
                            Number(data.bossCoinsAtual).toLocaleString('pt-BR');
                    }

                } else {

                    Swal.fire({
                        customClass: {
                            title: 'swal-game-error'
                        },
                        title: 'Compra não realizada',
                        text: data.mensagem || 'BossCoins insuficientes.',
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
                        timer: 8000,
                        showConfirmButton: false,
                        background: 'transparent',
                        color: '#ff3b3b'
                    });
                }

            } catch (error) {

                console.error('Erro ao comprar guerreiro elite:', error);

                Swal.fire({
                    customClass: {
                        title: 'swal-game-error'
                    },
                    title: 'Erro interno',
                    text: 'Não foi possível concluir a compra.',
                    timer: 8000,
                    showConfirmButton: false,
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

*/