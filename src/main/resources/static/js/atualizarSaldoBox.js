
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

function sortearFrase(lista) {
    const indice = Math.floor(Math.random() * lista.length);
    return lista[indice];
}

function mostrarFraseAleatoria() {
    if (bossCoinsEl) {
        bossCoinsEl.textContent = sortearFrase(frasesCarregando);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    mostrarFraseAleatoria();
});

/* Exemplo de uso antes de buscar o saldo */
function carregarBossCoins() {
    mostrarFraseAleatoria();

    fetch("/api/seu-endpoint")
        .then(response => response.json())
        .then(data => {
            bossCoinsEl.textContent = data.bossCoins ?? 0;
        })
        .catch(() => {
            bossCoinsEl.textContent = "Erro ao carregar";
        });
}
