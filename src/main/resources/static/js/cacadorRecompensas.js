document.addEventListener("DOMContentLoaded", () => {
    iniciarPaginaCacadorRecompensas();
});

let intervaloCacadorRecompensas = null;

async function iniciarPaginaCacadorRecompensas() {
    const textoStatus = document.getElementById("textoStatusRecompensa");
    const contador = document.getElementById("contadorCooldownRecompensa");
    const barra = document.getElementById("barraCooldownRecompensa");
    const btnIr = document.getElementById("btnIrAnuncio");
    const btnBloqueado = document.getElementById("btnBloqueadoAnuncio");
    const usuarioId = document.querySelector('meta[name="user-id"]')?.content;

    if (!textoStatus || !contador || !barra || !btnIr || !btnBloqueado) {
        return;
    }

    if (intervaloCacadorRecompensas) {
        clearInterval(intervaloCacadorRecompensas);
        intervaloCacadorRecompensas = null;
    }

    if (usuarioId) {
        try {
            const res = await fetch(`/api/anuncio-recompensa/status/${usuarioId}`);
            const dados = await res.json();

            if (dados.limiteAtingido) {
                mostrarRecompensasConcluidas(
                    textoStatus,
                    contador,
                    barra,
                    btnIr,
                    btnBloqueado
                );
                return;
            }
        } catch (e) {
            console.error("Erro ao verificar limite diário:", e);
        }
    }

    const cooldownTempo = parseInt(localStorage.getItem("cooldownTempo"));
    const ultimoUso = parseInt(localStorage.getItem("ultimoAnuncioTempo"));

    if (!cooldownTempo || !ultimoUso) {
        liberarRecompensa(textoStatus, contador, barra, btnIr, btnBloqueado);
        return;
    }

    function atualizar() {
        const agora = Date.now();
        const diff = Math.floor((agora - ultimoUso) / 1000);
        const restante = cooldownTempo - diff;

        if (restante <= 0) {
            clearInterval(intervaloCacadorRecompensas);
            intervaloCacadorRecompensas = null;

            liberarRecompensa(textoStatus, contador, barra, btnIr, btnBloqueado);
            return;
        }

        bloquearRecompensa(
            textoStatus,
            contador,
            barra,
            btnIr,
            btnBloqueado,
            restante,
            cooldownTempo
        );
    }

    atualizar();
    intervaloCacadorRecompensas = setInterval(atualizar, 5000);
}

function mostrarRecompensasConcluidas(
    textoStatus,
    contador,
    barra,
    btnIr,
    btnBloqueado
) {
    textoStatus.textContent = "🏆 Recompensas diárias concluídas!";
    contador.textContent = "Você já coletou todas as 20 recompensas de hoje.";

    barra.style.width = "100%";

    btnIr.style.display = "none";
    btnBloqueado.style.display = "block";
    btnBloqueado.textContent = "✅ 20/20 concluídas";
}

function liberarRecompensa(textoStatus, contador, barra, btnIr, btnBloqueado) {
    textoStatus.textContent = "🎁 Recompensa disponível agora!";
    contador.textContent = "Clique abaixo para assistir e receber sua recompensa.";

    barra.style.width = "100%";

    btnIr.style.display = "block";
    btnBloqueado.style.display = "none";
}

function bloquearRecompensa(
    textoStatus,
    contador,
    barra,
    btnIr,
    btnBloqueado,
    restante,
    cooldownTempo
) {
    const min = Math.floor(restante / 60);
    const seg = restante % 60;

    const tempoFormatado =
        `${min}m ${seg.toString().padStart(2, "0")}s`;

    textoStatus.textContent = "⏳ Próxima recompensa em:";
    contador.textContent = tempoFormatado;

    const passado = cooldownTempo - restante;
    const percentual = Math.min((passado / cooldownTempo) * 100, 100);

    barra.style.width = `${percentual}%`;

    btnIr.style.display = "none";
    btnBloqueado.style.display = "block";
    btnBloqueado.textContent = `Aguarde ${tempoFormatado}`;
}