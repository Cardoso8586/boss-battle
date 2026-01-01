async function carregarRankingDano() {
    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? Number(meta.content) : null;

    if (!usuarioId || isNaN(usuarioId)) {
        console.error('Usuário não encontrado');
        return;
    }

    try {
		const response = await fetch(`/api/boss/ranking/dano/${usuarioId}`);

        if (!response.ok) {
            throw new Error('Erro ao carregar ranking');
        }

        const data = await response.json();

        const ranking = data.top10;
        const minhaPosicao = data.minhaPosicao;

        const lista = document.getElementById('rankingDano');
        const minhaPosicaoEl = document.getElementById('minhaPosicaoRanking');

        // limpa lista
        lista.innerHTML = '';

        ranking.forEach((player, index) => {
            const li = document.createElement('li');

            // destaca o próprio usuário
            if (player.userId === usuarioId) {
                li.classList.add('meu-ranking');
            }

            li.innerHTML = `
                <span class="posicao">#${index + 1}</span>
                <span class="nome">${player.userName}</span>
                <span class="dano">${formatarNumero(player.damage)} Dano</span>
            `;

            lista.appendChild(li);
        });

        // mostra posição do usuário (caso não esteja no top 10)
        if (minhaPosicaoEl) {
            minhaPosicaoEl.textContent =
                minhaPosicao && minhaPosicao !== 'Sem ranking'
                    ? `Sua posição: ${minhaPosicao}`
                    : 'Você ainda não entrou no ranking';
        }

    } catch (error) {
        console.error(error);
    }
}

function formatarNumero(valor) {
    return Number(valor).toLocaleString('pt-BR');
}

// carrega automaticamente
document.addEventListener('DOMContentLoaded', carregarRankingDano);
setInterval(carregarRankingDano, 13000);

