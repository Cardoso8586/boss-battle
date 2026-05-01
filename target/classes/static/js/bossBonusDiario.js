
// =====================================
// PEGAR USUÁRIO LOGADO
// =====================================
function getUsuarioLogadoId() {
  const meta = document.querySelector('meta[name="user-id"]');
  return meta ? parseInt(meta.getAttribute("content")) : null;
}

const TEMPO_DELAY = 45 * 1000;

let contadorIntervaloBonus;
let bonusColetado = false;

// =====================================
// ABRIR MODAL BÔNUS
// =====================================
function abrirModalBonus() {

  const modal = document.getElementById("bonusModal");

  if (!modal) {
    Swal.fire({
      customClass: { title: 'swal-game-error' },
      icon: 'error',
      title: 'Erro',
      text: 'Modal de bônus não encontrado',
      background: 'transparent',
      color: '#ff3b3b'
    });
    return;
  }

  modal.classList.add("show");
  modal.setAttribute("aria-hidden", "false");

  // trava o scroll do fundo
  document.body.classList.add("modal-open");

  iniciarContadorBonus(60);
}

// =====================================
// FECHAR MODAL BÔNUS
// =====================================
function fecharModalBonus() {

  const modal = document.getElementById("bonusModal");

  if (!modal) return;

  // 🔥 remove classe (controle principal)
  modal.classList.remove("show");
  modal.setAttribute("aria-hidden", "true");

  // 🔥 libera scroll
  document.body.classList.remove("modal-open");

  clearInterval(contadorIntervaloBonus);

  // 🔥 reabrir depois de 8s se não coletou
  if (!bonusColetado) {

    setTimeout(() => {

      const usuarioId = getUsuarioLogadoId();

      if (usuarioId && !algumModalAberto()) {
        verificarBonus(usuarioId);
      }

    }, 8000);
  }
}

// =====================================
// CONTADOR
// =====================================
function iniciarContadorBonus(segundos) {

  clearInterval(contadorIntervaloBonus);

  let tempoRestante = segundos;

  const spanTempo = document.getElementById("tempo");

  if (spanTempo) {
    spanTempo.innerText = tempoRestante;
  }

  contadorIntervaloBonus = setInterval(() => {

    tempoRestante--;

    if (spanTempo) {
      spanTempo.innerText = tempoRestante;
    }

    if (tempoRestante <= 0) {
      clearInterval(contadorIntervaloBonus);
      fecharModalBonus();
    }

  }, 1000);
}

// =====================================
// CHECAR SE EXISTE MODAL ABERTO
// =====================================
function algumModalAberto() {
  return document.querySelectorAll(".modal.show").length > 0;
}

// =====================================
// VERIFICAR BÔNUS NO BACKEND
// =====================================
function verificarBonus(usuarioId) {

  if (!usuarioId) return;

  fetch(`/api/bonus/verificar/${usuarioId}`)
    .then(res => {

      if (!res.ok) {
        throw new Error("Erro HTTP");
      }

      return res.json();
    })
    .then(data => {

      if (data.disponivel) {

        bonusColetado = false;

        const mensagemFormatada =
          data.mensagem.replace(/(\d+)/g, '<span class="numero-destaque">$1</span>');

        const msg = document.getElementById("bonusMensagem");

        if (msg) {
          msg.innerHTML = mensagemFormatada;
        }

        const btnBonus = document.getElementById("btnColetarBonus");

        if (btnBonus) {
          btnBonus.disabled = false;
          btnBonus.innerText = "⚔️ Coletar Recompensa";
        }

        abrirModalBonus();
      }
    })
    .catch(() => {

      Swal.fire({
        customClass: { title: 'swal-game-error' },
        icon: 'error',
        title: 'Erro',
        text: 'Erro ao verificar bônus',
        background: 'transparent',
        color: '#ff3b3b'
      });

    });
}

// =====================================
// CONTROLAR DELAY
// =====================================
function verificarModalComDelay(usuarioId) {

  const agora = Date.now();

  let proximoBonus = localStorage.getItem("proximoBonus");

  if (!proximoBonus) {
    proximoBonus = agora + TEMPO_DELAY;
    localStorage.setItem("proximoBonus", proximoBonus);
  }

  const tempoRestante = proximoBonus - agora;

  if (tempoRestante <= 0) {

    if (!algumModalAberto()) {
      verificarBonus(usuarioId);
      localStorage.removeItem("proximoBonus");
    } else {
      setTimeout(() => verificarModalComDelay(usuarioId), 5000);
    }

  } else {
    setTimeout(() => verificarModalComDelay(usuarioId), tempoRestante);
  }
}

// =====================================
// COLETAR BÔNUS
// =====================================
function configurarBotaoBonus(usuarioId) {

  const btnBonus = document.getElementById("btnColetarBonus");

  if (!btnBonus) return;

  btnBonus.addEventListener("click", function () {

    const btn = this;

    btn.disabled = true;
    btn.innerText = "Processando...";

    fetch(`/api/bonus/coletar/${usuarioId}`, {
      method: "POST"
    })
      .then(res => {

        if (!res.ok) {
          throw new Error("Erro HTTP");
        }

        return res.json();
      })
      .then(data => {

        bonusColetado = true;

        const mensagemFormatada =
          data.mensagem.replace(/(\d+)/g, '<span class="numero-destaque">$1</span>');

        const msg = document.getElementById("bonusMensagem");

        if (msg) {
          msg.innerHTML = mensagemFormatada;
        }

        btn.innerText = "Bônus Coletado";
        btn.disabled = true;

        setTimeout(() => {
          fecharModalBonus();
        }, 6000);

      })
      .catch(() => {

        const msg = document.getElementById("bonusMensagem");

        if (msg) {
          msg.innerText = "❌ Erro ao coletar bônus, tente novamente.";
        }

        btn.disabled = false;
        btn.innerText = "⚔️ Coletar Recompensa";

        Swal.fire({
          customClass: { title: 'swal-game-error' },
          icon: 'error',
          title: 'Erro',
          text: 'Falha ao coletar bônus',
          background: 'transparent',
          color: '#ff3b3b'
        });

      });
  });
}

// =====================================
// CONFIGURAR FECHAMENTO DO MODAL BÔNUS
// =====================================
function configurarFechamentoModalBonus() {

  const closeBtn = document.querySelector("#bonusModal .close");

  if (closeBtn) {
    closeBtn.onclick = function () {
      fecharModalBonus();
    };
  }

  window.addEventListener("click", function (event) {

    const modal = document.getElementById("bonusModal");

    if (event.target === modal) {
      fecharModalBonus();
    }

  });
}

// =====================================
// INICIAR SISTEMA
// =====================================
window.addEventListener("DOMContentLoaded", () => {

  const usuarioId = getUsuarioLogadoId();

  if (!usuarioId) {

    Swal.fire({
      customClass: { title: 'swal-game-error' },
      icon: 'error',
      title: 'Erro',
      text: 'Usuário não identificado',
      background: 'transparent',
      color: '#ff3b3b'
    });

    return;
  }

  configurarBotaoBonus(usuarioId);
  configurarFechamentoModalBonus();
  verificarModalComDelay(usuarioId);
});

/*
// =====================================
// PEGAR USUÁRIO LOGADO
// =====================================

function getUsuarioLogadoId() {

  const meta = document.querySelector('meta[name="user-id"]');

  return meta ? parseInt(meta.getAttribute("content")) : null;

}

const TEMPO_DELAY = 45 * 1000; // 1 minuto

let contadorIntervalo;

let bonusColetado = false;

// =====================================
// ABRIR MODAL
// =====================================

function abrirModalBonus() {

  const modal = document.getElementById("bonusModal");

  if (!modal) {

    Swal.fire({
	  customClass: {title: 'swal-game-error'},
      icon: 'error',
      title: 'Erro',
      text: 'Modal de bônus não encontrado',
	  background: 'transparent',
	  color: '#ff3b3b'
    });

    return;

  }

  modal.classList.add('show');
  modal.setAttribute('aria-hidden', 'false');

  iniciarContador(60);

}


// =====================================
// FECHAR MODAL
// =====================================
function fecharModal() {

  const modal = document.getElementById("bonusModal");

  if (!modal) return;

  modal.classList.remove('show');
  modal.setAttribute('aria-hidden', 'true');

  clearInterval(contadorIntervalo);

  // Se o bônus NÃO foi coletado, tentar abrir novamente depois
  if (!bonusColetado) {

    setTimeout(() => {

      const usuarioId = getUsuarioLogadoId();

      if (usuarioId && !algumModalAberto()) {
        verificarBonus(usuarioId);
      }

    }, 8000); // 8 segundos

  }

}


// =====================================
// CONTADOR
// =====================================

function iniciarContador(segundos) {

  clearInterval(contadorIntervalo);

  let tempoRestante = segundos;

  const spanTempo = document.getElementById("tempo");

  if (spanTempo) {
    spanTempo.innerText = tempoRestante;
  }

  contadorIntervalo = setInterval(() => {

    tempoRestante--;

    if (spanTempo) {
      spanTempo.innerText = tempoRestante;
    }

    if (tempoRestante <= 0) {

      clearInterval(contadorIntervalo);

      fecharModal();

    }

  }, 1000);

}


// =====================================
// CHECAR SE EXISTE MODAL ABERTO
// =====================================

function algumModalAberto() {

  return document.querySelectorAll('.modal.show').length > 0;

}


// =====================================
// VERIFICAR BONUS NO BACKEND
// =====================================

function verificarBonus(usuarioId) {

  if (!usuarioId) return;

  fetch(`/api/bonus/verificar/${usuarioId}`)

    .then(res => {

      if (!res.ok) {
        throw new Error("Erro HTTP");
      }

      return res.json();

    })

    .then(data => {

      if (data.disponivel) {

        const mensagemFormatada =
          data.mensagem.replace(/(\d+)/g, '<span class="numero-destaque">$1</span>');

        const msg = document.getElementById("bonusMensagem");

        if (msg) {
          msg.innerHTML = mensagemFormatada;
        }

        abrirModalBonus();

      }

    })

    .catch(() => {
	  
      Swal.fire({
		customClass: {title: 'swal-game-error'},
        icon: 'error',
        title: 'Erro',
        text: 'Erro ao verificar bônus',
		background: 'transparent',
	    color: '#ff3b3b'
      });

    });

}


// =====================================
// CONTROLAR DELAY
// =====================================

function verificarModalComDelay(usuarioId) {

  const agora = Date.now();

  let proximoBonus = localStorage.getItem("proximoBonus");

  if (!proximoBonus) {

    proximoBonus = agora + TEMPO_DELAY;

    localStorage.setItem("proximoBonus", proximoBonus);

  }

  const tempoRestante = proximoBonus - agora;

  if (tempoRestante <= 0) {

    if (!algumModalAberto()) {

      verificarBonus(usuarioId);

      localStorage.removeItem("proximoBonus");

    } else {

      setTimeout(() => verificarModalComDelay(usuarioId), 5000);

    }

  } else {

    setTimeout(() => verificarModalComDelay(usuarioId), tempoRestante);

  }

}


// =====================================
// COLETAR BONUS
// =====================================

function configurarBotaoBonus(usuarioId) {

  const btnBonus = document.getElementById("btnColetarBonus");

  if (!btnBonus) return;

  btnBonus.addEventListener("click", function () {

    const btn = this;

    btn.disabled = true;

    fetch(`/api/bonus/coletar/${usuarioId}`, {
      method: 'POST'
    })

      .then(res => {

        if (!res.ok) {
          throw new Error("Erro HTTP");
        }

        return res.json();

      })

      .then(data => {
		bonusColetado = true;
		
        const mensagemFormatada =
          data.mensagem.replace(/(\d+)/g, '<span class="numero-destaque">$1</span>');

        const msg = document.getElementById("bonusMensagem");

        if (msg) {
          msg.innerHTML = mensagemFormatada;
        }

        btn.innerText = "Bônus Coletado";
        btn.disabled = true;

        setTimeout(() => fecharModal(), 6000);

      })

      .catch(() => {

        const msg = document.getElementById("bonusMensagem");

        if (msg) {
          msg.innerText = "❌ Erro ao coletar bônus, tente novamente.";
        }

        btn.disabled = false;
        btn.innerText = "Coletar Bônus";

        Swal.fire({
		 customClass: {title: 'swal-game-error'},
          icon: 'error',
          title: 'Erro',
          text: 'Falha ao coletar bônus',
		  background: 'transparent',
		  color: '#ff3b3b'
        });

      });

  });

}


// =====================================
// CONFIGURAR FECHAR MODAL
// =====================================

function configurarFechamentoModal() {

  const closeBtn = document.querySelector(".close");

  if (closeBtn) {

    closeBtn.onclick = function () {

      fecharModal();

    };

  }

  window.onclick = function (event) {

    const modal = document.getElementById("bonusModal");

    if (event.target === modal) {

      fecharModal();

    }

  };

}


// =====================================
// INICIAR SISTEMA
// =====================================

window.addEventListener('DOMContentLoaded', () => {

  const usuarioId = getUsuarioLogadoId();

  if (!usuarioId) {

    Swal.fire({
	  customClass: {title: 'swal-game-error'},
      icon: 'error',
      title: 'Erro',
      text: 'Usuário não identificado',
	  background: 'transparent',
	  color: '#ff3b3b'
    });

    return;

  }

  configurarBotaoBonus(usuarioId);

  configurarFechamentoModal();

  verificarModalComDelay(usuarioId);

});

*/