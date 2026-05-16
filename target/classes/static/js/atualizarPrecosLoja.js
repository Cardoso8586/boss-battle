

document.addEventListener('DOMContentLoaded', () => {

    const metaUsuario = document.querySelector('meta[name="user-id"]');

    if (!metaUsuario) {
        console.error('Meta user-id não encontrada');
        return;
    }

    const usuarioId = parseInt(metaUsuario.getAttribute('content'));

    if (!usuarioId) {
        console.error('Usuário não encontrado');
        return;
    }

    function atualizarPreco(id, valor) {
        const elemento = document.getElementById(id);

        if (!elemento || valor === null || valor === undefined) {
            return;
        }

        elemento.textContent =
            `${Number(valor).toLocaleString('pt-BR')} Boss Coins`;

        elemento.dataset.preco = valor;
    }

    function atualizarPrecoClasse(classe, valor) {
        const elemento = document.querySelector(classe);

        if (!elemento || valor === null || valor === undefined) {
            return;
        }

        elemento.textContent =
            `${Number(valor).toLocaleString('pt-BR')} Boss Coins`;

        elemento.dataset.preco = valor;
    }

    async function atualizarPrecosLoja() {

        try {
            const response = await fetch(`/api/loja/${usuarioId}`);

            if (!response.ok) {
                console.error('Erro ao buscar preços da loja');
                return;
            }

            const dadosLoja = await response.json();

            atualizarPreco('preco-guerreiros', dadosLoja.precoGuerreiros);
            atualizarPreco('preco-energia', dadosLoja.precoEnergia);
            atualizarPreco('preco-ataque-especial', dadosLoja.precoAtaqueEspecial);
            atualizarPreco('preco-auto-vigor', dadosLoja.PrecoPocaoAutomaticaVigor);
            atualizarPreco('preco-espada-flanejante', dadosLoja.PrecoEspadaFlanejante);
            atualizarPreco('preco-machado-dilacerador', dadosLoja.PrecoMachadoDilacerador);
            atualizarPreco('preco-escudo-primordial', dadosLoja.PrecoEscudoPrimordial);
            atualizarPreco('preco-arco-celestial', dadosLoja.precoArcoCelestial);

            atualizarPrecoClasse('.preco-lootbox-basica', dadosLoja.precoBasica);
            atualizarPrecoClasse('.preco-lootbox-avancada', dadosLoja.precoAvancada);
            atualizarPrecoClasse('.preco-lootbox-especial', dadosLoja.precoEspecial);
            atualizarPrecoClasse('.preco-lootbox-lendaria', dadosLoja.precoLendaria);

        } catch (error) {
            console.error('Erro ao carregar preços da loja:', error);
        }
    }

    atualizarPrecosLoja();

    setInterval(atualizarPrecosLoja, 10000);
});
/*

document.addEventListener('DOMContentLoaded', () => {

    const usuarioId = parseInt(
        document.querySelector('meta[name="user-id"]').getAttribute('content')
    );

    if (!usuarioId) {
        console.error('Usuário não encontrado');
        return;
    }

 
    async function atualizarPrecosLoja() {

        try {

            const response = await fetch(`/api/loja/${usuarioId}`);

            if (!response.ok) {
                console.error('Erro ao buscar preços da loja');
                return;
            }

            const dadosLoja = await response.json();

            // LOOTBOXES

            const lootboxBasica =
                document.querySelector('.preco-lootbox-basica');

            if (lootboxBasica) {

                lootboxBasica.textContent =
                    `${dadosLoja.precoBasica.toLocaleString('pt-BR')} Boss Coins`;
            }

            const lootboxAvancada =
                document.querySelector('.preco-lootbox-avancada');

            if (lootboxAvancada) {

                lootboxAvancada.textContent =
                    `${dadosLoja.precoAvancada.toLocaleString('pt-BR')} Boss Coins`;
            }

            const lootboxEspecial =
                document.querySelector('.preco-lootbox-especial');

            if (lootboxEspecial) {

                lootboxEspecial.textContent =
                    `${dadosLoja.precoEspecial.toLocaleString('pt-BR')} Boss Coins`;
            }

            const lootboxLendaria =
                document.querySelector('.preco-lootbox-lendaria');

            if (lootboxLendaria) {

                lootboxLendaria.textContent =
                    `${dadosLoja.precoLendaria.toLocaleString('pt-BR')} Boss Coins`;
            }

        } catch (error) {

            console.error(
                'Erro ao carregar preços da loja:',
                error
            );
        }
    }



    atualizarPrecosLoja();

    setInterval(atualizarPrecosLoja, 10000);

});
*/