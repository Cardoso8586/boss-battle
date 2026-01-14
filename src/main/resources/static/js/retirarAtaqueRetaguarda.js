
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
	// SWEETALERT WARNING AUTOMÁTICO (4s)
	// ==============================
	function swalWarningAuto(texto, segundos = 5) {
	    let tempo = segundos;

	    Swal.fire({
			customClass: {
			title: 'swal-game-error'
		    },
	        icon: 'warning',
	        title: 'Ação inválida',
	        html: `${texto}<br><b>Fechando em ${tempo}s</b>`,
	        timer: segundos * 1000,
	        timerProgressBar: true,
	        showConfirmButton: false,
			 background: 'transparent',					    
			color: '#ff3b3b',
	        didOpen: () => {
	            const interval = setInterval(() => {
	                tempo--;
	                const b = Swal.getHtmlContainer().querySelector('b');
	                if (b) b.textContent = `Fechando em ${tempo}s`;
	                if (tempo <= 0) clearInterval(interval);
	            }, 1000);
	        }
	    });
	}

	// ==============================
	// RETIRAR DO ATAQUE (COM TEMPORIZADOR)
	// ==============================
	if (btnRetirarAtaque) {

	    let emCooldownRetirarAtaque = false;
	    const tempoCooldownRetirarAtaque = 4; // segundos

	    btnRetirarAtaque.addEventListener('click', async () => {

	        if (emCooldownRetirarAtaque) return;

	        emCooldownRetirarAtaque = true;
	        btnRetirarAtaque.disabled = true;

	       // let tempoRestante = tempoCooldownRetirarAtaque;
	        const textoOriginal = btnRetirarAtaque.innerText;

	        btnRetirarAtaque.innerText = `Retirando...`;
			//btnRetirarAtaque.innerText = `Retirando... (${tempoRestante}s)`;

			/**
			 *      const timer = setInterval(() => {
	            tempoRestante--;
	            btnRetirarAtaque.innerText = `Retirando... (${tempoRestante}s)`;
	            if (tempoRestante <= 0) clearInterval(timer);
	        }, 1000);
			 */
	   

	        try {
	            const res = await fetch(`/retirar/ataque/${usuarioId}`, { method: 'POST' });

	            if (res.ok) {
	                Swal.fire({
						customClass: {
						      title: 'swal-game-text'
						    },
	                    icon: 'success',
	                    title: 'Guerreiro retirado!',
	                    text: 'O guerreiro voltou ao Acampamento.',
						timer: 5000,
						showConfirmButton: false,
						 background: 'transparent',
						color: '#ffb400'
	                });

	                atualizarStatus();

	            } else {
	                swalWarningAuto('Nenhum guerreiro no ataque.', 4);
	            }

	        } catch (e) {
	            console.error(e);
	            Swal.fire({
					customClass: {
					      title: 'swal-game-text'
					    },
	                icon: 'error',
	                title: 'Erro',
	                text: 'Erro ao retirar guerreiro do ataque.',
					timer: 5000,
					showConfirmButton: false,
				    background: 'transparent',
					color: '#ff3b3b'
	            });
	        } finally {
	            setTimeout(() => {
	                emCooldownRetirarAtaque = false;
	                btnRetirarAtaque.disabled = false;
	                btnRetirarAtaque.innerText = textoOriginal;
	            }, tempoCooldownRetirarAtaque * 1000);
	        }
	    });
	}

	// ==============================
	// RETIRAR DA RETAGUARDA (COM TEMPORIZADOR)
	// ==============================
	if (btnRetirarRetaguarda) {

	    let emCooldownRetirarRetaguarda = false;
	    const tempoCooldownRetirarRetaguarda = 4; // segundos

	    btnRetirarRetaguarda.addEventListener('click', async () => {

	        if (emCooldownRetirarRetaguarda) return;

	        emCooldownRetirarRetaguarda = true;
	        btnRetirarRetaguarda.disabled = true;

	      //  let tempoRestante = tempoCooldownRetirarRetaguarda;
	        const textoOriginal = btnRetirarRetaguarda.innerText;

			/**
			 *      const timer = setInterval(() => {
	            tempoRestante--;
	            btnRetirarRetaguarda.innerText = `Retirando... (${tempoRestante}s)`;
	            if (tempoRestante <= 0) clearInterval(timer);
	        }, 1000);
			 */
	        btnRetirarRetaguarda.innerText = `Retirando...`;
			//btnRetirarRetaguarda.innerText = `Retirando... (${tempoRestante}s)`;
	   

	        try {
	            const res = await fetch(`/retirar/retaguarda/${usuarioId}`, { method: 'POST' });

	            if (res.ok) {
	                Swal.fire({
						customClass: {
						      title: 'swal-game-text'
						    },
	                    icon: 'success',
	                    title: 'Retaguarda recuada!',
	                    text: 'O guerreiro voltou ao Acampamento.',
						timer: 5000,
						showConfirmButton: false,
						 background: 'transparent',
					 color: '#ffb400'
						
	                });

	                atualizarStatus();

	            } else {
	                swalWarningAuto('Nenhum guerreiro na retaguarda.', 4);
	            }

	        } catch (e) {
	            console.error(e);
	            Swal.fire({
					customClass: {
					      title: 'swal-game-text'
					    },
	                icon: 'error',
	                title: 'Erro',
	                text: 'Erro ao retirar guerreiro da retaguarda.',
					timer: 5000,
					showConfirmButton: false,
				    background: 'transparent',
					color: '#ff3b3b'
	            });
	        } finally {
	            setTimeout(() => {
	                emCooldownRetirarRetaguarda = false;
	                btnRetirarRetaguarda.disabled = false;
	                btnRetirarRetaguarda.innerText = textoOriginal;
	            }, tempoCooldownRetirarRetaguarda * 1000);
	        }
	    });
	}

    // ==============================
    // INIT
    // ==============================
    atualizarStatus();
    setInterval(atualizarStatus, 10000);

});

