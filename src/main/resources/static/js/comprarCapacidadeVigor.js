document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;
    if (!usuarioId) return;

    const btn = document.querySelector('[data-item="energia"]');
    if (!btn) return;

    let emCooldownEnergia = false;
    const tempoCompraEnergia = 3; // ⏱️ segundos

    btn.addEventListener('click', async () => {

        // 🔒 trava total
        if (emCooldownEnergia) return;

        const card = btn.closest('.loja-card');
        const quantidadeInput = card.querySelector('#quantidade-energia');
        const quantidade = parseInt(quantidadeInput.value);

        if (!quantidade || quantidade <= 0) {
            Swal.fire({
                icon: 'warning',
                title: 'Quantidade inválida',
                text: 'Informe uma quantidade válida.',
                confirmButtonText: 'Ok'
            });
            return;
        }

        emCooldownEnergia = true;
        btn.disabled = true;

        const textoOriginal = btn.innerText;
        let restante = tempoCompraEnergia;

        // ⏳ TEXTO DO BOTÃO
        btn.innerText = `Aplicando Vigor... (${restante}s)`;

        const timer = setInterval(() => {
            restante--;
            btn.innerText = `Aplicando Vigor... (${restante}s)`;
            if (restante <= 0) clearInterval(timer);
        }, 1000);

        try {
            const res = await fetch(`/comprar/energia/${usuarioId}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ quantidade })
            });

            if (res.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Capacidade aumentada!',
                    text: `Você comprou ${quantidade} unidade(s) de Vigor.`,
                    timer: 4000,
                    showConfirmButton: false
                });

                // 🔁 Atualiza UI após compra
                if (typeof atualizarUsuario === 'function') {
                    atualizarUsuario();
                }

            } else {
                const text = await res.text();
                Swal.fire({
                    icon: 'warning',
                    title: 'Saldo insuficiente',
                    text: text || 'Não foi possível comprar.',
                    timer: 4000,
                    showConfirmButton: false
                });
            }

        } catch (e) {
            console.error(e);

            Swal.fire({
                icon: 'error',
                title: 'Erro',
                text: 'Erro ao tentar aumentar capacidade de vigor.',
                timer: 4000,
                showConfirmButton: false
            });

        } finally {
            setTimeout(() => {
                emCooldownEnergia = false;
                btn.disabled = false;
                btn.innerText = textoOriginal;
            }, tempoCompraEnergia * 1000);
        }
    });
});

/**
 * 
 * 
 * document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;
    if (!usuarioId) return;

    const btn = document.querySelector('[data-item="energia"]');
    if (!btn) return;

    btn.addEventListener('click', async () => {
        const card = btn.closest('.loja-card');
        const quantidadeInput = card.querySelector('#quantidade-energia');
        const quantidade = parseInt(quantidadeInput.value);

        if (!quantidade || quantidade <= 0) {
            Swal.fire({
                icon: 'warning',
                title: 'Quantidade inválida',
                text: 'Informe uma quantidade válida.',
                confirmButtonText: 'Ok'
            });
            return;
        }

        try {
            const res = await fetch(`/comprar/energia/${usuarioId}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ quantidade })
            });

            if (res.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Capacidade Aumentada!',
                    text: `Você comprou ${quantidade} unidade(s) de Vigor.`,
                    confirmButtonText: 'Ok'
                });

                // 🔁 Atualiza UI após compra
                if (typeof atualizarUsuario === 'function') {
                    atualizarUsuario();
                }

            } else {
                const text = await res.text();
                Swal.fire({
                    icon: 'warning',
                    title: 'Saldo insuficiente',
                    text: text || 'Não foi possível comprar.',
                    confirmButtonText: 'Ok'
                });
            }

        } catch (e) {
            console.error(e);
            Swal.fire({
                icon: 'error',
                title: 'Erro',
                text: 'Erro ao tentar aumentar capacidade de vigor.',
                confirmButtonText: 'Ok'
            });
        }
    });
});

 */
