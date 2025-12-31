const audiosScary = [
    "audio/scary1.mp3",
	"audio/scary2.mp3",
	"audio/scary3.mp3",
	"audio/scary4.mp3",
	"audio/scary5.mp3",
	"audio/scary6.mp3"
 
];

let ultimoAudio = "";
let ambienciaAtiva = true;
let timeoutSom = null;

// 🔀 escolhe áudio sem repetir
function escolherAudio() {
    let src;
    do {
        src = audiosScary[Math.floor(Math.random() * audiosScary.length)];
    } while (src === ultimoAudio);

    ultimoAudio = src;
    return src;
}

// 🔊 fade in
function fadeIn(audio, target = 0.5, step = 0.02) {
    audio.volume = 0;
    audio.muted = false;

    const i = setInterval(() => {
        if (audio.volume < target) {
            audio.volume = Math.min(audio.volume + step, target);
        } else {
            clearInterval(i);
        }
    }, 60);
}

// 🔇 fade out
function fadeOut(audio, step = 0.02) {
    const i = setInterval(() => {
        if (audio.volume > step) {
            audio.volume -= step;
        } else {
            audio.pause();
            audio.volume = 0;
            clearInterval(i);
        }
    }, 60);
}

// ▶️ toca áudio aleatório
function tocarSomAleatorio() {
    if (!ambienciaAtiva) return;

    const audio = document.getElementById("super-scary");
    audio.src = escolherAudio();
    audio.muted = true; // 🔑 autoplay permitido
    audio.volume = 0;

    audio.play().then(() => {
        setTimeout(() => {
            fadeIn(audio, 0.4 + Math.random() * 0.2);
        }, 500); // pequeno delay cinematográfico
    }).catch(() => {});

    audio.onended = () => {
        if (!ambienciaAtiva) return;

        const proximoTempo = 8000 + Math.random() * 12000;
        timeoutSom = setTimeout(tocarSomAleatorio, proximoTempo);
    };
}

// 🚀 inicia automaticamente ao carregar a página
window.addEventListener("load", () => {
    tocarSomAleatorio();
});

// 🛑 parar ambiência (ex: boss real aparece)
function pararAmbiencia() {
    ambienciaAtiva = false;
    clearTimeout(timeoutSom);

    const audio = document.getElementById("super-scary");
    fadeOut(audio);
}
