const botoesDelete = document.querySelectorAll(".anuncio-delete-button");
const wrapperAnuncios = document.querySelector(".meus-anuncios-wrapper");

const ANUNCIOS_URL = `${window.location.origin}/anuncios`;

botoesDelete.forEach(botao => {
    botao.addEventListener("click", async (e) => {
        let idAnuncio = e.target.parentNode.id;
        let deleteUrl = `${ANUNCIOS_URL}/${idAnuncio}`
        
        let confirmado = window.confirm(`Você tem certeza que deseja apagar o anúncio?`);

        if(confirmado) {

            const anuncioApagado = await deleteData(deleteUrl);

            if(!anuncioApagado.ok) {
                let respostaJson = await anuncioApagado.json();
                mostrarToast(document.querySelector("table") , `Falha ao apagar anúncio: ${respostaJson.error}`, 'erro', 5000)
            } else {
                let tbody = document.querySelector("tbody");

                let linha = e.target
                .parentNode     // a
                .parentNode     // div
                .parentNode     // td
                .parentNode;    // tr
            
                tbody.removeChild(linha);

                if(tbody.children.length == 0) {
                    let novaLinha = document.createElement("tr");
                    let novoTd = document.createElement("td");
                    novoTd.className = "text-center";
                    novoTd.colSpan = 5;

                    let textoTd = document.createTextNode("Você ainda não possui anúncios publicados.")

                    novoTd.appendChild(textoTd);
                    novaLinha.appendChild(novoTd);
                }

                mostrarToast(document.querySelector("table") , `Anúncio apagado com sucesso`, 'sucesso', 5000)
            }
        }
    })
});