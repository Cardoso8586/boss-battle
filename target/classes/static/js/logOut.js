window.addEventListener("load", function () {
    const deveMostrar = sessionStorage.getItem("mostrarSpinnerSaida");
    if (!deveMostrar) return;

    sessionStorage.removeItem("mostrarSpinnerSaida");

    const loading = document.getElementById("loading");
    const text = document.getElementById("loading-text");

    const mensagensSaida = [
        "Voltando ao acampamento...",
        "Os guerreiros estão recuando...",
        "Encerrando a batalha...",
        "Retornando à base...",
        "Reagrupando tropas...",
        "Saindo da arena...",
        "Guardando armas e escudos...",
        "A batalha chegou ao fim...",
        "Os heróis estão deixando o campo de batalha...",
        "Retornando ao abrigo dos guerreiros..."
    ];

    function fraseAleatoria() {
        return mensagensSaida[Math.floor(Math.random() * mensagensSaida.length)];
    }

    if (!loading || !text) return;

    text.textContent = fraseAleatoria();

    loading.style.display = "flex";
    loading.style.visibility = "visible";
    loading.style.opacity = "1";

    text.style.display = "block";
    text.style.visibility = "visible";

    setTimeout(() => {
        loading.style.opacity = "0";

        setTimeout(() => {
            loading.style.display = "none";
            loading.style.visibility = "hidden";
        }, 500);
    }, 1500);
});