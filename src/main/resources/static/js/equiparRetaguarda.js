
document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;
    if (!usuarioId) return;

    // ==============================
    // ELEMENTOS DA RETAGUARDA
    // ==============================
    const guerreirosRetaguardaSpan = document.getElementById('guerreirosRetaguarda');
    const btnEquiparRetaguarda = document.getElementById('btnEquiparRetaguarda');
    const retaguardaInfos = document.querySelectorAll('.retaguarda-info');

    // ==============================
    // FORMATAÇÃO DE NÚMEROS
    // ==============================
    function formatarNumero(numero) {
        return new Intl.NumberFormat('pt-BR').format(numero);
    }

    // ==============================
    // FUNÇÃO DE ATUALIZAÇÃO DA RETAGUARDA
    // ==============================
    async function atualizarRetaguarda() {
        try {
            const res = await fetch(`/api/atualizar/status/ajustes/${usuarioId}`);
            if (!res.ok) return;

            const status = await res.json();
            atualizarNucleoRetaguarda(status);

        } catch (e) {
            console.error("Erro ao atualizar retaguarda:", e);
        }
    }

    function atualizarNucleoRetaguarda(status) {
        const guerreirosRetaguarda = status.guerreirosRetaguarda || 0;
        const estoqueGuerreiro = status.estoqueGuerreiro || 0;

        // Atualiza contador da retaguarda
        if (guerreirosRetaguardaSpan) {
            guerreirosRetaguardaSpan.textContent = formatarNumero(guerreirosRetaguarda);
        }

        // Controla botão de enviar para retaguarda
        if (btnEquiparRetaguarda) {
            if (estoqueGuerreiro > 0) {
                btnEquiparRetaguarda.classList.remove('hidden');
                btnEquiparRetaguarda.disabled = false;
            } else {
                btnEquiparRetaguarda.classList.add('hidden');
            }
        }

        // Infos visuais opcionais
        retaguardaInfos.forEach(elem => {
            if (guerreirosRetaguarda > 0) {
                elem.classList.remove('hidden');
                elem.textContent = `✔ ${formatarNumero(guerreirosRetaguarda)} em retaguarda`;
            } else {
                elem.classList.add('hidden');
            }
        });
    }

	// ==============================
	// BOTÃO ENVIAR PARA RETAGUARDA (COM TEMPORIZADOR)
	// ==============================

	if (btnEquiparRetaguarda) {

	    let emCooldownRetaguarda = false;
	    const tempoCooldownRetaguarda = 4; // segundos

	    btnEquiparRetaguarda.addEventListener('click', async () => {

	        if (emCooldownRetaguarda) return;

	        emCooldownRetaguarda = true;
	        btnEquiparRetaguarda.disabled = true;

	        let tempoRestante = tempoCooldownRetaguarda;
	        const textoOriginal = btnEquiparRetaguarda.innerText;

	        btnEquiparRetaguarda.innerText = `Enviando... (${tempoRestante}s)`;

	        const timer = setInterval(() => {
	            tempoRestante--;
	            btnEquiparRetaguarda.innerText = `Enviando... (${tempoRestante}s)`;
	            if (tempoRestante <= 0) clearInterval(timer);
	        }, 1000);

	        try {
	            const res = await fetch(`/equipar/retaguarda/${usuarioId}`, { method: 'POST' });

	            if (res.ok) {
	                Swal.fire({
	                    icon: 'success',
	                    title: 'Enviado para retaguarda!',
	                    text: 'O guerreiro foi enviado à retaguarda com sucesso.',
						timer: 4000,
						showConfirmButton: false,
						background: '#0f0f0f',
						color: '#ffb400'
	                });

	                atualizarRetaguarda();

	            } else {
	                // WARNING AUTOMÁTICO (4s)
	                swalWarningAuto(
	                    'Não foi possível enviar o guerreiro para a retaguarda.',
	                    4
	                );
	            }

	        } catch (e) {
	            console.error(e);
	            Swal.fire({
	                icon: 'error',
	                title: 'Erro',
	                text: 'Erro ao tentar enviar para retaguarda.',
					timer: 4000,
					showConfirmButton: false,
					background: '#0f0f0f',
					color: '#ffb400'
	            });
	        } finally {
	            setTimeout(() => {
	                emCooldownRetaguarda = false;
	                btnEquiparRetaguarda.disabled = false;
	                btnEquiparRetaguarda.innerText = textoOriginal;
	            }, tempoCooldownRetaguarda * 1000);
	        }
	    });
	}

    // ==============================
    // ATUALIZAÇÃO PERIÓDICA
    // ==============================
    atualizarRetaguarda();            // primeira atualização
    setInterval(atualizarRetaguarda, 5000); // a cada 5s

});

