// ==============================
// REPARO POR MINUTO (FETCH)
// ==============================

async function atualizarReparoRetaguarda() {
    try {
        const response = await fetch(`/retaguarda/reparo/${usuarioId}`);
        if (!response.ok) return;

        const data = await response.json();

        const reparoEfetivo = Number(data.reparoEfetivo || 0);
        const energiaAtual = Number(data.energiaAtual || 0);
        const energiaMaxima = Number(data.energiaMaxima || 0);

        // Se já estiver cheio, não anima
        if (energiaAtual >= energiaMaxima) return;

        if (reparoEfetivo > 0) {
            animarReparo(reparoEfetivo);

            // Atualiza energia no front
            const energiaEl = document.getElementById('energiaAtual');
            if (energiaEl) {
                energiaEl.textContent = new Intl.NumberFormat('pt-BR').format(
                    Math.min(energiaAtual + reparoEfetivo, energiaMaxima)
                );
            }
        }

    } catch (e) {
        console.error("Erro ao consultar reparo da retaguarda", e);
    }
}

// ==============================
// ANIMAÇÃO
// ==============================

function animarReparo(valor) {
    const container = document.getElementById('reparoRetaguarda');
    if (!container) return;

    const float = document.createElement('span');
    float.className = 'reparo-float';
    float.textContent = `+ ${valor}`;

    container.appendChild(float);

    requestAnimationFrame(() => {
        float.style.bottom = '50px';
        float.style.opacity = '0';
    });

    setTimeout(() => float.remove(), 2000);
}

// ==============================
// TIMERS
// ==============================

// Primeira execução imediata (sincroniza)
atualizarReparoRetaguarda();

// Depois, a cada 1 minuto
setInterval(atualizarReparoRetaguarda, 80000);

