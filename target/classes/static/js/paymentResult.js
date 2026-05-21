function verificarStatusPagamentoServidor() {

    const paymentBox =
        document.getElementById("paymentResult");

    if (!paymentBox) {
        return;
    }

    const paymentId =
        paymentBox.dataset.paymentId;

    if (!paymentId) {
        return;
    }

    fetch(`/depositos/nowpayments/status/${paymentId}`)
        .then(response => {

            if (!response.ok) {
                throw new Error("Erro ao consultar status");
            }

            return response.json();
        })
        .then(data => {

            const status = data.status;
            const creditado = data.creditado;

            console.log(
                "Status depósito:",
                status,
                "Creditado:",
                creditado
            );

            // =========================================
            // WAITING
            // =========================================

            if (status === "waiting") {

                setTimeout(
                    verificarStatusPagamentoServidor,
                    15000
                );

                return;
            }

            // =========================================
            // CONFIRMING / SENDING
            // =========================================

            if (
                status === "confirming" ||
                status === "sending"
            ) {

                paymentBox.style.display = "none";

                setTimeout(() => {
                    window.location.reload();
                }, 1000);

                return;
            }

            // =========================================
            // FINISHED / PARCIAL
            // =========================================

            if (
                status === "finished" ||
                status === "partially_paid" ||
                creditado === true
            ) {

                paymentBox.style.display = "none";

                setTimeout(() => {
                    window.location.reload();
                }, 1000);

                return;
            }

            // =========================================
            // FAILED / EXPIRED
            // =========================================

            if (
                status === "failed" ||
                status === "expired"
            ) {

                paymentBox.style.display = "none";

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