document.addEventListener('DOMContentLoaded', () => {
    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? Number(meta.content) : null;

    if (!usuarioId || isNaN(usuarioId)) {
        console.error('Usuário não encontrado');
        return;
    }

    const rewardLabels = {
        CURRENCY: "Moedas",
        CONSUMABLE: "Item",
        PROGRESSION: "Experiência",
        GUERREIRO: "Novo Guerreiro",
		SPECIAL:"Ataque especial"
    };

	const itemLabels = {
	    BOSS_COIN: "Boss Coin",
	    POCAO_VIGOR: "Poção de Vigor",
		ESPADA_FLANEJANTE:"Espada Flanejante",
		MACHADO_DILACERADOR:"Machado Dilacerador",
	    GUERREIRO_BASICO: "Guerreiro Básico",
	    EXP: "Experiência",
	    ATAQUE_SPECIAL: "Ataque Especial"
	};


	const allowedItems = ["BOSS_COIN", "POCAO_VIGOR", "GUERREIRO_BASICO", "EXP", "ATAQUE_SPECIAL", "ESPADA_FLANEJANTE", "MACHADO_DILACERADOR"];


    // Função para buscar e atualizar o preview
    function updateRewardPreview() {
        fetch(`/api/rewards/preview/${usuarioId}`)
            .then(res => {
                if (!res.ok) throw new Error('Erro ao buscar preview');
                return res.json();
            })
            .then(data => {
                if (!data) return;

                if (!allowedItems.includes(data.rewardItem)) {
                    console.warn("Recompensa ignorada:", data.rewardItem);
                    return;
                }

                document.getElementById("rewardImage").src =
                    data.imageUrl ? `/${data.imageUrl}` : "/img/reward_default.webp";

                document.getElementById("rewardTitle").innerText =
                    rewardLabels[data.rewardType] ?? "Recompensa";

                document.getElementById("rewardDesc").innerText =
                    data.rewardItem
                        ? (itemLabels[data.rewardItem] ?? data.rewardItem)
                        : "Recompensa especial!";

                document.getElementById("rewardAmount").innerText =
                    data.amount && data.amount > 1
                        ? `Quantidade: ${data.amount}`
                        : "";

						
											 
                document.getElementById("rewardCard").style.display = "block";
            })
            .catch(err => console.error("Erro ao carregar recompensa:", err));
    }

   
    updateRewardPreview();
    setInterval(updateRewardPreview, 10000);
});
