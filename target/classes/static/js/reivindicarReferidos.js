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
            title: 'Confirmar Reivindicação?',
            text: `Você irá receber ${ganhosPendentes.toFixed(1)} Boss.`,
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Sim, reivindicar!',
            cancelButtonText: 'Cancelar'
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

                await Swal.fire(
                    'Sucesso!',
                    'Seus ganhos foram reivindicados!',
                    'success'
                );

                // 🔥 atualiza estado após claim
                ganhosSpan.innerText = "0";
                claimBtn.style.display = "none";

            } catch (err) {
                Swal.fire(
                    'Erro',
                    'Não foi possível reivindicar os ganhos.',
                    'error'
                );
            }
        });
    });

    // 🚀 inicializa
    carregarGanhosReferral();
});
