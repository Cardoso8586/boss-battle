// Redireciona após ? minuto (120000 milissegundos)
const SESSION_TIMEOUT = 120000;

let timeout;

function resetTimer() {
    clearTimeout(timeout);
    timeout = setTimeout(() => {
        Swal.fire({
			customClass: { title: 'swal-game-text' },
            icon: 'warning',
            title: 'Atenção',
            text: 'Sua sessão expirou. Você será redirecionado.',
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