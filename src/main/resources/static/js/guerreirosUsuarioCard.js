

let guerreirosUsuario = [];
let guerreiroAtualIndex = 0;
let statusUsuarioAtual = null;

const CACHE_TEMPO_GUERREIROS = 60 * 1000; // 1 minuto

document.addEventListener("DOMContentLoaded", async () => {
    await carregarStatusUsuario();
    await carregarGuerreirosUsuario();

    setInterval(async () => {
        await carregarStatusUsuario();
        await carregarGuerreirosUsuario(true);
    }, CACHE_TEMPO_GUERREIROS);
});

function obterUsuarioId() {
    return document.querySelector('meta[name="user-id"]')?.content;
}

function obterChaveCacheGuerreiros() {
    const usuarioId = obterUsuarioId();
    return `cache_guerreiros_usuario_${usuarioId}`;
}

function salvarCacheGuerreiros(dados) {
    const chave = obterChaveCacheGuerreiros();

    localStorage.setItem(chave, JSON.stringify({
        atualizadoEm: Date.now(),
        dados: dados
    }));
}

function buscarCacheGuerreiros() {
    const chave = obterChaveCacheGuerreiros();
    const cache = localStorage.getItem(chave);

    if (!cache) return null;

    try {
        const cacheObj = JSON.parse(cache);
        const expirou = Date.now() - cacheObj.atualizadoEm > CACHE_TEMPO_GUERREIROS;

        if (expirou) return null;

        return cacheObj.dados;
    } catch (e) {
        localStorage.removeItem(chave);
        return null;
    }
}

async function carregarStatusUsuario() {
    const usuarioId = obterUsuarioId();
    if (!usuarioId) return;

    try {
        const response = await fetch(`/api/atualizar/status/usuario/${usuarioId}`, {
            cache: "no-store"
        });

        if (!response.ok) return;

        statusUsuarioAtual = await response.json();

    } catch (error) {
        console.error("Erro ao carregar status do usuário:", error);
    }
}

async function carregarGuerreirosUsuario(forcarAtualizacao = false) {
    const usuarioId = obterUsuarioId();

    if (!usuarioId) {
        console.error("Usuário não encontrado no meta user-id.");
        return;
    }

    if (!forcarAtualizacao) {
        const cache = buscarCacheGuerreiros();

        if (cache && cache.length > 0) {
            guerreirosUsuario = cache;

            guerreiroAtualIndex = Math.floor(
                Math.random() * guerreirosUsuario.length
            );

            preCarregarImagensGuerreiros();
            renderizarGuerreiroUsuario(true);
            return;
        }
    }

    try {
        const response = await fetch(`/guerreiros/usuario/${usuarioId}`, {
            cache: "no-store"
        });

        if (!response.ok) {
            console.error("Erro ao buscar guerreiros:", response.status);
            return;
        }

        const dados = await response.json();

        if (!dados || dados.length === 0) {
            console.warn("Nenhum guerreiro encontrado.");
            return;
        }

        salvarCacheGuerreiros(dados);

        const guerreiroAnterior = guerreirosUsuario[guerreiroAtualIndex]?.nome;

        guerreirosUsuario = dados;

        const novoIndex = guerreirosUsuario.findIndex(g => g.nome === guerreiroAnterior);

        guerreiroAtualIndex = novoIndex >= 0 ? novoIndex : 0;

        preCarregarImagensGuerreiros();

        renderizarGuerreiroUsuario(false);

    } catch (error) {
        console.error("Erro ao carregar guerreiros:", error);
    }
}

function mudarGuerreiroUsuario(direcao) {
    if (!guerreirosUsuario || guerreirosUsuario.length === 0) return;

    guerreiroAtualIndex += direcao;

    if (guerreiroAtualIndex >= guerreirosUsuario.length) {
        guerreiroAtualIndex = 0;
    }

    if (guerreiroAtualIndex < 0) {
        guerreiroAtualIndex = guerreirosUsuario.length - 1;
    }

    renderizarGuerreiroUsuario(true);
}

function renderizarGuerreiroUsuario(animar = true) {
    const guerreiro = guerreirosUsuario[guerreiroAtualIndex];

    if (!guerreiro) return;

    const card = document.getElementById("userGuerreiroCard");
    const imagem = document.getElementById("userGuerreiroImagem");
    const nome = document.getElementById("userGuerreiroNome");
    const quantidade = document.getElementById("userGuerreiroQuantidade");

    if (nome) {
        nome.textContent = guerreiro.nome || "Guerreiro";
    }

    if (quantidade) {
        quantidade.textContent =
            "Quantidade: " + Number(guerreiro.quantidade || 0).toLocaleString("pt-BR");
    }

    if (!imagem) return;

    const novaImagem = obterImagemDoGuerreiro(guerreiro);

    if (animar && card) {
        card.classList.remove("guerreiro-show");
        card.classList.add("guerreiro-hide");

        setTimeout(() => {
            trocarImagem(imagem, novaImagem, guerreiro);
            card.classList.remove("guerreiro-hide");
            card.classList.add("guerreiro-show");
        }, 150);

    } else {
        trocarImagem(imagem, novaImagem, guerreiro);
    }
}

function trocarImagem(img, src, guerreiro) {
    img.onerror = () => {
        img.onerror = null;
        img.src = obterImagemPadrao(guerreiro);
    };

    if (img.src !== window.location.origin + src) {
        img.src = src;
    }
}

function ehGuerreiroAntigo(guerreiro) {
    if (!guerreiro) return false;

    return guerreiro.elite === false ||
           guerreiro.nome === "Guerreiro Tradicional" ||
           guerreiro.id === 0;
}

function obterImagemDoGuerreiro(guerreiro) {
    const espadaAtiva = (statusUsuarioAtual?.ativaEspadaFlanejante ?? 0) > 0;
    const machadoAtivo = (statusUsuarioAtual?.ativarMachadoDilacerador ?? 0) > 0;
    const escudoAtivo = (statusUsuarioAtual?.ativarEscudoPrimordial ?? 0) > 0;

    if (ehGuerreiroAntigo(guerreiro)) {
        return obterImagemGuerreiroAntigo(espadaAtiva, machadoAtivo, escudoAtivo);
    }

    return obterImagemGuerreiroElite(guerreiro, espadaAtiva, machadoAtivo, escudoAtivo);
}

function obterImagemGuerreiroAntigo(espadaAtiva, machadoAtivo, escudoAtivo) {
    if (escudoAtivo && espadaAtiva) return "/icones/guerreiro_escudo_espada.webp";
    if (escudoAtivo && machadoAtivo) return "/icones/guerreiro_escudo_machado.webp";
    if (escudoAtivo) return "/icones/guerreiro_escudo_ativo.webp";
    if (espadaAtiva) return "/icones/guerreiroPadrao_espadaFlanejante.webp";
    if (machadoAtivo) return "/icones/guerreiroPadrao_machadoDilacerador.webp";

    return "/images/guerreiro_padrao.webp";
}

function obterImagemGuerreiroElite(guerreiro, espadaAtiva, machadoAtivo, escudoAtivo) {
    const pasta = obterPastaGuerreiro(guerreiro);
    const prefixo = obterPrefixoArquivo(guerreiro);

    if (escudoAtivo && espadaAtiva) return `${pasta}/${prefixo}_escudo_espada.webp`;
    if (escudoAtivo && machadoAtivo) return `${pasta}/${prefixo}_escudo_machado.webp`;
    if (escudoAtivo) return `${pasta}/${prefixo}_escudo.webp`;
    if (espadaAtiva) return `${pasta}/${prefixo}_espada.webp`;
    if (machadoAtivo) return `${pasta}/${prefixo}_machado.webp`;

    return `${pasta}/${prefixo}.webp`;
}

function obterImagemPadrao(guerreiro) {
    if (ehGuerreiroAntigo(guerreiro)) {
        return "/images/guerreiro_padrao.webp";
    }

    const pasta = obterPastaGuerreiro(guerreiro);
    const prefixo = obterPrefixoArquivo(guerreiro);

    return `${pasta}/${prefixo}_padrao.webp`;
}

function obterPastaGuerreiro(guerreiro) {
    const nome = guerreiro?.nome || "Guerreira Mística" || "Guerreiro Guardião";

    switch (nome) {
        case "Guerreira Mística":
            return "/images/guerreiros_elite/guerreira_mistica";

        case "Valgard":
            return "/images/guerreiros_elite/guerreiro_valgard";

	    case "Caçador de Boss":
			return "/images/guerreiros_elite/cacador_boss";	
			
	    case "Arqueira Real":
		    return "/images/guerreiros_elite/arqueira_real";
				
	    case "Guerreiro Guardião":
		    return "/images/guerreiros_elite/guerreiro_guardiao";	
					
						
        default:
            return `/images/guerreiros_elite/${normalizarParaPasta(nome)}`;
    }
}

function obterPrefixoArquivo(guerreiro) {
    const nome = guerreiro?.nome || "Guerreira Mística" || "Guerreiro Guardião";

    switch (nome) {
        case "Guerreira Mística":
            return "guerreira-mistica";

        case "Valgard":
            return "valgard";

	    case "Caçador de Boss":
			return "cacador-boss";
				
	    case "Arqueira Real":
			return "arqueira-real";	
			
		case "Guerreiro Guardião":
			return "guerreiro-guardiao";				
			
        default:
            return normalizarParaArquivo(nome);
    }
}

function normalizarParaPasta(nome) {
    return nome
        .toLowerCase()
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "")
        .replace(/ç/g, "c")
        .replace(/[^a-z0-9]+/g, "_")
        .replace(/^_+|_+$/g, "");
}

function normalizarParaArquivo(nome) {
    return nome
        .toLowerCase()
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "")
        .replace(/ç/g, "c")
        .replace(/[^a-z0-9]+/g, "-")
        .replace(/^-+|-+$/g, "");
}

function preCarregarImagensGuerreiros() {
    guerreirosUsuario.forEach(guerreiro => {
        if (ehGuerreiroAntigo(guerreiro)) {
            [
                "/icones/guerreiro_escudo_espada.webp",
                "/icones/guerreiro_escudo_machado.webp",
                "/icones/guerreiro_escudo_ativo.webp",
                "/icones/guerreiroPadrao_espadaFlanejante.webp",
                "/icones/guerreiroPadrao_machadoDilacerador.webp",
                "/images/guerreiro_padrao.webp"
            ].forEach(preloadImagem);

            return;
        }

        const pasta = obterPastaGuerreiro(guerreiro);
        const prefixo = obterPrefixoArquivo(guerreiro);

        [
            `${pasta}/${prefixo}.webp`,
            `${pasta}/${prefixo}_padrao.webp`,
            `${pasta}/${prefixo}_espada.webp`,
            `${pasta}/${prefixo}_machado.webp`,
            `${pasta}/${prefixo}_escudo.webp`,
            `${pasta}/${prefixo}_escudo_espada.webp`,
            `${pasta}/${prefixo}_escudo_machado.webp`
        ].forEach(preloadImagem);
    });
}

function preloadImagem(src) {
    const img = new Image();
    img.src = src;
}

/*
let guerreirosUsuario = [];
let guerreiroAtualIndex = 0;
let statusUsuarioAtual = null;

document.addEventListener("DOMContentLoaded", async () => {
    await carregarStatusUsuario();
    await carregarGuerreirosUsuario();

    setInterval(async () => {
        await carregarStatusUsuario();
        renderizarGuerreiroUsuario(false);
    }, 60000);
});

async function carregarStatusUsuario() {
    const usuarioId = document.querySelector('meta[name="user-id"]')?.content;
    if (!usuarioId) return;

    try {
        const response = await fetch(`/api/atualizar/status/usuario/${usuarioId}`, {
            cache: "no-store"
        });

        if (!response.ok) return;

        statusUsuarioAtual = await response.json();

    } catch (error) {
        console.error("Erro ao carregar status do usuário:", error);
    }
}

async function carregarGuerreirosUsuario() {
    const usuarioId = document.querySelector('meta[name="user-id"]')?.content;

    if (!usuarioId) {
        console.error("Usuário não encontrado no meta user-id.");
        return;
    }

    try {
        const response = await fetch(`/guerreiros/usuario/${usuarioId}`, {
            cache: "no-store"
        });

        if (!response.ok) {
            console.error("Erro ao buscar guerreiros:", response.status);
            return;
        }

        const dados = await response.json();

        if (!dados || dados.length === 0) {
            console.warn("Nenhum guerreiro encontrado.");
            return;
        }

        guerreirosUsuario = dados;

        guerreiroAtualIndex = Math.floor(
            Math.random() * guerreirosUsuario.length
        );

        preCarregarImagensGuerreiros();

        renderizarGuerreiroUsuario(true);

    } catch (error) {
        console.error("Erro ao carregar guerreiros:", error);
    }
}

function mudarGuerreiroUsuario(direcao) {
    if (!guerreirosUsuario || guerreirosUsuario.length === 0) return;

    guerreiroAtualIndex += direcao;

    if (guerreiroAtualIndex >= guerreirosUsuario.length) {
        guerreiroAtualIndex = 0;
    }

    if (guerreiroAtualIndex < 0) {
        guerreiroAtualIndex = guerreirosUsuario.length - 1;
    }

    renderizarGuerreiroUsuario(true);
}

function renderizarGuerreiroUsuario(animar = true) {
    const guerreiro = guerreirosUsuario[guerreiroAtualIndex];

    if (!guerreiro) return;

    const card = document.getElementById("userGuerreiroCard");
    const imagem = document.getElementById("userGuerreiroImagem");
    const nome = document.getElementById("userGuerreiroNome");
    const quantidade = document.getElementById("userGuerreiroQuantidade");

    if (nome) {
        nome.textContent = guerreiro.nome || "Guerreiro";
    }

    if (quantidade) {
        quantidade.textContent =
            "Quantidade: " + Number(guerreiro.quantidade || 0).toLocaleString("pt-BR");
    }

    if (!imagem) return;

    const novaImagem = obterImagemDoGuerreiro(guerreiro);

    if (animar && card) {
        card.classList.remove("guerreiro-show");
        card.classList.add("guerreiro-hide");

        setTimeout(() => {
            trocarImagem(imagem, novaImagem, guerreiro);
            card.classList.remove("guerreiro-hide");
            card.classList.add("guerreiro-show");
        }, 150);

    } else {
        trocarImagem(imagem, novaImagem, guerreiro);
    }
}



function trocarImagem(img, src, guerreiro) {
    img.onerror = () => {
        img.onerror = null;
        img.src = obterImagemPadrao(guerreiro);
    };

    img.src = src;
}

function ehGuerreiroAntigo(guerreiro) {
    if (!guerreiro) return false;

    return guerreiro.elite === false ||
           guerreiro.nome === "Guerreiro Tradicional" ||
           guerreiro.id === 0;
}

function obterImagemDoGuerreiro(guerreiro) {
    const espadaAtiva = (statusUsuarioAtual?.ativaEspadaFlanejante ?? 0) > 0;
    const machadoAtivo = (statusUsuarioAtual?.ativarMachadoDilacerador ?? 0) > 0;
    const escudoAtivo = (statusUsuarioAtual?.ativarEscudoPrimordial ?? 0) > 0;

    if (ehGuerreiroAntigo(guerreiro)) {
        return obterImagemGuerreiroAntigo(espadaAtiva, machadoAtivo, escudoAtivo);
    }

    return obterImagemGuerreiroElite(guerreiro, espadaAtiva, machadoAtivo, escudoAtivo);
}

function obterImagemGuerreiroAntigo(espadaAtiva, machadoAtivo, escudoAtivo) {

    if (escudoAtivo && espadaAtiva) {
        return "/icones/guerreiro_escudo_espada.webp";
    }

    if (escudoAtivo && machadoAtivo) {
        return "/icones/guerreiro_escudo_machado.webp";
    }

    if (escudoAtivo) {
        return "/icones/guerreiro_escudo_ativo.webp";
    }

    if (espadaAtiva) {
        return "/icones/guerreiroPadrao_espadaFlanejante.webp";
    }

    if (machadoAtivo) {
        return "/icones/guerreiroPadrao_machadoDilacerador.webp";
    }

    return "/images/guerreiro_padrao.webp";
}

function obterImagemGuerreiroElite(guerreiro, espadaAtiva, machadoAtivo, escudoAtivo) {
    const pasta = obterPastaGuerreiro(guerreiro);
    const prefixo = obterPrefixoArquivo(guerreiro);

    if (escudoAtivo && espadaAtiva) {
        return `${pasta}/${prefixo}_escudo_espada.webp`;
    }

    if (escudoAtivo && machadoAtivo) {
        return `${pasta}/${prefixo}_escudo_machado.webp`;
    }

    if (escudoAtivo) {
        return `${pasta}/${prefixo}_escudo.webp`;
    }

    if (espadaAtiva) {
        return `${pasta}/${prefixo}_espada.webp`;
    }

    if (machadoAtivo) {
        return `${pasta}/${prefixo}_machado.webp`;
    }

    return `${pasta}/${prefixo}.webp`;
}
function obterImagemPadrao(guerreiro) {
    if (ehGuerreiroAntigo(guerreiro)) {
        return "/images/guerreiro_padrao.webp";
    }

    const pasta = obterPastaGuerreiro(guerreiro);
    const prefixo = obterPrefixoArquivo(guerreiro);

    return `${pasta}/${prefixo}_padrao.webp`;
}

function obterPastaGuerreiro(guerreiro) {
    const nome = guerreiro?.nome || "Guerreira Mística";

    switch (nome) {
        case "Guerreira Mística":
            return "/images/guerreiros_elite/guerreira_mistica";

       case "Valgard":
			return "/images/guerreiros_elite/guerreiro_valgard";

        default:
            return `/images/guerreiros_elite/${normalizarParaPasta(nome)}`;
    }
}

function obterPrefixoArquivo(guerreiro) {
    const nome = guerreiro?.nome || "Guerreira Mística";

    switch (nome) {
        case "Guerreira Mística":
            return "guerreira-mistica";

        case "Valgard":
            return "valgard";

        default:
            return normalizarParaArquivo(nome);
    }
}

function normalizarParaPasta(nome) {
    return nome
        .toLowerCase()
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "")
        .replace(/ç/g, "c")
        .replace(/[^a-z0-9]+/g, "_")
        .replace(/^_+|_+$/g, "");
}

function normalizarParaArquivo(nome) {
    return nome
        .toLowerCase()
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "")
        .replace(/ç/g, "c")
        .replace(/[^a-z0-9]+/g, "-")
        .replace(/^-+|-+$/g, "");
}

function preCarregarImagensGuerreiros() {
    guerreirosUsuario.forEach(guerreiro => {

        if (ehGuerreiroAntigo(guerreiro)) {
            [
                "/icones/guerreiro_escudo_espada.webp",
                "/icones/guerreiro_escudo_machado.webp",
                "/icones/guerreiro_escudo_ativo.webp",
                "/icones/guerreiroPadrao_espadaFlanejante.webp",
                "/icones/guerreiroPadrao_machadoDilacerador.webp",
                "/images/guerreiro_padrao.webp"
            ].forEach(src => {
                const img = new Image();
                img.src = src;
            });

            return;
        }

        const pasta = obterPastaGuerreiro(guerreiro);
        const prefixo = obterPrefixoArquivo(guerreiro);

        [
            `${prefixo}.webp`,
            `${prefixo}_espada.webp`,
            `${prefixo}_machado.webp`,
            `${prefixo}_escudo.webp`,
            `${prefixo}_escudo_espada.webp`,
            `${prefixo}_escudo_machado.webp`
        ].forEach(arquivo => {
            const img = new Image();
            img.src = `${pasta}/${arquivo}`;
        });
    });
}
*/