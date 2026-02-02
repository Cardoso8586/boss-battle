document.addEventListener('DOMContentLoaded', () => {
    const btnNucleo = document.getElementById('btnNucleo');
    const nucleoCard = document.getElementById('nucleoCard');
    const fecharNucleo = document.getElementById('fecharNucleo');

    const btnNucleoArco = document.getElementById('btnNucleoArco');
    const nucleoArcoCard = document.getElementById('nucleoArcoCard');

    btnNucleo?.addEventListener('click', () => {
        // Fecha e esconde outro núcleo e seu botão sempre
        if (nucleoArcoCard) {
            nucleoArcoCard.classList.add('hidden');
            nucleoArcoCard.classList.remove('abrindo');
            btnNucleoArco?.classList.add('hidden');
        }

        // Abre este núcleo
        btnNucleo.classList.add('hidden');
        nucleoCard.classList.remove('hidden');
        void nucleoCard.offsetWidth; // força repaint
        nucleoCard.classList.add('abrindo');
    });

    fecharNucleo?.addEventListener('click', () => {
        nucleoCard.classList.add('hidden');
        nucleoCard.classList.remove('abrindo');
        btnNucleo.classList.remove('hidden');

        // Ao fechar, mostra novamente o outro botão
        btnNucleoArco?.classList.remove('hidden');
    });

    // **Inicialmente** esconde o outro botão se o núcleo estiver aberto
    if (!nucleoCard.classList.contains('hidden')) {
        btnNucleoArco?.classList.add('hidden');
    }
});


/**
 *const btnNucleo = document.getElementById('btnNucleo');
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
 
 * 
 */
