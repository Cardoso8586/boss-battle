const attackBtn = document.getElementById("attackBtn");
const Aguarde =  document.getElementById("aguarde");

const usuarioId = getUsuarioLogadoId();

let cooldownInterval = null;
let bossVivo = true;
let reloadDisparado = false;
let ataqueEmAndamento = false;

// ---------------------------------------------------
function getUsuarioLogadoId() {
    const meta = document.querySelector('meta[name="user-id"]');
    return meta ? parseInt(meta.getAttribute("content")) : null;
}

// ---------------------------------------------------
function showDamageFloating(value) {
    const container = document.getElementById("damageFloatContainer");
    const floatText = document.createElement("div");

    floatText.className = "damage-float";
    floatText.textContent = `-${value}`;

    container.appendChild(floatText);
    setTimeout(() => floatText.remove(), 5000);
}

// ---------------------------------------------------
// ⚠️ WARNING AUTO
function swalWarningAuto(texto, segundos = 10) {
    let tempo = segundos;

    Swal.fire({
        icon: 'warning',
        title: 'Atenção',
        html: `${texto}<br><b>Fechando em ${tempo}s</b>`,
        timer: segundos * 1000,
        timerProgressBar: true,
        showConfirmButton: false,
        didOpen: () => {
            const interval = setInterval(() => {
                tempo--;
                const b = Swal.getHtmlContainer().querySelector('b');
                if (b) b.textContent = `Fechando em ${tempo}s`;
                if (tempo <= 0) clearInterval(interval);
            }, 1000);
        }
    });
}

// ---------------------------------------------------
// ⏱️ COOLDOWN VISUAL
function iniciarCooldown(segundos) {

    if (!bossVivo) return;

    let restante = segundos;

    attackBtn.disabled = true;
   // attackBtn.innerText = `(${restante}s)`;
	//attackBtn.innerText = `Aguarde (${restante}s)`;

    if (cooldownInterval) {
        clearInterval(cooldownInterval);
        cooldownInterval = null;
    }

    cooldownInterval = setInterval(() => {
        restante--;

        if (restante <= 0) {
            clearInterval(cooldownInterval);
            cooldownInterval = null;

            if (bossVivo) {
                attackBtn.disabled = false;
                attackBtn.innerText = "Atacar Boss";
                ataqueEmAndamento = false;
            }
            return;
        }

        attackBtn.innerText = `${restante}`;
		//attackBtn.innerText = `Aguarde (${restante}s)`;
		
    }, 1000);
	if (restante !== 0) {
	  Aguarde.innerHTML = "Aguarde";
	} else {
	  Aguarde.innerHTML = "";
	}
	
	/**
	 * 	 document.getElementById("Aguarde").textContent =
	   restante !== 0 ? "Aguarde" : "";

	 */
}

// ---------------------------------------------------
// 💀 MORTE DO BOSS
function tratarMorteBoss() {

    if (reloadDisparado) return;

    reloadDisparado = true;
    bossVivo = false;

    if (cooldownInterval) {
        clearInterval(cooldownInterval);
        cooldownInterval = null;
    }

    attackBtn.disabled = true;
    attackBtn.innerText = "Boss derrotado";
	floatText.textContent = `Derrotado`;
    attackBtn.style.pointerEvents = "none";

    setTimeout(() => location.reload(), 600);
}

async function bossEstaAtivo() {
    try {
        const response = await fetch("/api/boss/active");
        const data = await response.json();
        return data.active === true;
    } catch (e) {
        console.error("Erro ao verificar boss ativo", e);
        return false; // segurança máxima
    }
}

// ---------------------------------------------------
// ⚔️ CLICK ATAQUE (COM TEMPORIZADOR DE ATAQUE)
attackBtn.addEventListener("click", async () => {

    // 🔒 trava absoluta
    if (ataqueEmAndamento || !bossVivo) return;

    ataqueEmAndamento = true;
    attackBtn.disabled = true;

    const textoOriginal = attackBtn.innerText;
    const tempoAtaque = 6; // ⏱️ tempo "atacando" (segundos)
    let restante = tempoAtaque;

    // ⏳ texto inicial
    attackBtn.innerText = `Atacando...`;
	//attackBtn.innerText = `Atacando... (${restante}s)`;

	
	// abre o alerta UMA VEZ
	Swal.fire({
	  title: `Preparando para atacar!`,
	 // title: `Preparando para atacar! ${restante}s`,
	  showConfirmButton: false,
	  background: 'transparent',
	  color: '#ffb400',
	  customClass: {
	      title: 'swal-game-text'
	    },
	  
	  allowOutsideClick: false,
	  didOpen: () => {
	    const title = Swal.getTitle();

		/**
		 * 
		 */ 
		
		title.textContent = `Preparando para atacar!`;
	   // const timerAtaque = setInterval(() => {
	     // restante--;
	    //  attackBtn.innerText = `Atacando...`;
		 // attackBtn.innerText = `Atacando... (${restante}s)`;
		
	     // title.textContent = `Preparando para atacar! ${restante}s`;

	      if (restante <= 0) {
	        clearInterval(timerAtaque);
	        Swal.close();
	        attackBtn.innerText = 'Atacar';
	      }
	   // }, 1000);
		
	   Swal.close();
	  }
	});


    if (!usuarioId) {
        clearInterval(timerAtaque);
        swalWarningAuto("Usuário não identificado.", 6);
        ataqueEmAndamento = false;
        attackBtn.disabled = false;
        attackBtn.innerText = textoOriginal;
        return;
    }

    try {
        const response = await fetch(
            `/api/boss/hit?usuarioId=${usuarioId}`,
            { method: "POST" }
        );

        const data = await response.json();
        const dano = data.damage;
		const message = data.message;

		

		if (data.success === false) {
		  Swal.fire({
		    customClass: {
		      title: 'swal-game-error'
		    },
		    title: message,
		    background: 'transparent',
		    showConfirmButton: false,
		    timer: 6000
		  });
		  
		  ataqueEmAndamento = false;
		         attackBtn.disabled = false;
		         attackBtn.innerText = textoOriginal;
		  return;
		}

        // 💀 boss morreu
        if (data.status === "BOSS_DEAD") {
            clearInterval(timerAtaque);
            tratarMorteBoss();
			floatText.textContent = `Derrotado`;
            return;
        }

		if (dano!== undefined) {
		    showDamageFloating(dano);
		 

		  
		}




        // 🔥 SEMPRE usa o cooldown do servidor
        verificarCooldownInicial();

    } catch (err) {
        console.error(err);

        clearInterval(timerAtaque);

        swalWarningAuto("Erro ao atacar o boss.", 6);

        ataqueEmAndamento = false;
        attackBtn.disabled = false;
        attackBtn.innerText = textoOriginal;
    }
});

// ---------------------------------------------------
// 🔄 VERIFICA COOLDOWN REAL NO SERVIDOR
async function verificarCooldownInicial() {

    if (!usuarioId || !bossVivo) return;

    try {
        const res = await fetch(`/api/boss/cooldown?usuarioId=${usuarioId}`);
        const data = await res.json();

        if (!data.podeAtacar) {
            iniciarCooldown(data.segundosRestantes || 300);
        } else {
            attackBtn.disabled = false;
            attackBtn.innerText = "Atacar Boss";
            ataqueEmAndamento = false;
        }

    } catch (e) {
        console.error("Erro ao verificar cooldown", e);
    }
}

// 🚀 INICIALIZA
verificarCooldownInicial();
setInterval(verificarCooldownInicial, 10000);


/**
 * 

const attackBtn = document.getElementById("attackBtn");
const usuarioId = getUsuarioLogadoId();

let cooldownInterval = null;

function getUsuarioLogadoId() {
    const meta = document.querySelector('meta[name="user-id"]');
    return meta ? parseInt(meta.getAttribute("content")) : null;
}

function showDamageFloating(value) {
    const container = document.getElementById("damageFloatContainer");
    const floatText = document.createElement("div");

    floatText.className = "damage-float";
    floatText.textContent = `-${value}`;

    container.appendChild(floatText);

    setTimeout(() => floatText.remove(), 1000);
}

// 🔄 Atualiza o texto do botão com contador e cor
function iniciarCooldown(segundos) {
    let restante = segundos;

    attackBtn.disabled = true;
    attackBtn.style.backgroundColor = "#ccc"; // cinza para cooldown
    attackBtn.innerText = `Aguarde (${restante}s)`;

    if (cooldownInterval) clearInterval(cooldownInterval);

    cooldownInterval = setInterval(() => {
        restante--;

        if (restante <= 0) {
            clearInterval(cooldownInterval);
            attackBtn.disabled = false;
            attackBtn.innerText = "Atacar Boss";
            attackBtn.style.backgroundColor = "#f39c12"; 
            return;
        }

        attackBtn.innerText = `Aguarde (${restante}s)`;
    }, 1000);
}


attackBtn.addEventListener("click", async () => {
    if (!usuarioId) {
        alert("Usuário não identificado.");
        return;
    }

	attackBtn.disabled = true;
	attackBtn.style.backgroundColor = "#ccc"; // cinza para cooldown
    try {
        const response = await fetch(`/api/boss/hit?usuarioId=${usuarioId}`, {
            method: "POST"
        });

        const data = await response.json();
        console.log("Resposta do servidor:", data);

        // ⏱️ Se estiver em cooldown
        if (data.status === "COOLDOWN") {
            iniciarCooldown(data.segundosRestantes || 300);
            return;
        }

        // ✅ Ataque OK
        if (data.damage !== undefined) {
            document.getElementById("damage").innerText =
                'Dano - ' + data.damage;

            showDamageFloating(data.damage);

            // Inicia cooldown com o valor do backend (ou 300s)
            iniciarCooldown(data.segundosRestantes || 300);
        }

        if (data.message) {
            console.log("Boss:", data.message);
        }

    } catch (err) {
        console.error(err);

        // Em caso de erro, libera o botão
        attackBtn.disabled = false;
        attackBtn.style.backgroundColor = "#f39c12";
    }
});

async function verificarCooldownInicial() {
    if (!usuarioId) return;

    try {
        const res = await fetch(`/api/boss/cooldown?usuarioId=${usuarioId}`);
        const data = await res.json();

        if (!data.podeAtacar) {
           // iniciarCooldown(data.segundosRestantes);
			iniciarCooldown(data.segundosRestantes || 300);
        } else {
            attackBtn.disabled = false;
            attackBtn.innerText = "Atacar Boss";
            attackBtn.style.backgroundColor = "#f39c12"; 
        }

    } catch (e) {
        console.error("Erro ao verificar cooldown", e);
    }
}

// 🚀 roda assim que a página carrega
verificarCooldownInicial();
 */

