const loginForm = document.getElementById('loginForm');
const loginMsg = document.getElementById('loginMsg');

loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const payload = {
        loginUser: document.getElementById('loginUser').value, // mesma chave do backend
        loginSenha: document.getElementById('loginSenha').value
    };

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const text = await response.text();

        if (response.ok) {
            // Redireciona para o dashboard após login
            window.location.href = '/dashboard';
        } else {
            // Mostra mensagem de erro no elemento <p id="loginMsg">
            loginMsg.textContent = text;
            loginMsg.style.color = 'red';
        }
    } catch (error) {
        loginMsg.textContent = "Erro ao conectar ao servidor.";
        loginMsg.style.color = 'red';
        console.error(error);
    }
});

