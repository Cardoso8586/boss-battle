document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;
    if (!usuarioId) return;

    // ==============================
    // ELEMENTOS DO GUERREIRO
    // ==============================
    const guerreirosCountSpan = document.getElementById('guerreirosCount');
    const btnEquiparGuerreiro = document.getElementById('btnEquiparGuerreiro');
    const guerreiroAtivoInfos = document.querySelectorAll('.guerreiro-ativo-info');

    // ==============================
    // FORMATAÇÃO DE NÚMEROS
    // ==============================
    function formatarNumero(numero) {
        return new Intl.NumberFormat('pt-BR').format(numero);
    }

    // ==============================
    // FUNÇÃO DE ATUALIZAÇÃO DO NÚCLEO / GUERREIRO
    // ==============================
    async function atualizarGuerreiro() {
        try {
            const res = await fetch(`/api/atualizar/status/ajustes/${usuarioId}`);
            if (!res.ok) return;

            const status = await res.json();
            atualizarNucleoGuerreiro(status);

        } catch (e) {
            //console.error("Erro ao atualizar núcleo do guerreiro:", e);
			Swal.fire({			
			customClass: {
			title: 'swal-game-error'
			},
			                icon: 'error',
			                title: 'Erro',
			                text: 'Erro ao atualizar núcleo do guerreiro:',
							timer: 4000,
							showConfirmButton: false,
							 background: 'transparent',
							color: '#ff3b3b' 
			            });
        }
    }
	function atualizarNucleoGuerreiro(status) {
	    const estoqueGuerreiro = status.estoqueGuerreiro || 0;

	    // Atualiza contador de estoque
	    if (guerreirosCountSpan) {
	        guerreirosCountSpan.textContent = formatarNumero(estoqueGuerreiro);
	    }

	    // Controla botão de equipar
	    if (btnEquiparGuerreiro) {
	        if (estoqueGuerreiro > 0) {
	            btnEquiparGuerreiro.classList.remove('hidden');
	            btnEquiparGuerreiro.disabled = false;
	        } else {
	            btnEquiparGuerreiro.classList.add('hidden');
	        }
	    }

	    // Opcional: atualizar elementos de guerreiro ativo
	    guerreiroAtivoInfos.forEach(elem => {
	        if (estoqueGuerreiro > 0) {
	            elem.classList.remove('hidden');
	            elem.textContent = `✔ Guerreiro enviado prara frente de batalha`;
	        } else {
	            elem.classList.add('hidden');
	        }
	    });
	}



    // ==============================
    // BOTÃO EQUIPAR GUERREIRO
    // ==============================

	// ==============================
	if (btnEquiparGuerreiro) {

	    let emCooldownGuerreiro = false;
	    const tempoCooldownGuerreiro = 4; // segundos

	    btnEquiparGuerreiro.addEventListener('click', async () => {

	        if (emCooldownGuerreiro) return;

	        emCooldownGuerreiro = true;
	        btnEquiparGuerreiro.disabled = true;

	        let tempoRestante = tempoCooldownGuerreiro;
	        const textoOriginal = btnEquiparGuerreiro.innerText;

	        btnEquiparGuerreiro.innerText = `Enviando... (${tempoRestante}s)`;

	        const timer = setInterval(() => {
	            tempoRestante--;
	            btnEquiparGuerreiro.innerText = `Enviando... (${tempoRestante}s)`;
	            if (tempoRestante <= 0) clearInterval(timer);
	        }, 1000);

	        try {
	            const res = await fetch(`/equipar/guerreiro/${usuarioId}`, { method: 'POST' });

	            if (res.ok) {
	                Swal.fire({
						customClass: {
						      title: 'swal-game-text'
						    },
	                    icon: 'success',
	                    title: 'Guerreiro enviado!',
	                    text: 'Seu guerreiro foi enviado para frente de batalha com sucesso.',
						timer: 4000,
						showConfirmButton: false,
						 background: 'transparent',
						color: '#ffb400'
	                });

	                atualizarGuerreiro();

	            } else {
	                // WARNING COM TIMER (4s)
	                swalWarningAuto(
	                    'Não foi possível enviar o guerreiro.',
	                    4
	                );
	            }

	        } catch (e) {
	          //  console.error(e);
	            Swal.fire({
					customClass: {
								title: 'swal-game-error'
								},
	                icon: 'error',
	                title: 'Erro',
	                text: 'Erro ao tentar equipar guerreiro.',
					timer: 4000,
					showConfirmButton: false,
					 background: 'transparent',
					color: '#ff3b3b' 
	            });
	        } finally {
	            setTimeout(() => {
	                emCooldownGuerreiro = false;
	                btnEquiparGuerreiro.disabled = false;
	                btnEquiparGuerreiro.innerText = textoOriginal;
	            }, tempoCooldownGuerreiro * 1000);
	        }
	    });
	}

    // ==============================
    // ATUALIZAÇÃO PERIÓDICA
    // ==============================
    atualizarGuerreiro(); // primeira atualização imediata
    setInterval(atualizarGuerreiro, 5000); // atualiza a cada 5 segundos

});
