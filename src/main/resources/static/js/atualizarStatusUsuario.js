document.addEventListener('DOMContentLoaded', () => {
    const btnRecarregar = document.getElementById('btnRecarregar');
    const energiaAtual = document.getElementById('energiaAtual');
    const energiaBar = document.getElementById('energiaBar');
    const energiaMaxima = document.getElementById('energiaMaxima');
	const ganhosRef = document.getElementById('ganhosPendentesSpan');
	const ataqueBase = document.getElementById('damage');
	const bossCoin = document.getElementById('boss_coins');
    const guerreiros = document.getElementById('guerreiros');
	const ataquePorMinuto  = document.getElementById('ataquePorMinuto');
	const xp = document.getElementById('xp');
	const guerreirosRetaguarda  = document.getElementById('guerreirosRetaguarda');
	
    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;

    function formatarNumero(numero) {
        return new Intl.NumberFormat('pt-BR').format(numero);
    }

    async function atualizarUsuario() {
        try {
            const res = await fetch(`/api/atualizar/status/usuario/${usuarioId}`);
            const data = await res.json();

            const energia = Math.max(0, data.energiaGuerreiros);
            const energiaMax = data.energiaGuerreirosPadrao;
			
			
			
			ganhosRef.textContent = formatarNumero(data.ganhosRef);
			ataqueBase.textContent = formatarNumero(data.ataqueBase);
			bossCoin.textContent = formatarNumero(data.bossCoin);
			guerreiros.textContent = formatarNumero(data.guerreiros);
			ataquePorMinuto.textContent = formatarNumero(data.ataquePorMinuto);
			xp.textContent = formatarNumero(data.xp);
			guerreirosRetaguarda.textContent  = formatarNumero(data.guerreirosRetaguarda);
			
			
            // Atualiza valores
            energiaAtual.textContent = formatarNumero(energia);
            energiaMaxima.textContent = formatarNumero(energiaMax);

            const percentualEnergia = Math.max(0, (energia / energiaMax) * 100);
            energiaBar.style.width = percentualEnergia + '%';

            // Liberar botão quando energia < 50%
            if (energia / energiaMax < 0.3) {
                btnRecarregar.disabled = false;
            } else {
                btnRecarregar.disabled = true;
            }

        } catch (err) {
            console.error(err);
        }
    /*** 
    // Evento de clique do botão continua igual
    btnRecarregar.addEventListener('click', async () => {
        try {
            await fetch(`/recarregar-energia?usuarioId=${usuarioId}`, { method: 'POST' });
            atualizarUsuario();
        } catch (err) {
            console.error(err);
        }
    });*/
}



    // Atualiza ao carregar e a cada 10s
    atualizarUsuario();
    setInterval(atualizarUsuario, 10000);
});


