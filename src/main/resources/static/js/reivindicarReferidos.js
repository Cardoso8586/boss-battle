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
