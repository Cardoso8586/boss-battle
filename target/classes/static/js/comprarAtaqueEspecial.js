document.addEventListener('DOMContentLoaded', () => {

    const metaUsuario = document.querySelector('meta[name="user-id"]');

    if (!metaUsuario) {
        console.error('Meta user-id não encontrada');
        return;
    }

    const usuarioId = Number(metaUsuario.getAttribute('content'));

    if (!Number.isInteger(usuarioId) || usuarioId <= 0) {
        console.error('Usuário não encontrado:', metaUsuario.getAttribute('content'));
        return;
    }

    const btn = document.querySelector('[data-item="ataqueEspecial"]');

    if (!btn) return;

    let emCooldownCompra = false;
    const tempoCompra = 5;

    const ataqueEspecialErroImg = "/icones/erro_img/ataque_especial_erro.webp";
    const ataqueEspecialOkImg = "/icones/ok_img/ataque_especial_ok.webp";
    const bossCoinErroImg = "/icones/erro_img/boss_coin_erro.webp";

    btn.addEventListener('click', async () => {

        if (emCooldownCompra) return;

        const card = btn.closest('.loja-card');
        const inputQuantidade = card.querySelector('#quantidade-ataque');
        const precoElemento = card.querySelector('#preco-ataque-especial');

        const quantidade = Number(inputQuantidade.value);

        if (!Number.isInteger(quantidade) || quantidade <= 0) {
            Swal.fire({
                customClass: {
                    title: 'swal-game-error'
                },
                title: 'Quantidade inválida',
                text: 'Informe uma quantidade válida.',
                imageUrl: ataqueEspecialErroImg,
                imageWidth: 120,
                imageHeight: 120,
                timer: 8000,
                showConfirmButton: false,
                background: 'transparent',
                color: '#ff3b3b'
            });

            return;
        }

        emCooldownCompra = true;
        btn.disabled = true;

        const textoOriginal = btn.innerText;
        btn.innerText = 'Aumentando Ataque Especial...';

        const precoUnitario = precoElemento?.dataset?.preco
            ? Number(precoElemento.dataset.preco)
            : Number(precoElemento.textContent.replace(/[^\d]/g, ''));

        try {
            const res = await fetch(`/comprar/ataque-especial/${usuarioId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    quantidade: quantidade
                })
            });

            if (res.ok) {
                Swal.fire({
                    customClass: {
                        title: 'swal-game-text'
                    },
                    title: 'Ataque especial aprimorado!',
                    html: quantidade <= 1
                        ? `Você adquiriu <b>${quantidade}</b> unidade de ataque especial.`
                        : `Você adquiriu <b>${quantidade}</b> unidades de ataque especial.`,
                    imageUrl: ataqueEspecialOkImg,
                    imageWidth: 120,
                    imageHeight: 120,
                    imageAlt: 'Ataque Especial',
                    timer: 5000,
                    showConfirmButton: false,
                    background: 'transparent',
                    color: '#ffb400'
                });

                if (typeof atualizarUsuario === 'function') {
                    atualizarUsuario();
                }

                if (typeof atualizarPrecosLoja === 'function') {
                    atualizarPrecosLoja();
                }

                return;
            }

            const erro = await res.json().catch(() => null);

            const custoEstimado = quantidade * precoUnitario;

            const saldoElement = document.getElementById('boss_coins');

            const saldo = saldoElement
                ? Number(saldoElement.textContent.replace(/\D/g, ''))
                : 0;

            Swal.fire({
                customClass: {
                    title: 'swal-game-error'
                },
                title: erro?.mensagem || 'Saldo insuficiente',
                text: erro?.detalhe ||
                    `Você precisa de ${custoEstimado.toLocaleString('pt-BR')} Boss Coins, mas só tem ${saldo.toLocaleString('pt-BR')} Boss Coins.`,
                imageUrl: bossCoinErroImg,
                imageWidth: 120,
                imageHeight: 120,
                timer: 5000,
                showConfirmButton: false,
                background: 'transparent',
                color: '#ff3b3b'
            });

        } catch (err) {
            console.error('Erro ao comprar ataque especial:', err);

            Swal.fire({
                customClass: {
                    title: 'swal-game-error'
                },
                title: 'Erro',
                text: 'Erro ao tentar comprar ataque especial.',
                imageUrl: ataqueEspecialErroImg,
                imageWidth: 120,
                imageHeight: 120,
                timer: 5000,
                showConfirmButton: false,
                background: 'transparent',
                color: '#ff3b3b'
            });

			}finally {

			    setTimeout(() => {

			        emCooldownCompra = false;

			        btn.disabled = false;

			        // VOLTA AO TEXTO ORIGINAL
			        btn.innerText = 'Comprar';

			    }, tempoCompra * 1000);
			}
    });
});

/**
 * document.addEventListener('DOMContentLoaded', () => {
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
 * 
 */

