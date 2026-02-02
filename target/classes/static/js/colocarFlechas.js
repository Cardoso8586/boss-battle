document.addEventListener("DOMContentLoaded", () => {
    // ⚡ ID do usuário
    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? Number(meta.content) : null;
    if (!usuarioId) return;
    window.usuarioId = usuarioId;

    // Botões de flecha
    const btnFlechas = {
        FERRO: document.getElementById("btnColocarFlechaFerro"),
        FOGO: document.getElementById("btnColocarFlechaFogo"),
        VENENO: document.getElementById("btnColocarFlechaVeneno"),
        DIAMANTE: document.getElementById("btnColocarFlechaDiamante")
    };

    // Elementos do DOM
    const arcoAtivoElem = document.getElementById("arcoAtivo");
    const aljavaCount = document.getElementById("aljavaCount");
    const tipoFlechaAtiva = document.getElementById("tipoFlechaAtiva");
    const btnEquiparArco = document.getElementById("btnEquiparArco");
    const btnReativarArco = document.getElementById("btnReativarArco");
    const nucleo = document.getElementById("nucleoArco");

    // Estoque real vindo do backend
    let estoque = { FERRO: 0, FOGO: 0, VENENO: 0, DIAMANTE: 0 };

    // Cria inputs de quantidade dinamicamente para cada flecha
    Object.entries(btnFlechas).forEach(([tipo, btn]) => {
		const container = document.createElement("div");
		   container.classList.add("flecha-control");
		   container.style.display = "flex";
		   container.style.alignItems = "center";
		   container.style.marginTop = "5px";

		   const input = document.createElement("input");
		   input.type = "number";
		   input.min = 1;
		   input.style.width = "100px";
		   input.style.marginRight = "5px";

		   const maxLabel = document.createElement("span");
		   maxLabel.textContent = ` / 0 disponíveis`;

		   container.appendChild(input);
		   container.appendChild(maxLabel);
		   btn.parentNode.insertBefore(container, btn.nextSibling);

        // Evento do clique
        btn.addEventListener("click", async () => {
            const quantidade = parseInt(input.value, 10);

            if (!quantidade || quantidade <= 0) {
                Swal.fire({
                    customClass: { title: 'swal-game-error' },
                    icon: 'error',
                    title: 'Erro',
                    text: 'Quantidade inválida!',
                    timer: 5000,
                    showConfirmButton: false,
                    background: 'transparent',
                    color: '#ff3b3b'
                });
                return;
            }

            if (quantidade > estoque[tipo]) {
                Swal.fire({
                    customClass: { title: 'swal-game-error' },
                    icon: 'error',
                    title: 'Erro',
                    text: `Não há flechas de ${tipo} suficientes!`,
                    timer: 5000,
                    showConfirmButton: false,
                    background: 'transparent',
                    color: '#ff3b3b'
                });
                return;
            }

            try {
                const response = await fetch(`/aljava/colocar`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ usuarioId, tipo, quantidade })
                });

                const data = await response.json();
                if (!response.ok) throw new Error(data.error || "Erro ao colocar flechas");

                // Atualiza DOM
                aljavaCount.textContent = data.aljava;
                tipoFlechaAtiva.textContent = data.tipoFlecha || "-";

                Swal.fire({
                    customClass: { title: 'swal-game-text' },
                    icon: 'success',
                    title: 'Colocando na Aljava!',
                    text: `Colocadas ${quantidade} flechas de ${tipo} na aljava!`,
                    timer: 5000,
                    showConfirmButton: false,
                    background: 'transparent',
                    color: '#ffb400'
                });

                atualizarStatus();

            } catch (err) {
               
                Swal.fire({
                    customClass: { title: 'swal-game-error' },
                    icon: 'error',
                    title: 'Erro',
					text: 'Não foi possível colocar as flechas. Verifique se há outra arma equipada ou flechas incompatíveis.',
					//text: 'Não foi possível colocar as flechas. Verifique se há outra arma equipada ou flechas incompatíveis.',
                    timer: 5000,
                    showConfirmButton: false,
                    background: 'transparent',
                    color: '#ff3b3b'
                });
            }
        });
    });

    // Atualiza status do backend e estoque real
    async function atualizarStatus() {
        try {
            const res = await fetch(`/api/atualizar/status/usuario/${usuarioId}`);
            const data = await res.json();

            arcoAtivoElem.textContent = data.arcoAtivo;
            aljavaCount.textContent = data.aljava;
            tipoFlechaAtiva.textContent = data.aljava > 0 ? data.tipoFlecha : "-";

            // Estoque real do backend
            estoque.FERRO = data.flechaFerro || 0;
            estoque.FOGO = data.flechaFogo || 0;
            estoque.VENENO = data.flechaVeneno || 0;
            estoque.DIAMANTE = data.flechaDiamante || 0;

            // Atualiza inputs e labels
			// Atualiza inputs, labels e VISIBILIDADE dos botões
			Object.entries(btnFlechas).forEach(([tipo, btn]) => {
			    const container = btn.nextElementSibling;
			    if (!container || !container.classList.contains("flecha-control")) return;

			    const input = container.querySelector("input");
			    const maxLabel = container.querySelector("span");

			    const quantidade = estoque[tipo];

			    if (quantidade <= 0) {
			        // 🚫 Sem flechas → esconde tudo
			        btn.classList.add("hidden");
			        container.classList.add("hidden");
			        return;
			    }

			    // ✅ Tem flechas → mostra tudo
			    btn.classList.remove("hidden");
			    container.classList.remove("hidden");

			    input.max = quantidade;
			    input.value = quantidade; // ⚡ começa com o TOTAL
			    maxLabel.textContent = ` / ${quantidade} disponíveis`;
			});

            atualizarBotoes(data);

        } catch (err) {
            console.error("Erro ao atualizar status do arco:", err);
        }
    }

    function atualizarBotoes(data) {
        const temFlechas = data.aljava > 0 || Object.values(estoque).some(qtd => qtd > 0);
        const temArcoDisponivel = data.arcoInventario > 0 || data.durabilidadeArco > 0;

        // Mostra núcleo se houver arco ou flechas
        nucleo.classList.toggle("hidden", !(temFlechas || temArcoDisponivel));

        // 🔹 Equipar arco só se houver arco no inventário e arco não ativo
        const podeEquipar = data.arcoInventario > 0 && data.arcoAtivo === 0;
        btnEquiparArco.classList.toggle("hidden", !podeEquipar);
        btnEquiparArco.disabled = !podeEquipar;

        // 🔹 Reativar arco só se houver durabilidade e arco não ativo
        const podeReativar = data.durabilidadeArco > 0 && data.arcoAtivo === 0;
        btnReativarArco.classList.toggle("hidden", !podeReativar);
        btnReativarArco.disabled = !podeReativar;

		// 🔹 Botões de flecha SEMPRE ativos (validação é no click)
		Object.values(btnFlechas).forEach(btn => {
		    btn.disabled = false;
		    btn.style.opacity = "1";
		});

    }

    atualizarStatus();
    setInterval(atualizarStatus, 5000);
});

  
