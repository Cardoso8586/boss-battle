function googleTranslateElementInit() {
    new google.translate.TranslateElement({
        pageLanguage: 'pt',
        autoDisplay: false
    }, 'google_translate_element');
}

// Carrega o script do Google Translate
var gtScript = document.createElement('script');
gtScript.type = 'text/javascript';
gtScript.src = "//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit";
document.head.appendChild(gtScript);

// Quando o usuário muda o idioma no dropdown
document.getElementById('lang-select').addEventListener('change', (e) => {
    const lang = e.target.value;
    const select = document.querySelector('.goog-te-combo');
    if(select) {
        select.value = lang; // define o idioma
        select.dispatchEvent(new Event('change')); // aplica a tradução
		
		
    }
});