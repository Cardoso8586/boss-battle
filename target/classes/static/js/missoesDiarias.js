
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
    }).format(valor || 0);
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

    //================ DANO =================

    const danoPercentual = missao.danoObjetivo > 0
        ? Math.min((missao.danoAtual / missao.danoObjetivo) * 100, 100)
        : 0;

    const podeResgatarDano = !!missao.podeResgatarDano;

    const danoConcluido = !podeResgatarDano
        && missao.danoAtual >= missao.danoObjetivo;

    const acaoDano = podeResgatarDano
        ? `<button class="btn-missao" onclick="resgatarDano(this)">Resgatar</button>`
        : danoConcluido
            ? `<button class="btn-missao btn-concluida" disabled>Tarefa concluída</button>`
            : `<span class="status-missao">Em andamento</span>`;

    const danoHTML = `
        <div class="card-missao">
            <h3>🔥 Dano Massivo</h3>

            <p>Nível: ${missao.nivelDano}</p>

            <p>
                ${formatarMoedaBR(missao.danoAtual)}
                /
                ${formatarMoedaBR(missao.danoObjetivo)}
            </p>

            <div class="barra-progresso">
                <div class="barra-preenchida" style="width: ${danoPercentual}%"></div>
            </div>

            <p>
                Recompensa:
                ${formatarMoedaBR(missao.recompensaDano)}
                Boss coins
            </p>

            ${acaoDano}
        </div>
    `;

    //================ ATAQUES =================

    const ataquesPercentual = missao.ataquesObjetivo > 0
        ? Math.min((missao.ataquesAtual / missao.ataquesObjetivo) * 100, 100)
        : 0;

    const podeResgatarAtaques = !!missao.podeResgatarAtaques;

    const ataquesConcluido = !podeResgatarAtaques
        && missao.ataquesAtual >= missao.ataquesObjetivo;

    const acaoAtaques = podeResgatarAtaques
        ? `<button class="btn-missao" onclick="resgatarAtaques(this)">Resgatar</button>`
        : ataquesConcluido
            ? `<button class="btn-missao btn-concluida" disabled>Tarefa concluída</button>`
            : `<span class="status-missao">Em andamento</span>`;

    const ataqueHTML = `
        <div class="card-missao">
            <h3>⚔️ Ataques Especiais</h3>

            <p>Nível: ${missao.nivelAtaques}</p>

            <p>
                ${missao.ataquesAtual}
                /
                ${missao.ataquesObjetivo}
            </p>

            <div class="barra-progresso">
                <div class="barra-preenchida" style="width: ${ataquesPercentual}%"></div>
            </div>

            <p>
                Recompensa:
                ${formatarMoedaBR(missao.recompensaAtaques)}
                Boss coins
            </p>

            ${acaoAtaques}
        </div>
    `;

    //================ PTC =================

    const ptcPercentual = missao.ptcObjetivo > 0
        ? Math.min((missao.ptcAtual / missao.ptcObjetivo) * 100, 100)
        : 0;

    const podeResgatarPtc = !!missao.podeResgatarPtc;

    const ptcConcluido = !podeResgatarPtc
        && missao.ptcAtual >= missao.ptcObjetivo;

    const acaoPtc = podeResgatarPtc
        ? `
        <button class="btn-missao" onclick="resgatarPtc(this)">
            🎁 Resgatar recompensa
        </button>
        `
        : ptcConcluido
            ? `
            <button class="btn-missao btn-concluida" disabled>
                ✅ Tarefa concluída
            </button>
            `
            : `
            <a href="/ganhar-boss-coins" class="btn-ptc-link">
                <span>📺</span>
                Visualizar PTC
            </a>
            `;

    const ptcHTML = `
        <div class="card-missao">
            <h3>📺 Visualizar PTC</h3>

            <p>Nível: ${missao.nivelPtc}</p>

            <p>
                ${missao.ptcAtual}
                /
                ${missao.ptcObjetivo}
            </p>

            <div class="barra-progresso">
                <div class="barra-preenchida" style="width: ${ptcPercentual}%"></div>
            </div>

            <p>
                Recompensa:
                ${formatarMoedaBR(missao.recompensaPtc)}
                Boss coins
            </p>

            ${acaoPtc}
        </div>
    `;

    //================ CAÇADOR DE RECOMPENSAS =================

    const cacadorPercentual = missao.cacadorObjetivo > 0
        ? Math.min((missao.cacadorAtual / missao.cacadorObjetivo) * 100, 100)
        : 0;

    const podeResgatarCacador = !!missao.podeResgatarCacador;

    const cacadorConcluido = !podeResgatarCacador
        && missao.cacadorAtual >= missao.cacadorObjetivo;

    const acaoCacador = podeResgatarCacador
        ? `
        <button class="btn-missao" onclick="resgatarCacador(this)">
            🏆 Resgatar recompensa
        </button>
        `
        : cacadorConcluido
            ? `
            <button class="btn-missao btn-concluida" disabled>
                ✅ Tarefa concluída
            </button>
            `
            : `
            <a href="/cacador-recompensas" class="btn-ptc-link">
                <span>🎁</span>
                Buscar Recompensas
            </a>
            `;

    const cacadorHTML = `
        <div class="card-missao">
            <h3>🏆 Caçador de Recompensas</h3>

            <p>Nível: ${missao.nivelCacador}</p>

            <p>
                ${missao.cacadorAtual}
                /
                ${missao.cacadorObjetivo}
            </p>

            <div class="barra-progresso">
                <div class="barra-preenchida" style="width: ${cacadorPercentual}%"></div>
            </div>

            <p>
                Recompensa:
                ${formatarMoedaBR(missao.recompensaCacador)}
                Boss coins
            </p>

            ${acaoCacador}
        </div>
    `;

    container.innerHTML =
        danoHTML +
        ataqueHTML +
        ptcHTML +
        cacadorHTML;
}

//================ SWAL PADRÃO =================

function swalSucesso(titulo, mensagem) {
    Swal.fire({
        customClass: {
            title: 'swal-game-text'
        },
        icon: 'success',
        title: titulo,
        html: `
            <div style="margin-bottom:10px;">
                ${mensagem}
            </div>

            <div id="areaAnuncioSwal" class="modal-anuncio"></div>
        `,
        timer: 8000,
        showConfirmButton: false,
        background: 'transparent',
        color: '#ffb400',
        didOpen: () => {
            const area = document.getElementById("areaAnuncioSwal");

            if (area) {
                area.innerHTML = `
                    <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                        width="468"
                        height="60"
                        scrolling="no"
                        frameborder="0"
                        style="max-width:100%; border:0;">
                    </iframe>
                `;
            }
        }
    });
}

function swalErro(mensagem) {
    Swal.fire({
        customClass: {
            title: 'swal-game-error'
        },
        icon: 'error',
        title: 'Erro',
        html: `
            <div style="margin-bottom:10px;">
                ${mensagem}
            </div>

            <div class="modal-anuncio">
                <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                    width="468"
                    height="60"
                    scrolling="no"
                    frameborder="0"
                    style="max-width:100%; border:0;">
                </iframe>
            </div>
        `,
        timer: 8000,
        showConfirmButton: false,
        background: 'transparent',
        color: '#ff3b3b'
    });
}

//================ RESGATAR DANO =================

window.resgatarDano = async function (botao) {
    try {
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

        swalSucesso(
            'Recompensa resgatada!',
            'Você resgatou sua missão de dano.'
        );

        setTimeout(() => {
            botao.disabled = false;
            botao.innerText = textoOriginal;
        }, 2000);

    } catch (error) {
        console.error("Erro ao resgatar dano:", error);

        botao.disabled = false;
        botao.innerText = "Resgatar";

        swalErro('Não foi possível resgatar a recompensa de dano.');
    }
};

//================ RESGATAR ATAQUES =================

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

        swalSucesso(
            'Recompensa resgatada!',
            'Você resgatou sua missão de ataques.'
        );

        setTimeout(() => {
            botao.disabled = false;
            botao.innerText = textoOriginal;
        }, 2000);

    } catch (error) {
        console.error("Erro ao resgatar ataques:", error);

        botao.disabled = false;
        botao.innerText = "Resgatar";

        swalErro('Não foi possível resgatar a recompensa de ataques.');
    }
};

//================ RESGATAR PTC =================

window.resgatarPtc = async function (botao) {
    try {
        botao.disabled = true;
        const textoOriginal = botao.innerText;
        botao.innerText = "Resgatando...";

        const response = await fetch(
            `/api/missoes-diarias/${window.usuarioId}/resgatar/ptc`,
            {
                method: "POST"
            }
        );

        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }

        await carregarMissoes();

        swalSucesso(
            'Recompensa resgatada!',
            'Você resgatou sua missão PTC.'
        );

        setTimeout(() => {
            botao.disabled = false;
            botao.innerText = textoOriginal;
        }, 2000);

    } catch (error) {
        console.error("Erro ao resgatar ptc:", error);

        botao.disabled = false;
        botao.innerText = "Resgatar";

        swalErro('Não foi possível resgatar a recompensa PTC.');
    }
};

//================ RESGATAR CAÇADOR DE RECOMPENSAS =================

window.resgatarCacador = async function (botao) {
    try {
        botao.disabled = true;
        const textoOriginal = botao.innerText;
        botao.innerText = "Resgatando...";

        const response = await fetch(
            `/api/missoes-diarias/${window.usuarioId}/resgatar/cacador`,
            {
                method: "POST"
            }
        );

        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }

        await carregarMissoes();

        swalSucesso(
            'Recompensa resgatada!',
            'Você resgatou sua missão Caçador de Recompensas.'
        );

        setTimeout(() => {
            botao.disabled = false;
            botao.innerText = textoOriginal;
        }, 2000);

    } catch (error) {
        console.error("Erro ao resgatar caçador de recompensas:", error);

        botao.disabled = false;
        botao.innerText = "Resgatar";

        swalErro('Não foi possível resgatar a missão Caçador de Recompensas.');
    }
};

//================ ATUALIZAR PTC =================

window.adicionarPtc = async function () {
    try {
        const response = await fetch(
            `/api/missoes-diarias/${window.usuarioId}/atualizar/ptc?quantidade=1`,
            {
                method: "POST"
            }
        );

        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }

        await carregarMissoes();

    } catch (error) {
        console.error("Erro ao adicionar ptc:", error);
    }
};

//================ ATUALIZAR CAÇADOR DE RECOMPENSAS =================

window.adicionarCacadorRecompensas = async function () {
    try {
        const response = await fetch(
            `/api/missoes-diarias/${window.usuarioId}/atualizar/cacador?quantidade=1`,
            {
                method: "POST"
            }
        );

        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }

        await carregarMissoes();

    } catch (error) {
        console.error("Erro ao adicionar caçador de recompensas:", error);
    }
};

//================ ATUALIZAR DANO =================

window.adicionarDano = async function (valor) {
    try {
        const response = await fetch(
            `/api/missoes-diarias/${window.usuarioId}/atualizar/dano?valor=${valor}`,
            {
                method: "POST"
            }
        );

        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }

        await carregarMissoes();

    } catch (error) {
        console.error("Erro ao adicionar dano:", error);
    }
};

//================ ATUALIZAR ATAQUES =================

window.adicionarAtaque = async function () {
    try {
        const response = await fetch(
            `/api/missoes-diarias/${window.usuarioId}/atualizar/ataques?quantidade=1`,
            {
                method: "POST"
            }
        );

        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }

        await carregarMissoes();

    } catch (error) {
        console.error("Erro ao adicionar ataque:", error);
    }
};

setInterval(carregarMissoes, 60000);

/**
 * 
 * document.addEventListener("DOMContentLoaded", () => {
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

 
	//================ PTC =================
	//================ PTC =================

	const ptcPercentual = missao.ptcObjetivo > 0
	    ? Math.min((missao.ptcAtual / missao.ptcObjetivo) * 100, 100)
	    : 0;

	const podeResgatarPtc = !!missao.podeResgatarPtc;

	const ptcConcluido = !podeResgatarPtc
	    && missao.ptcAtual >= missao.ptcObjetivo;

	const acaoPtc = podeResgatarPtc
	    ? `
	    <button class="btn-missao"
	            onclick="resgatarPtc(this)">
	        🎁 Resgatar recompensa
	    </button>
	    `
	    : ptcConcluido
	        ? `
	        <button class="btn-missao btn-concluida" disabled>
	            ✅ Tarefa concluída
	        </button>
	        `
	        : `
	        <a href="/ganhar-boss-coins"
	           class="btn-ptc-link">
	            <span>📺</span>
	            Visualizar PTC
	        </a>
	        `;

	const ptcHTML = `
	    <div class="card-missao">

	        <h3>📺 Visualizar PTC</h3>

	        <p>Nível: ${missao.nivelPtc}</p>

	        <p>
	            ${missao.ptcAtual} / ${missao.ptcObjetivo}
	        </p>

	        <div class="barra-progresso">
	            <div class="barra-preenchida"
	                 style="width: ${ptcPercentual}%">
	            </div>
	        </div>

	        <p>
	            Recompensa:
	            ${formatarMoedaBR(missao.recompensaPtc)}
	            Boss coins
	        </p>

	        ${acaoPtc}

	    </div>
	`;
	
	//================ ADICIONAR NO CONTAINER =================

	container.innerHTML = danoHTML + ataqueHTML + ptcHTML;

}

//================ RESGATAR =================

window.resgatarDano = async function (botao) {
    try {
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

        Swal.fire({
            customClass: {
                title: 'swal-game-text'
            },
            icon: 'success',
            title: 'Recompensa resgatada!',
            html: `
                <div style="margin-bottom:10px;">
                    Você resgatou sua missão de dano.
                </div>

                <div class="modal-anuncio">
                    <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                        width="468"
                        height="60"
                        scrolling="no"
                        frameborder="0"
                        style="max-width:100%; border:0;">
                    </iframe>
                </div>
            `,
            timer: 8000,
            showConfirmButton: false,
            background: 'transparent',
            color: '#ffb400'
        });

        setTimeout(() => {
            botao.disabled = false;
            botao.innerText = textoOriginal;
        }, 2000);

    } catch (error) {
        console.error("Erro ao resgatar dano:", error);

        botao.disabled = false;
        botao.innerText = "Resgatar";

        Swal.fire({
            customClass: {
                title: 'swal-game-error'
            },
            icon: 'error',
            title: 'Erro',
            html: `
                <div style="margin-bottom:10px;">
                    Não foi possível resgatar a recompensa de dano.
                </div>

                <div class="modal-anuncio">
                    <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                        width="468"
                        height="60"
                        scrolling="no"
                        frameborder="0"
                        style="max-width:100%; border:0;">
                    </iframe>
                </div>
            `,
            timer: 8000,
            showConfirmButton: false,
            background: 'transparent',
            color: '#ff3b3b'
        });
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

		Swal.fire({
		    customClass: {
		        title: 'swal-game-text'
		    },
		    icon: 'success',
		    title: 'Recompensa resgatada!',
		    html: `
		        <div style="margin-bottom:10px;">
		            Você resgatou sua missão de ataques.
		        </div>

		        <div id="areaAnuncioSwal" class="modal-anuncio"></div>
		    `,
		    timer: 8000,
		    showConfirmButton: false,
		    background: 'transparent',
		    color: '#ffb400',
		    didOpen: () => {
		        const area = document.getElementById("areaAnuncioSwal");

		        if (area) {
		            area.innerHTML = `
		                <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
		                    width="468"
		                    height="60"
		                    scrolling="no"
		                    frameborder="0"
		                    style="max-width:100%; border:0;">
		                </iframe>
		            `;
		        }
		    }
		});

        setTimeout(() => {
            botao.disabled = false;
            botao.innerText = textoOriginal;
        }, 2000);

    } catch (error) {
        console.error("Erro ao resgatar ataques:", error);

        botao.disabled = false;
        botao.innerText = "Resgatar";

        Swal.fire({
            customClass: {
                title: 'swal-game-error'
            },
            icon: 'error',
            title: 'Erro',
            html: `
                <div style="margin-bottom:10px;">
                    Não foi possível resgatar a recompensa de ataques.
                </div>

                <div class="modal-anuncio">
                    <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                        width="468"
                        height="60"
                        scrolling="no"
                        frameborder="0"
                        style="max-width:100%; border:0;">
                    </iframe>
                </div>
            `,
            timer: 8000,
            showConfirmButton: false,
            background: 'transparent',
            color: '#ff3b3b'
        });
    }
};



//================ RESGATAR PTC =================

window.resgatarPtc = async function (botao) {
    try {

        botao.disabled = true;

        const textoOriginal = botao.innerText;

        botao.innerText = "Resgatando...";

        const response = await fetch(
            `/api/missoes-diarias/${window.usuarioId}/resgatar/ptc`,
            {
                method: "POST"
            }
        );

        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }

        await carregarMissoes();

        Swal.fire({
            customClass: {
                title: 'swal-game-text'
            },
            icon: 'success',
            title: 'Recompensa resgatada!',
            html: `
                <div style="margin-bottom:10px;">
                    Você resgatou sua missão PTC.
                </div>

                <div id="areaAnuncioSwal"
                    class="modal-anuncio">
                </div>
            `,
            timer: 8000,
            showConfirmButton: false,
            background: 'transparent',
            color: '#ffb400',

            didOpen: () => {

                const area =
                    document.getElementById("areaAnuncioSwal");

                if (area) {

                    area.innerHTML = `
                        <iframe
                            src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                            width="468"
                            height="60"
                            scrolling="no"
                            frameborder="0"
                            style="max-width:100%; border:0;">
                        </iframe>
                    `;
                }
            }
        });

        setTimeout(() => {

            botao.disabled = false;

            botao.innerText = textoOriginal;

        }, 2000);

    } catch (error) {

        console.error("Erro ao resgatar ptc:", error);

        botao.disabled = false;

        botao.innerText = "Resgatar";

        Swal.fire({
            customClass: {
                title: 'swal-game-error'
            },
            icon: 'error',
            title: 'Erro',
            html: `
                <div style="margin-bottom:10px;">
                    Não foi possível resgatar
                    a recompensa PTC.
                </div>

                <div class="modal-anuncio">
                    <iframe
                        src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                        width="468"
                        height="60"
                        scrolling="no"
                        frameborder="0"
                        style="max-width:100%; border:0;">
                    </iframe>
                </div>
            `,
            timer: 8000,
            showConfirmButton: false,
            background: 'transparent',
            color: '#ff3b3b'
        });
    }
};
//================ ATUALIZAR PROGRESSO =================
//================ ATUALIZAR PTC =================

window.adicionarPtc = async function () {

    try {

        const response = await fetch(
            `/api/missoes-diarias/${window.usuarioId}/atualizar/ptc?quantidade=1`,
            {
                method: "POST"
            }
        );

        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }

        await carregarMissoes();

    } catch (error) {

        console.error("Erro ao adicionar ptc:", error);
    }
};

//================ ATUALIZAR DANO =================
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

//================ ATUALIZAR ATAQUES =================
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
 */