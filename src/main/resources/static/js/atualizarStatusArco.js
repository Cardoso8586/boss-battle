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
			arcoDisponiveis.textContent = data.arcoInventario;
			           arcoAtivoSpan.textContent = data.arcoAtivo;
			           durabilidadeSpan.textContent = data.durabilidadeArco;
			           aljavaSpan.textContent = data.aljava;
			           tipoFlechaSpan.textContent = data.aljava > 0 ? data.tipoFlecha : "-";

			        
			           const durabilidade = data.durabilidadeArco;
			           const arcoInventario = data.arcoInventario;
			           const flechasNaAljava = data.aljava;
					const guerreiroAtivo = data.ativoGuerreiro ?? 0;
				    const espadaAtiva = data.ativaEspadaFlanejante ?? 0;
					const machadoAtivo = data.ativarMachadoDilacerador ?? 0;
						  
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
          
           // const podeReativar = existeArco && durabilidade > 0;
		   const podeEquipar =
		   		    arcoInventario > 0 &&      // ✅ existe pelo menos 1 arco no inventário
		   		    !existeArco &&          // ✅ NÃO há arco equipado (durabilidade = 0)
		   		    arcoAtivo === 0 &&      // ✅ nenhum arco está ativo no momento
		   		    flechasNaAljava > 0 &&  // ✅ existe pelo menos 1 flecha na aljava
		   		    guerreiroAtivo > 0 &&   // ✅ guerreiro está ativo
		   		    espadaAtiva === 0 &&    // ✅ nenhuma espada equipada
		   		    machadoAtivo === 0;     // ✅ nenhum machado equipado

		   		// ação final
		   		if (podeEquipar) {
		   		    btnEquiparArco.classList.remove("hidden");
		   		    btnEquiparArco.disabled = false;
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
    setInterval(atualizarStatus, 5000);

});

/***
 *  document.addEventListener('DOMContentLoaded', () => {

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

          
             arcoDisponiveis.textContent = data.arcoInventario;
             arcoAtivoSpan.textContent = data.arcoAtivo;
             durabilidadeSpan.textContent = data.durabilidadeArco;
             aljavaSpan.textContent = data.aljava;
             tipoFlechaSpan.textContent = data.aljava > 0 ? data.tipoFlecha : "-";

          
             const durabilidade = data.durabilidadeArco;
             const arcoInventario = data.arcoInventario;
             const flechasNaAljava = data.aljava;
 			const guerreiroAtivo = data.ativoGuerreiro ?? 0;
 		    const espadaAtiva = data.ativaEspadaFlanejante ?? 0;
 			const machadoAtivo = data.ativarMachadoDilacerador ?? 0;
 				  
             const temArcoDisponivel = arcoInventario > 0 || durabilidade > 0;


             const estoqueFlechas = {
                 FERRO: data.flechaFerro,
                 FOGO: data.flechaFogo,
                 VENENO: data.flechaVeneno,
                 DIAMANTE: data.flechaDiamante
             };

             const temFlechas = Object.values(estoqueFlechas).some(qtd => qtd > 0);

         
             nucleo.classList.toggle("hidden", !(temArcoDisponivel || temFlechas));

         
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

 			

 			// estado base (sempre começa escondido)
 			btnEquiparArco.classList.add("hidden");
 			btnEquiparArco.disabled = true;

 			// verifica se já existe arco equipado
 			const existeArco = durabilidade > 0;

 		
 			const podeEquipar =
 			    arcoInventario > 0 &&      // ✅ existe pelo menos 1 arco no inventário
 			    !existeArco &&          // ✅ NÃO há arco equipado (durabilidade = 0)
 			    arcoAtivo === 0 &&      // ✅ nenhum arco está ativo no momento
 			    flechasNaAljava > 0 &&  // ✅ existe pelo menos 1 flecha na aljava
 			    guerreiroAtivo > 0 &&   // ✅ guerreiro está ativo
 			    espadaAtiva === 0 &&    // ✅ nenhuma espada equipada
 			    machadoAtivo === 0;     // ✅ nenhum machado equipado

 			// ação final
 			if (podeEquipar) {
 			    btnEquiparArco.classList.remove("hidden");
 			    btnEquiparArco.disabled = false;
 			}

            

         } catch (err) {
             console.error("Erro ao atualizar status do arco:", err);
         }
     }

     atualizarStatus();
     setInterval(atualizarStatus, 5000);

 });

 */