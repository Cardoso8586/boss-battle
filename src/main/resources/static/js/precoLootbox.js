document.addEventListener('DOMContentLoaded', () => {
    const cards = document.querySelectorAll('.lootbox-card');

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;
    if (!usuarioId) return;

    cards.forEach(async card => {
        const tipo = card.dataset.tipo;
        const precoElemento = card.querySelector('.preco-lootbox');

        try {
            // ⚡ Busca preço do backend
            const response = await fetch(`/lootbox/preco/${tipo}`, { credentials: 'include' });
            if (!response.ok) throw new Error("Erro ao carregar preço");

            const preco = await response.text();
            precoElemento.textContent = "Preço: " + preco + " moedas";

        } catch (err) {
            precoElemento.textContent = "Não foi possível carregar o preço";
            console.error(err);
        }
    });


});
