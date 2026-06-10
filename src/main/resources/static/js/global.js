// ======================================
// AUTO RELOAD INTELIGENTE
// ======================================

(function () {

    // ======================================
    // CONFIG
    // ======================================

    // só recarrega após 10s sem resposta
    const TEMPO_MAX_SEM_RESPOSTA =
        300000;

    // verifica a cada 30s
    const INTERVALO_VERIFICACAO =
        300000;

    // ======================================
    // STATE
    // ======================================

    let ultimaResposta =
        Date.now();

    let reloadando =
        false;

    let paginaCarregada =
        false;

    // ======================================
    // VIDA
    // ======================================

    function registrarResposta() {

        ultimaResposta =
            Date.now();
    }

    // ======================================
    // FETCH
    // ======================================

    const fetchOriginal =
        window.fetch;

    window.fetch =
        async function (...args) {

            try {

                const response =
                    await fetchOriginal(
                        ...args
                    );

                registrarResposta();

                return response;

            } catch (e) {

                throw e;
            }
        };

    // ======================================
    // XHR
    // ======================================

    const openOriginal =
        XMLHttpRequest.prototype.open;

    XMLHttpRequest.prototype.open =
        function (...args) {

            this.addEventListener(

                "load",

                registrarResposta
            );

            return openOriginal.apply(
                this,
                args
            );
        };

    // ======================================
    // DOM
    // ======================================

    document.addEventListener(

        "DOMContentLoaded",

        () => {

            registrarResposta();
        }
    );

    // ======================================
    // LOAD
    // ======================================

    window.addEventListener(

        "load",

        () => {

            paginaCarregada =
                true;

            registrarResposta();
        }
    );

    // ======================================
    // VERIFICA BOSS
    // ======================================

    function bossCarregado() {

        const bossNome =
            document.getElementById(
                "boss-name"
            );

        const bossImagem =
            document.getElementById(
                "boss-image"
            );

        // nome carregou
        const nomeOk =

            bossNome &&

            bossNome.innerText.trim() !== "";

        // imagem carregou
        const imagemOk =

            bossImagem &&

            bossImagem.complete &&

            bossImagem.src;

        return nomeOk || imagemOk;
    }

    // ======================================
    // WATCHDOG
    // ======================================

    setInterval(() => {

        // evita loop
        if (reloadando)
            return;

        // aba minimizada
        if (document.hidden)
            return;

        // página já carregou
        if (paginaCarregada)
            return;

        // boss carregado
        if (bossCarregado()) {

            paginaCarregada =
                true;

            registrarResposta();

            return;
        }

        const agora =
            Date.now();

        const tempoSemResposta =

            agora -
            ultimaResposta;

        // ======================================
        // SEM RESPOSTA
        // ======================================

        if (

            tempoSemResposta >=
            TEMPO_MAX_SEM_RESPOSTA

        ) {

            reloadando =
                true;

            console.warn(
                "Página travada. Recarregando..."
            );

            window.location.reload();
        }

    }, INTERVALO_VERIFICACAO);

})();