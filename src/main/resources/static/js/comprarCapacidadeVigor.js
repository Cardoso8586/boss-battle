document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;
    if (!usuarioId) return;

    const btn = document.querySelector('[data-item="energia"]');
    if (!btn) return;

	
	const capacidadeVigorErroImg ="icones/erro_img/capacidade_vigor_erro.webp";
    const capacidadeVigorOkImg ="icones/ok_img/capacidade_vigor_ok.webp";
	const bossCoinErroImg ="icones/erro_img/boss_coin_erro.webp";
		
		
    let emCooldownEnergia = false;
    const tempoCompraEnergia = 5; // ⏱️ segundos

    btn.addEventListener('click', async () => {

        // 🔒 trava total
        if (emCooldownEnergia) return;

        const card = btn.closest('.loja-card');
        const quantidadeInput = card.querySelector('#quantidade-energia');
        const quantidade = parseInt(quantidadeInput.value);

        if (!quantidade || quantidade <= 0) {
            Swal.fire({
				customClass: {
			    title: 'swal-game-error' },
              
                title: 'Quantidade inválida',
                text: 'Informe uma quantidade válida.',
				imageUrl: capacidadeVigorErroImg,							  
				imageWidth: 120,											   
			    imageHeight: 120,
                confirmButtonText: 'Ok',
				background: 'transparent',
			    color: '#ff3b3b'  
            });
            return;
        }

        emCooldownEnergia = true;
        btn.disabled = true;

        const textoOriginal = btn.innerText;
        let restante = tempoCompraEnergia;

        // ⏳ TEXTO DO BOTÃO
        btn.innerText = `Aplicando capacidade de Vigor...`;
		//btn.innerText = `Aplicando Vigor... (${restante}s)`;

       // const timer = setInterval(() => {
         //   restante--;
            btn.innerText = `Aplicando Vigor...`;
			//btn.innerText = `Aplicando Vigor... (${restante}s)`;
           // if (restante <= 0) clearInterval(timer);
       // }, 1000);

        try {
            const res = await fetch(`/comprar/energia/${usuarioId}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ quantidade })
            });

            if (res.ok) {
				
				if (quantidade <=1){
					
				    Swal.fire({
						customClass: {      
																		title: 'swal-game-text'
																		},
				    title: 'Capacidade aumentada!',
				    html: `Você comprou ${quantidade} unidade de Vigor.`,
				    imageUrl: capacidadeVigorOkImg,
				    imageWidth: 120,   
				    imageHeight: 120, 
				    imageAlt: 'Poção de Vigor',
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
							    title: 'Capacidade aumentada!',
							    html: `Você comprou ${quantidade} unidades de Vigor.`,
							    imageUrl: capacidadeVigorOkImg,
							    imageWidth: 120,   
							    imageHeight: 120, 
							    imageAlt: 'Poção de Vigor',
							    timer: 5000,
							    showConfirmButton: false,
								background: 'transparent',
							    color: '#ffb400'       
							});
					
				}
			


                // 🔁 Atualiza UI após compra
                if (typeof atualizarUsuario === 'function') {
                    atualizarUsuario();
                }

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
					timer: 4000,
                    showConfirmButton: false,
					background: 'transparent',
				    color: '#ff3b3b'     
                });
            }

        } catch (e) {
            console.error(e);

            Swal.fire({
				customClass: {
									   title: 'swal-game-error'
									 },
               
                title: 'Erro',
                text: 'Erro ao tentar aumentar capacidade de vigor.',
				imageUrl: capacidadeVigorErroImg,							  
			    imageWidth: 120,											   
				imageHeight: 120,
                timer: 5000,
                showConfirmButton: false,
				background: 'transparent',
				color: '#ff3b3b'      
            });

        } finally {
            setTimeout(() => {
                emCooldownEnergia = false;
                btn.disabled = false;
                btn.innerText = textoOriginal;
            }, tempoCompraEnergia * 1000);
        }
    });
});

/**
 * 
 * 
 * document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;
    if (!usuarioId) return;

    const btn = document.querySelector('[data-item="energia"]');
    if (!btn) return;

    btn.addEventListener('click', async () => {
        const card = btn.closest('.loja-card');
        const quantidadeInput = card.querySelector('#quantidade-energia');
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

        try {
            const res = await fetch(`/comprar/energia/${usuarioId}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ quantidade })
            });

            if (res.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Capacidade Aumentada!',
                    text: `Você comprou ${quantidade} unidade(s) de Vigor.`,
                    confirmButtonText: 'Ok'
                });

                // 🔁 Atualiza UI após compra
                if (typeof atualizarUsuario === 'function') {
                    atualizarUsuario();
                }

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
                text: 'Erro ao tentar aumentar capacidade de vigor.',
                confirmButtonText: 'Ok'
            });
        }
    });
});

 */
