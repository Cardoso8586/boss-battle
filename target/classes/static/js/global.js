// ======================================
// AUTO RELOAD ANTI TRAVAMENTO
// ======================================

(function () {

    let carregamentoFinalizado = false;

    window.addEventListener('load', () => {

        carregamentoFinalizado = true;

        sessionStorage.removeItem(
            'AUTO_RELOAD_EXECUTADO'
        );
    });

    setTimeout(() => {

        if (carregamentoFinalizado) {
            return;
        }

        const paginaCompleta =
            document.readyState === 'complete';

        if (paginaCompleta) {
            return;
        }

        const jaRecarregou =
            sessionStorage.getItem(
                'AUTO_RELOAD_EXECUTADO'
            );

        // evita loop infinito
        if (jaRecarregou === 'true') {
            return;
        }

        console.warn(
            'Página travou. Recarregando automaticamente...'
        );

        sessionStorage.setItem(
            'AUTO_RELOAD_EXECUTADO',
            'true'
        );

        location.reload();

    }, 10000);

})();