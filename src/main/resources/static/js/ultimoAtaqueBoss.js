let ultimoAtaqueMostrado = null;

setInterval(() => {

    fetch("/api/boss/ultimo-ataque")
        .then(response => {

            if (!response.ok) return null;
            return response.json();

        })
        .then(data => {

            if (!data) return;

            // remove nanossegundos
            const dataLimpa = data.dataAtaque
                ? data.dataAtaque.split(".")[0]
                : null;

            if (!dataLimpa) return;

            if (ultimoAtaqueMostrado !== dataLimpa) {

                ultimoAtaqueMostrado = dataLimpa;

                if (data.mensagem) {
                    mostrarDano(data.mensagem);
                }
            }

        })
        .catch(error => console.error("Erro ao buscar ataque:", error));

}, 3000);


function mostrarDano(texto) {

    const container = document.getElementById("damageContainer");

    const msg = document.createElement("div");
    msg.classList.add("damage-message");
    //msg.innerText = texto;
	msg.innerHTML = texto; 
    container.appendChild(msg);

    setTimeout(() => {
        msg.remove();
    }, 10600);
}




/**
 *  let ultimoAtaqueMostrado = null;

 setInterval(() => {

     fetch("/api/boss/ultimo-ataque")
         .then(response => response.json())
         .then(data => {

             if (!data) return;

 		
 			//debug
 			Swal.fire({
 			 title: "JSON RECEBIDO",
 			  html: `<pre style="text-align:left">${JSON.stringify(data, null, 2)}</pre>`,
 			width: 600
 			});//
 									
             // Só mostra se for um ataque novo
             if (ultimoAtaqueMostrado !== data.dataAtaque) {

                 ultimoAtaqueMostrado = data.dataAtaque;

                 if (data.mensagem) {
                     mostrarDano(data.mensagem);
                 }
             }

         })
         .catch(error => console.error("Erro ao buscar ataque:", error));

 }, 3000);


 function mostrarDano(texto) {

     const container = document.getElementById("damageContainer");

     const msg = document.createElement("div");
     msg.classList.add("damage-message");
     msg.innerText = texto;

     container.appendChild(msg);

     setTimeout(() => {
         msg.remove();
     }, 2500);
 }

 * 
 */

