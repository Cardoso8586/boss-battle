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
	// BOTÃO DE COMPRA (COM TEMPORIZADOR)
	// ==============================
	if (btnComprar) {

	    let emCooldownCompra = false;
	    const tempoCooldownCompra = 3; // segundos

	    btnComprar.addEventListener('click', async () => {

	        if (emCooldownCompra) return;

	        const card = btnComprar.closest('.loja-card');
	        const quantidadeInput = card.querySelector('.quantidade-item');
	        const quantidade = parseInt(quantidadeInput.value);

	        if (!quantidade || quantidade <= 0) {
	            Swal.fire({
					customClass: {
				    title: 'swal-game-error'
				    },
	                icon: 'warning',
	                title: 'Quantidade inválida',
	                text: 'Informe uma quantidade válida.',
	                confirmButtonText: 'Ok',
					background: 'transparent',
					color: '#ff3b3b '
	            });
	            return;
	        }

	        emCooldownCompra = true;
	        btnComprar.disabled = true;

	        let tempoRestante = tempoCooldownCompra;
	        const textoOriginal = btnComprar.innerText;

	        btnComprar.innerText = `Comprando... (${tempoRestante}s)`;

	        const timer = setInterval(() => {
	            tempoRestante--;
	            btnComprar.innerText = `Comprando... (${tempoRestante}s)`;
	            if (tempoRestante <= 0) clearInterval(timer);
	        }, 1000);

	        try {
	            const res = await fetch(`/comprar/pocao-vigor/${usuarioId}`, {
	                method: 'POST',
	                headers: { 'Content-Type': 'application/json' },
	                body: JSON.stringify({ quantidade })
	            });

	            if (res.ok) {
					
					if (quantidade<=1){
						Swal.fire({
						  customClass: {      
						    title: 'swal-game-text'
						  },
						  title: 'Compra realizada!',
						  html: `Você comprou <b>${quantidade}</b> poção de <b>Vigor Automático</b>.`,
						  imageUrl: '/icones/pocao_vigor.webp',
						  imageWidth: 90,
						  imageHeight: 90,
						  imageAlt: 'Poção de Vigor Automático',
						  timer: 4000,
						  showConfirmButton: false,
						  background: 'transparent',
						  color: '#ffb400'
						});

						
					}else{
						
						Swal.fire({
												  customClass: {      
												   title: 'swal-game-text'
												  },
												  title: 'Compra realizada!',
												  html: `Você comprou <b>${quantidade}</b> poções de <b>Vigor Automático</b>.`,
												  imageUrl: '/icones/pocao_vigor.webp',
												  imageWidth: 90,
												  imageHeight: 90,
												  imageAlt: 'Poção de Vigor Automático',
												  timer: 4000,
												  showConfirmButton: false,
												  background: 'transparent',
												  color: '#ffb400'
												});
						
					}
					


	                atualizarUsuario();

	            } else {
	                const text = await res.text();
	                Swal.fire({
						customClass: {
											   title: 'swal-game-error'
											 },
	                    icon: 'warning',
	                    title: 'Saldo insuficiente',
	                    text: text || 'Não foi possível comprar.',
	                    timer: 4000,
	                    showConfirmButton: false,
						// background: '#0f0f0f',
						background: 'transparent',
				        color: '#ff3b3b '
	                });
	            }

	        } catch (e) {
	            console.error(e);
	            Swal.fire({
					customClass: {
										   title: 'swal-game-error'
										 },
	                icon: 'error',
	                title: 'Erro',
	                text: 'Erro ao tentar comprar Poção Automática de Vigor.',
	                timer: 4000,
	                showConfirmButton: false,
					// background: '#0f0f0f',
					bbackground: 'transparent',
					color: '#ff3b3b '
	            });

	        } finally {
	            setTimeout(() => {
	                emCooldownCompra = false;
	                btnComprar.disabled = false;
	                btnComprar.innerText = textoOriginal;
	            }, tempoCooldownCompra * 1000);
	        }
	    });
	}

	let emCooldown = false;
	const tempoCooldown = 3; // segundos

	btnAtivarPocao.addEventListener('click', async () => {
	    if (emCooldown) return;

	    emCooldown = true;
	    btnAtivarPocao.disabled = true;

	    let tempoRestante = tempoCooldown;
	    const textoOriginal = btnAtivarPocao.innerText;

	    btnAtivarPocao.innerText = `Ativando... (${tempoRestante}s)`;

	    const timer = setInterval(() => {
	        tempoRestante--;
	        btnAtivarPocao.innerText = `Ativando... (${tempoRestante}s)`;

	        if (tempoRestante <= 0) {
	            clearInterval(timer);
	        }
	    }, 1000);

	    try {
	        const quantidade = 1;

	        const res = await fetch(
	            `/api/pocao-vigor/ativar?usuarioId=${usuarioId}&quantidade=${quantidade}`,
	            { method: 'POST' }
	        );

	        if (!res.ok) {
	            const erro = await res.text();
	            Swal.fire({
					customClass: {
										   title: 'swal-game-error'
										 },
	                icon: 'warning',
	                title: 'Erro',
	                text: erro,
					background: 'transparent',
				    color: '#ff3b3b '
	            });
	            return;
	        }

	        Swal.fire({
				customClass: {      
				title: 'swal-game-text'
				},
	            icon: 'success',
	            title: 'Poção ativada!',
	            text: 'Sua poção foi ativada com sucesso.',
				timer: 4000,
				showConfirmButton: false,
				background: 'transparent',
								
				color: '#ffb400'
	        });

	        await atualizarUsuario();

	    } catch (e) {
	        console.error(e);
	        Swal.fire({
				customClass: {
				title: 'swal-game-error'
				},
	            icon: 'error',
	            title: 'Erro',
	            text: 'Erro ao tentar ativar poção.',
				background: 'transparent',
								
				color: '#ff3b3b'
	        });
	    } finally {
	        setTimeout(() => {
	            emCooldown = false;
	            btnAtivarPocao.disabled = false;
	            btnAtivarPocao.innerText = textoOriginal;
	        }, tempoCooldown * 1000);
	    }
	});


    // ==============================
    // ATUALIZAÇÃO PERIÓDICA
    // ==============================
    atualizarUsuario(); // primeira atualização imediata
    setInterval(atualizarUsuario, 5000); // atualiza a cada 5 segundos
});

