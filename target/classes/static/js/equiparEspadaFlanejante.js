/**
 * Espada Flanejante – Equipar
 */

document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute('content')) : null;
    if (!usuarioId) return;

    // ==============================
    // ELEMENTOS
    // ==============================
    const espadaSpan = document.getElementById('espadaFlanejante');
    const btnAtivarEspada = document.getElementById('btnAtivarEspadaFlanejante');
   // const espadaAtivaInfo = document.querySelector('.espada-flanejante-ativa-info');
	const espadaInfos = document.querySelectorAll('.espada-flanejante-ativa-info');
    // ==============================
    // FORMATAÇÃO
    // ==============================
    const formatarNumero = n =>
        new Intl.NumberFormat('pt-BR').format(n);

    // ==============================
    // ATUALIZAR USUÁRIO
    // ==============================
    async function atualizarUsuario() {
        try {
            const res = await fetch(`/api/atualizar/status/ajustes/${usuarioId}`);
            if (!res.ok) return;

            const status = await res.json();
            atualizarEspada(status);

        } catch (e) {
            console.error('Erro ao atualizar espada:', e);
        }
    }

    // ==============================
    // ATUALIZA ESPADA
    // ==============================
    function atualizarEspada(status) {

		const estoque = status.espadaFlanejanteEstoque ?? 0;
		const ativa = status.espadaFlanejanteAtiva ?? 0;
		const podeAtivar = status.podeAtivarEspadaFlanejante === true;

		const espadaItem = espadaSpan?.closest('.nucleo-item-flanejante');

		// ITEM (imagem + texto + quantidade)
		if (espadaItem && espadaSpan) {
		    if (estoque === 0) {
		        espadaItem.classList.add('hidden');   // melhor que display direto
		    } else {
		        espadaItem.classList.remove('hidden');
		        espadaSpan.textContent = formatarNumero(estoque);
		    }
		}

		// BOTÃO ATIVAR
		if (btnAtivarEspada) {
		    const mostrarBotao = estoque > 0 && podeAtivar && !ativa;

		    btnAtivarEspada.classList.toggle('hidden', !mostrarBotao);
		    btnAtivarEspada.disabled = !mostrarBotao;
		}

        // Info ativa
        if (espadaInfos) {
			espadaInfos.forEach(div => {
			    if (ativa > 0) {   // ativa = número de espadas ativas
					div.classList.remove('hidden');
					div.textContent = `✔ Espada equipada (${ativa})`;
			    } else {
			        div.classList.add('hidden');    // esconde a mensagem
			    }
			});
        }
    }

    // ==============================
    // ATIVAR ESPADA
    // ==============================
    let emCooldown = false;
    const tempoCooldown = 5;

    if (btnAtivarEspada) {
        btnAtivarEspada.addEventListener('click', async () => {

            if (emCooldown) return;

            emCooldown = true;
            btnAtivarEspada.disabled = true;

          //  let restante = tempoCooldown;
            const textoOriginal = btnAtivarEspada.innerText;

            btnAtivarEspada.innerText = `Ativando...`;
			//btnAtivarEspada.innerText = `Ativando... (${restante}s)`;
			
           // const timer = setInterval(() => {
               // restante--;
              //  btnAtivarEspada.innerText = `Ativando...`;
				//btnAtivarEspada.innerText = `Ativando... (${restante}s)`;
				
              //  if (restante <= 0) clearInterval(timer);
           // }, 1000);

            try {
                const res = await fetch(
                    `/api/espada-flanejante/ativar?usuarioId=${usuarioId}&quantidade=1`,
                    { method: 'POST' }
                );

                if (!res.ok) {
                    const erro = await res.text();
                    Swal.fire({
                        customClass: { title: 'swal-game-error' },
                        icon: 'warning',
                        title: 'Erro',
                        text: erro,
                        background: 'transparent',
                        color: '#ff3b3b'
                    });
                    return;
                }

                Swal.fire({
                    customClass: { title: 'swal-game-text' },
                    icon: 'success',
                    title: 'Espada equipada!',
                    text: 'Sua Espada Flanejante foi equipada com sucesso.',
                    timer: 5000,
                    showConfirmButton: false,
                    background: 'transparent',
                    color: '#ffb400'
                });

                await atualizarUsuario();

            } catch (e) {
                console.error(e);
                Swal.fire({
                    customClass: { title: 'swal-game-error' },
                    icon: 'error',
                    title: 'Erro',
                    text: 'Erro ao tentar equipar espada.',
                    background: 'transparent',
                    color: '#ff3b3b'
                });
            } finally {
                setTimeout(() => {
                    emCooldown = false;
                    btnAtivarEspada.disabled = false;
                    btnAtivarEspada.innerText = textoOriginal;
                }, tempoCooldown * 1000);
            }
        });
    }

    // ==============================
    // LOOP
    // ==============================
    atualizarUsuario();
    setInterval(atualizarUsuario, 5000);
});

