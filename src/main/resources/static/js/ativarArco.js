document.addEventListener('DOMContentLoaded', () => {

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? Number(meta.content) : null;
    if (!usuarioId) return;

    // ==============================
    // ELEMENTOS
    // =============================
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
				customClass: {title: 'swal-game-text'
				},
	            icon: 'success',
	            title: 'Arco equipado!',
	            text: 'O Arco Celestial foi equipado com sucesso.',
	            background: 'transparent',
	            color: '#ffb400',
	            timer: 4000,
	            showConfirmButton: false
	        });

	      

	    } catch (e) {
	        Swal.fire({
				customClass: { title: 'swal-game-error' },
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
