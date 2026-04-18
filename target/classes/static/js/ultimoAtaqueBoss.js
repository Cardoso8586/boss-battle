
document.addEventListener("DOMContentLoaded", () => {

let ultimoAtaqueMostrado = null;

const meta = document.querySelector('meta[name="user-id"]');
const usuarioId = meta ? parseInt(meta.getAttribute("content")) : null;


if (!usuarioId) {
    console.warn("usuarioId não encontrado");
}

// ===============================
// UTILS
// ===============================
function formatarNumero(numero) {
    return new Intl.NumberFormat('pt-BR').format(numero || 0);
}

function calcularDanoRecebido(danoBase, temEscudoAtivo) {
    let danoFinal = Number(danoBase) || 0;
    let usouEscudo = false;

    if (temEscudoAtivo) {
        const reducao = Math.floor((danoFinal * 40) / 100);
        danoFinal = Math.max(0, danoFinal - reducao);
        usouEscudo = true;
    }

    return { danoFinal, usouEscudo };
}

// ===============================
// STATUS USUÁRIO
// ===============================
async function obterStatusUsuario(usuarioId) {
    try {
        const response = await fetch(`/api/atualizar/status/usuario/${usuarioId}`);

        if (!response.ok) return null;

        return await response.json();
    } catch (error) {
        console.error("Erro status usuário:", error);
        return null;
    }
}

// ===============================
// RENDER
// ===============================
function mostrarDano(texto) {
    const container = document.getElementById("damageContainer");

    if (!container) {
        console.warn("damageContainer não encontrado");
        return;
    }

    const msg = document.createElement("div");
    msg.classList.add("damage-message");
    msg.innerHTML = texto;

    container.appendChild(msg);

    setTimeout(() => msg.remove(), 80000);
}

// ===============================
// ATAQUE
// ===============================
async function carregarUltimoAtaque() {
    try {
        const response = await fetch("/api/boss/ultimo-ataque");
		if (response.status === 204) return;
		if (!response.ok) return;

        const data = await response.json();

        if (!data) return;

        // 🔥 CHAVE SEGURA (não depende de campo específico)
        const chave = JSON.stringify(data);

        if (ultimoAtaqueMostrado === chave) return;
        ultimoAtaqueMostrado = chave;

        // 🔥 COMPATÍVEL COM QUALQUER NOME DO BACK
        const nomeBoss = data.nomeBoss || data.bossName || "Boss";
        const danoBase = Number(data.dano) || 0;

        // 🔥 ESCUDO
        let temEscudoAtivo = false;

        if (usuarioId) {
            const status = await obterStatusUsuario(usuarioId);

            if (status) {
                temEscudoAtivo = (status.ativarEscudoPrimordial ?? 0) > 0;
            }
        }

        const resultado = calcularDanoRecebido(danoBase, temEscudoAtivo);

        let mensagem;

		if (resultado.usouEscudo) {
		    mensagem = `
		        <span class="bossName">${nomeBoss}</span>
		        atacou, mas seu <span class="escudo-ativo">Escudo Primordial</span>
		        reduziu o dano para 
		        <span class="dano-valor">${formatarNumero(resultado.danoFinal)}</span> de dano!
		    `;
		} else {
		    mensagem = `
		        <span class="bossName">${nomeBoss}</span>
		        atacou causando 
		        <span class="dano-valor">${formatarNumero(resultado.danoFinal)}</span>
		        de dano!
		    `;
		}

        mostrarDano(mensagem);

    } catch (error) {
        console.error("Erro ataque:", error);
    }
}

// ===============================
// INIT
// ===============================



   

    carregarUltimoAtaque();

    setInterval(() => {
        carregarUltimoAtaque();
    }, 3000);
});

/*
let ultimoAtaqueMostrado = null;

setInterval(() => {

    fetch("/api/boss/ultimo-ataque")
        .then(response => {

            if (!response.ok) return null;
            return response.json();

        })
        .then(data => {

            if (!data) return;

            // remove nanossegundos
            const dataLimpa = data.dataAtaque
                ? data.dataAtaque.split(".")[0]
                : null;

            if (!dataLimpa) return;

            if (ultimoAtaqueMostrado !== dataLimpa) {

                ultimoAtaqueMostrado = dataLimpa;

                if (data.mensagem) {
                    mostrarDano(data.mensagem);
                }
            }

        })
        .catch(error => console.error("Erro ao buscar ataque:", error));

}, 3000);


function mostrarDano(texto) {

    const container = document.getElementById("damageContainer");

    const msg = document.createElement("div");
    msg.classList.add("damage-message");
    //msg.innerText = texto;
	msg.innerHTML = texto; 
    container.appendChild(msg);

    setTimeout(() => {
        msg.remove();
    }, 10600);
}


*/

//-------------------------------------------------------------------------




