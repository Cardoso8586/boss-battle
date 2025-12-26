
document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;
    if (!usuarioId) return;

    // ==============================
    // BOTÕES
    // ==============================
    const btnRetirarAtaque = document.getElementById('btnRetirarGuerreiro');
    const btnRetirarRetaguarda = document.getElementById('btnRetirarRetaguarda');

    // ==============================
    // ATUALIZAR STATUS
    // ==============================
    async function atualizarStatus() {
        try {
            const res = await fetch(`/api/atualizar/status/ajustes/${usuarioId}`);
            if (!res.ok) return;

            const status = await res.json();

            const guerreirosAtivo = status.ativoGuerreiro || 0;
            const guerreirosRetaguarda = status.guerreirosRetaguarda || 0;

            // Controle botão ATAQUE
            if (btnRetirarAtaque) {
                guerreirosAtivo > 0
                    ? btnRetirarAtaque.classList.remove('hidden')
                    : btnRetirarAtaque.classList.add('hidden');
            }

            // Controle botão RETAGUARDA
            if (btnRetirarRetaguarda) {
                guerreirosRetaguarda > 0
                    ? btnRetirarRetaguarda.classList.remove('hidden')
                    : btnRetirarRetaguarda.classList.add('hidden');
            }

        } catch (e) {
            console.error("Erro ao atualizar status:", e);
        }
    }

    // ==============================
    // RETIRAR DO ATAQUE
    // ==============================
    if (btnRetirarAtaque) {
        btnRetirarAtaque.addEventListener('click', async () => {
            btnRetirarAtaque.disabled = true;

            try {
                const res = await fetch(`/retirar/ataque/${usuarioId}`, {
                    method: 'POST'
                });

                if (res.ok) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Guerreiro retirado!',
                        text: 'O guerreiro voltou ao Acampamento.',
                        confirmButtonText: 'Ok'
                    });
                    atualizarStatus();
                } else {
                    Swal.fire({
                        icon: 'warning',
                        title: 'Ação inválida',
                        text: 'Nenhum guerreiro no ataque.',
                        confirmButtonText: 'Ok'
                    });
                }
            } catch (e) {
                console.error(e);
                Swal.fire({
                    icon: 'error',
                    title: 'Erro',
                    text: 'Erro ao retirar guerreiro do ataque.',
                    confirmButtonText: 'Ok'
                });
            } finally {
                btnRetirarAtaque.disabled = false;
            }
        });
    }

    // ==============================
    // RETIRAR DA RETAGUARDA
    // ==============================
    if (btnRetirarRetaguarda) {
        btnRetirarRetaguarda.addEventListener('click', async () => {
            btnRetirarRetaguarda.disabled = true;

            try {
                const res = await fetch(`/retirar/retaguarda/${usuarioId}`, {
                    method: 'POST'
                });

                if (res.ok) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Retaguarda recuada!',
                        text: 'O guerreiro voltou ao Acampamento.',
                        confirmButtonText: 'Ok'
                    });
                    atualizarStatus();
                } else {
                    Swal.fire({
                        icon: 'warning',
                        title: 'Ação inválida',
                        text: 'Nenhum guerreiro na retaguarda.',
                        confirmButtonText: 'Ok'
                    });
                }
            } catch (e) {
                console.error(e);
                Swal.fire({
                    icon: 'error',
                    title: 'Erro',
                    text: 'Erro ao retirar guerreiro da retaguarda.',
                    confirmButtonText: 'Ok'
                });
            } finally {
                btnRetirarRetaguarda.disabled = false;
            }
        });
    }

    // ==============================
    // INIT
    // ==============================
    atualizarStatus();
    setInterval(atualizarStatus, 5000);

});

