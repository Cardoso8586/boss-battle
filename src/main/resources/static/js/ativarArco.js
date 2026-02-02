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

        const aljava = status.aljava ?? 0;
        const tipoFlecha = status.tipoFlechaAtiva ?? '-';

        const guerreiroAtivo = status.ativoGuerreiro ?? 0;
        const espadaAtiva = status.ativaEspadaFlanejante ?? 0;
        const machadoAtivo = status.ativarMachadoDilacerador ?? 0;

        // ==============================
        // 🔒 NÚCLEO INTEIRO (só aparece se tiver flecha)
        // ==============================
        if (aljava > 0) {
            nucleoArco.classList.remove('hidden');
        } else {
            nucleoArco.classList.add('hidden');
            return; // não faz mais nada
        }

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
        // BOTÃO EQUIPAR ARCO
        // ==============================
        const podeEquipar =
            arcoEstoque > 0 &&
            arcoAtivo === 0 &&
            aljava > 0 &&
            guerreiroAtivo > 0 &&
            espadaAtiva === 0 &&
            machadoAtivo === 0;

        btnEquiparArco.classList.toggle('hidden', !podeEquipar);
        btnEquiparArco.disabled = !podeEquipar;
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
	            throw new Error(resposta?.message || 'Não foi possível equipar o arco');
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
