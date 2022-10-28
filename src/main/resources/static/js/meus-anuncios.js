const botoesDelete = document.querySelectorAll(".anuncio-delete-button");
const wrapperAnuncios = document.querySelector(".meus-anuncios-wrapper");

const ANUNCIOS_URL = `${window.location.origin}/anuncios`;

botoesDelete.forEach(botao => {
    botao.addEventListener("click", async (e) => {
        let idAnuncio = e.target.parentNode.id;
        let deleteUrl = `${ANUNCIOS_URL}/${idAnuncio}/delete`
        
        let confirmado = window.confirm(`Você tem certeza que deseja apagar o anúncio?`);
        if(confirmado) {
            // const anuncioApagado = await deleteData(deleteUrl);
            // if(!anuncioApagado.ok) {
            //     mostrarToast(document.querySelector("form") , `Falha ao apagar anúncio: ${anuncioApagado.text}`, 'erro', 5000)
            // } else {
            //     mostrarMensagemSucesso();
            // }
            mostrarMensagemSucesso();
        }
    })
});

// Mostra mensagem de sucesso
function mostrarMensagemSucesso() {
    let divMensagem = document.createElement("div");
    divMensagem.className = "mensagem-confirmacao border border-dark d-flex mx-auto bg-success p-2 px-4 m-3 rounded text-white";
    divMensagem.textContent = "Anúncio apagado com sucesso"

    wrapperAnuncios.prepend(divMensagem);

    setTimeout(() => {
        wrapperAnuncios.removeChild(divMensagem);
    }, 5000);
}

// Envia uma requisição DELETE ao servidor de uma URL
async function deleteData(url) {
    return await fetch(url, {
        method: 'DELETE',
        credentials: "same-origin"
    });
}