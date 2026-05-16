

document.addEventListener('DOMContentLoaded', async () => {

  const BOSS_POR_USDT = 10_000_000;
  const BOSS_COIN_MINIMO = 20_000;
  const BOSS_COIN_MAXIMO = 1_000_000;
  const LIMITE_SAQUES_DIA = 3;

  const moedas = {
    USDT: 1,
    DOGE: 0.06,
    LTC: 70.00,
    TRX: 0.07,
    DGB: 0.007
  };

  let retiradaEmAndamento = false;

  const meta = document.querySelector('meta[name="user-id"]');
  const userId = meta ? Number(meta.getAttribute('content')) : null;

  let saldoBossCoin = 0;
  let emailUsuario = '';
  let quantidadeSaquesHoje = 0;
  let ultimoValorRecebido = 0;

  const formatter = new Intl.NumberFormat('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 8
  });

  function formatar(valor) {
    return formatter.format(Number(valor || 0));
  }

  function atualizarInfoSaques() {
    const usados = Number(quantidadeSaquesHoje || 0);
    const restantes = Math.max(0, LIMITE_SAQUES_DIA - usados);

    const elInfo = document.getElementById('infoSaques');

    if (!elInfo) return;

    if (restantes <= 0) {
      elInfo.innerText = `${usados}/${LIMITE_SAQUES_DIA} - Limite diário de saques atingido.`;
      return;
    }

    elInfo.innerText =
      `${usados}/${LIMITE_SAQUES_DIA} - Você ainda tem ${restantes} ${restantes === 1 ? 'saque' : 'saques'} hoje.`;
  }

  async function pegarPreco(id, fallback) {
    try {
      const res = await fetch(
        `https://api.coingecko.com/api/v3/simple/price?ids=${id}&vs_currencies=usd`,
        { cache: 'force-cache' }
      );

      if (!res.ok) {
        throw new Error(`Erro HTTP: ${res.status}`);
      }

      const data = await res.json();

      return Number(data[id].usd);

    } catch (err) {
      console.error(`Erro ao buscar preço ${id}:`, err);
      return fallback;
    }
  }

  async function atualizarPrecos() {
    const [doge, ltc, trx, dgb] = await Promise.all([
      pegarPreco('dogecoin', 0.06),
      pegarPreco('litecoin', 70.00),
      pegarPreco('tron', 0.07),
      pegarPreco('digibyte', 0.007)
    ]);

    moedas.DOGE = doge;
    moedas.LTC = ltc;
    moedas.TRX = trx;
    moedas.DGB = dgb;

    atualizarCards();
  }

  async function buscarSaldo() {
    if (!userId) {
      console.error('Usuário não encontrado');
      return;
    }

    try {
      const res = await fetch(`/api/atualizar/status/usuario/${userId}`, {
        cache: 'no-store'
      });

      if (!res.ok) {
        throw new Error('Erro ao buscar saldo do usuário.');
      }

      const data = await res.json();

      saldoBossCoin = Number(data.bossCoin || 0);
      emailUsuario = data.email || '';

      quantidadeSaquesHoje = Number(data.quantidadeSaquesHoje || 0);
      ultimoValorRecebido = Number(data.ultimoValorRecebido || 0);

      atualizarInfoSaques();
      atualizarCards();

    } catch (err) {
      console.error('Erro ao buscar saldo:', err);
    }
  }

  function atualizarCards() {
    document.querySelectorAll('.moeda-card').forEach(card => {
      const moeda = card.dataset.moeda;
      const rate = moedas[moeda] || 1;

      const usdtDisponivel = saldoBossCoin / BOSS_POR_USDT;
      const valorMoeda = usdtDisponivel / rate;

      const quantidadeEl = card.querySelector('.quantidade');
      const bossEl = card.querySelector('.boss');
      const btn = card.querySelector('.btn-resgatar');

      if (quantidadeEl) {
        quantidadeEl.innerText = formatar(valorMoeda);
      }

      if (bossEl) {
        bossEl.innerText = saldoBossCoin.toLocaleString('pt-BR');
      }

      if (!btn) return;

      if (retiradaEmAndamento) {
        btn.disabled = true;
        return;
      }

      if (quantidadeSaquesHoje >= LIMITE_SAQUES_DIA) {
        btn.disabled = true;
        btn.innerText = 'Limite diário atingido';
        return;
      }

      if (saldoBossCoin < BOSS_COIN_MINIMO) {
        btn.disabled = true;
        btn.innerText = 'Saldo insuficiente';
        return;
      }

      btn.disabled = false;
      btn.innerText = 'Resgatar';
      btn.onclick = () => solicitarRetirada(moeda, valorMoeda, btn);
    });
  }

  async function solicitarRetirada(moeda, valorMoeda, btn) {
    if (retiradaEmAndamento) return;

    retiradaEmAndamento = true;

    const allButtons = document.querySelectorAll('.btn-resgatar');

    allButtons.forEach(b => b.disabled = true);
    btn.innerText = 'Processando...';

    try {
      if (quantidadeSaquesHoje >= LIMITE_SAQUES_DIA) {
        throw new Error('Você já atingiu o limite diário de saques.');
      }

      if (saldoBossCoin < BOSS_COIN_MINIMO) {
        throw new Error(
          `Saldo mínimo de ${BOSS_COIN_MINIMO.toLocaleString('pt-BR')} BossCoin.`
        );
      }

      let valorSaque = saldoBossCoin;
      let maxAtingido = '';

      if (saldoBossCoin > BOSS_COIN_MAXIMO) {
        valorSaque = BOSS_COIN_MAXIMO;

        const mensagensMax = [
          '⚠️ Atenção: seu máximo de saque foi atingido!',
          '💰 Limite máximo alcançado, não é possível sacar mais.',
          '🚫 Você atingiu o teto de saque permitido.',
          '🔔 O valor máximo de saque foi aplicado automaticamente.',
          '⚡ Apenas o máximo permitido pode ser sacado agora.'
        ];

        maxAtingido =
          mensagensMax[Math.floor(Math.random() * mensagensMax.length)];
      }

      valorMoeda = valorSaque / BOSS_POR_USDT / moedas[moeda];

      const restantes = Math.max(0, LIMITE_SAQUES_DIA - quantidadeSaquesHoje);

      const { value: email } = await Swal.fire({
        customClass: {
          popup: 'swal-game-popup',
          title: 'swal-game-text',
          confirmButton: 'swal-game-confirm',
          cancelButton: 'swal-game-cancel'
        },

        title: `Retirar ${formatar(valorMoeda)} ${moeda}`,

        input: 'email',

        inputLabel: 'E-mail FaucetPay',

        inputValue: emailUsuario,

        html: `
          <p style="color:#ffb400;">
            Saques hoje: ${quantidadeSaquesHoje}/${LIMITE_SAQUES_DIA}<br>
            Você ainda tem ${restantes} ${restantes === 1 ? 'saque' : 'saques'} hoje.
          </p>

          ${maxAtingido ? `
            <p style="color:#ffb400;">
              ${maxAtingido}
            </p>` : ''}

          <div class="modal-anuncio">
            <iframe
              src="https://zerads.com/ad/ad.php?width=468&ref=10783"
              width="468"
              height="60"
              scrolling="no"
              frameborder="0">
            </iframe>
          </div>
        `,

        showCancelButton: true,

        confirmButtonText: 'Retirar',

        cancelButtonText: 'Cancelar',

        buttonsStyling: false
      });

      if (!email) {
        return;
      }

      btn.innerText = 'Enviando...';

      const res = await fetch('/api/faucetpay/retirada', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          userId: userId,
          moeda: moeda,
          bossCoin: valorSaque,
          email: email
        })
      });

      let data = {};

      try {
        data = await res.json();
      } catch (e) {
        throw new Error('Resposta inválida do servidor.');
      }

      if (!res.ok) {
        throw new Error(data.message || 'Erro na requisição de saque.');
      }

      if (!data.success) {
        throw new Error(data.message || 'Saque recusado pelo servidor.');
      }

      saldoBossCoin = Math.max(0, saldoBossCoin - valorSaque);

      await buscarSaldo();

      await Swal.fire({
        customClass: {
          popup: 'swal-game-popup',
          title: 'swal-game-text',
          confirmButton: 'swal-game-confirm'
        },

        icon: 'success',

        title: 'Sucesso',

        html: `
          <p style="margin-bottom:18px;">
            ${data.message || 'Saque realizado com sucesso.'}
          </p>

          <p style="color:#ffb400;">
            Saques hoje: ${quantidadeSaquesHoje}/${LIMITE_SAQUES_DIA}
          </p>

          <div class="modal-anuncio">
            <iframe
              src="https://zerads.com/ad/ad.php?width=468&ref=10783"
              width="468"
              height="60"
              scrolling="no"
              frameborder="0">
            </iframe>
          </div>
        `,

        timer: 8000,

        confirmButtonText: 'OK',

        buttonsStyling: false,

        color: '#ffb400'
      });

    } catch (err) {
      console.error('Erro ao solicitar retirada:', err);

      await Swal.fire({
        customClass: {
          popup: 'swal-game-popup',
          title: 'swal-game-error',
          confirmButton: 'swal-game-confirm'
        },

        icon: 'error',

        title: 'Erro no saque',

        html: `
          <p>
            ${err.message || 'Não foi possível realizar o saque agora.'}
          </p>

          <div class="modal-anuncio">
            <iframe
              src="https://zerads.com/ad/ad.php?width=468&ref=10783"
              width="468"
              height="60"
              scrolling="no"
              frameborder="0">
            </iframe>
          </div>
        `,

        confirmButtonText: 'OK',

        buttonsStyling: false,

        color: '#ff3b3b'
      });

    } finally {
      retiradaEmAndamento = false;
      atualizarInfoSaques();
      atualizarCards();
    }
  }

  if (userId) {
    await atualizarPrecos();
    await buscarSaldo();
  }

});
/*
document.addEventListener('DOMContentLoaded', async () => {

  const BOSS_POR_USDT = 10_000_000;
  const BOSS_COIN_MINIMO = 20_000;
  const BOSS_COIN_MAXIMO = 1_000_000;

  const moedas = {
    USDT: 1,
    DOGE: 0.06,
    LTC: 70.00,
    TRX: 0.07,
    DGB: 0.007
  };

  let retiradaEmAndamento = false;

  const meta = document.querySelector('meta[name="user-id"]');
  const userId = meta ? Number(meta.getAttribute('content')) : null;

  let saldoBossCoin = 0;
  let emailUsuario = '';

  const formatter = new Intl.NumberFormat('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 8
  });

  function formatar(valor) {
    return formatter.format(Number(valor || 0));
  }

  async function pegarPreco(id, fallback) {
    try {
      const res = await fetch(
        `https://api.coingecko.com/api/v3/simple/price?ids=${id}&vs_currencies=usd`
      );

      if (!res.ok) {
        throw new Error(`Erro HTTP: ${res.status}`);
      }

      const data = await res.json();

      return Number(data[id].usd);

    } catch (err) {
      console.error(`Erro ao buscar preço ${id}:`, err);
      return fallback;
    }
  }

  async function atualizarPrecos() {
    const [doge, ltc, trx, dgb] = await Promise.all([
      pegarPreco('dogecoin', 0.06),
      pegarPreco('litecoin', 70.00),
      pegarPreco('tron', 0.07),
      pegarPreco('digibyte', 0.007)
    ]);

    moedas.DOGE = doge;
    moedas.LTC = ltc;
    moedas.TRX = trx;
    moedas.DGB = dgb;

    atualizarCards();
  }

  async function buscarSaldo() {
    if (!userId) {
      console.error('Usuário não encontrado');
      return;
    }

    try {
      const res = await fetch(`/api/atualizar/status/usuario/${userId}`);

      if (!res.ok) {
        throw new Error('Erro ao buscar saldo do usuário.');
      }

      const data = await res.json();

      saldoBossCoin = Number(data.bossCoin || 0);
      emailUsuario = data.email || '';

      atualizarCards();

    } catch (err) {
      console.error('Erro ao buscar saldo:', err);
    }
  }

  function atualizarCards() {
    document.querySelectorAll('.moeda-card').forEach(card => {
      const moeda = card.dataset.moeda;
      const rate = moedas[moeda] || 1;

      const usdtDisponivel = saldoBossCoin / BOSS_POR_USDT;
      const valorMoeda = usdtDisponivel / rate;

      const quantidadeEl = card.querySelector('.quantidade');
      const bossEl = card.querySelector('.boss');
      const btn = card.querySelector('.btn-resgatar');

      if (quantidadeEl) {
        quantidadeEl.innerText = formatar(valorMoeda);
      }

      if (bossEl) {
        bossEl.innerText = saldoBossCoin.toLocaleString('pt-BR');
      }

      if (!btn) {
        return;
      }

      if (retiradaEmAndamento) {
        btn.disabled = true;
        return;
      }

      if (saldoBossCoin < BOSS_COIN_MINIMO) {
        btn.disabled = true;
        btn.innerText = 'Saldo insuficiente';
        return;
      }

      btn.disabled = false;
      btn.innerText = 'Resgatar';
      btn.onclick = () => solicitarRetirada(moeda, valorMoeda, btn);
    });
  }

  async function solicitarRetirada(moeda, valorMoeda, btn) {
    if (retiradaEmAndamento) {
      return;
    }

    retiradaEmAndamento = true;

    const allButtons = document.querySelectorAll('.btn-resgatar');

    allButtons.forEach(b => b.disabled = true);
    btn.innerText = 'Processando...';

    try {
      if (saldoBossCoin < BOSS_COIN_MINIMO) {
        throw new Error(
          `Saldo mínimo de ${BOSS_COIN_MINIMO.toLocaleString('pt-BR')} BossCoin.`
        );
      }

      let valorSaque = saldoBossCoin;
      let maxAtingido = '';

      if (saldoBossCoin > BOSS_COIN_MAXIMO) {
        valorSaque = BOSS_COIN_MAXIMO;

        const mensagensMax = [
          '⚠️ Atenção: seu máximo de saque foi atingido!',
          '💰 Limite máximo alcançado, não é possível sacar mais.',
          '🚫 Você atingiu o teto de saque permitido.',
          '🔔 O valor máximo de saque foi aplicado automaticamente.',
          '⚡ Apenas o máximo permitido pode ser sacado agora.'
        ];

        maxAtingido =
          mensagensMax[Math.floor(Math.random() * mensagensMax.length)];
      }

      valorMoeda = valorSaque / BOSS_POR_USDT / moedas[moeda];

      const { value: email } = await Swal.fire({
        customClass: {
          popup: 'swal-game-popup',
          title: 'swal-game-text',
          confirmButton: 'swal-game-confirm',
          cancelButton: 'swal-game-cancel'
        },

        title: `Retirar ${formatar(valorMoeda)} ${moeda}`,

        input: 'email',

        inputLabel: 'E-mail FaucetPay',

        inputValue: emailUsuario,

        html: `
          ${maxAtingido ? `
            <p style="color:#ffb400;">
              ${maxAtingido}
            </p>` : ''}

          <div class="modal-anuncio">
            <iframe
              src="https://zerads.com/ad/ad.php?width=468&ref=10783"
              width="468"
              height="60"
              scrolling="no"
              frameborder="0">
            </iframe>
          </div>
        `,

        showCancelButton: true,

        confirmButtonText: 'Retirar',

        cancelButtonText: 'Cancelar',

        buttonsStyling: false
      });

      if (!email) {
        return;
      }

      btn.innerText = 'Enviando...';

      const res = await fetch('/api/faucetpay/retirada', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          userId: userId,
          moeda: moeda,
          bossCoin: valorSaque,
          email: email
        })
      });

      let data = {};

      try {
        data = await res.json();
      } catch (e) {
        throw new Error('Resposta inválida do servidor.');
      }

      if (!res.ok) {
        throw new Error(data.message || 'Erro na requisição de saque.');
      }

      if (!data.success) {
        throw new Error(data.message || 'Saque recusado pelo servidor.');
      }

      saldoBossCoin = Math.max(0, saldoBossCoin - valorSaque);

      atualizarCards();

      await buscarSaldo();

      await Swal.fire({
        customClass: {
          popup: 'swal-game-popup',
          title: 'swal-game-text',
          confirmButton: 'swal-game-confirm'
        },

        icon: 'success',

        title: 'Sucesso',

        html: `
          <p style="margin-bottom:18px;">
            ${data.message || 'Saque realizado com sucesso.'}
          </p>

          <div class="modal-anuncio">
            <iframe
              src="https://zerads.com/ad/ad.php?width=468&ref=10783"
              width="468"
              height="60"
              scrolling="no"
              frameborder="0">
            </iframe>
          </div>
        `,

        timer: 8000,

        confirmButtonText: 'OK',

        buttonsStyling: false,

        color: '#ffb400'
      });

    } catch (err) {
      console.error('Erro ao solicitar retirada:', err);

      await Swal.fire({
        customClass: {
          popup: 'swal-game-popup',
          title: 'swal-game-error',
          confirmButton: 'swal-game-confirm'
        },

        icon: 'error',

        title: 'Erro no saque',

        html: `
          <p>
            ${err.message || 'Não foi possível realizar o saque agora.'}
          </p>

          <div class="modal-anuncio">
            <iframe
              src="https://zerads.com/ad/ad.php?width=468&ref=10783"
              width="468"
              height="60"
              scrolling="no"
              frameborder="0">
            </iframe>
          </div>
        `,

        confirmButtonText: 'OK',

        buttonsStyling: false,

        color: '#ff3b3b'
      });

    } finally {
      retiradaEmAndamento = false;
      atualizarCards();
    }
  }

  if (userId) {
    await atualizarPrecos();
    await buscarSaldo();
  }

  setInterval(atualizarPrecos, 300000);
  setInterval(buscarSaldo, 300000);
});
*/
