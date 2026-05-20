// session-keepalive.js

const KEEP_ALIVE_INTERVAL = 240000;

async function manterSessaoAtiva() {

    try {

        await fetch("/api/session/ping", {
            method: "GET",
            credentials: "include"
        });

        console.log("Sessão ativa");

    } catch (e) {

        console.log("Falha keep alive");

    }
}

document.addEventListener("DOMContentLoaded", () => {

    manterSessaoAtiva();

    setInterval(manterSessaoAtiva, KEEP_ALIVE_INTERVAL);

});