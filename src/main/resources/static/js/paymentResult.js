function verificarStatusPagamentoServidor() {

    const paymentBox =
        document.getElementById("paymentResult");

    const formBox =
        document.getElementById("depositFormBox");

    const infoBox =
        document.getElementById("depositInfoBox");

    if (!paymentBox) return;

    const paymentId =
        paymentBox.dataset.paymentId;

    if (!paymentId) return;

    fetch(`/depositos/nowpayments/status/${paymentId}`)
        .then(response => {

            if (!response.ok) {
                throw new Error("Erro ao consultar status");
            }

            return response.json();
        })
        .then(data => {

            const status =
                String(data.status || "").toUpperCase();

            const creditado =
                data.creditado === true;

            console.log(
                "Status depósito:",
                status,
                "Creditado:",
                creditado
            );

            // =========================
            // FINALIZADO
            // =========================

            if (
                status === "FINISHED" ||
                status === "PARTIALLY_PAID" ||
                creditado
            ) {

                paymentBox.style.display = "none";

                if (formBox) {
                    formBox.style.display = "block";
                }

                if (infoBox) {
                    infoBox.style.display = "block";
                }

                return;
            }

            // =========================
            // FALHOU / EXPIRADO
            // =========================

            if (
                status === "FAILED" ||
                status === "EXPIRED"
            ) {

                paymentBox.style.display = "none";

                if (formBox) {
                    formBox.style.display = "block";
                }

                if (infoBox) {
                    infoBox.style.display = "block";
                }

                return;
            }

            setTimeout(
                verificarStatusPagamentoServidor,
                15000
            );
        })
        .catch(error => {

            console.error(error);

            setTimeout(
                verificarStatusPagamentoServidor,
                30000
            );
        });
}

verificarStatusPagamentoServidor();