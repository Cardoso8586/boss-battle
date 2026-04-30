let tempoAnuncio = 20;
let intervaloAnuncio = null;
let anuncioLiberado = false;
let recompensaRecebida = false;

const btnAbrirAnuncio = document.getElementById("btnAbrirAnuncio");
const modalAnuncio = document.getElementById("modalAnuncioRecompensa");
const fecharModalAnuncio = document.getElementById("fecharModalAnuncio");
const contadorAnuncio = document.getElementById("contadorAnuncio");
const btnReceberRecompensa = document.getElementById("btnReceberRecompensaAnuncio");

const resultadoBox = document.getElementById("resultadoRecompensa");
const imgRecompensa = document.getElementById("imgRecompensa");
const textoRecompensa = document.getElementById("textoRecompensa");
const textoStreak = document.getElementById("textoStreak");

btnAbrirAnuncio?.addEventListener("click", abrirModalAnuncio);
fecharModalAnuncio?.addEventListener("click", fecharModal);

function abrirModalAnuncio() {
    if (!modalAnuncio) return;

    modalAnuncio.style.display = "flex";

    tempoAnuncio = 20;
    anuncioLiberado = false;
    recompensaRecebida = false;

    contadorAnuncio.textContent = tempoAnuncio;

    btnReceberRecompensa.disabled = true;
    btnReceberRecompensa.textContent = "Aguarde...";

    if (resultadoBox) resultadoBox.style.display = "none";
    if (imgRecompensa) imgRecompensa.src = "";
    if (textoRecompensa) textoRecompensa.textContent = "";
    if (textoStreak) textoStreak.textContent = "";

    clearInterval(intervaloAnuncio);

    intervaloAnuncio = setInterval(() => {
        tempoAnuncio--;
        contadorAnuncio.textContent = tempoAnuncio;

        if (tempoAnuncio <= 0) {
            clearInterval(intervaloAnuncio);
            anuncioLiberado = true;

            btnReceberRecompensa.disabled = false;
            btnReceberRecompensa.textContent = "🎁 Receber recompensa";
        }
    }, 1000);
}

function fecharModal() {
    if (modalAnuncio) {
        modalAnuncio.style.display = "none";
    }

    clearInterval(intervaloAnuncio);

    anuncioLiberado = false;
    recompensaRecebida = false;
}

btnReceberRecompensa?.addEventListener("click", async () => {

    if (!anuncioLiberado) {
        alert("Aguarde o tempo do anúncio terminar.");
        return;
    }

    if (recompensaRecebida) {
        alert("Você já recebeu essa recompensa.");
        fecharModal();
        return;
    }

    const usuarioId = document.querySelector('meta[name="user-id"]')?.content;

    if (!usuarioId) {
        alert("Usuário não encontrado.");
        fecharModal();
        return;
    }

    btnReceberRecompensa.disabled = true;
    btnReceberRecompensa.textContent = "Processando...";

    try {
        const resposta = await fetch(`/api/anuncio-recompensa/receber/${usuarioId}`, {
            method: "POST"
        });

        const dados = await resposta.json();

        if (dados.success) {

            recompensaRecebida = true;

            if (resultadoBox) resultadoBox.style.display = "block";

            if (imgRecompensa) {
                imgRecompensa.src = dados.ganhouItem && dados.imagemItem
                    ? "/" + dados.imagemItem
                    : "/icones/boss_coin.webp";
            }

            if (textoRecompensa) {
                textoRecompensa.textContent = dados.descricao || "Recompensa recebida!";
            }

            if (textoStreak) {
                textoStreak.textContent = `🔥 Streak: ${dados.streakAtual}/10`;
            }

            if (typeof atualizarSaldoBossCoins === "function") {
                atualizarSaldoBossCoins();
            }

            if (typeof carregarSaldo === "function") {
                carregarSaldo();
            }

            setTimeout(fecharModal, 8200);

        } else {
            alert(dados.message || "Erro ao receber recompensa.");
            fecharModal();
        }

    } catch (erro) {
        console.error("Erro:", erro);
        alert("Erro ao conectar com servidor.");
        fecharModal();
    }
});
/*
let tempoAnuncio = 30;
let intervaloAnuncio = null;
let anuncioLiberado = false;
let recompensaRecebida = false;

const btnAbrirAnuncio = document.getElementById("btnAbrirAnuncio");
const modalAnuncio = document.getElementById("modalAnuncioRecompensa");
const fecharModalAnuncio = document.getElementById("fecharModalAnuncio");
const contadorAnuncio = document.getElementById("contadorAnuncio");
const btnReceberRecompensa = document.getElementById("btnReceberRecompensaAnuncio");

const resultadoBox = document.getElementById("resultadoRecompensa");
const imgRecompensa = document.getElementById("imgRecompensa");
const textoRecompensa = document.getElementById("textoRecompensa");
const textoStreak = document.getElementById("textoStreak");

if (btnAbrirAnuncio) {
    btnAbrirAnuncio.addEventListener("click", abrirModalAnuncio);
}

if (fecharModalAnuncio) {
    fecharModalAnuncio.addEventListener("click", fecharModal);
}

function abrirModalAnuncio() {
    modalAnuncio.style.display = "flex";

    tempoAnuncio = 30;
    anuncioLiberado = false;
    recompensaRecebida = false;

    contadorAnuncio.textContent = tempoAnuncio;

    btnReceberRecompensa.disabled = true;
    btnReceberRecompensa.textContent = "Aguarde...";

    if (resultadoBox) {
        resultadoBox.style.display = "none";
    }

    if (imgRecompensa) {
        imgRecompensa.src = "";
    }

    if (textoRecompensa) {
        textoRecompensa.textContent = "";
    }

    if (textoStreak) {
        textoStreak.textContent = "";
    }

    clearInterval(intervaloAnuncio);

    intervaloAnuncio = setInterval(() => {
        tempoAnuncio--;
        contadorAnuncio.textContent = tempoAnuncio;

        if (tempoAnuncio <= 0) {
            clearInterval(intervaloAnuncio);
            anuncioLiberado = true;

            btnReceberRecompensa.disabled = false;
            btnReceberRecompensa.textContent = "🎁 Receber recompensa";
        }
    }, 1000);
}

function fecharModal() {
    modalAnuncio.style.display = "none";
    clearInterval(intervaloAnuncio);
}

if (btnReceberRecompensa) {
    btnReceberRecompensa.addEventListener("click", async () => {

        if (!anuncioLiberado) {
            alert("Aguarde o tempo do anúncio terminar.");
            return;
        }

        if (recompensaRecebida) {
            alert("Você já recebeu esta recompensa.");
            return;
        }

        const usuarioId = document.querySelector('meta[name="user-id"]')?.content;

        if (!usuarioId) {
            alert("Usuário não encontrado.");
            return;
        }

        btnReceberRecompensa.disabled = true;
        btnReceberRecompensa.textContent = "Processando...";

        try {
            const resposta = await fetch(`/api/anuncio-recompensa/receber/${usuarioId}`, {
                method: "POST"
            });

            const dados = await resposta.json();

            if (dados.success) {
                recompensaRecebida = true;

                if (resultadoBox) {
                    resultadoBox.style.display = "block";
                }

                if (imgRecompensa) {
                    if (dados.ganhouItem && dados.imagemItem) {
                        imgRecompensa.src = "/" + dados.imagemItem;
                    } else {
                        imgRecompensa.src = "/icones/boss_coin.webp";
                    }
                }

                if (textoRecompensa) {
                    textoRecompensa.textContent = dados.descricao || "Recompensa recebida!";
                }

                if (textoStreak) {
                    textoStreak.textContent = `🔥 Streak: ${dados.streakAtual}/10`;
                }

                btnReceberRecompensa.textContent = "✅ Recompensa recebida";
                btnReceberRecompensa.disabled = true;

                if (typeof atualizarSaldoBossCoins === "function") {
                    atualizarSaldoBossCoins();
                }

                if (typeof carregarSaldo === "function") {
                    carregarSaldo();
                }

            } else {
                alert(dados.message || "Não foi possível receber a recompensa.");

                btnReceberRecompensa.disabled = false;
                btnReceberRecompensa.textContent = "🎁 Receber recompensa";
            }

        } catch (erro) {
            console.error("Erro ao receber recompensa:", erro);
            alert("Erro ao receber recompensa.");

            btnReceberRecompensa.disabled = false;
            btnReceberRecompensa.textContent = "🎁 Receber recompensa";
        }
    });
}

*/