document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;
    if (!usuarioId) return;

	const pocaoVigorErroImg ="icones/erro_img/pocao_vigor_erro.webp";
	const pocaoVigorOkImg ="icones/ok_img/pocao_vigor_ok.webp";
    const bossCoinErroImg ="icones/erro_img/boss_coin_erro.webp";
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
        
		const estoque = Number(status.estoque) || 0;
		const equipada = status.ativa;
		const podeAtivar = status.podeAtivar;

		const pocaoVigorItem = pocaoVigorSpan?.closest('.nucleo-item-pocao');
		
				
		if (pocaoVigorItem && pocaoVigorSpan) {
		    if (estoque <= 0) {
		        pocaoVigorItem.classList.add('hidden'); // some tudo
		    } else {
		        pocaoVigorItem.classList.remove('hidden');
		        pocaoVigorSpan.textContent = formatarNumero(estoque);
		    }
		}

		
		//
        // Atualiza estoque
        ///if (pocaoVigorSpan) {
         //   pocaoVigorSpan.textContent = formatarNumero(estoque);
     //   }

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
	    const tempoCooldownCompra = 5; // segundos

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
	             
	                title: 'Quantidade inválida',
	                text: 'Informe uma quantidade válida.',
					imageUrl: pocaoVigorErroImg,							  
					imageWidth: 90,											   
					imageHeight: 120,
	                confirmButtonText: 'Ok',
					background: 'transparent',
					color: '#ff3b3b '
	            });
	            return;
	        }

	        emCooldownCompra = true;
	        btnComprar.disabled = true;

	      //  let tempoRestante = tempoCooldownCompra;
			
	        const textoOriginal = btnComprar.innerText;

			btnComprar.innerText = `Comprando poção de Vigor...`;
	     

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
						  title: `Você comprou <b>${quantidade}</b> poção de <b>Vigor Automático</b>.`,
						  html: 'Compra realizada!',
						  imageUrl: pocaoVigorOkImg,
						  imageWidth: 90,   
						  imageHeight: 120, 
						  imageAlt: 'Poção de Vigor Automático',
						  timer: 5000,
						  showConfirmButton: false,
						  background: 'transparent',
						  color: '#ffb400'
						});

						
					}else{
						
						Swal.fire({
												  customClass: {      
												   title: 'swal-game-text'
												  },
												  title: `Você comprou <b>${quantidade}</b> poções de <b>Vigor Automático</b>.`,
												  html: 'Compra realizada!',
												  imageUrl: pocaoVigorOkImg,
												  imageWidth: 90,   
												  imageHeight: 120,
												  imageAlt: 'Poção de Vigor Automático',
												  timer: 5000,
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
	                
	                    title: 'Saldo insuficiente',
	                    text: text || 'Não foi possível comprar.',
						imageUrl: bossCoinErroImg,							  
						imageWidth: 120,											   
						imageHeight: 120,
	                    timer: 5000,
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
	                text: 'Erro ao tentar comprar Poção de Vigor.',
					imageUrl: pocaoVigorErroImg,							  
					imageWidth: 90,											   
					imageHeight: 120,
	                timer: 5000,
					
	                showConfirmButton: false,
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
	const tempoCooldown = 5; // segundos

	btnAtivarPocao.addEventListener('click', async () => {
	    if (emCooldown) return;

	    emCooldown = true;
	    btnAtivarPocao.disabled = true;

	  //  let tempoRestante = tempoCooldown;
	    const textoOriginal = btnAtivarPocao.innerText;
		
		btnAtivarPocao.innerText = `Ativando poção...`;
	  
	    try {
			const res1 = await fetch(`/api/atualizar/status/ajustes/${usuarioId}`);
			if (!res1.ok) return;

			const status = await res1.json();
	        const quantidade = 1;
			const ativoGuerreiro = status.ativoGuerreiro;
							
			if ( ativoGuerreiro <= 0  )return;
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
				timer: 5000,
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

