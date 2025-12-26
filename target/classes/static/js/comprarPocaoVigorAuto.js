document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;
    if (!usuarioId) return;

    // ==============================
    // ELEMENTOS DA POÇÃO
    // ==============================
    const pocaoVigorSpan  = document.getElementById('pocaoVigor');
    const btnAtivarPocao  = document.getElementById('btnAtivarPocao');
    const pocaoAtivaInfos = document.querySelectorAll('.pocao-ativa-info');

    // ==============================
    // ELEMENTOS DE COMPRA
    // ==============================
    const btnComprar = document.querySelector('[data-item="PocaoAutomaticaVigor"]');

    // ==============================
    // FORMATAÇÃO DE NÚMEROS
    // ==============================
    function formatarNumero(numero) {
        return new Intl.NumberFormat('pt-BR').format(numero);
    }

    // ==============================
    // FUNÇÃO DE ATUALIZAÇÃO DO NÚCLEO / POÇÃO
    // ==============================
    async function atualizarUsuario() {
        try {
            const res = await fetch(`/api/atualizar/status/ajustes/${usuarioId}`);
            if (!res.ok) return;

            const status = await res.json();
            atualizarNucleo(status);

        } catch (e) {
            console.error("Erro ao atualizar núcleo:", e);
        }
    }

    function atualizarNucleo(status) {
        const estoque    = status.estoque;
        const equipada   = status.ativa;
        const podeAtivar = status.podeAtivar;

        // Atualiza estoque
        if (pocaoVigorSpan) {
            pocaoVigorSpan.textContent = formatarNumero(estoque);
        }

        // Controla botão de ativar
        if (btnAtivarPocao) {
            if (estoque > 0 && equipada < 3 && podeAtivar) {
                btnAtivarPocao.classList.remove('hidden');
                btnAtivarPocao.disabled = false;
            } else {
                btnAtivarPocao.classList.add('hidden');
            }
        }

        // Atualiza todos os elementos de poção ativa
        pocaoAtivaInfos.forEach(elem => {
            if (equipada > 0) {
                elem.classList.remove('hidden');
                elem.textContent = `✔ Poção equipada (${equipada})`;
            } else {
                elem.classList.add('hidden');
            }
        });
    }

    // ==============================
    // BOTÃO DE COMPRA
    // ==============================
    if (btnComprar) {
        btnComprar.addEventListener('click', async () => {
            const card = btnComprar.closest('.loja-card');
            const quantidadeInput = card.querySelector('.quantidade-item');
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

            btnComprar.disabled = true; // evita clique múltiplo

            try {
                const res = await fetch(`/comprar/pocao-vigor/${usuarioId}`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ quantidade })
                });

                if (res.ok) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Compra realizada!',
                        text: `Você comprou ${quantidade} poção(ões) de Vigor Automático.`,
                        confirmButtonText: 'Ok'
                    });

                    // Atualiza UI imediatamente após compra
                    atualizarUsuario();
					

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
                    text: 'Erro ao tentar comprar Poção Automática de Vigor.',
                    confirmButtonText: 'Ok'
                });
            } finally {
                btnComprar.disabled = false;
            }
        });
    }
	// ==============================
	// BOTÃO DE ATIVAR POÇÃO
	// ==============================
	if (btnAtivarPocao) {
	    btnAtivarPocao.addEventListener('click', async () => {
	        btnAtivarPocao.disabled = true;

	        try {
	            const res = await fetch(`/ativar/pocao-vigor/${usuarioId}`, {
	                method: 'POST',
	                headers: { 'Content-Type': 'application/json' }
	            });

	            if (res.ok) {
	                Swal.fire({
	                    icon: 'success',
	                    title: 'Poção ativada!',
	                    text: 'Sua poção de vigor foi ativada com sucesso.',
	                    confirmButtonText: 'Ok'
	                });

	                // Atualiza UI imediatamente após ativar
	                atualizarUsuario();

	            } else {
	                const text = await res.text();
	                Swal.fire({
	                    icon: 'warning',
	                    title: 'Não foi possível ativar',
	                    text: text || 'Erro ao ativar poção.',
	                    confirmButtonText: 'Ok'
	                });
	            }
	        } catch (e) {
	            console.error(e);
	            Swal.fire({
	                icon: 'error',
	                title: 'Erro',
	                text: 'Erro ao tentar ativar poção.',
	                confirmButtonText: 'Ok'
	            });
	        } finally {
	            btnAtivarPocao.disabled = false;
	        }
	    });
	}
	/**
	 * 
	 * 
	 */
	// ==============================
	   // Evento do botão ativar poção
	   // ==============================
	   btnAtivarPocao.addEventListener('click', async () => {

	       btnAtivarPocao.disabled = true;

	       try {
	           const quantidade = 1; // ativa 1 poção por clique

	           const res = await fetch(
	               `/api/pocao-vigor/ativar?usuarioId=${usuarioId}&quantidade=${quantidade}`,
	               { method: 'POST' }
	           );

	           if (!res.ok) {
	               const erro = await res.text();
	               alert(erro);
	               return;
	           }

	           await atualizarUsuario(); // atualiza UI imediatamente
	       } catch (e) {
	           console.error(e);
	       } finally {
	           btnAtivarPocao.disabled = false;
	       }
	   });


    // ==============================
    // ATUALIZAÇÃO PERIÓDICA
    // ==============================
    atualizarUsuario(); // primeira atualização imediata
    setInterval(atualizarUsuario, 5000); // atualiza a cada 5 segundos
});

