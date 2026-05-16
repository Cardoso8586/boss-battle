document.addEventListener("DOMContentLoaded", () => {

    const claimBtn =
        document.getElementById("claimBtn");

    const claimForm =
        document.getElementById("claimForm");

    const ganhosSpan =
        document.getElementById("ganhosPendentesSpan");

    // =========================================
    // CARREGA CSS DO SWAL AUTOMATICAMENTE
    // =========================================

    if (!document.getElementById("swal-popup-css")) {

        const link = document.createElement("link");

        link.id = "swal-popup-css";

        link.rel = "stylesheet";

        link.href = "/css/swal2-popup.css";

        document.head.appendChild(link);
    }

    if (!claimBtn || !claimForm || !ganhosSpan) {
        return;
    }

    // começa escondido
    claimBtn.style.display = "none";

    const meta =
        document.querySelector(
            'meta[name="user-id"]'
        );

    const usuarioId =
        meta
            ? parseInt(meta.getAttribute("content"))
            : null;

    if (!usuarioId) {
        return;
    }

    // =========================================
    // FORMATAR
    // =========================================

    function formatarNumero(numero) {

        return new Intl.NumberFormat(
            'pt-BR'
        ).format(numero);
    }

    // =========================================
    // CARREGAR GANHOS
    // =========================================

    async function carregarGanhosReferral() {

        try {

            const res = await fetch(
                `/api/atualizar/status/usuario/${usuarioId}`
            );

            if (!res.ok) {
                throw new Error(
                    "Erro ao carregar referral"
                );
            }

            const data = await res.json();

            const ganhosRef =
                Number(data.ganhosRef || 0);

            console.log(
                "GANHOS REF =",
                ganhosRef
            );

            ganhosSpan.innerText =
                formatarNumero(ganhosRef);

            // exibição do botão
            if (ganhosRef > 0) {

                claimBtn.style.display =
                    "inline-block";

            } else {

                claimBtn.style.display =
                    "none";
            }

            return ganhosRef;

        } catch (err) {

            console.error(
                "Erro ao buscar ganhos referral",
                err
            );

            claimBtn.style.display = "none";

            return 0;
        }
    }

    // =========================================
    // CLAIM
    // =========================================

    claimForm.addEventListener(
        "submit",
        async (e) => {

            e.preventDefault();

            const ganhosPendentes =
                parseFloat(
                    ganhosSpan.innerText
                        .replace(/\./g, '')
                        .replace(',', '.')
                );

            if (
                isNaN(ganhosPendentes)
                || ganhosPendentes <= 0
            ) {
                return;
            }

            const confirmacao =
                await Swal.fire({

                    customClass: {
                        popup: 'swal-game-popup',
                        title: 'swal-game-text',
                        confirmButton:
                            'swal-game-confirm',
                        cancelButton:
                            'swal-game-cancel'
                    },

                    title:
                        'Confirmar Reivindicação?',

                    html: `
                        <p>
                            Você irá receber
                            <b>
                                ${formatarNumero(
                                    ganhosPendentes.toFixed(1)
                                )}
                            </b>
                            BossCoin.
                        </p>

                        <div class="modal-anuncio">
                            <iframe
                                src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                                width="468"
                                height="60"
                                scrolling="no"
                                frameborder="0">
                            </iframe>
                        </div>
                    `,

                    showCancelButton: true,

                    confirmButtonText:
                        'Sim, reivindicar!',

                    cancelButtonText:
                        'Cancelar',

                    buttonsStyling: false,

                    background: 'transparent',

                    color: '#ffb400'
                });

            if (!confirmacao.isConfirmed) {
                return;
            }

            try {

                claimBtn.disabled = true;

                claimBtn.innerText =
                    "Processando...";

                const response =
                    await fetch(
                        '/claim-referidos',
                        {
                            method: 'POST',

                            headers: {
                                'Content-Type':
                                    'application/json'
                            },

                            body: JSON.stringify({
                                usuarioId:
                                    usuarioId,

                                valor:
                                    ganhosPendentes
                            })
                        }
                    );

                // =========================================
                // VERIFICA ERRO HTTP
                // =========================================

                if (!response.ok) {

                    let erroTexto =
                        "Erro ao reivindicar";

                    try {

                        const erroJson =
                            await response.json();

                        erroTexto =
                            erroJson.message
                            || erroTexto;

                    } catch (_) {}

                    throw new Error(erroTexto);
                }

                // =========================================
                // RESPONSE JSON OPCIONAL
                // =========================================

                let data = {};

                try {

                    data =
                        await response.json();

                } catch (e) {

                    // backend vazio mas sucesso
                    data = {
                        success: true,
                        message:
                            'Ganhos reivindicados com sucesso!'
                    };
                }

                // =========================================
                // SUCESSO
                // =========================================

                await Swal.fire({

                    customClass: {
                        popup:
                            'swal-game-popup',

                        title:
                            'swal-game-text',

                        confirmButton:
                            'swal-game-confirm'
                    },

                    icon: 'success',

                    title: 'Sucesso!',

                    html: `
                        <p>
                            ${data.message
                                || 'Seus ganhos foram reivindicados!'}
                        </p>

                        <div class="modal-anuncio">
                            <iframe
                                src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                                width="468"
                                height="60"
                                scrolling="no"
                                frameborder="0">
                            </iframe>
                        </div>
                    `,

                    buttonsStyling: false,

                    background: 'transparent',

                    color: '#ffb400'
                });

                // atualiza visual
                ganhosSpan.innerText = "0";

                claimBtn.style.display =
                    "none";

            } catch (err) {

                console.error(err);

                Swal.fire({

                    customClass: {
                        popup:
                            'swal-game-popup',

                        title:
                            'swal-game-error',

                        confirmButton:
                            'swal-game-confirm'
                    },

                    title:
                        '❌ Falha na Reivindicação',

                    html: `
                        <p>
                            ${err.message
                                || 'Não foi possível reivindicar os ganhos.'}
                        </p>

                        <div class="modal-anuncio">
                            <iframe
                                src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                                width="468"
                                height="60"
                                scrolling="no"
                                frameborder="0">
                            </iframe>
                        </div>
                    `,

                    background: 'transparent',

                    color: '#ff3b3b',

                    buttonsStyling: false
                });

            } finally {

                claimBtn.disabled = false;

                claimBtn.innerText =
                    "Reivindicar Ganhos";

                carregarGanhosReferral();
            }
        }
    );

    // =========================================
    // INICIAR
    // =========================================

    carregarGanhosReferral();
});


/*
document.addEventListener("DOMContentLoaded", () => {

    const claimBtn = document.getElementById("claimBtn");
    const claimForm = document.getElementById("claimForm");
    const ganhosSpan = document.getElementById("ganhosPendentesSpan");

    // começa sempre escondido (blindagem)
    claimBtn.style.display = "none";

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;

    if (!usuarioId) return;

    // --------------------------------------------------
    function formatarNumero(numero) {
        return new Intl.NumberFormat('pt-BR').format(numero);
    }

    // --------------------------------------------------
    async function carregarGanhosReferral() {
        try {
            const res = await fetch(`/api/atualizar/status/usuario/${usuarioId}`);
            const data = await res.json();

            const ganhosRef = Number(data.ganhosRef || 0);

            console.log("GANHOS REF =", ganhosRef);

            // atualiza texto
            ganhosSpan.innerText = formatarNumero(ganhosRef);

            // 🔥 decisão ÚNICA de exibição
            if (ganhosRef > 0) {
                claimBtn.style.display = "inline-block";
            } else {
                claimBtn.style.display = "none";
            }

            return ganhosRef;

        } catch (err) {
            console.error("Erro ao buscar ganhos referral", err);
            claimBtn.style.display = "none";
            return 0;
        }
    }

    // --------------------------------------------------
    claimForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const ganhosPendentes = parseFloat(
            ganhosSpan.innerText.replace(/\./g, '').replace(',', '.')
        );

        if (ganhosPendentes <= 0) return;

        Swal.fire({
			customClass: {      
			title: 'swal-game-text'
			},
			
            title: 'Confirmar Reivindicação?',
            showCancelButton: true,
            confirmButtonText: 'Sim, reivindicar!',
            cancelButtonText: 'Cancelar',
			html: `
			  Você irá receber ${formatarNumero(ganhosPendentes.toFixed(1))} Boss.
			  <div class="modal-anuncio">
			    <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
			      width="468"
			      height="60"
			      scrolling="no"
			      frameborder="0">
			    </iframe>
			  </div>
			`,
			background: 'transparent',
			color: '#ffb400'
        }).then(async (result) => {

            if (!result.isConfirmed) return;

            try {
                const response = await fetch('/claim-referidos', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        usuarioId: usuarioId,
                        valor: ganhosPendentes
                    })
                });

                if (!response.ok) {
                    throw new Error("Erro ao reivindicar");
                }

				await Swal.fire({
					customClass: {      
					title: 'swal-game-text'
					},
				    title: 'Sucesso!',
				    text: 'Seus ganhos foram reivindicados!',
				    icon: 'success',
					html: `
													      <div class="modal-anuncio">
													        <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
													          width="468"
													          height="60"
													          scrolling="no"
													          frameborder="0">
													        </iframe>
													      </div>
													    `,
					background: 'transparent',
				    color: '#ffb400',
				    confirmButtonColor: '#00ff88'
				});


                // 🔥 atualiza estado após claim
                ganhosSpan.innerText = "0";
                claimBtn.style.display = "none";

            } catch (err) {
				Swal.fire({
					customClass: {
					title: 'swal-game-error'
					},
				    title: '❌ Falha na Reivindicação',
				    html: '<b>Não foi possível reivindicar os ganhos.</b>',
				    imageUrl: '/icones/erro.webp',
				    imageWidth: 90,
				    imageHeight: 90,
				    imageAlt: 'Erro',
					html: `
									  		      <div class="modal-anuncio">
									  		        <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
									  		          width="468"
									  		          height="60"
									  		          scrolling="no"
									  		          frameborder="0">
									  		        </iframe>
									  		      </div>
									  		    `,
					background: 'transparent',
				    color: '#ff3b3b',
				    confirmButtonColor: '#ff3c00'
				});

            }
        });
    });

    // 🚀 inicializa
    carregarGanhosReferral();
});
*/
