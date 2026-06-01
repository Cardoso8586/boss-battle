document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById('loginForm');
    const loginMsg = document.getElementById('loginMsg');
    const loginBtn = document.getElementById('loginBtn');

    if (!loginForm || !loginMsg || !loginBtn) {
        console.error("Erro: loginForm, loginMsg ou loginBtn não encontrado.");
        return;
    }

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const captchaToken = document.getElementById('captchaToken').value;

        if (!captchaToken) {
            loginMsg.textContent = "Confirme o captcha.";
            loginMsg.style.color = "red";
            return;
        }

        loginBtn.classList.add('loading');
        loginBtn.disabled = true;

        const payload = {
            loginUser: document.getElementById('loginUser').value,
            loginSenha: document.getElementById('loginSenha').value,
            captcha: captchaToken
        };

        try {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            const text = await response.text();

            if (response.ok) {
                window.location.href = '/arena';
                return;
            }

            if (response.status === 502) {
                loginMsg.textContent = "Servidor temporariamente indisponível. Tente novamente em instantes.";
            } else {
                loginMsg.textContent = text;
            }

            loginMsg.style.color = 'red';
            loginBtn.classList.remove('loading');
            loginBtn.disabled = false;

        } catch (error) {
            loginMsg.textContent = "Erro ao conectar ao servidor.";
            loginMsg.style.color = 'red';

            loginBtn.classList.remove('loading');
            loginBtn.disabled = false;
        }
    });
});

/*
const loginForm = document.getElementById('loginForm');
const loginMsg = document.getElementById('loginMsg');

loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const captchaToken = document.getElementById('captchaToken').value;

    if (!captchaToken) {
        loginMsg.textContent = "Confirme o captcha.";
        loginMsg.style.color = "red";
        return;
    }

    const payload = {
        loginUser: document.getElementById('loginUser').value,
        loginSenha: document.getElementById('loginSenha').value,
        captcha: captchaToken
    };

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const text = await response.text();

        if (response.ok) {
            window.location.href = '/arena';
        } else if (response.status === 502) {
            loginMsg.textContent = "Servidor temporariamente indisponível. Tente novamente em instantes.";
            loginMsg.style.color = 'red';
        } else {
            loginMsg.textContent = text;
            loginMsg.style.color = 'red';
        }
    } catch (error) {
        loginMsg.textContent = "Erro ao conectar ao servidor.";
        loginMsg.style.color = 'red';
        // console.error(error);
    }
});

 * 
 */

