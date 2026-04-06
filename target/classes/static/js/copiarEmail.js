
document.addEventListener("DOMContentLoaded", function () {
  const emailLink = document.getElementById("emailSuporte");

  if (!emailLink) return;

  emailLink.addEventListener("click", function (e) {
    e.preventDefault();

    const email = emailLink.textContent.trim();

    // 🔒 evita spam de clique
    if (emailLink.dataset.loading === "true") return;
    emailLink.dataset.loading = "true";

    copiarEmail(email);

    // 📧 abre cliente com assunto já preenchido
    window.location.href =
      `mailto:${email}?subject=Suporte Boss Battle Arena&body=Olá, preciso de ajuda com...`;

    setTimeout(() => {
      emailLink.dataset.loading = "false";
    }, 1500);
  });
});

function copiarEmail(email) {
  // método moderno
  if (navigator.clipboard && window.isSecureContext) {
    navigator.clipboard.writeText(email)
      .then(() => mostrarFeedbackEmail("📋 Email copiado!"))
      .catch(() => copiarFallback(email));
  } else {
    copiarFallback(email);
  }
}

// fallback (funciona em mais navegadores)
function copiarFallback(email) {
  const input = document.createElement("input");
  input.value = email;
  document.body.appendChild(input);
  input.select();
  document.execCommand("copy");
  input.remove();

  mostrarFeedbackEmail("📋 Email copiado!");
}

function mostrarFeedbackEmail(texto) {
  const el = document.createElement("div");

  el.innerText = texto;
  el.style.position = "fixed";
  el.style.bottom = "30px";
  el.style.left = "50%";
  el.style.transform = "translateX(-50%) scale(0.9)";
  el.style.background = "rgba(0,0,0,0.9)";
  el.style.color = "#ffb400";
  el.style.padding = "10px 18px";
  el.style.borderRadius = "10px";
  el.style.fontSize = "14px";
  el.style.zIndex = "9999";
  el.style.boxShadow = "0 0 12px rgba(255,180,0,0.6)";
  el.style.opacity = "0";
  el.style.transition = "all 0.25s ease";

  document.body.appendChild(el);

  // anima entrada
  setTimeout(() => {
    el.style.opacity = "1";
    el.style.transform = "translateX(-50%) scale(1)";
  }, 10);

  // saída
  setTimeout(() => {
    el.style.opacity = "0";
    el.style.transform = "translateX(-50%) scale(0.9)";
    setTimeout(() => el.remove(), 250);
  }, 2000);
}
