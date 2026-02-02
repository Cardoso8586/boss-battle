document.addEventListener('DOMContentLoaded', () => {
    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;

    function formatarNumero(numero) {
        return new Intl.NumberFormat('pt-BR').format(numero);
    }

    async function animarDanoPorMinuto() {
        try {
            const res = await fetch(`/api/atualizar/status/usuario/${usuarioId}`);
            const data = await res.json();

            const container = document.getElementById("damageFloatContainer");

            const energia = Math.max(0, data.energiaGuerreiros);
            const espadaFlanejanteAtiva = data.ativaEspadaFlanejante;
			const machadoDilaceradorAtivo = data.ativarMachadoDilacerador;
            const ataquePorMinuto = data.ataquePorMinuto;
			const tipoFlecha = data.tipoFlecha;
			const arcoAtivo = data.arcoAtivo;
          
            let valor = ataquePorMinuto;
            // Verifica se há energia
            if (!energia) return;
			if (energia <= 0) return;
            // Se espada flanejante estiver ativa, aumenta 20%
            if (espadaFlanejanteAtiva) {
                valor *= 1.2;
            }
			// Se machado dilacerador ativo, aumenta 10%
			 if (machadoDilaceradorAtivo) {
			    valor *= 1.1;
			 }
			
			 if (arcoAtivo > 0) {
			     const bonusFlecha = {
			         FERRO: 0.03,
			         FOGO: 0.05,
			         VENENO: 0.07,
			         DIAMANTE: 0.10
			     };

			     const bonus = bonusFlecha[tipoFlecha] || 0;
			     valor *= 1 + bonus;
			 }
/**7
 * 
 *  // Se arco está ativo
			 if (arcoAtivo > 0) {

			     switch(tipoFlecha) {
			         case "FERRO":
			             valor *= 1.03; // +3%
			             break;
			         case "FOGO":
			             valor *= 1.05; // +5%
			             break;
			         case "VENENO":
			             valor *= 1.08; // +8%
			             break;
			         case "DIAMANTE":
			             valor *= 1.10; // +10%
			             break;
			         default:
			             // Nenhum tipo válido → sem multiplicador
			             break;
			     }

			 }

 */
			
			 

            // Cria e exibe o dano flutuante
            const dmg = document.createElement("span");
            dmg.className = "damage-float";
            dmg.textContent = `-${formatarNumero(valor)}`;
            container.appendChild(dmg);

            // Remove após 1.2s
            setTimeout(() => dmg.remove(), 3200);

        } catch (err) {
            console.error('Erro ao animar dano:', err);
        }
    }

    // Atualiza ao carregar e a cada 1 minuto
    animarDanoPorMinuto();
    setInterval(animarDanoPorMinuto, 60000);
});

/** 
 * 
 * // ⏱️ anima exatamente a cada 1 minuto
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
*/
