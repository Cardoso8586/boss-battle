document.addEventListener("DOMContentLoaded", () => {
    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? Number(meta.content) : null;
    if (!usuarioId) return;

    const btnFlechas = {
        FERRO: document.getElementById("btnColocarFlechaFerro"),
        FOGO: document.getElementById("btnColocarFlechaFogo"),
        VENENO: document.getElementById("btnColocarFlechaVeneno"),
        DIAMANTE: document.getElementById("btnColocarFlechaDiamante")
    };

    const arcoAtivoElem = document.getElementById("arcoAtivo");
    const aljavaCount = document.getElementById("aljavaCount");
    const tipoFlechaAtiva = document.getElementById("tipoFlechaAtiva");
  
	const aljavaErroImg ="icones/erro_img/aljava_erro.webp";
	const aljavaImg ="icones/ok_img/aljava_ok.webp";
	
    let estoque = { FERRO: 0, FOGO: 0, VENENO: 0, DIAMANTE: 0 };

    // Cria input de quantidade para cada tipo de flecha
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

        // Clique do botão pega valor do input
		btn.addEventListener("click", async () => {

		    if (btn.dataset.loading === "true") return;

		    const quantidade = Number(input.value);

		    if (!quantidade || quantidade <= 0) {
				Swal.fire({
								customClass: { title: 'swal-game-error' },
							    imageUrl: aljavaErroImg,							  
								imageWidth: 90,											   
							    imageHeight: 120,
				                title: 'Quantidade inválida!',
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
			    imageUrl: aljavaErroImg,							  
				imageWidth: 90,											   
			    imageHeight: 120,
                title: `Não há flechas de ${tipo} suficientes!`,
			    timer: 5000,
				showConfirmButton: false,
				background: 'transparent',
			    color: '#ff3b3b'											
												                   
																 						
		    });
		        return;
		    }

		    // 🔒 trava SEM disabled
		    btn.dataset.loading = "true";
		    btn.style.pointerEvents = "none";
		    bloqueioStatus = true;

		    try {
		        const res = await fetch("/aljava/colocar", {
		            method: "POST",
		            headers: { "Content-Type": "application/json" },
		            body: JSON.stringify({ usuarioId, tipo, quantidade })
		        });

		        const data = await res.json();
		        if (!res.ok) throw new Error(data.error);
             
			
				
				
				
		        estoque[tipo] -= quantidade;

		        aljavaCount.textContent = data.aljava;
		        tipoFlechaAtiva.textContent = data.tipoFlecha || "-";

				if(quantidade <=1){
					
			    Swal.fire({
				customClass: { title: 'swal-game-text' },
				//icon: 'success',
				title: `Colocadas ${quantidade} flecha de ${tipo} na aljava!`,
				imageUrl: aljavaImg,
			    imageWidth: 90,
				imageHeight: 120,
				text: 'Colocando na Aljava!',
				timer: 5000,
				showConfirmButton: false,
			    background: 'transparent',
				color: '#ffb400'
				});

				}else{
												
				Swal.fire({
				customClass: { title: 'swal-game-text' },
				//icon: 'success',
				title: `Colocadas ${quantidade} flechas de ${tipo} na aljava!`,
				imageUrl: aljavaImg,
				imageWidth: 90,
				imageHeight: 120,
				text: 'Colocando na Aljava!',
				// title: 'Colocando na Aljava!',
			    //text: `Colocadas ${quantidade} flechas de ${tipo} na aljava!`,
				timer: 5000,
				showConfirmButton: false,
				background: 'transparent',
				color: '#ffb400'
				
				});
		}

		        setTimeout(atualizarStatus, 700);

		    } catch (err) {
				
				
				Swal.fire({
								                   customClass: { title: 'swal-game-error' },
								                   //icon: 'error',
												   imageUrl: aljavaErroImg,
												   imageWidth: 90,
												   imageHeight: 120,
								                   title: 'Não foi possível colocar as flechas.',
												   text: 'Verifique se há outra arma equipada, flechas incompatíveis ou se há Arco equipado.',
												//text: 'Não foi possível colocar as flechas. Verifique se há outra arma equipada ou flechas incompatíveis.',
								                   timer: 5000,
								                   showConfirmButton: false,
								                   background: 'transparent',
								                   color: '#ff3b3b'
								               });
											   
							 
				            
		    } finally {
		        btn.dataset.loading = "false";
		        btn.style.pointerEvents = "auto";

		        setTimeout(() => {
		            bloqueioStatus = false;
		        }, 500);
		    }
		});

		/*
		btn.addEventListener("click", async () => {
			
		    if (btn.dataset.loading === "true") return;
		  
		    btn.disabled = true;

            const quantidade = parseInt(input.value, 10);

            if (!quantidade || quantidade <= 0) {
                Swal.fire({
                    icon: 'error',
                    title: 'Quantidade inválida!',
                    timer: 3000,
                    showConfirmButton: false
                });
                return;
            }

            if (quantidade > estoque[tipo]) {
                Swal.fire({
                    icon: 'error',
                    title: `Não há flechas de ${tipo} suficientes!`,
                    timer: 3000,
                    showConfirmButton: false
                });
                return;
            }
			
			// 🔒 trava SOMENTE depois que passou nas validações
			  btn.dataset.loading = "true";
			  btn.disabled = true;

            try {
                const res = await fetch("/aljava/colocar", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ usuarioId, tipo, quantidade })
                });
                const data = await res.json();
                if (!res.ok) throw new Error(data.error || "Erro ao colocar flechas");

                aljavaCount.textContent = data.aljava;
                tipoFlechaAtiva.textContent = data.tipoFlecha || "-";

				if(quantidade <=1){
								Swal.fire({
							    customClass: { title: 'swal-game-text' },
								icon: 'success',
								title: `Colocadas ${quantidade} flecha de ${tipo} na aljava!`,
								text: 'Colocando na Aljava!',
								// title: 'Colocando na Aljava!',
							    //text: `Colocadas ${quantidade} flechas de ${tipo} na aljava!`,
							    timer: 5000,
								showConfirmButton: false,
								background: 'transparent',
								color: '#ffb400'
						    });

								
							}else{
								
								Swal.fire({
				                customClass: { title: 'swal-game-text' },
				                icon: 'success',
								title: `Colocadas ${quantidade} flechas de ${tipo} na aljava!`,
								text: 'Colocando na Aljava!',
				               // title: 'Colocando na Aljava!',
				                //text: `Colocadas ${quantidade} flechas de ${tipo} na aljava!`,
				                timer: 5000,
				                showConfirmButton: false,
				                background: 'transparent',
				                color: '#ffb400'
				            });
							
							
							
				        }
						
						
                       //atualizarStatus();
					   aljavaCount.textContent = data.aljava;
					   tipoFlechaAtiva.textContent = data.tipoFlecha || "-";

               
            } catch (err) {
				Swal.fire({
				                   customClass: { title: 'swal-game-error' },
				                   icon: 'error',
				                   title: 'Não foi possível colocar as flechas.',
								   text: 'Verifique se há outra arma equipada, flechas incompatíveis ou se há Arco equipado.',
								//text: 'Não foi possível colocar as flechas. Verifique se há outra arma equipada ou flechas incompatíveis.',
				                   timer: 5000,
				                   showConfirmButton: false,
				                   background: 'transparent',
				                   color: '#ff3b3b'
				               });
							   
			 
            }
			finally {
			    btn.dataset.loading = "false";
			    btn.disabled = false;
			}

        });
		*/
    });

    // Atualiza status do backend e inputs, sem mexer na visibilidade dos botões
	async function atualizarStatus() {
	    try {
	        const res = await fetch(`/api/atualizar/status/usuario/${usuarioId}`);
	        const data = await res.json();

	        arcoAtivoElem.textContent = data.arcoAtivo;
	        aljavaCount.textContent = data.aljava;
	        tipoFlechaAtiva.textContent = data.aljava > 0 ? data.tipoFlecha : "-";

			const espadaAtiva = data.ativaEspadaFlanejante ?? 0;
			const machadoAtivo = data.ativarMachadoDilacerador ?? 0;
	        // Atualiza estoque
	        estoque.FERRO = data.flechaFerro || 0;
	        estoque.FOGO = data.flechaFogo || 0;
	        estoque.VENENO = data.flechaVeneno || 0;
	        estoque.DIAMANTE = data.flechaDiamante || 0;

			const tipoEquipado = data.tipoFlecha || null;
			Object.entries(btnFlechas).forEach(([tipo, btn]) => {
			    const container = btn.nextElementSibling;
			    if (!container || !container.classList.contains("flecha-control")) return;

			    const input = container.querySelector("input");
			    const maxLabel = container.querySelector("span");

			    const quantidade = estoque[tipo];

			    const durabilidade = data.durabilidadeArco ?? 0;
			    const arcoQuebrado = durabilidade <= 0;

			    const temAramasAtivas = espadaAtiva > 0 || machadoAtivo > 0;

			    // 🔄 RESET
			    btn.disabled = false;
			    input.disabled = false;
			    btn.classList.remove("flecha-ativa", "flecha-inativa", "hidden");
			    container.classList.remove("hidden");

			    // 🔒 Armas ativas → esconder tudo
			    if (temAramasAtivas) {
			        btn.classList.add("hidden");
			        container.classList.add("hidden");
			        btn.disabled = true;
			        input.disabled = true;
			        return;
			    }

			    // ❌ Sem estoque → esconder
			    if (quantidade <= 0) {
			        btn.classList.add("hidden");
			        container.classList.add("hidden");
			        btn.disabled = true;
			        input.disabled = true;
			        return;
			    }

			    // ✅ Tem estoque → mostrar
			    input.max = quantidade;
			    input.value = quantidade;
			    maxLabel.textContent = ` / ${quantidade} disponíveis`;

			    // 🔒 Arco quebrado → INATIVO mas visível
			    if (arcoQuebrado && data.aljava > 0) {
			        btn.disabled = true;
			        input.disabled = true;

			        btn.classList.add("flecha-inativa");
			        btn.classList.remove("flecha-ativa");

			      
			        return;
			    }

			    // ✅ Arco OK → comportamento normal
			    if (tipoEquipado) {
			        const ativo = tipo === tipoEquipado;
			        btn.disabled = !ativo;
			        btn.classList.toggle("flecha-ativa", ativo);
			        btn.classList.toggle("flecha-inativa", !ativo);
			    } else {
			        btn.disabled = false;
			        btn.classList.remove("flecha-ativa", "flecha-inativa");
			    }
			});

				
			
	    } catch (err) {
	        console.error("Erro ao atualizar status:", err);
	    }
	}


  //  atualizarStatus();
    setInterval(atualizarStatus, 5000);
});

