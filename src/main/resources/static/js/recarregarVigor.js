document.addEventListener('DOMContentLoaded', () => {

    const btnRecarregar = document.getElementById('btnRecarregar');
    const energiaAtual = document.getElementById('energiaAtual');
    const energiaMaxima = document.getElementById('energiaMaxima');
    const energiaBar = document.getElementById('energiaBar');

    const metaUserId = document.querySelector('meta[name="user-id"]');

    if (!btnRecarregar || !energiaAtual || !energiaMaxima || !energiaBar || !metaUserId) {
        return;
    }

    const usuarioId = parseInt(metaUserId.getAttribute('content'));

    let emCooldownRecarregar = false;
    const tempoCooldownRecarregar = 4;
    const textoOriginal = btnRecarregar.innerText;

    function controlarBotaoRecarregar() {

        const vigorAtual = Number(energiaAtual.textContent);
        const vigorMaximo = Number(energiaMaxima.textContent);

        // Só bloqueia quando estiver cheio
        btnRecarregar.disabled = vigorAtual >= vigorMaximo;
    }

    function atualizarBarra(vigorAtual, vigorMaximo) {

        const percentual = vigorMaximo > 0
            ? Math.min((vigorAtual * 100) / vigorMaximo, 100)
            : 0;

        energiaAtual.textContent = vigorAtual;
        energiaMaxima.textContent = vigorMaximo;
        energiaBar.style.width = percentual + '%';

        controlarBotaoRecarregar();
    }

    // Estado inicial do botão
    controlarBotaoRecarregar();

    btnRecarregar.addEventListener('click', async () => {

        // Impede clique durante cooldown
        if (emCooldownRecarregar) {
            return;
        }

        // Verifica novamente se já está cheio
        const vigorAtual = Number(energiaAtual.textContent);
        const vigorMaximo = Number(energiaMaxima.textContent);

        if (vigorAtual >= vigorMaximo) {
            return;
        }

        emCooldownRecarregar = true;

        btnRecarregar.disabled = true;
        btnRecarregar.innerText = 'Recarregando Vigor...';

        try {

            const res = await fetch(
                `/recarregar-energia?usuarioId=${usuarioId}`,
                { method: 'POST' }
            );

            if (!res.ok) {

                Swal.fire({
                    customClass: {
                        title: 'swal-game-error'
                    },
                    icon: 'error',
                    title: 'Erro',
                    html: `
                        <p>Não foi possível recarregar o Vigor.</p>

                        <div class="modal-anuncio">
                            <iframe
                                src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                                width="468"
                                height="60"
                                scrolling="no"
                                frameborder="0">
                            </iframe>
                        </div>
                    `,
                    confirmButtonText: 'Ok',
                    background: 'transparent',
                    color: '#ff3b3b'
                });

                return;
            }

            const data = await res.json();

            atualizarBarra(
                Number(data.energiaGuerreiros),
                Number(data.energiaGuerreirosPadrao)
            );

            Swal.fire({
                customClass: {
                    title: 'swal-game-text'
                },
                title: 'Vigor restaurado!',
                html: `
                    <div class="modal-anuncio">
                        <iframe
                            src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                            width="468"
                            height="60"
                            scrolling="no"
                            frameborder="0">
                        </iframe>
                    </div>
                `,
                timer: 7000,
                showConfirmButton: false,
                background: 'transparent',
                color: '#ffb400'
            });

        } catch (err) {

            console.error(err);

            Swal.fire({
                customClass: {
                    title: 'swal-game-error'
                },
                icon: 'error',
                title: 'Erro',
                html: `
                    <p>Erro ao tentar recarregar Vigor.</p>

                    <div class="modal-anuncio">
                        <iframe
                            src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                            width="468"
                            height="60"
                            scrolling="no"
                            frameborder="0">
                        </iframe>
                    </div>
                `,
                confirmButtonText: 'Ok',
                background: 'transparent',
                color: '#ff3b3b'
            });

        } finally {

            setTimeout(() => {

                emCooldownRecarregar = false;

                btnRecarregar.innerText = textoOriginal;

                // Atualiza estado do botão
                controlarBotaoRecarregar();

            }, tempoCooldownRecarregar * 1000);
        }
    });
});
/*

ANTIGO DA PAGINA DASBOARD
document.addEventListener('DOMContentLoaded', () => {

    const btnRecarregar = document.getElementById('btnRecarregar');
    const energiaAtual = document.getElementById('energiaAtual');
    const energiaBar = document.getElementById('energiaBar');
    const usuarioId = parseInt(
        document.querySelector('meta[name="user-id"]').getAttribute('content')
    );

    if (!btnRecarregar) return;

    let emCooldownRecarregar = false;
    const tempoCooldownRecarregar = 4; // segundos

    btnRecarregar.addEventListener('click', async () => {

        if (emCooldownRecarregar) return;

        emCooldownRecarregar = true;
        btnRecarregar.disabled = true;

       // let tempoRestante = tempoCooldownRecarregar;
        const textoOriginal = btnRecarregar.innerText;

		
		
        btnRecarregar.innerText = `Recarregando Vigor...`;



        try {
            const res = await fetch(
                `/recarregar-energia?usuarioId=${usuarioId}`,
                { method: 'POST' }
            );

            if (!res.ok) {
		
					Swal.fire({
					stomClass: {title: 'swal-game-error'},
					icon: 'error',
					title: 'Erro',
					text: 'Não foi possível recarregar o Vigor.',
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

            const data = await res.json();
            energiaAtual.textContent = data.energiaGuerreiros;
            energiaBar.style.width =
            Math.min(data.energiaGuerreiros, 100) + '%';
				
			Swal.fire({
				customClass: {
				      title: 'swal-game-text'
				    },
			  title: 'Vigor restaurado!',
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
			  timer: 7000,
			  showConfirmButton: false,
			  background: 'transparent',
			  color: '#ffb400'
			
			});


            // se chegou no máximo, mantém desabilitado
            if (data.energiaGuerreiros >= data.energiaGuerreirosPadrao) {
                btnRecarregar.disabled = true;
            }

        } catch (err) {
            console.error(err);
			
		
			   Swal.fire({
			   stomClass: {title: 'swal-game-error'},
                icon: 'error',
                title: 'Erro',
                text: 'Erro ao tentar recarregar Vigor.',
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
*/