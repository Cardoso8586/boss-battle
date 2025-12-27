document.addEventListener('DOMContentLoaded', () => {

    const btnRecarregar = document.getElementById('btnRecarregar');
    const energiaAtual = document.getElementById('energiaAtual');
    const energiaBar = document.getElementById('energiaBar');
    const usuarioId = parseInt(
        document.querySelector('meta[name="user-id"]').getAttribute('content')
    );

    if (!btnRecarregar) return;

    let emCooldownRecarregar = false;
    const tempoCooldownRecarregar = 5; // segundos

    btnRecarregar.addEventListener('click', async () => {

        if (emCooldownRecarregar) return;

        emCooldownRecarregar = true;
        btnRecarregar.disabled = true;

        let tempoRestante = tempoCooldownRecarregar;
        const textoOriginal = btnRecarregar.innerText;

        btnRecarregar.innerText = `Recarregando... (${tempoRestante}s)`;

        const timer = setInterval(() => {
            tempoRestante--;
            btnRecarregar.innerText = `Recarregando... (${tempoRestante}s)`;
            if (tempoRestante <= 0) clearInterval(timer);
        }, 1000);

        try {
            const res = await fetch(
                `/recarregar-energia?usuarioId=${usuarioId}`,
                { method: 'POST' }
            );

            if (!res.ok) {
                swalWarningAuto(
                    'Não foi possível recarregar energia agora.',
                    4
                );
                return;
            }

            const data = await res.json();

            energiaAtual.textContent = data.energiaGuerreiros;
            energiaBar.style.width =
                Math.min(data.energiaGuerreiros, 100) + '%';

            // se chegou no máximo, mantém desabilitado
            if (data.energiaGuerreiros >= data.energiaGuerreirosPadrao) {
                btnRecarregar.disabled = true;
            }

        } catch (err) {
            console.error(err);
            Swal.fire({
                icon: 'error',
                title: 'Erro',
                text: 'Erro ao tentar recarregar energia.',
                confirmButtonText: 'Ok'
            });
        } finally {
            setTimeout(() => {
                emCooldownRecarregar = false;

                // só reativa se não estiver no máximo
                if (
                    energiaAtual.textContent <
                    energiaBar.style.width.replace('%', '')
                ) {
                    btnRecarregar.disabled = false;
                }

                btnRecarregar.innerText = textoOriginal;
            }, tempoCooldownRecarregar * 1000);
        }
    });
});
