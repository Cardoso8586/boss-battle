let rankingInterval = null;

/**
 * ================================
 * FUNÇÃO PRINCIPAL DE CARREGAMENTO
 * ================================
 */
function carregarRanking() {
  console.log("🚀 Carregando ranking...");

  const lista = document.getElementById("rankingLista");

  if (!lista) {
    console.error("❌ Elemento #rankingLista NÃO encontrado no HTML");
    return;
  }

  fetch("/api/ranking-ataques-especial")
    .then(res => {
      console.log("📡 Status resposta ranking:", res.status);

      if (!res.ok) {
        throw new Error(`Erro HTTP: ${res.status}`);
      }

      return res.json();
    })
    .then(data => {
      console.log("📊 Dados recebidos do ranking:", data);

      const ranking = Array.isArray(data) ? data : (data.content || []);

      if (!ranking.length) {
        lista.innerHTML = `
          <div class="ranking-item">
            <span>-</span>
            <span>Sem dados no ranking</span>
            <span>-</span>
          </div>
        `;
        return;
      }

      preencherRankingLista(ranking);
    })
    .catch(err => {
      console.error("🔥 Erro ao carregar ranking:", err);

      lista.innerHTML = `
        <div class="ranking-item">
          <span>-</span>
          <span>Erro ao carregar ranking</span>
          <span>-</span>
        </div>
      `;
    });
}

/**
 * ================================
 * MONTA A LISTA DO RANKING
 * ================================
 */
function preencherRankingLista(dadosRanking) {
  const lista = document.getElementById("rankingLista");
  if (!lista) return;

  lista.innerHTML = "";

  // Filtra somente quem tem mais de 0 ataques
  const rankingFiltrado = dadosRanking.filter(item => {
    const ataques = item.quantidadeAtaquesSemanal ?? item.ataques ?? item.totalAtaques ?? 0;
    return ataques > 0;
  });

  // Se depois do filtro não sobrar ninguém
  if (rankingFiltrado.length === 0) {
    lista.innerHTML = `
      <div class="ranking-item">
        <span>-</span>
        <span>Nenhum jogador com pontuação nesta semana</span>
        <span>-</span>
      </div>
    `;
    return;
  }

  // Se quiser garantir top 10 no front
  const top10 = rankingFiltrado.slice(0, 10);

  top10.forEach((item, index) => {
    const div = document.createElement("div");
    div.className = "ranking-item";

    const id = item.id ?? "?";
    const username = item.username || item.nome || item.usuarioNome || "Usuário";
    const ataques = item.quantidadeAtaquesSemanal ?? item.ataques ?? item.totalAtaques ?? 0;

    div.innerHTML = `
      <span class="posicao">${index + 1}°</span>
      <span class="nome">${username}</span>
      <span class="id">ID-${id}</span>
      <span class="views">${ataques}</span>
    `;

    lista.appendChild(div);
  });
}

/**
 * ================================
 * POSIÇÃO DO USUÁRIO
 * ================================
 */
function carregarMinhaPosicao() {
  const div = document.getElementById("posicaoUsuario");
  if (!div) {
    console.error("❌ Elemento #posicaoUsuario não encontrado.");
    return;
  }

  const meta = document.querySelector('meta[name="user-id"]');
  const usuarioId = meta ? parseInt(meta.content, 10) : null;

  if (!usuarioId || isNaN(usuarioId)) {
    div.innerHTML = "Usuário não identificado.";
    return;
  }

  fetch(`/api/minha-posicao-ataques-especial?usuarioId=${usuarioId}`)
    .then(res => {
      console.log("📡 Status resposta posição:", res.status);

      if (!res.ok) {
        if (res.status === 404) {
          div.innerHTML = "Você ainda não aparece no ranking.";
          return null;
        }
        throw new Error(`Erro HTTP: ${res.status}`);
      }
      return res.json();
    })
    .then(data => {
      if (!data) return;

      const ataques = data.quantidadeAtaquesSemanal ?? data.ataques ?? data.totalAtaques ?? 0;
      const posicao = data.posicao ?? "?";

      // Não mostra posição se o valor for zero
      if (ataques === 0) {
        div.innerHTML = "Você ainda não possui ataques nesta semana.";
        return;
      }

      div.innerHTML = `
        🏆 Sua posição: <strong>#${posicao}</strong><br>
        ⚔️ Ataques na semana: <strong>${ataques}</strong>
      `;
    })
    .catch(err => {
      console.error("🔥 Erro ao carregar posição:", err);
      div.innerHTML = "Erro ao carregar sua posição.";
    });
}

/**
 * ================================
 * CARREGA TUDO
 * ================================
 */
function carregarDadosRanking() {
  carregarRanking();
  carregarMinhaPosicao();
}

/**
 * ================================
 * INICIA ATUALIZAÇÃO AUTOMÁTICA
 * ================================
 */
function iniciarAtualizacaoRanking() {
  if (rankingInterval !== null) {
    console.log("⏱️ Atualização automática já está ativa.");
    return;
  }

  console.log("⏱️ Iniciando atualização automática do ranking a cada 1 minuto.");

  rankingInterval = setInterval(() => {
    console.log("🔄 Atualização automática do ranking...");
    carregarDadosRanking();
  }, 60000);
}

/**
 * ================================
 * OPCIONAL: PARAR ATUALIZAÇÃO
 * ================================
 */
function pararAtualizacaoRanking() {
  if (rankingInterval !== null) {
    clearInterval(rankingInterval);
    rankingInterval = null;
    console.log("⛔ Atualização automática do ranking parada.");
  }
}

/**
 * ================================
 * INICIALIZAÇÃO
 * ================================
 */
document.addEventListener("DOMContentLoaded", () => {
  console.log("✅ DOM carregado");

  // Carrega ao abrir a página
  carregarDadosRanking();

  // Inicia atualização automática a cada 1 minuto
  iniciarAtualizacaoRanking();

  // Recarrega ao clicar na aba "missoes"
  document.querySelectorAll('[data-section="missoes"]').forEach(link => {
    link.addEventListener("click", () => {
      console.log("📌 Aba missoes clicada");

      setTimeout(() => {
        carregarDadosRanking();
      }, 200);
    });
  });
});