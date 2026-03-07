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

    }, 40000); // 40 segundos

  }

}
/*
function fecharModal() {

  const modal = document.getElementById("bonusModal");

  if (!modal) return;

  modal.classList.remove('show');
  modal.setAttribute('aria-hidden', 'true');

  clearInterval(contadorIntervalo);

}
*/

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
        icon: 'error',
        title: 'Erro',
        text: 'Erro ao verificar bônus'
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
          icon: 'error',
          title: 'Erro',
          text: 'Falha ao coletar bônus'
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