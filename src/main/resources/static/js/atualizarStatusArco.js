document.addEventListener('DOMContentLoaded', () => {

    const nucleo = document.getElementById("nucleoArco");

    const btnFerro = document.getElementById("btnColocarFlechaFerro");
    const btnFogo = document.getElementById("btnColocarFlechaFogo");
    const btnVeneno = document.getElementById("btnColocarFlechaVeneno");
    const btnDiamante = document.getElementById("btnColocarFlechaDiamante");

    const btnEquiparArco = document.getElementById("btnEquiparArco");
   // const btnReativarArco = document.getElementById("btnReativarArco");

    const arcoAtivoSpan = document.getElementById("arcoAtivo");
    const durabilidadeSpan = document.getElementById("durabilidadeArco");
    const aljavaSpan = document.getElementById("aljavaCount");
    const tipoFlechaSpan = document.getElementById("tipoFlechaAtiva");
    const arcoDisponiveis = document.getElementById("arcoDisponiveis");

    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? Number(meta.content) : null;

    async function atualizarStatus() {
        if (!usuarioId) return;

        try {
            const res = await fetch(`/api/atualizar/status/usuario/${usuarioId}`);
            const data = await res.json();

            /* ===============================
               TEXTOS
            =============================== */
            arcoDisponiveis.textContent = data.arcoInventario;
            arcoAtivoSpan.textContent = data.arcoAtivo;
            durabilidadeSpan.textContent = data.durabilidadeArco;
            aljavaSpan.textContent = data.aljava;
            tipoFlechaSpan.textContent = data.aljava > 0 ? data.tipoFlecha : "-";

            const arcoAtivo = data.arcoAtivo;
            const durabilidade = data.durabilidadeArco;
            const arcoInventario = data.arcoInventario;
            const aljava = data.aljava;

            const temArcoDisponivel = arcoInventario > 0 || durabilidade > 0;

			if (aljava > 0) {
			           btnEquiparArco.classList.remove('hidden');
			       } else {
			           btnEquiparArco.classList.add('hidden');
			           return; // não faz mais nada
			       }

            const estoqueFlechas = {
                FERRO: data.flechaFerro,
                FOGO: data.flechaFogo,
                VENENO: data.flechaVeneno,
                DIAMANTE: data.flechaDiamante
            };

            const temFlechas = Object.values(estoqueFlechas).some(qtd => qtd > 0);

            /* ===============================
               NÚCLEO
            =============================== */
            nucleo.classList.toggle("hidden", !(temArcoDisponivel || temFlechas));

            /* ===============================
               BOTÕES DE FLECHA
            =============================== */
            const tipoAtivo = data.tipoFlecha || null;

			function atualizarBotao(botao, tipo, quantidade, texto) {
			    const imagens = {
			        FERRO: "/icones/flecha_ferro.webp",
			        FOGO: "/icones/flecha_fogo.webp",
			        VENENO: "/icones/flecha_veneno.webp",
			        DIAMANTE: "/icones/flecha_diamante.webp"
			    };

			    const habilitar =
			        temArcoDisponivel &&
			        quantidade > 0 &&
			        (!tipoAtivo || tipoAtivo === tipo);

			    botao.disabled = !habilitar;
			    botao.style.opacity = habilitar ? "1" : "0.5";

			    botao.innerHTML = `
			        <div style="display:flex; align-items:center; gap:6px;">
			            <img src="${imagens[tipo]}" alt="${tipo}" style="width:24px; height:54px;">
			            <span>${texto} (${quantidade})</span>
			        </div>
			    `;
			}


            atualizarBotao(btnFerro, "FERRO", estoqueFlechas.FERRO, "Colocar Flecha de Ferro");
            atualizarBotao(btnFogo, "FOGO", estoqueFlechas.FOGO, "Colocar Flecha de Fogo");
            atualizarBotao(btnVeneno, "VENENO", estoqueFlechas.VENENO, "Colocar Flecha de Veneno");
            atualizarBotao(btnDiamante, "DIAMANTE", estoqueFlechas.DIAMANTE, "Colocar Flecha de Diamante");

            /* ===============================
               BOTÕES DE ARCO (LÓGICA FINAL)
            =============================== */

            // Estado base — nunca sobra botão
            btnEquiparArco.classList.add("hidden");
            btnReativarArco.classList.add("hidden");
            btnEquiparArco.disabled = true;
           // btnReativarArco.disabled = true;

            // ❌ BLOQUEIOS ABSOLUTOS (ANTES DE TUDO)
            if (arcoAtivo > 0) return;
            if (aljava <= 0) return;

            const existeArco = durabilidade > 0;
            const podeEquipar = arcoInventario > 0 && !existeArco;
           // const podeReativar = existeArco && durabilidade > 0;

            // ✔ Equipar arco novo
            if (podeEquipar) {
                btnEquiparArco.classList.remove("hidden");
                btnEquiparArco.disabled = false;
                return;
            }
			
          /**
 *
            // 🔁 Reativar arco existente
            if (podeReativar) {
                btnReativarArco.classList.remove("hidden");
                btnReativarArco.disabled = false;
            } 
          */

        } catch (err) {
            console.error("Erro ao atualizar status do arco:", err);
        }
    }

    atualizarStatus();
    setInterval(atualizarStatus, 15000);

});

