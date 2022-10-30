const mostrarToast = (pai, mensagem, tipo, duracao) => {
    let cor = 'light';

    if(tipo === 'sucesso') cor = 'success';
    if(tipo === 'erro') cor = 'danger';

    const toast = document.createElement("div");
    const toastCorpo = document.createElement("div");
    const texto = document.createTextNode(mensagem);

    toastCorpo.appendChild(texto)
    toast.className = `mensagem-confirmacao d-flex rounded border justify-content-center border-dark p-3 align-items-center bg-${cor} text-light fw-bold`;
    toast.style = "position: fixed; top: 0; right: 0; margin: 1px; min-height: 70px; width: 250px; text-align: center; z-index: 100;"
    toast.appendChild(toastCorpo)

    pai.appendChild(toast)

    setTimeout(() => {
        pai.removeChild(toast);
    }, duracao)
}

// Envia uma requisição DELETE ao servidor de uma URL
async function deleteData(url) {
    return await fetch(url, {
        method: 'DELETE',
        credentials: "same-origin"
    });
}