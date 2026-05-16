document.addEventListener('DOMContentLoaded', async () => {

    const meta =
        document.querySelector(
            'meta[name="user-id"]'
        );

    const usuarioId =
        meta
            ? Number(meta.content)
            : null;

    if (!usuarioId) return;

    // ==============================
    // ELEMENTOS
    // ==============================
    const escudoSpan =
        document.getElementById(
            'escudoPrimordial'
        );

    const btnAtivarEscudo =
        document.getElementById(
            'btnAtivarEscudoPrimordial'
        );

    const btnEquiparArco =
        document.getElementById(
            'btnEquiparArco'
        );

    const escudoInfos =
        document.querySelectorAll(
            '.escudo-primordial-ativo-info'
        );

    // ==============================
    // FORMATAÇÃO
    // ==============================
    const formatarNumero = n =>

        new Intl.NumberFormat(
            'pt-BR'
        ).format(n);

    // ==============================
    // CONTROLE
    // ==============================
    let emCooldown = false;

    // ==============================
    // ALERTA PADRÃO
    // ==============================
    function mostrarAlerta({

        icon = 'warning',

        title = 'Aviso',

        text = '',

        color = '#ff3b3b',

        titleClass = 'swal-game-text'

    }) {

        Swal.fire({

            customClass: {
                title: titleClass
            },

            icon,

            title,

            text,

            html: `
                <div class="modal-anuncio">
                    <iframe
                        src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                        width="468"
                        height="60"
                        scrolling="no"
                        frameborder="0">
                    </iframe>
                </div>
            `,

            background: 'transparent',

            color
        });
    }

    // ==============================
    // ATUALIZA ESCUDO
    // ==============================
    window.atualizarEscudoPrimordial =
        function(status) {

            const escudoEstoque =
                status.escudoPrimordial ?? 0;

            const escudoAtivo =
                status.ativarEscudoPrimordial ?? 0;

            const arcoAtivo =
                status.arcoAtivo ?? 0;

            const podeAtivar =

                escudoEstoque > 0 &&
                escudoAtivo === 0 &&
                arcoAtivo === 0;

            const escudoItem =
                escudoSpan?.closest(
                    '.nucleo-item-escudo'
                );

            // ==============================
            // ITEM
            // ==============================
            if (
                escudoItem &&
                escudoSpan
            ) {

                if (
                    escudoEstoque === 0
                ) {

                    escudoItem
                        .classList
                        .add('hidden');

                } else {

                    escudoItem
                        .classList
                        .remove('hidden');

                    escudoSpan.textContent =
                        formatarNumero(
                            escudoEstoque
                        );
                }
            }

            // ==============================
            // BOTÃO ESCUDO
            // ==============================
            if (btnAtivarEscudo) {

                btnAtivarEscudo
                    .classList
                    .toggle(
                        'hidden',
                        !podeAtivar
                    );

                btnAtivarEscudo.disabled =

                    !podeAtivar ||

                    emCooldown;
            }

            // ==============================
            // BOTÃO ARCO
            // ==============================
            if (btnEquiparArco) {

                if (escudoAtivo > 0) {

                    btnEquiparArco.disabled =
                        true;

                    btnEquiparArco
                        .classList
                        .add('hidden');

                } else {

                    btnEquiparArco.disabled =
                        false;
                }
            }

            // ==============================
            // INFO
            // ==============================
            escudoInfos.forEach(div => {

                if (escudoAtivo > 0) {

                    div
                        .classList
                        .remove('hidden');

                    div.textContent =

                        `✔ Escudo Primordial equipado (${escudoAtivo})`;

                } else {

                    div
                        .classList
                        .add('hidden');
                }
            });
        };

    // ==============================
    // ATIVAR ESCUDO
    // ==============================
    if (btnAtivarEscudo) {

        btnAtivarEscudo.addEventListener(
            'click',
            async () => {

                if (emCooldown)
                    return;

                emCooldown = true;

                btnAtivarEscudo.disabled =
                    true;

                const textoOriginal =
                    btnAtivarEscudo.innerText;

                btnAtivarEscudo.innerText =
                    'Ativando...';

                try {

                    // ==============================
                    // STATUS
                    // ==============================
                    const res1 =
                        await fetch(
                            `/api/atualizar/status/ajustes/${usuarioId}`
                        );

                    if (!res1.ok) {

                        throw new Error(
                            'Falha ao consultar status.'
                        );
                    }

                    const status =
                        await res1.json();

                    const ativoGuerreiro =
                        status.ativoGuerreiro ?? 0;

                    const arcoAtivo =
                        status.arcoAtivo ?? 0;

                    const escudoAtivo =
                        status.qtdEscudoPrimordialAtivo ?? 0;

                    // ==============================
                    // SEM GUERREIRO
                    // ==============================
                    if (
                        ativoGuerreiro <= 0
                    ) {

                        mostrarAlerta({

                            icon: 'warning',

                            title:
                                'Ação bloqueada',

                            text:
                                'Você não pode equipar o escudo agora.',

                            color:
                                '#ff3b3b',

                            titleClass:
                                'swal-game-text'
                        });

                        return;
                    }

                    // ==============================
                    // ARCO EQUIPADO
                    // ==============================
                    if (arcoAtivo > 0) {

                        mostrarAlerta({

                            icon: 'warning',

                            title:
                                'Equipamento incompatível',

                            text:
                                'Desequipe o arco antes de equipar o escudo.',

                            color:
                                '#ff3b3b',

                            titleClass:
                                'swal-game-error'
                        });

                        return;
                    }

                    // ==============================
                    // ESCUDO JÁ EQUIPADO
                    // ==============================
                    if (escudoAtivo > 0) {

                        mostrarAlerta({

                            icon: 'warning',

                            title:
                                'Escudo já equipado',

                            text:
                                'Você já possui um escudo equipado.',

                            color:
                                '#ff3b3b',

                            titleClass:
                                'swal-game-error'
                        });

                        return;
                    }

                    // ==============================
                    // EQUIPAR
                    // ==============================
                    const res =
                        await fetch(
                            `/api/escudo-primordial/ativar?usuarioId=${usuarioId}&quantidade=1`,
                            {
                                method: 'POST'
                            }
                        );

                    // ==============================
                    // ERRO
                    // ==============================
                    if (!res.ok) {

                        const erro =
                            await res.text();

                        mostrarAlerta({

                            icon: 'warning',

                            title:
                                'Erro',

                            text:
                                erro ||

                                'Não foi possível equipar o escudo.',

                            color:
                                '#ff3b3b',

                            titleClass:
                                'swal-game-error'
                        });

                        return;
                    }

                    // ==============================
                    // SUCESSO
                    // ==============================
                    Swal.fire({

                        customClass: {
                            title:
                                'swal-game-text'
                        },

                        icon: 'success',

                        title:
                            'Escudo equipado!',

                        text:
                            'Seu Escudo Primordial foi equipado com sucesso.',

                        html: `
                            <div class="modal-anuncio">
                                <iframe
                                    src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                                    width="468"
                                    height="60"
                                    scrolling="no"
                                    frameborder="0">
                                </iframe>
                            </div>
                        `,

                        timer: 7000,

                        showConfirmButton:
                            false,

                        background:
                            'transparent',

                        color:
                            '#ffb400'
                    });

                } catch (e) {

                    console.error(
                        'Erro ao tentar equipar o escudo:',
                        e
                    );

                    mostrarAlerta({

                        icon: 'error',

                        title: 'Erro',

                        text:
                            'Erro ao tentar equipar o escudo.',

                        color:
                            '#ff3b3b',

                        titleClass:
                            'swal-game-error'
                    });

                } finally {

                    // ==============================
                    // UPDATE GLOBAL
                    // ==============================
                    await atualizarTudo(
                        usuarioId
                    );

                    emCooldown = false;

                    btnAtivarEscudo.disabled =
                        false;

                    btnAtivarEscudo.innerText =
                        textoOriginal;
                }
            }
        );
    }

    // ==============================
    // PRIMEIRO LOAD
    // ==============================
    await atualizarTudo(usuarioId);

});

/**
document.addEventListener('DOMContentLoaded', () => {
    const meta = document.querySelector('meta[name="user-id"]');
    const usuarioId = meta ? Number(meta.content) : null;
    if (!usuarioId) return;

    // ==============================
    // ELEMENTOS
    // ==============================
    const escudoSpan = document.getElementById('escudoPrimordial');
    const btnAtivarEscudo = document.getElementById('btnAtivarEscudoPrimordial');
    const escudoInfos = document.querySelectorAll('.escudo-primordial-ativo-info');

    // ==============================
    // FORMATAÇÃO
    // ==============================
    const formatarNumero = n => new Intl.NumberFormat('pt-BR').format(n);

    // ==============================
    // CONTROLE
    // ==============================
    let emCooldown = false;
    const tempoCooldown = 4;

    // ==============================
    // ALERTA PADRÃO
    // ==============================
    function mostrarAlerta({
        icon = 'warning',
        title = 'Aviso',
        text = '',
        color = '#ff3b3b',
        titleClass = 'swal-game-text'
    }) {
        Swal.fire({
            customClass: {
                title: titleClass
            },
            icon,
            title,
            text,
            html: `
                <div class="modal-anuncio">
                    <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                        width="468"
                        height="60"
                        scrolling="no"
                        frameborder="0">
                    </iframe>
                </div>
            `,
            background: 'transparent',
            color
        });
    }

    // ==============================
    // ATUALIZAR USUÁRIO
    // ==============================
    async function atualizarUsuario() {
        try {
            const res = await fetch(`/api/atualizar/status/ajustes/${usuarioId}`);
            if (!res.ok) return;

            const status = await res.json();
            atualizarEscudoPrimordial(status);

        } catch (e) {
            console.error('Erro ao atualizar escudo:', e);
        }
    }

    // ==============================
    // ATUALIZA ESCUDO
    // ==============================
	function atualizarEscudoPrimordial(status) {

	    const escudoEstoque = status.escudoPrimordial ?? 0;
	    const escudoAtivo = status.ativarEscudoPrimordial ?? 0;
	    const arcoAtivo = status.arcoAtivo ?? 0;

	    // ⚔️ OUTRAS ARMAS
	   // const espadaAtiva = status.espadaFlanejanteAtiva ?? 0;
	   // const machadoAtivo = status.qtdMachadoDilaceradorAtivo ?? 0;

	    const podeAtivar =
	        escudoEstoque > 0 &&
	        escudoAtivo === 0 &&
	        arcoAtivo === 0 ;
	     //   espadaAtiva === 0 &&
	       // machadoAtivo === 0;

	    const escudoItem = escudoSpan?.closest('.nucleo-item-escudo');

	    // ITEM
	    if (escudoItem && escudoSpan) {
	        if (escudoEstoque === 0) {
	            escudoItem.classList.add('hidden');
	        } else {
	            escudoItem.classList.remove('hidden');
	            escudoSpan.textContent = formatarNumero(escudoEstoque);
	        }
	    }

	    // 🔥 BOTÃO (AGORA COM TRAVA REAL)
	    if (btnAtivarEscudo) {
	        btnAtivarEscudo.classList.toggle('hidden', !podeAtivar);
	        btnAtivarEscudo.disabled = !podeAtivar || emCooldown;
	    }

	    // INFO
	    escudoInfos.forEach(div => {
	        if (escudoAtivo > 0) {
	            div.classList.remove('hidden');
	            div.textContent = `✔ Escudo Primordial equipado (${escudoAtivo})`;
	        } else {
	            div.classList.add('hidden');
	        }
	    });
	}

    // ==============================
    // ATIVAR ESCUDO
    // ==============================
    if (btnAtivarEscudo) {
        btnAtivarEscudo.addEventListener('click', async () => {
            if (emCooldown) return;

            emCooldown = true;
            btnAtivarEscudo.disabled = true;

            const textoOriginal = btnAtivarEscudo.innerText;
            btnAtivarEscudo.innerText = 'Ativando...';

            try {
                const res1 = await fetch(`/api/atualizar/status/ajustes/${usuarioId}`);
                if (!res1.ok) {
                    throw new Error('Falha ao consultar status do usuário.');
                }

                const status = await res1.json();
                const ativoGuerreiro = status.ativoGuerreiro ?? 0;
                const arcoAtivo = status.arcoAtivo ?? 0;
                const escudoAtivo = status.qtdEscudoPrimordialAtivo ?? 0;

                if (ativoGuerreiro <= 0) {
                    mostrarAlerta({
                        icon: 'warning',
                        title: 'Ação bloqueada',
                        text: 'Você não pode equipar o escudo agora.',
                        color: '#ff3b3b',
                        titleClass: 'swal-game-text'
                    });
                    return;
                }

                if (arcoAtivo > 0) {
                    mostrarAlerta({
                        icon: 'warning',
                        title: 'Equipamento incompatível',
                        text: 'Desequipe o arco antes de equipar o escudo.',
                        color: '#ff3b3b',
                        titleClass: 'swal-game-error'
                    });
                    return;
                }

                if (escudoAtivo > 0) {
                    mostrarAlerta({
                        icon: 'warning',
                        title: 'Escudo já equipado',
                        text: 'Você já possui um escudo equipado.',
                        color: '#ff3b3b',
                        titleClass: 'swal-game-error'
                    });
                    return;
                }

                const res = await fetch(
                    `/api/escudo-primordial/ativar?usuarioId=${usuarioId}&quantidade=1`,
                    { method: 'POST' }
                );

                if (!res.ok) {
                    const erro = await res.text();

                    mostrarAlerta({
                        icon: 'warning',
                        title: 'Erro',
                        text: erro || 'Não foi possível equipar o escudo.',
                        color: '#ff3b3b',
                        titleClass: 'swal-game-error'
                    });
                    return;
                }

                Swal.fire({
                    customClass: {
                        title: 'swal-game-text'
                    },
                    icon: 'success',
                    title: 'Escudo equipado!',
                    text: 'Seu Escudo Primordial foi equipado com sucesso.',
                    html: `
                        <div class="modal-anuncio">
                            <iframe src="https://zerads.com/ad/ad.php?width=468&ref=10783"
                                width="468"
                                height="60"
                                scrolling="no"
                                frameborder="0">
                            </iframe>
                        </div>
                    `,
                    timer: 7000,
                    showConfirmButton: false,
                    background: 'transparent',
                    color: '#ffb400'
                });

                await atualizarUsuario();

            } catch (e) {
                console.error('Erro ao tentar equipar o escudo:', e);

                mostrarAlerta({
                    icon: 'error',
                    title: 'Erro',
                    text: 'Erro ao tentar equipar o escudo.',
                    color: '#ff3b3b',
                    titleClass: 'swal-game-error'
                });

            } finally {
                setTimeout(async () => {
                    emCooldown = false;
                    btnAtivarEscudo.innerText = textoOriginal;
                    await atualizarUsuario();
                }, tempoCooldown * 1000);
            }
        });
    }

    // ==============================
    // LOOP
    // ==============================
    atualizarUsuario();
    window.loopEscudo = setInterval(atualizarUsuario, 5000);
});

*/
