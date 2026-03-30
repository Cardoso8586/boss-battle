
const bossCoinsEl = document.getElementById("boss_coins");
const frasesCarregando = [
    "Buscando dados...",
    "Carregando saldo...",
    "Sincronizando moedas...",
    "Atualizando recompensas...",
    "Consultando servidor...",
    "Verificando saldo...",
    "Preparando dados...",
    "Obtendo informações...",
    "Buscando recompensas...",
    "Calculando saldo..."
];

let ultimoIndice = -1;

function sortearFraseSemRepetir(lista) {
    if (lista.length === 1) return lista[0];

    let indice;
    do {
        indice = Math.floor(Math.random() * lista.length);
    } while (indice === ultimoIndice);

    ultimoIndice = indice;
    return lista[indice];
}

function mostrarFraseAleatoria() {
    if (bossCoinsEl) {
       bossCoinsEl.textContent = sortearFraseSemRepetir(frasesCarregando);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    mostrarFraseAleatoria();
});


