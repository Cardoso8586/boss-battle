
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
                Swal.fire({
					
                    icon: 'error',
                    title: text,
                    text: 'error',
                    timer: 8000,
                    showConfirmButton: false,
                    background: 'transparent',
                    color: '#ff3b3b '
                }).then(() => {
					    // 👇 AQUI só roda quando der ERRO
					    submitBtn.disabled = false;

					     limparFormulario();
					});
				
				
            }
        } else {
            const errorText = await response.text();

            Swal.fire({
				customClass: {
								title: 'swal-game-error'
								},
                icon: 'error',
                title: errorText,
                text: 'Erro',
                timer: 8000,
                showConfirmButton: false,
                background: 'transparent',
                color: '#ff3b3b '
            }).then(() => {
				    // 👇 AQUI só roda quando der ERRO
				    submitBtn.disabled = false;

				   limparFormulario();
				});
			
		
        }

    } catch (err) {
        console.error(err);

        Swal.fire({
			customClass: {
							title: 'swal-game-error'
							},
            icon: 'error',
            title:  err,
            text: 'erro',
            timer: 8000,
            showConfirmButton: false,
            background: 'transparent',
            color: '#ff3b3b '
        }).then(() => {
			    // 👇 AQUI só roda quando der ERRO
			    submitBtn.disabled = false;

			    
			   limparFormulario();
			});

		
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



/**
 * 
 * 
 * const form = document.getElementById('registerForm');
const submitBtn = document.getElementById('submitBtn');

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
                alert("Cadastro realizado com sucesso!");
                window.location.href = "/login"; // redireciona para login
            } else {
                alert(text);
            }
        } else {
            const errorText = await response.text();
            alert(errorText);
        }
    } catch (err) {
        console.error(err);
        alert("Erro ao enviar cadastro.");
    }
});

 */