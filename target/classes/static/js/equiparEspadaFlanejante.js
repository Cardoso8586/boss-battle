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
    const btnAtivarMachado = document.getElementById('btnAtivarMachadoDilacerador'); 
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

	    // ⚔️ OUTRA ARMA
	    const machadoAtivo = status.qtdMachadoDilaceradorAtivo ?? 0;

	    const espadaItem = espadaSpan?.closest('.nucleo-item-flanejante');
		const arcoAtivo= status.arcoAtivo;
		
	    // ITEM (estoque)
	    if (espadaItem && espadaSpan) {
	        if (estoque === 0) {
	            espadaItem.classList.add('hidden');
	        } else {
	            espadaItem.classList.remove('hidden');
	            espadaSpan.textContent = formatarNumero(estoque);
	        }
	    }

	    // 🔒 BOTÃO ATIVAR (REGRA COMPLETA)
	    if (btnAtivarEspada) {
	        const mostrarBotao =
	            estoque > 0 &&
	            ativa === 0 &&
				arcoAtivo === 0 &&
	            podeAtivar &&
	            machadoAtivo === 0; // 🚫 BLOQUEIA SE MACHADO ATIVO

	        btnAtivarEspada.classList.toggle('hidden', !mostrarBotao);
	        btnAtivarEspada.disabled = !mostrarBotao;
	    }

	    // ℹ️ INFO ATIVO
	    espadaInfos.forEach(div => {
	        if (ativa > 0) {
	            div.classList.remove('hidden');
	            div.textContent = `✔ Espada equipada (${ativa})`;
	        } else {
	            div.classList.add('hidden');
	        }
	    });
	}


    // ==============================
    // ATIVAR ESPADA
    // ==============================
    let emCooldown = false;
    const tempoCooldown = 4;
	if (btnAtivarEspada) {

	    btnAtivarEspada.addEventListener('click', async () => {

	        if (emCooldown) return;

	        emCooldown = true;
	        btnAtivarEspada.disabled = true;

	        // 🔒 trava o botão do machado imediatamente
	        btnAtivarMachado.style.pointerEvents = 'none';
	        btnAtivarMachado.style.opacity = '0.5';

	        const textoOriginal = btnAtivarEspada.innerText;
	        btnAtivarEspada.innerText = 'Ativando...';

	        try {
	            const res1 = await fetch(`/api/atualizar/status/ajustes/${usuarioId}`);
	            if (!res1.ok) {
	                // 🔄 destrava machado se falhar
	                btnAtivarMachado.style.pointerEvents = 'auto';
	                btnAtivarMachado.style.opacity = '1';
	                return;
	            }

	            const status = await res1.json();
	            const ativoGuerreiro = status.ativoGuerreiro;
	            const machadoAtivo = status.ativarMachadoDilacerador ?? 0;

	            clearInterval(window.loopMachado);

	            if (ativoGuerreiro <= 0) {
	                Swal.fire({
	                    icon: 'warning',
	                    title: 'Ação bloqueada',
	                    text: 'Você não pode equipar armas agora.',
	                    background: 'transparent',
	                    color: '#ff3b3b'
	                });

	                // 🔄 destrava machado
	                btnAtivarMachado.style.pointerEvents = 'auto';
	                btnAtivarMachado.style.opacity = '1';
	                return;
	            }

	            if (machadoAtivo === 1) {
	                Swal.fire({
	                    icon: 'info',
	                    title: 'Machado já equipado',
	                    text: 'Desequipe o machado antes de usar a espada.',
	                    background: 'transparent',
	                    color: '#ffb400'
	                });

	                // 🔄 destrava machado
	                btnAtivarMachado.style.pointerEvents = 'auto';
	                btnAtivarMachado.style.opacity = '1';
	                return;
	            }

	            const res = await fetch(
	                `/api/espada-flanejante/ativar?usuarioId=${usuarioId}&quantidade=1`,
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

	                // 🔄 destrava machado
	                btnAtivarMachado.style.pointerEvents = 'auto';
	                btnAtivarMachado.style.opacity = '1';
	                return;
	            }

	            Swal.fire({
	                icon: 'success',
	                title: 'Espada equipada!',
	                text: 'Sua Espada Flanejante foi equipada com sucesso.',
	                timer: 7000,
	                showConfirmButton: false,
	                background: 'transparent',
	                color: '#ffb400'
	            });

	            await atualizarUsuario();
	            // ✅ sucesso → machado permanece travado

	        } catch (e) {
	            console.error(e);

	            Swal.fire({
	                icon: 'error',
	                title: 'Erro',
	                text: 'Erro ao tentar equipar espada.',
	                background: 'transparent',
	                color: '#ff3b3b'
	            });

	            // 🔄 destrava machado no catch
	            btnAtivarMachado.style.pointerEvents = 'auto';
	            btnAtivarMachado.style.opacity = '1';

	        } finally {
	            setTimeout(() => {
	                emCooldown = false;
	                btnAtivarEspada.disabled = false;
	                btnAtivarEspada.innerText = textoOriginal;
	            }, tempoCooldown * 1000);
	        }
	    });
	}

	/**
	 * 	 if (btnAtivarEspada) {

	        btnAtivarEspada.addEventListener('click', async () => {
	 	
	            if (emCooldown) return;
	 		

	            emCooldown = true;
	            btnAtivarEspada.disabled = true;

	          //  let restante = tempoCooldown;
	 	  
	            const textoOriginal = btnAtivarEspada.innerText;

	            btnAtivarEspada.innerText = `Ativando...`;
	 	

	            try {
	 			const res1 = await fetch(`/api/atualizar/status/ajustes/${usuarioId}`);
	 			if (!res1.ok) return;
	 			const status = await res1.json();
	 			const ativoGuerreiro = status.ativoGuerreiro;
	 			const machadoAtivo = status.ativarMachadoDilacerador ?? 0;
	 				
	 			
	 		//	if ( ativoGuerreiro <= 0  )return;
	 		clearInterval(window.loopMachado);

	                const res = await fetch(
	                    `/api/espada-flanejante/ativar?usuarioId=${usuarioId}&quantidade=1`,
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

	 			if (machadoAtivo === 1) {
	 			    Swal.fire({
	 			        icon: 'info',
	 			        title: 'Machado já equipado',
	 			        text: 'Você já está usando o Machado Dilacerador.',
	 			        background: 'transparent',
	 			        color: '#ffb400'
	 			    });
	 			    return;
	 			}


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
	                    timer: 7000,
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
	 * 
	 */
   

    // ==============================
    // LOOP
    // ==============================
    atualizarUsuario();
	window.loopMachado = setInterval(atualizarUsuario, 5000);

    //setInterval(atualizarUsuario, 5000);
});

