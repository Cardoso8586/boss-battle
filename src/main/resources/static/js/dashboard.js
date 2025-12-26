window.addEventListener('load', () => {
    const locElem = document.getElementById('user-location');

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                const { latitude, longitude } = position.coords;
                locElem.textContent = `Lat: ${latitude.toFixed(4)}, Lon: ${longitude.toFixed(4)}`;
            },
            (error) => {
                locElem.textContent = "Não foi possível obter sua localização.";
            }
        );
    } else {
        locElem.textContent = "Geolocalização não suportada pelo navegador.";
    }
});
