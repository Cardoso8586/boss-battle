document.addEventListener("DOMContentLoaded", () => {
    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? Number(meta.content) : null;
    if (!usuarioId) return;

    const btnFlechas = {
        FERRO: document.getElementById("btnColocarFlechaFerro"),
        FOGO: document.getElementById("btnColocarFlechaFogo"),
        VENENO: document.getElementById("btnColocarFlechaVeneno"),
        DIAMANTE: document.getElementById("btnColocarFlechaDiamante")
    };

    const arcoAtivoElem = document.getElementById("arcoAtivo");
    const aljavaCount = document.getElementById("aljavaCount");
    const tipoFlechaAtiva = document.getElementById("tipoFlechaAtiva");

    const aljavaErroImg = "icones/erro_img/aljava_erro.webp";
    const aljavaImg = "icones/ok_img/aljava_ok.webp";

    let estoque = { FERRO: 0, FOGO: 0, VENENO: 0, DIAMANTE: 0 };
    let acaoEmAndamento = false;

    function mostrarErro(titulo, texto = "") {
        Swal.fire({
            customClass: { title: 'swal-game-error' },
            imageUrl: aljavaErroImg,
            imageWidth: 90,
            imageHeight: 120,
            title: titulo,
            text: texto,
            timer: 5000,
            showConfirmButton: false,
            background: 'transparent',
            color: '#ff3b3b'
        });
    }

    function mostrarSucesso(titulo, texto = "") {
        Swal.fire({
            customClass: { title: 'swal-game-text' },
            imageUrl: aljavaImg,
            imageWidth: 90,
            imageHeight: 120,
            title: titulo,
            text: texto,
            timer: 5000,
            showConfirmButton: false,
            background: 'transparent',
            color: '#ffb400'
        });
    }

    function travarBotao(btn) {
        btn.dataset.loading = "true";
        btn.disabled = true;
        btn.style.pointerEvents = "none";
    }

    function destravarBotao(btn) {
        btn.dataset.loading = "false";
        btn.disabled = false;
        btn.style.pointerEvents = "auto";
    }

    async function atualizarStatus() {
        if (acaoEmAndamento) return;

        try {
            const res = await fetch(`/api/atualizar/status/usuario/${usuarioId}`);
            const data = await res.json();

            arcoAtivoElem.textContent = data.arcoAtivo;
            aljavaCount.textContent = data.aljava;
            tipoFlechaAtiva.textContent = data.aljava > 0 ? data.tipoFlecha : "-";

            const espadaAtiva = data.ativaEspadaFlanejante ?? 0;
            const machadoAtivo = data.ativarMachadoDilacerador ?? 0;

            estoque.FERRO = data.flechaFerro || 0;
            estoque.FOGO = data.flechaFogo || 0;
            estoque.VENENO = data.flechaVeneno || 0;
            estoque.DIAMANTE = data.flechaDiamante || 0;

            const tipoEquipado = data.tipoFlecha || null;

            Object.entries(btnFlechas).forEach(([tipo, btn]) => {
                if (!btn) return;

                const container = btn.nextElementSibling;
                if (!container || !container.classList.contains("flecha-control")) return;

                const input = container.querySelector("input");
                const maxLabel = container.querySelector("span");

                const quantidade = estoque[tipo];
                const durabilidade = data.durabilidadeArco ?? 0;
                const arcoQuebrado = durabilidade <= 0;
                const temArmasAtivas = espadaAtiva > 0 || machadoAtivo > 0;

                btn.disabled = false;
                input.disabled = false;
                btn.classList.remove("flecha-ativa", "flecha-inativa", "hidden");
                container.classList.remove("hidden");

                if (temArmasAtivas) {
                    btn.classList.add("hidden");
                    container.classList.add("hidden");
                    btn.disabled = true;
                    input.disabled = true;
                    return;
                }

                if (quantidade <= 0) {
                    btn.classList.add("hidden");
                    container.classList.add("hidden");
                    btn.disabled = true;
                    input.disabled = true;
                    return;
                }

                input.max = quantidade;
                if (!input.value || Number(input.value) > quantidade) {
                    input.value = quantidade;
                }

                maxLabel.textContent = ` / ${quantidade} disponíveis`;

                if (arcoQuebrado && data.aljava > 0) {
                    btn.disabled = true;
                    input.disabled = true;
                    btn.classList.add("flecha-inativa");
                    btn.classList.remove("flecha-ativa");
                    return;
                }

                if (tipoEquipado) {
                    const ativo = tipo === tipoEquipado;
                    btn.disabled = !ativo;
                    input.disabled = !ativo;
                    btn.classList.toggle("flecha-ativa", ativo);
                    btn.classList.toggle("flecha-inativa", !ativo);
                } else {
                    btn.disabled = false;
                    input.disabled = false;
                    btn.classList.remove("flecha-ativa", "flecha-inativa");
                }
            });

        } catch (err) {
            console.error("Erro ao atualizar status:", err);
        }
    }

    Object.entries(btnFlechas).forEach(([tipo, btn]) => {
        if (!btn) return;

        const container = document.createElement("div");
        container.classList.add("flecha-control");
        container.style.display = "flex";
        container.style.alignItems = "center";
        container.style.marginTop = "5px";

        const input = document.createElement("input");
        input.type = "number";
        input.min = 1;
        input.style.width = "100px";
        input.style.marginRight = "5px";

        const maxLabel = document.createElement("span");
        maxLabel.textContent = ` / 0 disponíveis`;

        container.appendChild(input);
        container.appendChild(maxLabel);
        btn.parentNode.insertBefore(container, btn.nextSibling);

        btn.addEventListener("click", async () => {
            if (acaoEmAndamento) return;
            if (btn.dataset.loading === "true") return;

            const quantidade = Number(input.value);

            if (!quantidade || quantidade <= 0) {
                mostrarErro("Quantidade inválida!");
                return;
            }

            if (quantidade > estoque[tipo]) {
                mostrarErro(`Não há flechas de ${tipo} suficientes!`);
                return;
            }

            acaoEmAndamento = true;
            travarBotao(btn);

            try {
                const res = await fetch("/aljava/colocar", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ usuarioId, tipo, quantidade })
                });

                const data = await res.json();

                if (!res.ok) {
                    throw new Error(data.error || "Erro ao colocar flechas.");
                }

                estoque[tipo] -= quantidade;

                aljavaCount.textContent = data.aljava;
                tipoFlechaAtiva.textContent = data.tipoFlecha || "-";

                if (quantidade <= 1) {
                    mostrarSucesso(
                        `Colocada ${quantidade} flecha de ${tipo} na aljava!`,
                        "Colocando na Aljava!"
                    );
                } else {
                    mostrarSucesso(
                        `Colocadas ${quantidade} flechas de ${tipo} na aljava!`,
                        "Colocando na Aljava!"
                    );
                }

                await atualizarStatus();

            } catch (err) {
                console.error(err);
                mostrarErro(
                    "Não foi possível colocar as flechas.",
                    "Verifique se há outra arma equipada, flechas incompatíveis ou se há Arco equipado."
                );
            } finally {
                destravarBotao(btn);

                setTimeout(() => {
                    acaoEmAndamento = false;
                    atualizarStatus();
                }, 300);
            }
        });
    });

    atualizarStatus();
    setInterval(() => {
        atualizarStatus();
    }, 5000);
});