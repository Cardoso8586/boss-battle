
let ultimoHashHistorico = null;

document.addEventListener('DOMContentLoaded', () => {
    carregarHistoricoSaques(true);
    setInterval(() => carregarHistoricoSaques(false), 15000);
});

/* ===============================
   UTILIDADES
================================ */

function getUsuarioLogadoId() {
    const meta = document.querySelector('meta[name="user-id"]');
    return meta ? parseInt(meta.getAttribute('content')) : null;
}

function mascararEmail(email) {
    if (!email || !email.includes("@")) return "-";
    const [user, domain] = email.split("@");
    if (user.length <= 2) return `**@${domain}`;
    return `${user[0]}*****${user.slice(-2)}@${domain}`;
}

function formatarData(dataISO) {
    if (!dataISO) return "-";
    const d = new Date(dataISO);
    return isNaN(d) ? "-" : d.toLocaleString("pt-BR");
}

function formatarValor(valor) {
    const n = Number(valor);
    return isNaN(n) ? "-" : n.toFixed(8);
}

function gerarHash(lista) {
    return JSON.stringify(
        lista.map(i => `${i.id || ""}-${i.status}-${i.amount}`)
    );
}

/* ===============================
   CARREGAR HISTÓRICO
================================ */

async function carregarHistoricoSaques(primeiraVez = false) {
    const userId = getUsuarioLogadoId();
    const tbody = document.getElementById("historicoRetirada");

    if (!userId || !tbody) return;

    if (primeiraVez) {
        tbody.innerHTML = `
            <tr>
                <td colspan="5" style="text-align:center;padding:10px;">
                    Carregando histórico...
                </td>
            </tr>
        `;
    }

    try {
        const response = await fetch(`/api/faucetpay/historico?userId=${userId}`);
        if (!response.ok) throw new Error("Erro HTTP");

        const lista = await response.json();
        const hashAtual = gerarHash(lista);

        // 🔒 NÃO ATUALIZA SE NADA MUDOU
        if (hashAtual === ultimoHashHistorico) return;

        ultimoHashHistorico = hashAtual;
        atualizarHistoricoReal(tbody, lista);

    } catch (error) {
        console.error("Erro ao carregar histórico:", error);
        if (primeiraVez) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="5" style="text-align:center;color:red;">
                        Erro ao carregar histórico
                    </td>
                </tr>
            `;
        }
    }
}

/* ===============================
   RENDERIZAÇÃO
================================ */

function atualizarHistoricoReal(tbody, lista) {
    tbody.innerHTML = "";

    if (!lista || lista.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="5" style="text-align:center;padding:10px;">
                    Nenhuma retirada encontrada
                </td>
            </tr>
        `;
        return;
    }

    lista.sort((a, b) =>
        new Date(b.createdAt) - new Date(a.createdAt)
    );

    lista.forEach(item => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${item.currency || "USDT"}</td>
            <td>${formatarValor(item.amount)} ${item.currency}</td>
            <td>${mascararEmail(item.email)}</td>
            <td>${item.status || "-"}</td>
            <td>${formatarData(item.createdAt)}</td>
        `;
        tbody.appendChild(tr);
    });
}


