document.addEventListener('DOMContentLoaded', () => {

    const cards = document.querySelectorAll('.promo-card');
    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.content) : null;
    if (!usuarioId) return;

    const formatter = new Intl.NumberFormat('pt-BR');

    cards.forEach(async card => {

        const tipo = card.dataset.tipo;
        const precoElemento = card.querySelector(`.preco-promo-${tipo}`);

        try {
            const response = await fetch(
                `/promo/preco/${usuarioId}/${tipo.toUpperCase()}`,
                { credentials: 'include' }
            );

            if (!response.ok) throw new Error();

            const data = await response.json();

            precoElemento.innerHTML = `
                <span class="preco-bruto">
                    ${formatter.format(data.precoBruto)} BossCoins
                </span>
                <br>
                <span class="preco-final">
                    ${formatter.format(data.precoFinal)} BossCoins
                </span>
                <span class="desconto">
                    (-${data.descontoPercentual}%)
                </span>
            `;

        } catch {
            precoElemento.textContent = "Erro ao carregar preço";
        }
    });
});

