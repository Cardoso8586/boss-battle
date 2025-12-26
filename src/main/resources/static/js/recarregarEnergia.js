document.addEventListener('DOMContentLoaded', () => {
    const btnRecarregar = document.getElementById('btnRecarregar');
    const energiaAtual = document.getElementById('energiaAtual');
    const energiaBar = document.getElementById('energiaBar');
    const usuarioId = parseInt(document.querySelector('meta[name="user-id"]').getAttribute('content'));

    btnRecarregar.addEventListener('click', () => {
        fetch(`/recarregar-energia?usuarioId=${usuarioId}`, { method: 'POST' })
            .then(res => res.json())
            .then(data => {
                energiaAtual.textContent = data.energiaGuerreiros;
                energiaBar.style.width = Math.min(data.energiaGuerreiros, 100) + '%';
                btnRecarregar.disabled = data.energiaGuerreiros >= data.energiaGuerreirosPadrao;
            })
            .catch(err => console.error(err));
    });
});
