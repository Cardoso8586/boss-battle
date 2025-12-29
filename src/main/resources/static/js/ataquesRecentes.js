
document.addEventListener('DOMContentLoaded', () => {

    const lista = document.getElementById('listaAtaques');

    async function carregarAtaques() {
        try {
            const res = await fetch('/api/boss/ataques-recentes');
            if (!res.ok) return;

            const ataques = await res.json();
            lista.innerHTML = '';

            ataques.forEach(a => {
                const li = document.createElement('li');
				li.innerHTML = `<strong>${a.username}</strong> causou <b>${a.damage}</b> de dano`;

                lista.appendChild(li);
            });

        } catch (e) {
            console.error('Erro ao carregar ataques', e);
        }
    }
	

    carregarAtaques();
    setInterval(carregarAtaques, 3000);
});
