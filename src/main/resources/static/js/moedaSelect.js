const moedaSelect = document.getElementById("moeda");
const valorInput = document.getElementById("valorUsd");
const valorMinimoTexto = document.getElementById("valorMinimoTexto");
const bossCoinsTexto = document.getElementById("bossCoinsTexto");

const BOSS_COINS_POR_DOLAR = 10000000;

function formatarNumero(valor) {
    return new Intl.NumberFormat("pt-BR", {
        maximumFractionDigits: 0
    }).format(valor);
}

function atualizarBossCoins() {

    const valorUsd = Number(valorInput.value || 0);

    const bossCoins =
        valorUsd * BOSS_COINS_POR_DOLAR;

    bossCoinsTexto.textContent =
        formatarNumero(bossCoins) + " Boss Coins";
}

function atualizarMinimo() {

    const optionSelecionada =
        moedaSelect.options[moedaSelect.selectedIndex];

    const minimo =
        Number(optionSelecionada.getAttribute("data-min"));

    valorInput.min = minimo;

    // =========================
    // INPUT RECEBE O MINIMO
    // =========================

    valorInput.value = minimo.toFixed(2);

    // =========================
    // PLACEHOLDER
    // =========================

    valorInput.placeholder =
        "Mínimo US$ " + minimo.toFixed(2);

    // =========================
    // TEXTO MINIMO
    // =========================

    valorMinimoTexto.textContent =
        "US$ " + minimo.toFixed(2);

    atualizarBossCoins();
}

moedaSelect.addEventListener(
    "change",
    atualizarMinimo
);

valorInput.addEventListener(
    "input",
    atualizarBossCoins
);

atualizarMinimo();