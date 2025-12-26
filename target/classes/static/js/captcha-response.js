const form = document.getElementById('registerForm');
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
