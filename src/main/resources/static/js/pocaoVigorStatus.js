document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? Number(meta.getAttribute("content")) : null;

    if (!usuarioId) return;

    const pocaoVigorSpan = document.getElementById('pocaoVigor');
    const btnAtivarPocao = document.getElementById('btnAtivarPocao');
    const pocaoAtivaInfos = document.querySelectorAll('.pocao-ativa-info');

    function formatarNumero(numero) {
        return new Intl.NumberFormat('pt-BR').format(numero);
    }

    async function atualizarPocaoVigor() {
        try {
            const res = await fetch(`/api/atualizar/status/ajustes/${usuarioId}`);

            if (!res.ok) return;

            const status = await res.json();

            const estoque = Number(status.estoque) || 0;
            const equipada = Number(status.ativa) || 0;
            const podeAtivar = status.podeAtivar;
            const ativoGuerreiro = Number(status.ativoGuerreiro) || 0;

            const pocaoVigorItem = pocaoVigorSpan?.closest('.nucleo-item-pocao');

            if (pocaoVigorItem && pocaoVigorSpan) {
                if (estoque <= 0) {
                    pocaoVigorItem.classList.add('hidden');
                } else {
                    pocaoVigorItem.classList.remove('hidden');
                    pocaoVigorSpan.textContent = formatarNumero(estoque);
                }
            }

            if (btnAtivarPocao) {
                if (estoque > 0 && equipada < 3 && podeAtivar && ativoGuerreiro > 0) {
                    btnAtivarPocao.classList.remove('hidden');
                    btnAtivarPocao.disabled = false;
                } else {
                    btnAtivarPocao.classList.add('hidden');
                }
            }

            pocaoAtivaInfos.forEach(elem => {
                if (equipada > 0) {
                    elem.classList.remove('hidden');
                    elem.textContent = `✔ Poção equipada (${equipada})`;
                } else {
                    elem.classList.add('hidden');
                }
            });

        } catch (e) {
            console.error("Erro ao atualizar poção:", e);
        }
    }

    window.atualizarPocaoVigor = atualizarPocaoVigor;

    if (btnAtivarPocao) {

        let emCooldown = false;
        const tempoCooldown = 5;

        btnAtivarPocao.addEventListener('click', async () => {

            if (emCooldown) return;

            emCooldown = true;
            btnAtivarPocao.disabled = true;

            const textoOriginal = 'Ativar';
            btnAtivarPocao.innerText = 'Ativando poção...';

            try {
                const quantidade = 1;

                const res = await fetch(
                    `/api/pocao-vigor/ativar?usuarioId=${usuarioId}&quantidade=${quantidade}`,
                    { method: 'POST' }
                );

                if (!res.ok) {
                    const erro = await res.text();

                    Swal.fire({
                        customClass: { title: 'swal-game-error' },
                        title: 'Erro',
                        text: erro || 'Erro ao ativar poção.',
                        timer: 5000,
                        showConfirmButton: false,
                        background: 'transparent',
                        color: '#ff3b3b'
                    });

                    return;
                }

                Swal.fire({
                    customClass: { title: 'swal-game-text' },
                    title: 'Poção ativada!',
                    text: 'Sua poção foi ativada com sucesso.',
                    timer: 5000,
                    showConfirmButton: false,
                    background: 'transparent',
                    color: '#ffb400'
                });

                await atualizarPocaoVigor();

            } catch (e) {
                console.error(e);

            } finally {
                setTimeout(() => {
                    emCooldown = false;
                    btnAtivarPocao.disabled = false;
                    btnAtivarPocao.innerText = textoOriginal;
                }, tempoCooldown * 1000);
            }
        });
    }

    atualizarPocaoVigor();
    setInterval(atualizarPocaoVigor, 10000);
});