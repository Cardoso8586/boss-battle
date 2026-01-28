
document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? Number(meta.content) : null;
    if (!usuarioId) return;

    // ==============================
    // ELEMENTOS
    // ==============================
    const machadoSpan = document.getElementById('machadoDilacerador');
    const btnAtivarMachado = document.getElementById('btnAtivarMachadoDilacerador');
	const btnAtivarEspada = document.getElementById('btnAtivarEspadaFlanejante');
    const machadoInfos = document.querySelectorAll('.machado-dilacerador-ativo-info');

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
            atualizarMachadoDilacerador(status);

        } catch (e) {
            console.error('Erro ao atualizar machado:', e);
        }
    }

    // ==============================
    // ATUALIZA MACHADO
    // ==============================
	function atualizarMachadoDilacerador(status) {

	    const machadoEstoque = status.qtdMachadoDilaceradorEstoque ?? 0;
	    const machadoAtivo = status.qtdMachadoDilaceradorAtivo ?? 0;
	    const podeAtivar = status.podeAtivarMachadoDilacerador === true;

	    // ⚔️ OUTRA ARMA
	    const espadaAtiva = status.espadaFlanejanteAtiva ?? 0;

	    const machadoItem = machadoSpan?.closest('.nucleo-item-dilacerador');

		
	
	    // ITEM (estoque)
	    if (machadoItem && machadoSpan) {
	        if (machadoEstoque === 0 ) {
	            machadoItem.classList.add('hidden');
	        } else {
	            machadoItem.classList.remove('hidden');
	            machadoSpan.textContent = formatarNumero(machadoEstoque);
	        }
	    }

	    // 🔒 BOTÃO ATIVAR (REGRA COMPLETA)
	    if (btnAtivarMachado) {
	        const mostrarBotao =
	            machadoEstoque > 0 &&
	            machadoAtivo === 0 &&
	            podeAtivar &&
	            espadaAtiva === 0;   // 🚫 BLOQUEIA SE ESPADA ATIVA

	        btnAtivarMachado.classList.toggle('hidden', !mostrarBotao);
	        btnAtivarMachado.disabled = !mostrarBotao;
	    }

	    // ℹ️ INFO ATIVO
	    machadoInfos.forEach(div => {
	        if (machadoAtivo > 0) {
	            div.classList.remove('hidden');
	            div.textContent = `✔ Machado Dilacerador equipado (${machadoAtivo})`;
	        } else {
	            div.classList.add('hidden');
	        }
	    });
	}

    // ==============================
    // ATIVAR MACHADO
    // ==============================
    let emCooldown = false;
    const tempoCooldown = 4;
	if (btnAtivarMachado) {
	    btnAtivarMachado.addEventListener('click', async () => {

	        if (emCooldown) return;

	        emCooldown = true;
	        btnAtivarMachado.disabled = true;

	        // 🔒 trava botão da espada imediatamente
	        btnAtivarEspada.style.pointerEvents = 'none';
	        btnAtivarEspada.style.opacity = '0.5';

	        const textoOriginal = btnAtivarMachado.innerText;
	        btnAtivarMachado.innerText = 'Ativando...';

	        try {
	            const res1 = await fetch(`/api/atualizar/status/ajustes/${usuarioId}`);
	            if (!res1.ok) {
	                // 🔄 destrava espada se falhar
	                btnAtivarEspada.style.pointerEvents = 'auto';
	                btnAtivarEspada.style.opacity = '1';
	                return;
	            }

	            const status = await res1.json();
	            const ativoGuerreiro = status.ativoGuerreiro;
	            const espadaAtiva = status.espadaFlanejanteAtiva ?? 0;

	            clearInterval(window.loopMachado);

	            if (ativoGuerreiro <= 0) {
	                Swal.fire({
	                    icon: 'warning',
	                    title: 'Ação bloqueada',
	                    text: 'Você não pode equipar armas agora.',
	                    background: 'transparent',
	                    color: '#ff3b3b'
	                });

	                // 🔄 destrava espada
	                btnAtivarEspada.style.pointerEvents = 'auto';
	                btnAtivarEspada.style.opacity = '1';
	                return;
	            }

	            if (espadaAtiva === 1) {
	                Swal.fire({
	                    icon: 'warning',
	                    title: 'Arma incompatível',
	                    text: 'Desequipe a espada antes de equipar o machado.',
	                    background: 'transparent',
	                    color: '#ff3b3b'
	                });

	                // 🔄 destrava espada
	                btnAtivarEspada.style.pointerEvents = 'auto';
	                btnAtivarEspada.style.opacity = '1';
	                return;
	            }

	            const res = await fetch(
	                `/api/machado-dilacerador/ativar?usuarioId=${usuarioId}&quantidade=1`,
	                { method: 'POST' }
	            );

	            if (!res.ok) {
	                const erro = await res.text();
	                Swal.fire({
	                    icon: 'warning',
	                    title: 'Erro',
	                    text: erro,
	                    background: 'transparent',
	                    color: '#ff3b3b'
	                });

	                // 🔄 destrava espada
	                btnAtivarEspada.style.pointerEvents = 'auto';
	                btnAtivarEspada.style.opacity = '1';
	                return;
	            }

	            Swal.fire({
	                icon: 'success',
	                title: 'Machado equipado!',
	                text: 'Seu Machado Dilacerador foi equipado com sucesso.',
	                timer: 7000,
	                showConfirmButton: false,
	                background: 'transparent',
	                color: '#ffb400'
	            });

	            await atualizarUsuario();
	            // ✅ sucesso → espada permanece travada

	        } catch (e) {
	            console.error(e);

	            Swal.fire({
	                icon: 'error',
	                title: 'Erro',
	                text: 'Erro ao tentar equipar o machado.',
	                background: 'transparent',
	                color: '#ff3b3b'
	            });

	            // 🔄 destrava espada no catch
	            btnAtivarEspada.style.pointerEvents = 'auto';
	            btnAtivarEspada.style.opacity = '1';

	        } finally {
	            setTimeout(() => {
	                emCooldown = false;
	                btnAtivarMachado.disabled = false;
	                btnAtivarMachado.innerText = textoOriginal;
	            }, tempoCooldown * 1000);
	        }
	    });
	}

/**
 *  if (btnAtivarMachado) {
        btnAtivarMachado.addEventListener('click', async () => {

            if (emCooldown) return;
		
				
            emCooldown = true;
            btnAtivarMachado.disabled = true;

            const textoOriginal = btnAtivarMachado.innerText;
            btnAtivarMachado.innerText = 'Ativando...';

            try {
				const res1 = await fetch(`/api/atualizar/status/ajustes/${usuarioId}`);
			    if (!res1.ok) return;
				const status = await res1.json();
				const ativoGuerreiro = status.ativoGuerreiro;
				const espadaAtiva = status.espadaFlanejanteAtiva ?? 0;

				
			
				//if ( ativoGuerreiro <= 0  )return;
				clearInterval(window.loopMachado);

                const res = await fetch(
                    `/api/machado-dilacerador/ativar?usuarioId=${usuarioId}&quantidade=1`,
                    { method: 'POST' }
                );

				if (ativoGuerreiro <= 0) {
				    Swal.fire({
				        icon: 'warning',
				        title: 'Ação bloqueada',
				        text: 'Você não pode equipar armas agora.',
				        background: 'transparent',
				        color: '#ff3b3b'
				    });
				    return;
				}

				if (espadaAtiva === 1) {
				    Swal.fire({
				        icon: 'warning',
				        title: 'Arma incompatível',
				        text: 'Desequipe a espada antes de equipar o machado.',
				        background: 'transparent',
				        color: '#ff3b3b'
				    });
				    return;
				}


				
                if (!res.ok) {
                    const erro = await res.text();
                    Swal.fire({
                        icon: 'warning',
                        title: 'Erro',
                        text: erro,
                        background: 'transparent',
                        color: '#ff3b3b'
                    });
                    return;
                }

                Swal.fire({
                    icon: 'success',
                    title: 'Machado equipado!',
                    text: 'Seu Machado Dilacerador foi equipado com sucesso.',
                    timer: 7000,
                    showConfirmButton: false,
                    background: 'transparent',
                    color: '#ffb400'
                });

                await atualizarUsuario();

            } catch (e) {
                console.error(e);
                Swal.fire({
                    icon: 'error',
                    title: 'Erro',
                    text: 'Erro ao tentar equipar o machado.',
                    background: 'transparent',
                    color: '#ff3b3b'
                });
            } finally {
                setTimeout(() => {
                    emCooldown = false;
                    btnAtivarMachado.disabled = false;
                    btnAtivarMachado.innerText = textoOriginal;
                }, tempoCooldown * 1000);
            }
        });
    }
 * 
 */
   

    // ==============================
    // LOOP
    // ==============================
    atualizarUsuario();
	window.loopMachado = setInterval(atualizarUsuario, 5000);

   // setInterval(atualizarUsuario, 5000);
});


