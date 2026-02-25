
document.addEventListener('DOMContentLoaded', async () => {

  // =============================
  // CONFIGURAÇÕES
  // =============================
  const BOSS_POR_USDT = 10_000_000;
  const BOSS_COIN_MINIMO = 100_000;
  const BOSS_COIN_MAXIMO = 30_000_000;
  // Preços em USDT (exemplo)
  const moedas = {
    USDT: 1,
    DOGE: 0.06,
    LTC: 70.00,
    TRX: 0.07,
    DGB: 0.007
  };

  async function atualizarPrecos() {
    const [
      doge,
      ltc,
      trx,
      dgb
    ] = await Promise.all([
      pegarPrecoDOGE(),
      pegarPrecoLTC(),
      pegarPrecoTRX(),
      pegarPrecoDGB()
    ]);

    moedas.DOGE = doge;
    moedas.LTC  = ltc;
    moedas.TRX  = trx;
    moedas.DGB  = dgb;

    console.log('Preços atualizados:', moedas);
  }

  
  async function pegarPrecoLTC() {
    try {
      const res = await fetch('https://api.coingecko.com/api/v3/simple/price?ids=litecoin&vs_currencies=usd');
      if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
      const data = await res.json();
      return parseFloat(data.litecoin.usd);
    } catch (err) {
      console.error('Erro ao buscar preço LTC:', err);
      return 70.00; 
    }
  }

  async function pegarPrecoTRX() {
    try {
      const res = await fetch('https://api.coingecko.com/api/v3/simple/price?ids=tron&vs_currencies=usd');
      if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
      const data = await res.json();
      return parseFloat(data.tron.usd);
    } catch (err) {
      console.error('Erro ao buscar preço TRX:', err);
      return 0.07; // fallback
    }
  }

  async function pegarPrecoDOGE() {
    try {
      const res = await fetch('https://api.coingecko.com/api/v3/simple/price?ids=dogecoin&vs_currencies=usd');
      if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
      const data = await res.json();
      return parseFloat(data.dogecoin.usd);
    } catch (err) {
      console.error('Erro ao buscar preço DOGE:', err);
      return 0.06; // fallback
    }
  }

  async function pegarPrecoDGB() {
    try {
      const res = await fetch('https://api.coingecko.com/api/v3/simple/price?ids=digibyte&vs_currencies=usd');
      if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
      const data = await res.json();
      return parseFloat(data.digibyte.usd);
    } catch (err) {
      console.error('Erro ao buscar preço DGB:', err);
      return 0.007; // fallback
    }
  }


  // =============================
  // USUÁRIO
  // =============================
  const meta = document.querySelector('meta[name="user-id"]');
  const userId = meta ? Number(meta.getAttribute('content')) : null;

  let saldoBossCoin = 0;
  let emailUsuario = '';


  // =============================
  // FORMATADORES
  // =============================
  const formatter2 = new Intl.NumberFormat('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 8
  });

  const formatter8 = new Intl.NumberFormat('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 8
  });

  function formatar(valor, moeda) {
    return moeda === 'LTC'
      ? formatter8.format(valor)
      : formatter2.format(valor);
  }

  // =============================
  // BUSCAR SALDO
  // =============================
  async function buscarSaldo() {
    try {
      const res = await fetch(`/api/atualizar/status/usuario/${userId}`);
      const data = await res.json();

      saldoBossCoin = Number(data.bossCoin);
	  emailUsuario = data.email || ''; 
	  
      atualizarCards();

    } catch (err) {
      console.error('Erro ao buscar saldo', err);
    }
  }

  // =============================
  // ATUALIZAR CARDS
  // =============================
  function atualizarCards() {

    document.querySelectorAll('.moeda-card').forEach(card => {
      const moeda = card.dataset.moeda;
      const rate = moedas[moeda];

      // BossCoin -> USDT -> moeda
      const usdtDisponivel = saldoBossCoin / BOSS_POR_USDT;
      const valorMoeda = usdtDisponivel / rate;

      card.querySelector('.quantidade').innerText =
        formatar(valorMoeda, moeda);

      card.querySelector('.boss').innerText =
        saldoBossCoin.toLocaleString('pt-BR');

      const btn = card.querySelector('.btn-resgatar');

      if (saldoBossCoin < BOSS_COIN_MINIMO) {
        btn.disabled = true;
        btn.innerText = 'Saldo insuficiente';
      } else {
        btn.disabled = false;
        btn.innerText = 'Resgatar';
        btn.onclick = () => solicitarRetirada(moeda, valorMoeda, btn);
      }
    });
  }

  async function solicitarRetirada(moeda, valorMoeda, btn) {
	btn.disabled = true;
	
      if (saldoBossCoin < BOSS_COIN_MINIMO) {
          return Swal.fire({
              icon: 'error',
              title: 'Erro',
              text: `Saldo mínimo de ${BOSS_COIN_MINIMO.toLocaleString()} BossCoin`
          });
      }

      // Ajustar valor do saque se ultrapassar o máximo
      let valorSaque = saldoBossCoin;
	  let maxAtingido = '';
	     if (saldoBossCoin > BOSS_COIN_MAXIMO) {
	         valorSaque = BOSS_COIN_MAXIMO;
			 // Array de mensagens diferentes
			       const mensagensMax = [
			           '⚠️ Atenção: seu máximo de saque foi atingido!',
			           '💰 Limite máximo alcançado, não é possível sacar mais.',
			           '🚫 Você atingiu o teto de saque permitido.',
			           '🔔 O valor máximo de saque foi aplicado automaticamente.',
			           '⚡ Apenas o máximo permitido pode ser sacado agora.'
			       ];

			       // Escolhe uma mensagem aleatória
			       maxAtingido = mensagensMax[Math.floor(Math.random() * mensagensMax.length)];
	     }
      // Converter para moeda específica
      valorMoeda = valorSaque / BOSS_POR_USDT / moedas[moeda];

      // Solicitar e-mail do usuário
      const { value: email } = await Swal.fire({
          customClass: { title: 'swal-game-text' },
		  html: maxAtingido ? `<p style="color: #ffb400;">${maxAtingido}</p>` : '',
          title: `Retirar ${formatar(valorMoeda, moeda)} ${moeda}`,
          input: 'email',
          inputLabel: 'E-mail FaucetPay',
          inputValue: emailUsuario,
          showCancelButton: true,
		  timer: 8000,
          background: '#0b0f14'
      });

      if (!email) return;

      btn.disabled = true;
      btn.innerText = 'Processando...';

      try {
          const res = await fetch('/api/faucetpay/retirada', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({
                  userId,
                  moeda,
                  bossCoin: valorSaque, // envia apenas até o máximo
                  email
              })
          });

          const data = await res.json();

          if (!data.success) throw new Error(data.message);

          Swal.fire({
              customClass: { title: 'swal-game-text' },
              icon: 'success',
              title: 'Sucesso',
              text: data.message,
              timer: 8000,
              background: 'transparent',
              color: '#ffb400'
          });

          buscarSaldo();

      } catch (err) {
          Swal.fire({
              customClass: { title: 'swal-game-error' },
              icon: 'error',
              title: 'Erro',
              text: err.message,
              timer: 8000,
              background: 'transparent',
              color: '#ff3b3b'
          });
      }
  }
  // =============================
  // SOLICITAR RETIRADA
  // =============================
  /*
  async function solicitarRetirada(moeda, valorMoeda, btn) {
	
	//const btn = card.querySelector('.btn-resgatar');
	//btn.disabled = true;
	
    if (saldoBossCoin < BOSS_COIN_MINIMO) {
      return Swal.fire({
        icon: 'error',
        title: 'Erro',
        text: 'Saldo mínimo de 100.000 BossCoin '
      });
    }
	
	if (saldoBossCoin > BOSS_COIN_MAXIMO) {
		  
		valorMoeda = BOSS_COIN_MAXIMO;
     }
		
	const { value: email } = await Swal.fire({
	  customClass: {
	    title: 'swal-game-text'
	  },
	  title: `Retirar ${formatar(valorMoeda, moeda)} ${moeda}`,

	  input: 'email',
	  inputLabel: 'E-mail FaucetPay',
	  inputValue: emailUsuario, 
	  showCancelButton: true,
	  background: '#0b0f14',
	});


    if (!email) return;

    btn.disabled = true;
    btn.innerText = 'Processando...';

    try {
      const res = await fetch('/api/faucetpay/retirada', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          userId,
          moeda,
		  bossCoin: saldoBossCoin,
          email
        })
      });

      const data = await res.json();

      if (!data.success) throw new Error(data.message);

      Swal.fire({
	  customClass: {
	  title: 'swal-game-text'
	  },
      icon: 'success',
      title: 'Sucesso',
      text: data.message,
	  timer: 8000,
	  background: 'transparent',
	  color: '#ffb400'
      });

      buscarSaldo();

    } catch (err) {
      Swal.fire({
		customClass: {
		title: 'swal-game-error'
		},
        icon: 'error',
        title: 'Erro',
		text: err.message,
		timer: 8000,
		background: 'transparent',
		color: '#ff3b3b' 
      });
    }
  }
*/
  // =============================
  // INIT
  // =============================
  if (userId) {
    await atualizarPrecos();
    buscarSaldo();
  }
  
  setInterval(atualizarPrecos, 10000);
  setInterval(buscarSaldo, 10000);
});

