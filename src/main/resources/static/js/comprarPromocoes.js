document.addEventListener('DOMContentLoaded', () => {
    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;

    if (!usuarioId) return;

    const bossCoinErroImg = "icones/erro_img/boss_coin_erro.webp";
    const formatter = new Intl.NumberFormat('pt-BR');

    const promoImg = {
        normal: "/icones/promo_normal.webp",
        avancada: "/icones/promo_avancada.webp",
        especial: "/icones/promo_especial.webp",
        lendaria: "/icones/promo_lendaria.webp"
    };

    function gerarHtmlAnuncio(texto) {
        return `
            <div style="margin-bottom: 12px;">${texto}</div>
            <div class="modal-anuncio">
                <iframe
                    src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                    width="468"
                    height="60"
                    scrolling="no"
                    frameborder="0">
                </iframe>
            </div>
        `;
    }

    function travarBotao(botao, texto = 'Processando...') {
        botao.disabled = true;
        botao.dataset.comprando = "true";
        botao.dataset.textoOriginal = botao.innerHTML;
        botao.innerHTML = `<span class="btn-promo-loader"></span> ${texto}`;
        botao.classList.add('btn-promo-processando');
        botao.style.pointerEvents = 'none';
    }

    function destravarBotao(botao) {
        const textoOriginal = botao.dataset.textoOriginal || 'Comprar';
        botao.disabled = false;
        botao.dataset.comprando = "false";
        botao.innerHTML = textoOriginal;
        botao.classList.remove('btn-promo-processando');
        botao.style.pointerEvents = 'auto';
    }

    function animarSucessoBotao(botao) {
        botao.classList.remove('btn-promo-erro');
        botao.classList.add('btn-promo-sucesso');
        botao.innerHTML = '✓ Comprado';

        setTimeout(() => {
            botao.classList.remove('btn-promo-sucesso');
            destravarBotao(botao);
        }, 1600);
    }

    function animarErroBotao(botao) {
        botao.classList.remove('btn-promo-sucesso');
        botao.classList.add('btn-promo-erro');
        botao.innerHTML = 'Tentar novamente';

        setTimeout(() => {
            botao.classList.remove('btn-promo-erro');
            destravarBotao(botao);
        }, 1400);
    }

    async function atualizarPrecosPromocoes() {
        const cards = document.querySelectorAll('.promo-card');

        for (const card of cards) {
            const tipo = card.dataset.tipo;
            if (!tipo) continue;

            const precoElemento = card.querySelector(`.preco-promo-${tipo}`);
            if (!precoElemento) continue;

            precoElemento.classList.add('preco-atualizando');

            try {
                const response = await fetch(
                    `/promo/preco/${usuarioId}/${tipo.toUpperCase()}`,
                    { credentials: 'include' }
                );

                if (!response.ok) {
                    throw new Error('Erro ao carregar preço');
                }

                const data = await response.json();

                precoElemento.innerHTML = `
                    <span class="preco-bruto">
                        ${formatter.format(data.precoBruto)} BossCoins
                    </span>
                    <br>
                    <span class="preco-final">
                        ${formatter.format(data.precoFinal)} BossCoins
                    </span>
                    <span class="desconto">
                        (-${data.descontoPercentual}%)
                    </span>
                `;

                precoElemento.classList.remove('preco-atualizando');
                precoElemento.classList.add('preco-atualizado');

                setTimeout(() => {
                    precoElemento.classList.remove('preco-atualizado');
                }, 900);

            } catch (error) {
                precoElemento.textContent = "Erro ao carregar preço";
                precoElemento.classList.remove('preco-atualizando');
            }
        }
    }

    async function comprarPromocao(botao) {
        if (!botao || botao.dataset.comprando === "true") {
            return;
        }

        const card = botao.closest('.promo-card');
        if (!card) return;

        const tipo = card.dataset.tipo;
        if (!tipo) return;

        travarBotao(botao, 'Comprando...');

        try {
            const response = await fetch(
                `/promo/comprar/${usuarioId}/${tipo.toUpperCase()}`,
                {
                    method: 'POST',
                    credentials: 'include'
                }
            );

            const mensagem = await response.text();

            if (!response.ok) {
                animarErroBotao(botao);

                await Swal.fire({
                    customClass: { title: 'swal-game-error' },
                    title: mensagem || 'Saldo insuficiente.',
                    imageUrl: bossCoinErroImg,
                    imageWidth: 120,
                    imageHeight: 120,
                    html: gerarHtmlAnuncio('Não foi possível comprar'),
                    background: 'transparent',
                    color: '#ff3b3b'
                });

                return;
            }

            await atualizarPrecosPromocoes();

            if (typeof atualizarUsuario === 'function') {
                await atualizarUsuario();
            }

            animarSucessoBotao(botao);

            await Swal.fire({
                customClass: { title: 'swal-game-text' },
                title: mensagem || 'Compra realizada com sucesso!',
                imageUrl: promoImg[tipo] || promoImg.normal,
                imageWidth: 120,
                imageHeight: 120,
                html: gerarHtmlAnuncio('Promoção comprada!'),
                background: 'transparent',
                color: '#ffb400'
            });

        } catch (err) {
            console.error('Erro ao comprar promoção:', err);

            animarErroBotao(botao);

            await Swal.fire({
                customClass: { title: 'swal-game-error' },
                icon: 'error',
                title: 'Erro inesperado',
                html: gerarHtmlAnuncio('Não foi possível comprar a promoção agora.'),
                background: 'transparent',
                color: '#ff3b3b'
            });
        }
    }

    document.querySelectorAll('.btn-promo').forEach(botao => {
        botao.addEventListener('click', () => {
            comprarPromocao(botao);
        });
    });

    atualizarPrecosPromocoes();
});