
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById('registerForm');
    const submitBtn = document.getElementById('submitBtn');
    const originalBtnText = submitBtn.innerText;

    window.onTurnstileSuccess = function(token) {
        document.getElementById('captcha-response').value = token;
        submitBtn.disabled = false;
    };

    window.onTurnstileError = function() {
        document.getElementById('captcha-response').value = "";
        submitBtn.disabled = true;
    };

    window.onTurnstileExpired = function() {
        document.getElementById('captcha-response').value = "";
        submitBtn.disabled = true;
    };

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        if (submitBtn.disabled) return;

        submitBtn.disabled = true;
        submitBtn.classList.add('loading');
        submitBtn.innerText = 'Processando...';

        const usuario = {
            username: document.getElementById('username').value,
            email: document.getElementById('email').value,
            senha: document.getElementById('senha').value,
            captcha: document.getElementById('captcha-response').value,
            ref: document.getElementById('ref').value
        };

        let sucesso = false;

        try {
            const response = await fetch('/api/auth/cadastro', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(usuario)
            });

            if (response.ok) {
                const text = await response.text();

                if (text === "SUCESSO") {
                    sucesso = true;

                    Swal.fire({
                        customClass: {
                            title: 'swal-game-text'
                        },
                        icon: 'success',
                        title: 'Bem-Vindo!',
                        text: 'Seu guerreiro marchou para a linha de frente da batalha!',
                        timer: 8000,
                        showConfirmButton: false,
                        allowOutsideClick: false,
                        background: 'transparent',
                        color: '#ffb400'
                    });

                    setTimeout(() => {
                        window.location.href = "/login";
                    }, 4000);

                    return;
                }
            } else {
				// 👇 AQUI só roda quando der ERRO
                const errorText = await response.text();
                console.error(errorText);
				
				Swal.fire({
				customClass: {
				title: 'swal-game-error'
				},
				                  
				title: 'Erro no cadstro',
				text: errorText,
				timer: 8000,
				showConfirmButton: false,
				allowOutsideClick: false,
				background: 'transparent',
				color: '#ff3b3b'
				
				});
				
                limparFormulario();
            }

        } catch (err) {
			// 👇 AQUI só roda quando der ERRO
            console.error(err);
			
			Swal.fire({
						customClass: {
						title: 'swal-game-error'
						},
						                  
						title: 'Erro no cadstro',
						text: errorText,
						timer: 8000,
						showConfirmButton: false,
						allowOutsideClick: false,
						background: 'transparent',
						color: '#ff3b3b'
						
						});
            limparFormulario();

        } finally {
            if (!sucesso) {
                submitBtn.disabled = false;
                submitBtn.classList.remove('loading');
                submitBtn.innerText = originalBtnText;
            }
        }
    });

    function limparFormulario() {
        form.reset();

        if (window.turnstile) {
            turnstile.reset();
        }

        document.getElementById('captcha-response').value = "";
        submitBtn.disabled = true;
        submitBtn.classList.remove('loading');
        submitBtn.innerText = originalBtnText;
    }
});
/**
 * 
 * 
 *
const form = document.getElementById('registerForm');
const submitBtn = document.getElementById('submitBtn');
const originalBtnText = submitBtn.innerText;

window.onTurnstileSuccess = function(token) {
    document.getElementById('captcha-response').value = token;
    submitBtn.disabled = false;
}

window.onTurnstileError = function() {
    submitBtn.disabled = true;
}

window.onTurnstileExpired = function() {
    submitBtn.disabled = true;
}

form.addEventListener('submit', async (e) => {
    e.preventDefault();

    // 🔒 evita clique duplo
    if (submitBtn.disabled) return;

    // 🔄 botão em estado de processamento
    submitBtn.disabled = true;
    submitBtn.innerText = 'Processando...';

    const usuario = {
        username: document.getElementById('username').value,
        email: document.getElementById('email').value,
        senha: document.getElementById('senha').value,
        captcha: document.getElementById('captcha-response').value,
        ref: document.getElementById('ref').value 
    };

    try {
        const response = await fetch('/api/auth/cadastro', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(usuario)
        });

        if (response.ok) {
            const text = await response.text();

            if (text === "SUCESSO") {
				Swal.fire({
				    customClass: {
				        title: 'swal-game-text'
				    },
				    icon: 'success',
				    title: 'Bem-Vindo!',
				    text: 'Seu guerreiro marchou para a linha de frente da batalha!',
				    timer: 8000,
				    showConfirmButton: false,
				    allowOutsideClick: false,
				    background: 'transparent',
				    color: '#ffb400'
				});


                setTimeout(() => {
                    window.location.href = "/login";
                }, 4000);

                return;
            } else {
				// 👇 AQUI só roda quando der ERRO
								    submitBtn.disabled = false;

			
            }
        } else {
            const errorText = await response.text();
			console.error(errorText);
			// 👇 AQUI só roda quando der ERRO
						    submitBtn.disabled = false;

						   limparFormulario();
			

		
        }

    } catch (err) {
        console.error(err);
		// 👇 AQUI só roda quando der ERRO
				    submitBtn.disabled = false;

				    
				   limparFormulario();
	
		
    } finally {
        // 🔁 restaura botão se NÃO redirecionou
        submitBtn.disabled = false;
        submitBtn.innerText = originalBtnText;
    }
});


function limparFormulario() {
    form.reset();

    if (window.turnstile) {
        turnstile.reset();
    }

    submitBtn.disabled = true;
}




 */