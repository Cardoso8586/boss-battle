
// Função para formatar números com separadores de milhar
function formatarNumero(numero) {
    return new Intl.NumberFormat('pt-BR').format(numero);
}




async function carregarBossAtivo() {
    try {
        const response = await fetch("/api/boss/active");
        if (!response.ok) return;

        const boss = await response.json();

        const nameEl = document.getElementById("boss-name");
        const imgEl = document.getElementById("boss-image");
        const hpBarEl = document.getElementById("boss-hp-bar");
        const hpTextEl = document.getElementById("boss-hp-text");
        const reward = document.getElementById("boss-reward");
        const bossXp = document.getElementById("boss-xp");

        if (!boss || boss.alive === false) {
            setTimeout(() => {
                location.reload();
            }, 600);
            if (bossEstavaVivo) bossEstavaVivo = false;

            nameEl.innerText = "Nenhum boss ativo no momento!";
            imgEl.style.display = "none";
            hpBarEl.style.width = "0%";
            hpTextEl.innerText = "";
            reward.innerText = "0";
            bossXp.innerText = "0";
            return;
        }

        bossEstavaVivo = true;

        // Carrega a imagem antes de mostrar
        const img = new Image();
        img.src = boss.imageUrl;
        img.onload = () => {
            imgEl.src = boss.imageUrl;
            imgEl.style.display = "block";

            // Atualiza os outros elementos só depois que a imagem carregou
            nameEl.innerText = boss.bossName;
            reward.innerText = formatarNumero(boss.rewardBoss);
            bossXp.innerText = formatarNumero(boss.rewardExp);

            const percent = (boss.currentHp / boss.maxHp) * 100;
            hpBarEl.style.width = percent + "%";
            hpTextEl.innerText = `${formatarNumero(boss.currentHp)} / ${formatarNumero(boss.maxHp)}`;
        };

    } catch (e) {
        console.error("Erro ao carregar boss ativo:", e);
    }
}


carregarBossAtivo(); // ⚡ carrega imediatamente
setInterval(carregarBossAtivo, 3000);





