document.addEventListener('DOMContentLoaded', () => {
    const btnNucleoArco = document.getElementById('btnNucleoArco');
    const nucleoArcoCard = document.getElementById('nucleoArcoCard');
    const fecharNucleoArco = document.getElementById('fecharNucleoArco');

    const btnNucleo = document.getElementById('btnNucleo');
    const nucleoCard = document.getElementById('nucleoCard');

    btnNucleoArco?.addEventListener('click', () => {
        // Fecha e esconde outro núcleo e seu botão sempre
        if (nucleoCard) {
            nucleoCard.classList.add('hidden');
            nucleoCard.classList.remove('abrindo');
            btnNucleo?.classList.add('hidden');
        }

        // Abre este núcleo
        btnNucleoArco.classList.add('hidden');
        nucleoArcoCard.classList.remove('hidden');
        void nucleoArcoCard.offsetWidth; // força repaint
        nucleoArcoCard.classList.add('abrindo');
    });

    fecharNucleoArco?.addEventListener('click', () => {
        nucleoArcoCard.classList.add('hidden');
        nucleoArcoCard.classList.remove('abrindo');
        btnNucleoArco.classList.remove('hidden');

        // Ao fechar, mostra novamente o outro botão
        btnNucleo?.classList.remove('hidden');
    });

    // **Inicialmente** esconde o outro botão se o núcleo estiver aberto
    if (!nucleoArcoCard.classList.contains('hidden')) {
        btnNucleo?.classList.add('hidden');
    }
});


/**
 * const btnNucleoArco = document.getElementById('btnNucleoArco');
const nucleoArcoCard = document.getElementById('nucleoArcoCard');
const fecharNucleoArco = document.getElementById('fecharNucleoArco');

if (btnNucleoArco && nucleoArcoCard && fecharNucleoArco) {

    btnNucleoArco.addEventListener('click', () => {
        btnNucleoArco.classList.add('hidden');
        nucleoArcoCard.classList.remove('hidden');

        // força repaint para animação
        void nucleoArcoCard.offsetWidth;
        nucleoArcoCard.classList.add('abrindo');
    });

    fecharNucleoArco.addEventListener('click', () => {
        nucleoArcoCard.classList.add('hidden');
        nucleoArcoCard.classList.remove('abrindo');

        btnNucleoArco.classList.remove('hidden');
    });
}
 */


