const frases = [
  "Enfrente chefes lendários, fortaleça seu exército de guerreiros e domine a arena com estratégia, poder e glória.",
  "Derrote inimigos épicos, expanda suas tropas e conquiste a arena com inteligência e força.",
  "Construa um exército imbatível e prove sua superioridade em batalhas intensas.",
  "A arena aguarda os mais fortes. Evolua, lute e conquiste seu lugar no topo.",
  "Somente os estrategistas sobreviverão. Recrute guerreiros e enfrente desafios lendários.",
  "Cada batalha conta. Cada decisão importa. Domine a arena."
];

const texto = document.getElementById("descricao-dinamica");

function trocarTexto() {
  const indiceAleatorio = Math.floor(Math.random() * frases.length);
  texto.style.opacity = 0;

  setTimeout(() => {
    texto.textContent = frases[indiceAleatorio];
    texto.style.opacity = 1;
  }, 300);
}

// troca inicial
trocarTexto();

// troca a cada 4 segundos
setInterval(trocarTexto, 15000);
