// ⏱️ anima exatamente a cada 1 minuto
setInterval(animarDanoPorMinuto, 60000);

// opcional: anima uma vez ao carregar
animarDanoPorMinuto();

function animarDanoPorMinuto() {
    const span = document.getElementById("ataquePorMinuto");
    const container = document.getElementById("damageFloatContainer");

    if (!span || !container) return;

    const valor = span.innerText.trim();
    if (!valor || valor === "0") return;

    const dmg = document.createElement("span");
    dmg.className = "damage-float";
    dmg.textContent = `-${valor}`;

    container.appendChild(dmg);

    setTimeout(() => dmg.remove(), 1200);
}
