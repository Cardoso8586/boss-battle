const btnNucleo = document.getElementById('btnNucleo');
const nucleoCard = document.getElementById('nucleoCard');
const fecharNucleo = document.getElementById('fecharNucleo');

btnNucleo.addEventListener('click', () => {
    btnNucleo.classList.add('hidden');
    nucleoCard.classList.remove('hidden');

    void nucleoCard.offsetWidth;
    nucleoCard.classList.add('abrindo');
});

fecharNucleo.addEventListener('click', () => {
    nucleoCard.classList.add('hidden');
    nucleoCard.classList.remove('abrindo');

    btnNucleo.classList.remove('hidden');
});
