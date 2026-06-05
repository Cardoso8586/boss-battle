//console.log("ARQUIVO atualizarCacheGuerreiros.js CARREGADO");

window.atualizarCacheGuerreiros = async function () {

    //console.log("ENTROU atualizarCacheGuerreiros");

    const usuarioId =
        document.querySelector('meta[name="user-id"]')?.content;

    if (!usuarioId) {
        console.error("user-id não encontrado");
        return;
    }

    try {
        const response = await fetch(`/guerreiros/usuario/${usuarioId}`, {
            cache: "no-store"
        });

        if (!response.ok) {
            throw new Error("Erro ao buscar guerreiros");
        }

        const dados = await response.json();

        const chave = `cache_guerreiros_usuario_${usuarioId}`;

        localStorage.setItem(
            chave,
            JSON.stringify({
                atualizadoEm: Date.now(),
                dados: dados
            })
        );

       // console.log("Cache guerreiros atualizado:", dados);

    } catch (e) {
        console.error("Erro ao atualizar cache guerreiros:", e);
    }
};