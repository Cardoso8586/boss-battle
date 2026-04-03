document.addEventListener("DOMContentLoaded", () => {
    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? Number(meta.content) : null;

    console.log("Meta user-id:", meta);
    console.log("usuarioId:", usuarioId);

    if (!usuarioId || Number.isNaN(usuarioId)) {
        console.error("Usuário não encontrado no meta user-id");
        return;
    }

    window.usuarioId = usuarioId;
    carregarMissoes();
});


function formatarMoedaBR(valor) {
    return new Intl.NumberFormat('pt-BR', {
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    }).format(valor);
}
//================ BUSCAR MISSÃO =================
async function carregarMissoes() {
    try {
        const response = await fetch(`/api/missoes-diarias/missoes/${window.usuarioId}`, {
            method: "GET",
            headers: {
                "Accept": "application/json"
            }
        });

        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }

        const data = await response.json();
        console.log("Missões recebidas:", data);

        renderizarMissoes(data);

    } catch (error) {
        console.error("Erro ao carregar missões:", error);

        const mensagem = document.getElementById("mensagem-diarias");
        const container = document.getElementById("diarias");

        if (container) {
            container.innerHTML = "";
        }

        if (mensagem) {
            mensagem.style.display = "block";
        }
    }
}

//================ RENDER =================

function renderizarMissoes(missao) {
    const container = document.getElementById("diarias");
    const mensagem = document.getElementById("mensagem-diarias");

    if (!container || !mensagem) {
        console.error("Elementos da missão não encontrados no HTML");
        return;
    }

    container.innerHTML = "";

    if (!missao) {
        mensagem.style.display = "block";
        return;
    }

    mensagem.style.display = "none";

    const danoPercentual = missao.danoObjetivo > 0
        ? Math.min((missao.danoAtual / missao.danoObjetivo) * 100, 100)
        : 0;

    const ataquesPercentual = missao.ataquesObjetivo > 0
        ? Math.min((missao.ataquesAtual / missao.ataquesObjetivo) * 100, 100)
        : 0;

    // USA O QUE VEM DO BACKEND
    const podeResgatarDano = !!missao.podeResgatarDano;
    const podeResgatarAtaques = !!missao.podeResgatarAtaques;

    // considera concluída quando não pode mais resgatar e já bateu o objetivo
    const danoConcluido = !podeResgatarDano && missao.danoAtual >= missao.danoObjetivo;
    const ataquesConcluido = !podeResgatarAtaques && missao.ataquesAtual >= missao.ataquesObjetivo;

    const acaoDano = podeResgatarDano
        ? `<button class="btn-missao" onclick="resgatarDano(this)">Resgatar</button>`
        : danoConcluido
            ? `<button class="btn-missao btn-concluida" disabled>Tarefa concluída</button>`
            : `<span class="status-missao">Em andamento</span>`;

    const acaoAtaques = podeResgatarAtaques
        ? `<button class="btn-missao" onclick="resgatarAtaques(this)">Resgatar</button>`
        : ataquesConcluido
            ? `<button class="btn-missao btn-concluida" disabled>Tarefa concluída</button>`
            : `<span class="status-missao">Em andamento</span>`;

    const danoHTML = `
        <div class="card-missao">
            <h3>🔥 Dano Massivo (use ataque especial)</h3>
            <p>Nível: ${missao.nivelDano}</p>
            <p>${formatarMoedaBR(missao.danoAtual)} / ${formatarMoedaBR(missao.danoObjetivo)}</p>
            <div class="barra-progresso">
                <div class="barra-preenchida" style="width: ${danoPercentual}%"></div>
            </div>
            <p>Recompensa: ${formatarMoedaBR(missao.recompensaDano)} Boss coins</p>
            ${acaoDano}
        </div>
    `;

    const ataqueHTML = `
        <div class="card-missao">
            <h3>⚔️ Ataques Especiais</h3>
            <p>Nível: ${missao.nivelAtaques}</p>
            <p>${missao.ataquesAtual} / ${missao.ataquesObjetivo}</p>
            <div class="barra-progresso">
                <div class="barra-preenchida" style="width: ${ataquesPercentual}%"></div>
            </div>
            <p>Recompensa: ${formatarMoedaBR(missao.recompensaAtaques)} Boss coins</p>
            ${acaoAtaques}
        </div>
    `;

    container.innerHTML = danoHTML + ataqueHTML;
}
/*
function renderizarMissoes(missao) {
    const container = document.getElementById("diarias");
    const mensagem = document.getElementById("mensagem-diarias");

    if (!container || !mensagem) {
        console.error("Elementos da missão não encontrados no HTML");
        return;
    }

    container.innerHTML = "";

    if (!missao) {
        mensagem.style.display = "block";
        return;
    }

    mensagem.style.display = "none";

    const danoPercentual = missao.danoObjetivo > 0
        ? Math.min((missao.danoAtual / missao.danoObjetivo) * 100, 100)
        : 0;

    const ataquesPercentual = missao.ataquesObjetivo > 0
        ? Math.min((missao.ataquesAtual / missao.ataquesObjetivo) * 100, 100)
        : 0;

    const podeResgatarDanoReal =
        missao.danoObjetivo > 0 && missao.danoAtual >= missao.danoObjetivo;

    const podeResgatarAtaquesReal =
        missao.ataquesObjetivo > 0 && missao.ataquesAtual >= missao.ataquesObjetivo;

    const acaoDano = podeResgatarDanoReal
        ? `<button class="btn-missao" onclick="resgatarDano()">Resgatar</button>`
        : `<span class="status-missao">Em andamento</span>`;

    const acaoAtaques = podeResgatarAtaquesReal
        ? `<button class="btn-missao" onclick="resgatarAtaques()">Resgatar</button>`
        : `<span class="status-missao">Em andamento</span>`;

    const danoHTML = `
        <div class="card-missao">
         <h3>🔥 Dano Massivo (use ataque especial)</h3>
            <p>Nível: ${missao.nivelDano}</p>
            <p>${formatarMoedaBR(missao.danoAtual)} / ${formatarMoedaBR(missao.danoObjetivo)}</p>
            <div class="barra-progresso">
                <div class="barra-preenchida" style="width: ${danoPercentual}%"></div>
            </div>
            <p>Recompensa: ${formatarMoedaBR(missao.recompensaDano)} coins</p>
            ${acaoDano}
        </div>
    `;

    const ataqueHTML = `
        <div class="card-missao">
            <h3>⚔️ Ataques Especiais</h3>
            <p>Nível: ${missao.nivelAtaques}</p>
            <p>${missao.ataquesAtual} / ${missao.ataquesObjetivo}</p>
            <div class="barra-progresso">
                <div class="barra-preenchida" style="width: ${ataquesPercentual}%"></div>
            </div>
            <p>Recompensa: ${formatarMoedaBR(missao.recompensaAtaques)} coins</p>
            ${acaoAtaques}
        </div>
    `;

    container.innerHTML = danoHTML + ataqueHTML;
}
*/

//================ RESGATAR =================

window.resgatarDano = async function (botao) {
    try {
        // 🔒 trava botão
        botao.disabled = true;
        const textoOriginal = botao.innerText;
        botao.innerText = "Resgatando...";

        const response = await fetch(`/api/missoes-diarias/${window.usuarioId}/resgatar/dano`, {
            method: "POST"
        });

        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }

        await carregarMissoes();

        // ⏱️ pequena pausa antes de voltar
        setTimeout(() => {
            botao.disabled = false;
            botao.innerText = textoOriginal;
        }, 2000);

    } catch (error) {
        console.error("Erro ao resgatar dano:", error);

        // volta normal se der erro
        botao.disabled = false;
        botao.innerText = "Resgatar";
    }
};
window.resgatarAtaques = async function (botao) {
    try {
        botao.disabled = true;
        const textoOriginal = botao.innerText;
        botao.innerText = "Resgatando...";

        const response = await fetch(`/api/missoes-diarias/${window.usuarioId}/resgatar/ataques`, {
            method: "POST"
        });

        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }

        await carregarMissoes();

        setTimeout(() => {
            botao.disabled = false;
            botao.innerText = textoOriginal;
        }, 2000);

    } catch (error) {
        console.error("Erro ao resgatar ataques:", error);

        botao.disabled = false;
        botao.innerText = "Resgatar";
    }
};
/*
window.resgatarDano = async function () {
    try {
        const response = await fetch(`/api/missoes-diarias/${window.usuarioId}/resgatar/dano`, {
            method: "POST"
        });

        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }

        await carregarMissoes();

    } catch (error) {
        console.error("Erro ao resgatar dano:", error);
    }
};

window.resgatarAtaques = async function () {
    try {
        const response = await fetch(`/api/missoes-diarias/${window.usuarioId}/resgatar/ataques`, {
            method: "POST"
        });

        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }

        await carregarMissoes();

    } catch (error) {
        console.error("Erro ao resgatar ataques:", error);
    }
};
*/
//================ ATUALIZAR PROGRESSO =================
window.adicionarDano = async function (valor) {
    try {
        const response = await fetch(`/api/missoes-diarias/${window.usuarioId}/atualizar/dano?valor=${valor}`, {
            method: "POST"
        });

        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }

        await carregarMissoes();

    } catch (error) {
        console.error("Erro ao adicionar dano:", error);
    }
};

window.adicionarAtaque = async function () {
    try {
        const response = await fetch(`/api/missoes-diarias/${window.usuarioId}/atualizar/ataques?quantidade=1`, {
            method: "POST"
        });

        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }

        await carregarMissoes();

    } catch (error) {
        console.error("Erro ao adicionar ataque:", error);
    }
};

setInterval(carregarMissoes, 60000);