const SESSION_TIMEOUT = 900000; // produção (15 min)
// const SESSION_TIMEOUT = 10000; // teste (10s)

let timeout;

// 🔥 frases do jogo
const mensagensInatividade = [
    "Você ficou tempo demais fora da arena... retornando ao acampamento.",
    "Tempo demais fora da arena... retornando ao acampamento.",
    "Você se ausentou da arena... retornando ao acampamento.",
    "A arena foi abandonada... retornando ao acampamento.",
    "Tempo esgotado na arena... retorno imediato ao acampamento.",
    "O tempo passou... seus guerreiros estão retornando ao acampamento.",
    "Sem ação na arena... as tropas recuam para o acampamento.",
    "A batalha esfriou... retornando ao acampamento.",
    "A arena ficou em silêncio... voltando ao acampamento.",
    "Seus guerreiros aguardaram... mas você não voltou. Retornando ao acampamento."
];

// função de frase aleatória
function getMensagemAleatoria() {
    return mensagensInatividade[Math.floor(Math.random() * mensagensInatividade.length)];
}

function resetTimer() {
    clearTimeout(timeout);

    timeout = setTimeout(() => {
        Swal.fire({
            customClass: { title: 'swal-game-text' },
            icon: 'warning',
            title: 'Atenção',
            text: getMensagemAleatoria(), 
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
            timer: 8000,
            timerProgressBar: true,
            showConfirmButton: false,
            allowOutsideClick: false,
            allowEscapeKey: false,
            allowEnterKey: false,
            background: 'transparent',
            color: '#ffb400'
        }).then(() => {
            window.location.href = "/logout";
        });
    }, SESSION_TIMEOUT);
}

// inicia
document.addEventListener("DOMContentLoaded", () => {
    resetTimer();
});

// eventos que resetam o tempo
["click", "mousemove", "keydown", "scroll", "touchstart"].forEach(event => {
    document.addEventListener(event, resetTimer);
});

/*
const SESSION_TIMEOUT = 900000; // produção (15 min)
//const SESSION_TIMEOUT = 10000;   // teste (10s)
let timeout;

function resetTimer() {
    clearTimeout(timeout);

    timeout = setTimeout(() => {
        Swal.fire({
            customClass: { title: 'swal-game-text' },
            icon: 'warning',
            title: 'Atenção',
           text: "Você ficou tempo demais fora da arena... retornando ao acampamento.",
            timer: 6000,
            timerProgressBar: true,
            showConfirmButton: false,
            allowOutsideClick: false,
            allowEscapeKey: false,
            allowEnterKey: false,
            background: 'transparent',
            color: '#ffb400'
        }).then(() => {
            window.location.href = "/logout";
        });
    }, SESSION_TIMEOUT);
}

document.addEventListener("DOMContentLoaded", () => {
    resetTimer();
});

["click", "mousemove", "keydown", "scroll", "touchstart"].forEach(event => {
    document.addEventListener(event, resetTimer);
});

/*

// Redireciona após 8 minutos (480000 milissegundos)
const SESSION_TIMEOUT = 480000; // produção (15 min)

let timeout;

function resetTimer() {
    clearTimeout(timeout);
    timeout = setTimeout(() => {
        Swal.fire({
			customClass: { title: 'swal-game-text' },
            icon: 'warning',
            title: 'Atenção',
            text: "Você ficou tempo demais fora da arena... retornando.",
            timer: 6000,
            timerProgressBar: true,
            showConfirmButton: false,
            allowOutsideClick: false,
            allowEscapeKey: false,
            allowEnterKey: false,
			background: 'transparent',
			color: '#ffb400'
        }).then(() => {
			
			
			
            // Executa o redirecionamento após o alerta fechar
            window.location.href = "/logout"; // ou outra rota desejada
        });
    }, SESSION_TIMEOUT);
}

window.onload = resetTimer;
document.onmousemove = resetTimer;
document.onkeypress = resetTimer;
*/