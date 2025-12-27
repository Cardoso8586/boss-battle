
document.addEventListener('DOMContentLoaded', () => {

    const btn = document.querySelector('[data-item="ataqueEspecial"]');
    if (!btn) return;

    let emCooldownCompra = false;
    const tempoCompra = 3; // ⏱️ segundos

    btn.addEventListener('click', async () => {

        // 🔒 trava total
        if (emCooldownCompra) return;

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

        emCooldownCompra = true;
        btn.disabled = true;

        const textoOriginal = btn.innerText;
        let restante = tempoCompra;

        // ⏳ TEXTO CORRETO
        btn.innerText = `Aumentando Ataque Especial... (${restante}s)`;

        const timer = setInterval(() => {
            restante--;
            btn.innerText = `Aumentando Ataque Especial... (${restante}s)`;
            if (restante <= 0) clearInterval(timer);
        }, 1000);

        const precoUnitario = parseFloat(
            card.querySelector('.preco').textContent.replace(/[^\d]/g, '')
        );

        try {
            const res = await fetch(
                `/comprar/ataque-especial/${usuarioId}`,
                {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ quantidade })
                }
            );

            if (res.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Ataque especial aprimorado!',
                    text: `Você adquiriu ${quantidade} unidade(s) de ataque especial.`,
                    timer: 4000,
                    showConfirmButton: false
                });

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
                    timer: 4000,
                    showConfirmButton: false
                });
            }

        } catch (err) {
            console.error(err);

            Swal.fire({
                icon: 'error',
                title: 'Erro',
                text: 'Erro ao tentar comprar ataque especial.',
                timer: 4000,
                showConfirmButton: false
            });

        } finally {
            setTimeout(() => {
                emCooldownCompra = false;
                btn.disabled = false;
                btn.innerText = textoOriginal;
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

