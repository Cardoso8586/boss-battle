let intervaloBadgeAnuncio = null;

const ANUNCIO_MENU_STATUS_CACHE_KEY = "anuncio_menu_status_cache";
const ANUNCIO_MENU_STATUS_CACHE_TTL = 60000; // 60 segundos

async function atualizarBadgeAnuncio() {
    const badge = document.getElementById("contadorAnuncioMenu");
    const link = document.getElementById("linkAnuncioMenu");
    const usuarioId = document.querySelector('meta[name="user-id"]')?.content;

    if (!badge || !link) return;

    if (intervaloBadgeAnuncio) {
        clearInterval(intervaloBadgeAnuncio);
        intervaloBadgeAnuncio = null;
    }

    let dadosStatus = pegarStatusAnuncioMenuCache();

    if (!dadosStatus && usuarioId) {
        try {
            const res = await fetch(`/api/anuncio-recompensa/status/${usuarioId}`);

            dadosStatus = await res.json();

            salvarStatusAnuncioMenuCache(dadosStatus);

        } catch (e) {
            console.error("Erro ao verificar limite do anúncio no menu:", e);
        }
    }

    if (dadosStatus && dadosStatus.limiteAtingido) {
        bloquearLinkLimiteDiario(link, badge);
        return;
    }

    const cooldownTempo = parseInt(localStorage.getItem("cooldownTempo"));
    const ultimoUso = parseInt(localStorage.getItem("ultimoAnuncioTempo"));

    if (!cooldownTempo || !ultimoUso) {
        liberarLinkAnuncio(link, badge);
        return;
    }

    function atualizar() {
        const agora = Date.now();
        const diff = Math.floor((agora - ultimoUso) / 1000);
        const restante = cooldownTempo - diff;

        if (restante <= 0) {
            clearInterval(intervaloBadgeAnuncio);
            intervaloBadgeAnuncio = null;

            liberarLinkAnuncio(link, badge);
            return;
        }

        bloquearLinkCooldown(link, badge, restante);
    }

    atualizar();

   intervaloBadgeAnuncio = setInterval(atualizar, 15000);
}

function salvarStatusAnuncioMenuCache(dados) {
    if (!dados) return;

    localStorage.setItem(ANUNCIO_MENU_STATUS_CACHE_KEY, JSON.stringify({
        time: Date.now(),
        data: dados
    }));
}

function pegarStatusAnuncioMenuCache() {
    try {
        const cache = JSON.parse(
            localStorage.getItem(ANUNCIO_MENU_STATUS_CACHE_KEY)
        );

        if (!cache) return null;

        if (Date.now() - cache.time > ANUNCIO_MENU_STATUS_CACHE_TTL) {
            localStorage.removeItem(ANUNCIO_MENU_STATUS_CACHE_KEY);
            return null;
        }

        return cache.data;

    } catch (e) {
        localStorage.removeItem(ANUNCIO_MENU_STATUS_CACHE_KEY);
        return null;
    }
}

function limparStatusAnuncioMenuCache() {
    localStorage.removeItem(ANUNCIO_MENU_STATUS_CACHE_KEY);
}

function bloquearLinkCooldown(link, badge, restante) {
    const min = Math.floor(restante / 60);
    const seg = restante % 60;

    badge.textContent = `${min}:${seg.toString().padStart(2, "0")}`;

    link.classList.add("menu-anuncio-bloqueado");
    link.classList.remove("menu-anuncio-limite");
    link.setAttribute("aria-disabled", "true");
}

function bloquearLinkLimiteDiario(link, badge) {
    badge.textContent = "20/20";

    link.classList.add("menu-anuncio-bloqueado");
    link.classList.add("menu-anuncio-limite");
    link.setAttribute("aria-disabled", "true");
}

function liberarLinkAnuncio(link, badge) {
    badge.textContent = "🎁";

    link.classList.remove("menu-anuncio-bloqueado");
    link.classList.remove("menu-anuncio-limite");
    link.removeAttribute("aria-disabled");
}

document.addEventListener("DOMContentLoaded", atualizarBadgeAnuncio);