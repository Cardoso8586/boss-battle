document.addEventListener('DOMContentLoaded', () => {
    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? Number(meta.content) : null;

    console.log('usuarioId:', usuarioId);

    if (!usuarioId) {
        console.error('Usuário não encontrado no meta user-id');
        return;
    }

    const escudoPrimordialErroImg = "icones/erro_img/escudo_primordial_erro.webp";
    const escudoPrimordialOkImg = "icones/ok_img/escudo_primordial_ok.webp";
    const bossCoinErroImg = "icones/erro_img/boss_coin_erro.webp";

    const botoesCompra = document.querySelectorAll('.btn-comprar');
    console.log('Botões encontrados:', botoesCompra.length);

    botoesCompra.forEach(botao => {
        console.log('Botão encontrado:', botao, 'data-item:', botao.dataset.item);

        if (botao.dataset.item !== 'escudo-primordial') return;

        let emCooldown = false;
        const tempoCooldown = 5;

        botao.addEventListener('click', async () => {
            console.log('Clicou no botão de comprar escudo');

            if (emCooldown) {
                console.log('Botão em cooldown');
                return;
            }

            const card = botao.closest('.loja-card');
            if (!card) {
                console.error('Card .loja-card não encontrado');
                return;
            }

            const quantidadeInput = card.querySelector('.quantidade-escudo');
            if (!quantidadeInput) {
                console.error('Input .quantidade-escudo não encontrado');
                return;
            }

            const quantidade = Number(quantidadeInput.value);
            console.log('Quantidade:', quantidade);

            if (quantidade <= 0 || isNaN(quantidade)) {
                Swal.fire({
                    customClass: {
                        title: 'swal-game-error'
                    },
                    title: 'Quantidade inválida',
                    text: 'Informe uma quantidade válida.',
                    imageUrl: escudoPrimordialErroImg,
                    imageWidth: 120,
                    imageHeight: 160,
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
                    background: 'transparent',
                    color: '#ff3b3b'
                });
                return;
            }

            emCooldown = true;
            botao.disabled = true;

            const textoOriginal = botao.textContent;
            botao.textContent = 'Comprando Escudo Primordial...';

            try {
                console.log('Enviando compra...');

                const response = await fetch(`/comprar/escudo/primordial/${usuarioId}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ quantidade })
                });

                console.log('Status da resposta:', response.status);

                const mensagem = await response.text();
                console.log('Mensagem retorno:', mensagem);

                if (response.ok) {
                    Swal.fire({
                        customClass: {
                            title: 'swal-game-text'
                        },
                        title: `Você comprou ${quantidade} ${quantidade > 1 ? 'Escudos Primordiais' : 'Escudo Primordial'}`,
                        text: mensagem || 'Compra realizada com sucesso.',
                        imageUrl: escudoPrimordialOkImg,
                        imageWidth: 120,
                        imageHeight: 160,
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

                    quantidadeInput.value = 1;

                    if (typeof atualizarPrecosLoja === 'function') {
                        atualizarPrecosLoja();
                    }

                    if (typeof carregarSaldoUsuario === 'function') {
                        carregarSaldoUsuario();
                    }

                    if (typeof atualizarUsuario === 'function') {
                        atualizarUsuario();
                    }

                } else {
                    Swal.fire({
                        customClass: {
                            title: 'swal-game-error'
                        },
                        title: 'Erro na compra',
                        text: mensagem || 'Saldo insuficiente.',
                        imageUrl: bossCoinErroImg,
                        imageWidth: 120,
                        imageHeight: 120,
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
                        background: 'transparent',
                        color: '#ff3b3b'
                    });
                }

            } catch (error) {
                console.error('Erro ao comprar escudo primordial:', error);

                Swal.fire({
                    customClass: {
                        title: 'swal-game-error'
                    },
                    title: 'Erro interno',
                    text: 'Não foi possível concluir a compra do Escudo Primordial.',
                    imageUrl: escudoPrimordialErroImg,
                    imageWidth: 120,
                    imageHeight: 160,
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
    if (!usuarioId) return;

    const escudoPrimordialErroImg = "icones/erro_img/escudo_primordial_erro.webp";
    const escudoPrimordialOkImg = "icones/ok_img/escudo_primordial_ok.webp";
    const bossCoinErroImg = "icones/erro_img/boss_coin_erro.webp";

    document.querySelectorAll('.btn-comprar').forEach(botao => {

        if (botao.dataset.item !== 'escudo-primordial') return;

        let emCooldown = false;
        const tempoCooldown = 5; // segundos

        botao.addEventListener('click', async () => {

            if (emCooldown) return;

            const card = botao.closest('.loja-card');
            if (!card) return;

            const quantidadeInput = card.querySelector('.quantidade-escudo');
            if (!quantidadeInput) return;

            const quantidade = Number(quantidadeInput.value);

            if (quantidade <= 0 || isNaN(quantidade)) {
                Swal.fire({
                    customClass: {
                        title: 'swal-game-error'
                    },
                    title: 'Quantidade inválida',
                    text: 'Informe uma quantidade válida.',
                    imageUrl: escudoPrimordialErroImg,
                    imageWidth: 120,
                    imageHeight: 160,
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
                    background: 'transparent',
                    color: '#ff3b3b'
                });
                return;
            }
			

            emCooldown = true;
            botao.disabled = true;

            const textoOriginal = botao.textContent;
            botao.textContent = `Comprando Escudo Primordial`;

         
			
            try {
                const response = await fetch(`/comprar/escudo/primordial/${usuarioId}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ quantidade })
                });

                const mensagem = await response.text();

                if (response.ok) {
                    Swal.fire({
                        customClass: {
                            title: 'swal-game-title'
                        },
                        title: 'Compra realizada!',
                        text: mensagem,
                        imageUrl: escudoPrimordialOkImg,
                        imageWidth: 120,
                        imageHeight: 160,
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
                        background: 'transparent',
                        color: '#ffd700'
                    });

                    quantidadeInput.value = 1;

                    if (typeof atualizarPrecosLoja === 'function') {
                        atualizarPrecosLoja();
                    }

                    if (typeof carregarSaldoUsuario === 'function') {
                        carregarSaldoUsuario();
                    }

                } else {
                    Swal.fire({
                        customClass: {
                            title: 'swal-game-error'
                        },
                        title: 'Erro na compra',
                        text: mensagem || 'Saldo insuficiente.',
                        imageUrl: bossCoinErroImg,
                        imageWidth: 120,
                        imageHeight: 160,
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
                        background: 'transparent',
                        color: '#ff3b3b'
                    });
                }

            } catch (error) {
                console.error('Erro ao comprar escudo primordial:', error);

                Swal.fire({
                    customClass: {
                        title: 'swal-game-error'
                    },
                    title: 'Erro interno',
                    text: 'Não foi possível concluir a compra do Escudo Primordial.',
                    imageUrl: escudoPrimordialErroImg,
                    imageWidth: 120,
                    imageHeight: 160,
                    background: 'transparent',
                    color: '#ff3b3b'
                });
            }				finally {
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