document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? Number(meta.content) : null;
    if (!usuarioId) return;

    // ==============================
    // ELEMENTOS
    // ==============================
    const nucleoArco = document.getElementById('nucleoArco');
    const arcoSpan = document.getElementById('arcoAtivo');
    const durabilidadeSpan = document.getElementById('durabilidadeArco');
    const aljavaSpan = document.getElementById('aljavaCount');
    const tipoFlechaSpan = document.getElementById('tipoFlechaAtiva');

    const btnEquiparArco = document.getElementById('btnEquiparArco');

    const arcoInfos = document.querySelectorAll('.arco-ativo-info');

    // ==============================
    // FORMATAÇÃO
    // ==============================
    const formatar = n => new Intl.NumberFormat('pt-BR').format(n ?? 0);

    // ==============================
    // ATUALIZAR USUÁRIO
    // ==============================
    async function atualizarUsuario() {
        try {
              const res = await fetch(`/api/atualizar/status/usuario/${usuarioId}`);
            if (!res.ok) return;

            const status = await res.json();
            atualizarArco(status);

        } catch (e) {
            console.error('Erro ao atualizar arco:', e);
        }
    }

    // ==============================
    // ATUALIZA ARCO
    // ==============================
    function atualizarArco(status) {

        const arcoEstoque = status.arcoInventario ?? 0;
        const arcoAtivo = status.arcoAtivo ?? 0;
        const durabilidade = status.durabilidadeArco ?? 0;

        const flechasNaAljava = status.aljava ?? 0;
        const tipoFlecha = status.tipoFlechaAtiva ?? '-';

        const guerreiroAtivo = status.ativoGuerreiro ?? 0;
        const espadaAtiva = status.ativaEspadaFlanejante ?? 0;
        const machadoAtivo = status.ativarMachadoDilacerador ?? 0;

        // ==============================
        // 🔒 NÚCLEO INTEIRO (só aparece se tiver flecha)
        // ==============================
       

        // ==============================
        // UI BÁSICA
        // ==============================
        arcoSpan.textContent = formatar(arcoEstoque);
        durabilidadeSpan.textContent = formatar(durabilidade);
        aljavaSpan.textContent = formatar(aljava);
        tipoFlechaSpan.textContent = tipoFlecha;

        // ==============================
        // INFO ATIVO
        // ==============================
       
		 arcoInfos.forEach(div => {
            div.classList.toggle('hidden', arcoAtivo === 0);
        });

		// ==============================
		// BOTÃO EQUIPAR ARCO — LÓGICA FINAL
		// ==============================

		// existe arco equipado se durabilidade > 0
		const existeArco = durabilidade > 0;

		const podeEquipar =
					    arcoEstoque > 0 &&      // ✅ existe pelo menos 1 arco no inventário
					    !existeArco &&          // ✅ NÃO há arco equipado (durabilidade = 0)
					    arcoAtivo === 0 &&      // ✅ nenhum arco está ativo no momento
					    flechasNaAljava > 0 &&  // ✅ existe pelo menos 1 flecha na aljava
					    guerreiroAtivo > 0 &&   // ✅ guerreiro está ativo
					    espadaAtiva === 0 &&    // ✅ nenhuma espada equipada
					    machadoAtivo === 0;     // ✅ nenhum machado equipado
						
						
		// estado base (sempre igual)
		btnEquiparArco.classList.add('hidden');
		btnEquiparArco.disabled = true;

		// mostrar apenas se TODAS as condições forem verdadeiras
		if (podeEquipar) {
		    btnEquiparArco.classList.remove('hidden');
		    btnEquiparArco.disabled = false;
		}


    }

    // ==============================
    // ATIVAR ARCO
    // ==============================
    let emCooldown = false;

	btnEquiparArco?.addEventListener('click', async () => {

	    if (emCooldown || btnEquiparArco.disabled) return;

	    emCooldown = true;
	    btnEquiparArco.disabled = true;

	    const textoOriginal = btnEquiparArco.innerText;
	    btnEquiparArco.innerText = 'Equipando...';

	    try {
	        const res = await fetch(
	            `/arco/equipar?usuarioId=${usuarioId}`,
	            { method: 'POST' }
	        );

	        const resposta = await res.json().catch(() => null);

	        // 🔒 VALIDAÇÃO REAL (NÃO CONFIA SÓ NO HTTP)
	        if (!res.ok || !resposta || resposta.success !== true) {
	            throw new Error(resposta?.message || 'Já existe um arco equipado');
	        }

	        Swal.fire({
	            icon: 'success',
	            title: 'Arco equipado!',
	            text: 'O arco foi equipado com sucesso.',
	            background: 'transparent',
	            color: '#ffb400',
	            timer: 4000,
	            showConfirmButton: false
	        });

	        await atualizarUsuario();

	    } catch (e) {
	        Swal.fire({
	            icon: 'error',
	            title: 'Erro',
	            text: e.message,
	            background: 'transparent',
	            color: '#ff3b3b'
	        });
	    } finally {
	        btnEquiparArco.innerText = textoOriginal;
	        emCooldown = false;
	    }
	});


    // ==============================
    // LOOP
    // ==============================
    atualizarUsuario();
   
});
