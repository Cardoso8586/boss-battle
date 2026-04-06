

let modalPremioAtaqueEspecialAberto = false;
let recebendoPremioAtaqueEspecial = false;

function formatarMoedaBR(valor) {
  return Number(valor || 0).toLocaleString("pt-BR", {
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
  });
}

function getUsuarioLogadoId() {
  const meta = document.querySelector('meta[name="user-id"]');
  const id = meta ? parseInt(meta.getAttribute("content"), 10) : null;
  console.log("Usuário logado ID:", id);
  return id;
}

function atualizarStatusPremio(texto, tipo = "") {
  const status = document.getElementById("statusPremioAtaqueEspecial");
  if (!status) return;

  status.textContent = texto || "";
  status.className = "status-premio";

  if (tipo) {
    status.classList.add(tipo);
  }
}

function abrirModalPremioAtaqueEspecial(valor) {
  const valorNumerico = Number(valor || 0);
  console.log("Tentando abrir modal com valor:", valorNumerico);

  const modal = document.getElementById("modalPremioAtaqueEspecial");
  const valorElemento = document.getElementById("valorPremioAtaqueEspecial");
  const mensagemPremio = document.getElementById("textoPremioAtaqueEspecial");
  const btnReceber = document.getElementById("btnReceberPremioAtaqueEspecial");

  if (!modal) {
    console.error("Elemento #modalPremioAtaqueEspecial não encontrado.");
    return;
  }

  if (!valorElemento) {
    console.error("Elemento #valorPremioAtaqueEspecial não encontrado.");
    return;
  }

  if (valorNumerico <= 0) {
    console.warn("Valor do prêmio <= 0. Modal não será aberto.");
    return;
  }

  valorElemento.textContent = formatarMoedaBR(valorNumerico);

  if (mensagemPremio) {
    if (valorNumerico === 50000) {
      mensagemPremio.textContent = "🥇 Parabéns! Você ficou em 1º lugar!";
    } else if (valorNumerico === 30000) {
      mensagemPremio.textContent = "🥈 Excelente! Você ficou em 2º lugar!";
    } else if (valorNumerico === 20000) {
      mensagemPremio.textContent = "🥉 Muito bom! Você ficou em 3º lugar!";
    } else if (valorNumerico === 10000) {
      mensagemPremio.textContent = "🏅 Ótimo! Você ficou em 4º lugar!";
    } else if (valorNumerico === 5000) {
      mensagemPremio.textContent = "🎖️ Bom! Você ficou em 5º lugar!";
    } else {
      mensagemPremio.textContent = "🎁 Você recebeu um prêmio especial!";
    }
  }

  atualizarStatusPremio("");

  if (btnReceber) {
    btnReceber.disabled = false;
    btnReceber.innerHTML = '<i class="fa-solid fa-coins"></i> Receber';
    btnReceber.style.display = "inline-flex";
  }

  modal.classList.add("ativo");
  modalPremioAtaqueEspecialAberto = true;

  console.log("Modal aberto.");
}

function fecharModalPremioAtaqueEspecial() {
  const modal = document.getElementById("modalPremioAtaqueEspecial");
  if (modal) {
    modal.classList.remove("ativo");
  }

  modalPremioAtaqueEspecialAberto = false;
  recebendoPremioAtaqueEspecial = false;
  atualizarStatusPremio("");

  console.log("Modal fechado.");
}

async function verificarPremioPendenteAtaqueEspecial() {
  try {
    const usuarioId = getUsuarioLogadoId();

    if (!usuarioId) {
      console.error("Usuário não identificado.");
      return;
    }

    if (recebendoPremioAtaqueEspecial) {
      return;
    }

    const url = `/api/usuario/${usuarioId}/premio-pendente-ataque-especial`;
    const response = await fetch(url, { method: "GET" });

    if (!response.ok) {
      console.error("Erro ao consultar prêmio pendente.");
      return;
    }

    const texto = await response.text();
    const valorPremio = Number(texto);

    if (isNaN(valorPremio)) {
      console.error("A API não retornou um número válido.");
      return;
    }

    if (valorPremio > 0 && !modalPremioAtaqueEspecialAberto) {
      abrirModalPremioAtaqueEspecial(valorPremio);
    }

    if (valorPremio <= 0 && modalPremioAtaqueEspecialAberto && !recebendoPremioAtaqueEspecial) {
      fecharModalPremioAtaqueEspecial();
    }

  } catch (error) {
    console.error("Erro ao verificar prêmio pendente:", error);
  }
}

document.addEventListener("DOMContentLoaded", function () {
  console.log("DOM carregado.");

  const btnReceber = document.getElementById("btnReceberPremioAtaqueEspecial");
  const btnFechar = document.getElementById("btnFecharModalPremio");

  if (btnFechar) {
    btnFechar.addEventListener("click", fecharModalPremioAtaqueEspecial);
  }

  if (btnReceber) {
    btnReceber.addEventListener("click", async function () {
      if (recebendoPremioAtaqueEspecial) {
        return;
      }

      const usuarioId = getUsuarioLogadoId();

      if (!usuarioId) {
        atualizarStatusPremio("Usuário não identificado.", "erro");
        return;
      }

      recebendoPremioAtaqueEspecial = true;
      this.disabled = true;
      this.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Recebendo...';

      atualizarStatusPremio("Processando seu prêmio...", "carregando");

      try {
        const response = await fetch(`/api/usuario/${usuarioId}/confirmar-premio-ataque-especial`, {
          method: "POST"
        });

        const mensagem = await response.text();

        if (!response.ok) {
          throw new Error(mensagem || "Erro ao confirmar prêmio.");
        }

        atualizarStatusPremio(mensagem || "Prêmio recebido com sucesso!", "sucesso");

        const valorElemento = document.getElementById("valorPremioAtaqueEspecial");
        if (valorElemento) {
          valorElemento.textContent = "0";
        }

        this.style.display = "none";

        setTimeout(() => {
          fecharModalPremioAtaqueEspecial();
        }, 2500);

      } catch (error) {
        console.error("Erro ao receber prêmio:", error);
        atualizarStatusPremio(error.message || "Erro ao receber prêmio.", "erro");

        this.disabled = false;
        this.innerHTML = '<i class="fa-solid fa-coins"></i> Receber';
      } finally {
        recebendoPremioAtaqueEspecial = false;
      }
    });
  }

  verificarPremioPendenteAtaqueEspecial();

  setInterval(() => {
    verificarPremioPendenteAtaqueEspecial();
  }, 5000);
});

/*
let modalPremioAtaqueEspecialAberto = false;

function formatarMoedaBR(valor) {
  return Number(valor || 0).toLocaleString("pt-BR", {
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
  });
}

function getUsuarioLogadoId() {
  const meta = document.querySelector('meta[name="user-id"]');
  const id = meta ? parseInt(meta.getAttribute("content"), 10) : null;
  console.log("Usuário logado ID:", id);
  return id;
}

function abrirModalPremioAtaqueEspecial(valor) {
  const valorNumerico = Number(valor || 0);
  console.log("Tentando abrir modal com valor:", valorNumerico);

  const modal = document.getElementById("modalPremioAtaqueEspecial");
  const valorElemento = document.getElementById("valorPremioAtaqueEspecial");
  const mensagemPremio = document.getElementById("textoPremioAtaqueEspecial");

  if (!modal) {
    console.error("Elemento #modalPremioAtaqueEspecial não encontrado.");
    return;
  }

  if (!valorElemento) {
    console.error("Elemento #valorPremioAtaqueEspecial não encontrado.");
    return;
  }

  if (valorNumerico <= 0) {
    console.warn("Valor do prêmio <= 0. Modal não será aberto.");
    return;
  }

  valorElemento.textContent = formatarMoedaBR(valorNumerico);

  // 👇 FRASES PERSONALIZADAS
  if (mensagemPremio) {
    if (valorNumerico === 50000) {
      mensagemPremio.textContent = "🥇 Parabéns! Você ficou em 1º lugar!";
    } else if (valorNumerico === 30000) {
      mensagemPremio.textContent = "🥈 Excelente! Você ficou em 2º lugar!";
    } else if (valorNumerico === 20000) {
      mensagemPremio.textContent = "🥉 Muito bom! Você ficou em 3º lugar!";
    } else if (valorNumerico === 10000) {
      mensagemPremio.textContent = "🏅 Ótimo! Você ficou em 4º lugar!";
    } else if (valorNumerico === 5000) {
      mensagemPremio.textContent = "🎖️ Bom! Você ficou em 5º lugar!";
    } else {
      mensagemPremio.textContent = "🎁 Você recebeu um prêmio especial!";
    }
  }

  modal.classList.add("ativo");
  modalPremioAtaqueEspecialAberto = true;

  console.log("Modal aberto.");
}

/*
function abrirModalPremioAtaqueEspecial(valor) {
  const valorNumerico = Number(valor || 0);
  console.log("Tentando abrir modal com valor:", valorNumerico);

  const modal = document.getElementById("modalPremioAtaqueEspecial");
  const valorElemento = document.getElementById("valorPremioAtaqueEspecial");

  if (!modal) {
    console.error("Elemento #modalPremioAtaqueEspecial não encontrado.");
    return;
  }

  if (!valorElemento) {
    console.error("Elemento #valorPremioAtaqueEspecial não encontrado.");
    return;
  }

  if (valorNumerico <= 0) {
    console.warn("Valor do prêmio <= 0. Modal não será aberto.");
    return;
  }

  valorElemento.textContent = formatarMoedaBR(valorNumerico);
  modal.classList.add("ativo");
  modalPremioAtaqueEspecialAberto = true;

  console.log("Modal aberto.");
}
*/

/*
function fecharModalPremioAtaqueEspecial() {
  const modal = document.getElementById("modalPremioAtaqueEspecial");
  if (modal) {
    modal.classList.remove("ativo");
  }
  modalPremioAtaqueEspecialAberto = false;
  console.log("Modal fechado.");
}

async function verificarPremioPendenteAtaqueEspecial() {
  try {
    const usuarioId = getUsuarioLogadoId();

    if (!usuarioId) {
      console.error("Usuário não identificado.");
      return;
    }

    const url = `/api/usuario/${usuarioId}/premio-pendente-ataque-especial`;
    console.log("Consultando prêmio pendente em:", url);

    const response = await fetch(url, { method: "GET" });

    console.log("Status da API:", response.status);

    if (!response.ok) {
      console.error("Erro ao consultar prêmio pendente.");
      return;
    }

    const texto = await response.text();
    console.log("Resposta bruta da API:", texto);
	
	

    const valorPremio = Number(texto);

    if (isNaN(valorPremio)) {
      console.error("A API não retornou um número válido.");
      return;
    }

    if (valorPremio > 0 && !modalPremioAtaqueEspecialAberto) {
      abrirModalPremioAtaqueEspecial(valorPremio);
    } else {
      console.log("Nenhum prêmio disponível para abrir modal.");
    }
  } catch (error) {
    console.error("Erro ao verificar prêmio pendente:", error);
  }
}

document.addEventListener("DOMContentLoaded", function () {
  console.log("DOM carregado.");

  const btnReceber = document.getElementById("btnReceberPremioAtaqueEspecial");
  const btnFechar = document.getElementById("btnFecharModalPremio");

  if (btnFechar) {
    btnFechar.addEventListener("click", fecharModalPremioAtaqueEspecial);
  }

  if (btnReceber) {
    btnReceber.addEventListener("click", async function () {
      const usuarioId = getUsuarioLogadoId();

      if (!usuarioId) {
        alert("Usuário não identificado.");
        return;
      }

      this.disabled = true;
      this.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Recebendo...';

      try {
        const response = await fetch(`/api/usuario/${usuarioId}/confirmar-premio-ataque-especial`, {
          method: "POST"
        });

        const mensagem = await response.text();
        //console.log("Resposta confirmar prêmio:", mensagem);
		
		Swal.fire({
					customClass: {
					      title: 'swal-game-text'
					    },
				  title: mensagem,
				  html: `
				  		      <div class="modal-anuncio">
				  		        <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
				  		          width="468"
				  		          height="60"
				  		          scrolling="no"
				  		          frameborder="0">
				  		        </iframe>
				  		      </div>
				  		    `,
				  timer: 7000,
				  showConfirmButton: false,
				  background: 'transparent',
				  color: '#ffb400'
				
				});


        if (!response.ok) {
          throw new Error(mensagem || "Erro ao confirmar prêmio.");
        }

        fecharModalPremioAtaqueEspecial();
       // alert(mensagem || "Prêmio recebido com sucesso!");
		
		Swal.fire({
							customClass: {
							      title: 'swal-game-text'
							    },
						  title: mensagem,
						  html: `
						  		      <div class="modal-anuncio">
						  		        <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
						  		          width="468"
						  		          height="60"
						  		          scrolling="no"
						  		          frameborder="0">
						  		        </iframe>
						  		      </div>
						  		    `,
						  timer: 10000,
						  showConfirmButton: false,
						  background: 'transparent',
						  color: '#ffb400'
						
						});

      } catch (error) {
        console.error("Erro ao receber prêmio:", error);
       // alert(error.message || "Erro ao receber prêmio.");
		

				Swal.fire({
				stomClass: {title: 'swal-game-error'},
				icon: 'error',
				title: 'Erro',
				text: error.message,
				html: `
				    <div class="modal-anuncio">
				      <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
				        width="468"
				        height="60"
				        scrolling="no"
				        frameborder="0">
				      </iframe>
				    </div>
				  `,
				confirmButtonText: 'Ok',
				background: 'transparent',
				color: '#ff3b3b '      
		});
      } finally {
        this.disabled = false;
        this.innerHTML = '<i class="fa-solid fa-coins"></i> Receber';
      }
    });
  } else {
   // console.error("Botão de receber prêmio não encontrado.");
	
	Swal.fire({
					stomClass: {title: 'swal-game-error'},
					icon: 'error',
					title: 'Erro',
					text: error,
					html: `
					    <div class="modal-anuncio">
					      <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
					        width="468"
					        height="60"
					        scrolling="no"
					        frameborder="0">
					      </iframe>
					    </div>
					  `,
					confirmButtonText: 'Ok',
					background: 'transparent',
					color: '#ff3b3b '      
			});

  }

  verificarPremioPendenteAtaqueEspecial();

  setInterval(() => {
    verificarPremioPendenteAtaqueEspecial();
  }, 5000);
});

*/