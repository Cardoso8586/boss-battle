// ======================================
// AUTO RELOAD EXTREMO
// ======================================

(function () {

    // ======================================
    // CONFIG
    // ======================================

    const TEMPO_MAX_SEM_RESPOSTA = 8000;

    const INTERVALO_VERIFICACAO = 1000;

    // ======================================
    // STATE
    // ======================================

    let ultimaResposta =
        Date.now();

    let reloadando = false;

    // ======================================
    // MARCA VIDA
    // ======================================

    function registrarResposta() {

        ultimaResposta = Date.now();
    }

    // ======================================
    // FETCH HOOK
    // ======================================

    const fetchOriginal =
        window.fetch;

    window.fetch =
        async function (...args) {

            try {

                const response =
                    await fetchOriginal(...args);

                registrarResposta();

                return response;

            } catch (e) {

                throw e;
            }
        };

    // ======================================
    // XHR HOOK
    // ======================================

    const openOriginal =
        XMLHttpRequest.prototype.open;

    XMLHttpRequest.prototype.open =
        function (...args) {

            this.addEventListener(
                'load',
                registrarResposta
            );

            return openOriginal.apply(
                this,
                args
            );
        };

    // ======================================
    // DOM PRONTO
    // ======================================

    document.addEventListener(
        'DOMContentLoaded',
        registrarResposta
    );

    // ======================================
    // WINDOW LOAD
    // ======================================

    window.addEventListener(
        'load',
        registrarResposta
    );

    // ======================================
    // BOSS RENDER
    // ======================================

    setInterval(() => {

        const bossNome =
            document.getElementById(
                'boss-name'
            );

        const bossImagem =
            document.getElementById(
                'boss-image'
            );

        if (

            bossNome &&
            bossNome.innerText.trim() !== ''

        ) {

            registrarResposta();
        }

        if (

            bossImagem &&
            bossImagem.complete

        ) {

            registrarResposta();
        }

    }, 1500);

    // ======================================
    // WATCHDOG
    // ======================================

    setInterval(() => {

        if (reloadando) {
            return;
        }

        const agora =
            Date.now();

        const tempoSemResposta =
            agora - ultimaResposta;

        // ======================================
        // SEM VIDA
        // ======================================

        if (

            tempoSemResposta >=
            TEMPO_MAX_SEM_RESPOSTA

        ) {

            reloadando = true;

            console.warn(
                'Página travada. Recarregando...'
            );

            // hard reload
            window.location.reload();
        }

    }, INTERVALO_VERIFICACAO);

})();