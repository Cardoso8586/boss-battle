const KEEP_ALIVE_INTERVAL = 240000; // 4 minutos

async function manterSessaoAtiva() {
    try {
        await fetch("/api/session/ping", {
            method: "GET",
            credentials: "include"
        });

        console.log("Sessão ativa");

    } catch (e) {
        console.log("Falha ao manter sessão ativa");
    }
}

document.addEventListener("DOMContentLoaded", () => {
    manterSessaoAtiva();

    setInterval(manterSessaoAtiva, KEEP_ALIVE_INTERVAL);
});