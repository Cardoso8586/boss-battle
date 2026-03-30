
document.addEventListener('DOMContentLoaded', () => {

    const btn = document.querySelector('[data-item="ataqueEspecial"]');
    if (!btn) return;

    let emCooldownCompra = false;
    const tempoCompra = 5; // ⏱️ segundos

	const ataqueEspecialErroImg ="icones/erro_img/ataque_especial_erro.webp";
	const ataqueEspeciaOkImg ="icones/ok_img/ataque_especial_ok.webp";
    const bossCoinErroImg ="icones/erro_img/boss_coin_erro.webp";
		
    btn.addEventListener('click', async () => {

        // 🔒 trava total
        if (emCooldownCompra) return;

        const card = btn.closest('.loja-card');

        const quantidade = parseInt(
            card.querySelector('#quantidade-ataque').value
        );

        if (isNaN(quantidade) || quantidade <= 0) {
            Swal.fire({
				customClass: {
				title: 'swal-game-error'
				},
               
                title: 'Quantidade inválida',
                text: 'Informe uma quantidade válida.',
				imageUrl: ataqueEspecialErroImg,							  
				imageWidth: 120,											   
				imageHeight: 120,
				html: `
								  		      <div class="modal-anuncio">
								  		        <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
								  		          width="468"
								  		          height="60"
								  		          scrolling="no"
								  		          frameborder="0">
								  		        </iframe>
								  		      </div>
								  		    `,
                confirmButtonText: 'Ok',
				background: 'transparent',
				color: '#ff3b3b '      
            });
            return;
        }

        emCooldownCompra = true;
        btn.disabled = true;

        const textoOriginal = btn.innerText;
       // let restante = tempoCompra;

        // ⏳ TEXTO CORRETO
		btn.innerText = `Aumentando Ataque Especial...`;
      

        const precoUnitario = parseFloat(
            card.querySelector('.preco').textContent.replace(/[^\d]/g, '')
        );

        try {
            const res = await fetch(
                `/comprar/ataque-especial/${usuarioId}`,
                {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ quantidade })
                }
            );

            if (res.ok) {
				
				if (quantidade<=1){
					
				Swal.fire({
					customClass: {      
										title: 'swal-game-text'
										},
				    title: 'Ataque especial aprimorado!',
				    html: `Você adquiriu <b>${quantidade}</b> unidade de ataque especial.`,
					imageUrl: ataqueEspeciaOkImg,
					imageWidth: 120,   
					imageHeight: 120, 
				    imageAlt: 'Ataque Especial',
					html: `
									  		      <div class="modal-anuncio">
									  		        <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
									  		          width="468"
									  		          height="60"
									  		          scrolling="no"
									  		          frameborder="0">
									  		        </iframe>
									  		      </div>
									  		    `,
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
									    title: 'Ataque especial aprimorado!',
									    html: `Você adquiriu <b>${quantidade}</b> unidades de ataque especial.`,
										imageUrl: ataqueEspeciaOkImg,
										imageWidth: 120,   
										imageHeight: 120, 
									    imageAlt: 'Ataque Especial',
										html: `
														  		      <div class="modal-anuncio">
														  		        <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
														  		          width="468"
														  		          height="60"
														  		          scrolling="no"
														  		          frameborder="0">
														  		        </iframe>
														  		      </div>
														  		    `,
									    timer: 5000,
									    showConfirmButton: false,
										background: 'transparent',
									    color: '#ffb400'
									  
									    
									});
					
				}
				

                if (typeof atualizarUsuario === 'function') {
                    atualizarUsuario();
                }

            } else {
                const custoEstimado = quantidade * precoUnitario;
                const saldoElement = document.getElementById('boss_coins');
                const saldo = saldoElement
                    ? parseFloat(saldoElement.textContent.replace(/\D/g, ''))
                    : 0;

                Swal.fire({
					customClass: {
																   title: 'swal-game-error'
																 },
                  
                    title: 'Saldo insuficiente',
                    text: `Você precisa de ${custoEstimado.toLocaleString('pt-BR')} Boss Coins, mas só tem ${saldo.toLocaleString('pt-BR')} Boss Coins.`,
					imageUrl: bossCoinErroImg,							  
					imageWidth: 120,											   
					imageHeight: 120,
					html: `
									  		      <div class="modal-anuncio">
									  		        <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
									  		          width="468"
									  		          height="60"
									  		          scrolling="no"
									  		          frameborder="0">
									  		        </iframe>
									  		      </div>
									  		    `,
                    timer: 5000,
                    showConfirmButton: false,
					background: 'transparent',
				   color: '#ff3b3b '      
					
                });
            }

        } catch (err) {
            console.error(err);

            Swal.fire({
				customClass: {
															   title: 'swal-game-error'
															 },
               
                title: 'Erro',
                text: 'Erro ao tentar comprar ataque especial.',
                timer: 5000,
				imageUrl: ataqueEspecialErroImg,							  
				imageWidth: 120,											   
				imageHeight: 120,
				html: `
								  		      <div class="modal-anuncio">
								  		        <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
								  		          width="468"
								  		          height="60"
								  		          scrolling="no"
								  		          frameborder="0">
								  		        </iframe>
								  		      </div>
								  		    `,
                showConfirmButton: false,
				background: 'transparent',
			    color: '#ff3b3b '      
            });

        } finally {
            setTimeout(() => {
                emCooldownCompra = false;
                btn.disabled = false;
                btn.innerText = textoOriginal;
            }, tempoCompra * 1000);
        }
    });
});

/**
 * document.addEventListener('DOMContentLoaded', () => {
     const btn = document.querySelector('[data-item="ataqueEspecial"]');

     if (!btn) return;

     btn.addEventListener('click', async () => {
         const card = btn.closest('.loja-card');

         const quantidade = parseInt(
             card.querySelector('#quantidade-ataque').value
         );

         if (isNaN(quantidade) || quantidade <= 0) {
             Swal.fire({
                 icon: 'warning',
                 title: 'Quantidade inválida',
                 text: 'Informe uma quantidade válida.',
                 confirmButtonText: 'Ok'
             });
             return;
         }

         const precoUnitario = parseFloat(
             card.querySelector('.preco').textContent.replace(/[^\d]/g, '')
         );

         try {
             const res = await fetch(`/comprar/ataque-especial/${usuarioId}`, {
                 method: 'POST',
                 headers: { 'Content-Type': 'application/json' },
                 body: JSON.stringify({
                     quantidade: quantidade,
                    
                 })
             });

             if (res.ok) {
                 Swal.fire({
                     icon: 'success',
                     title: 'Ataque especial aprimorado!',
                     text: `Você comprou ${quantidade} unidade(s) de ataque especial!`,
                     confirmButtonText: 'Ok',
                     background: '#fff',
                     color: '#000'
                 });

                 // Atualiza os dados do usuário
                 if (typeof atualizarUsuario === 'function') {
                     atualizarUsuario();
                 }

             } else {
                 const custoEstimado = quantidade * precoUnitario;
                 const saldoElement = document.getElementById('boss_coins');
                 const saldo = saldoElement
                     ? parseFloat(saldoElement.textContent.replace(/\D/g, ''))
                     : 0;

                 Swal.fire({
                     icon: 'warning',
                     title: 'Saldo insuficiente',
                     text: `Você precisa de ${custoEstimado.toLocaleString('pt-BR')} Boss Coins, mas só tem ${saldo.toLocaleString('pt-BR')} Boss Coins.`,
                     confirmButtonText: 'Ok',
                     background: '#fff',
                     color: '#000'
                 });
             }
         } catch (err) {
             console.error(err);
             Swal.fire({
                 icon: 'error',
                 title: 'Erro',
                 text: 'Erro ao tentar comprar ataque especial.',
                 confirmButtonText: 'Ok'
             });
         }
     });
 }); 
 * 
 */

