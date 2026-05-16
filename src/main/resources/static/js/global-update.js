// global-update.js

document.addEventListener('DOMContentLoaded', () => {

    // ==============================
    // ATUALIZAÇÃO GLOBAL
    // ==============================
    window.atualizarTudo = async function(usuarioId) {

        try {

            const res =
                await fetch(`/api/atualizar/status/ajustes/${usuarioId}`);

            if (!res.ok) return;

            const status = await res.json();

            // ==============================
            // GUERREIRO
            // ==============================
            if (typeof window.atualizarNucleoGuerreiro === 'function') {
                window.atualizarNucleoGuerreiro(status);
            }

            // ==============================
            // RETAGUARDA
            // ==============================
            if (typeof window.atualizarNucleoRetaguarda === 'function') {
                window.atualizarNucleoRetaguarda(status);
            }

            // ==============================
            // STATUS BOTÕES
            // ==============================
            if (typeof window.atualizarStatusBotoes === 'function') {
                window.atualizarStatusBotoes(status);
            }

            // ==============================
            // MACHADO
            // ==============================
            if (typeof window.atualizarMachadoDilacerador === 'function') {
                window.atualizarMachadoDilacerador(status);
            }

            // ==============================
            // ESPADA
            // ==============================
            if (typeof window.atualizarEspadaFlanejante === 'function') {
                window.atualizarEspadaFlanejante(status);
            }

            // ==============================
            // ARCO
            // ==============================
            if (typeof window.atualizarArcoCelestial === 'function') {
                window.atualizarArcoCelestial(status);
            }

            // ==============================
            // ESCUDO
            // ==============================
            if (typeof window.atualizarEscudoPrimordial === 'function') {
                window.atualizarEscudoPrimordial(status);
            }

            // ==============================
            // HUD / SALDO / USUÁRIO
            // ==============================
            if (typeof window.atualizarHudUsuario === 'function') {
                window.atualizarHudUsuario(status);
            }

        } catch (e) {

            console.error(
                'Erro ao atualizar tudo:',
                e
            );
        }
    };

});