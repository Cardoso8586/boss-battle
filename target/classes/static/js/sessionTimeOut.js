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