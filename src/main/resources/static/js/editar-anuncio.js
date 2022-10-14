const botoesApagar = Array.from(document.getElementsByClassName("editar-anuncio-imagem-apagar-btn"));
const anuncioThumbnails = Array.from(document.getElementsByClassName("anuncio-imagem-thumbnail"));
const galeriaContainer = document.getElementById("galeria-container");
const uploadInputWrapper = document.getElementById("upload-wrapper");
const uploadInput = document.getElementById("editar-anuncio-upload-input");
const thumbnailContainers = Array.from(document.getElementsByClassName('anuncio-imagem-thumbnail-wrapper'));
const thumbnailInput = document.getElementById("editar-anuncio-thumbnail");
const visualizacaoImagem = document.getElementById("img-principal");
const valorInput = document.getElementById("valor-input");

const ID_ANUNCIO = window.location.pathname.split("/")[2];
const UPLOAD_URL = `upload/`;
const FOTO_URL = `fotos/`

let thumbnailEscolhida = null;

// Configura a funcionalidade do botão de apagar imagem
botoesApagar.map(botao => {
    botao.addEventListener("click", (e) => {
        let id = e.target.id.split("-")[1];
        let thumbnail = anuncioThumbnails.filter(thumb => thumb.id == id)[0].parentNode;

        deleteData(`${FOTO_URL}${id}`)
            .then(async response => {
                if(response.status !== 204) {
                    console.log("Erro ao apagar foto - ", await response.json())
                } else {
                    thumbnail.removeEventListener("mouseenter", () => {}, true);
                    thumbnail.removeEventListener("mouseleave", () => {}, true);
                    galeriaContainer.removeChild(thumbnail);
                }
            })

    });
})

thumbnailContainers.map(container => {
    container.addEventListener("mouseenter", (e) => {
        const botaoFechar = e.target.childNodes[1];
        if(!botaoFechar.classList.contains("btn-ativo")) {
            botaoFechar.classList.add("btn-ativo");
        }

        printToast('sucesso', 'Teste', document.body);
    })

    container.addEventListener("mouseleave", (e) => {
        const botaoFechar = e.target.childNodes[1];
        if(botaoFechar.classList.contains("btn-ativo")) {
            botaoFechar.classList.remove("btn-ativo");
        }
    })
});

// Configura a seleção da imagem principal.
anuncioThumbnails.map(thumb => {
    thumb.addEventListener("click", (e) => {
        const imagemClicada = anuncioThumbnails.filter(thumb => e.target.id === thumb.id)[0];
        anuncioThumbnails.forEach(thumb => {
            if(thumb.classList.contains("anuncio-imagem-ativa")) {
                thumb.classList.remove("anuncio-imagem-ativa");
            }
        });

        if(!imagemClicada.classList.contains("anuncio-imagem-ativa")) {
            imagemClicada.classList.add("anuncio-imagem-ativa");
            thumbnailInput.value = imagemClicada.id;
        }
        
    });
});

uploadInput.addEventListener("change", async (e) => {

    let novaImagemWrapper = document.createElement("div");
    novaImagemWrapper.className = "anuncio-imagem-thumbnail-wrapper";

    // Adiciona o ícone de carregamento enquanto a imagem não é preenchida
    let spinner = document.createElement("span");
    spinner.className = "spinner-border";
    spinner.setAttribute("role", "status");
    novaImagemWrapper.appendChild(spinner);
    
    galeriaContainer.insertBefore(novaImagemWrapper, uploadInputWrapper);

    postData(UPLOAD_URL, uploadInput.files[0])
        .then(async (response) => {
            let respostaJson = await response.json();

            // Remove o spinner de carregamento
            novaImagemWrapper.removeChild(spinner);

            // Adicionando botão de remover
            let novoBotao = document.createElement("button");
            novoBotao.className = "btn btn-close editar-anuncio-imagem-apagar-btn";
            novoBotao.id = `btn-${respostaJson.id}`;

            // Adicionando nova imagem
            let novaImagem = document.createElement("img");
            novaImagem.src = await respostaJson.path;
            novaImagem.className = "anuncio-imagem-thumbnail ms-2";
            novaImagem.id = respostaJson.id;
            
            novaImagemWrapper.appendChild(novoBotao);
            novaImagemWrapper.appendChild(novaImagem);

            novaImagemWrapper.addEventListener("mouseenter", (e) => {
                if(!novoBotao.classList.contains("btn-ativo")) {
                    novoBotao.classList.add("btn-ativo");
                }
            });
        
            novaImagemWrapper.addEventListener("mouseleave", (e) => {
                if(novoBotao.classList.contains("btn-ativo")) {
                    novoBotao.classList.remove("btn-ativo");
                }
            });

            novoBotao.addEventListener("click", (e) => {
                let id = respostaJson.id;
        
                deleteData(`${FOTO_URL}${id}`)
                    .then(async response => {
                        if(response.status !== 204) {
                            console.log("Erro ao apagar foto - ", await response.json())
                        } else {
                            thumbnail.removeEventListener("mouseenter", () => {}, true);
                            thumbnail.removeEventListener("mouseleave", () => {}, true);
                            galeriaContainer.removeChild(novaImagemWrapper);
                        }
                    })
        
            });
        });


});

// Envia uma requisição DELETE ao servidor de uma URL
async function deleteData(url) {
    return await fetch(url, {
        method: 'DELETE',
        credentials: "same-origin"
    });
}

// Envia um arquivo ao servidor de uma URL com uma requisição POST
async function postData(url, data) {
    const formData  = new FormData();

    formData.append("file", data);
  
    return await fetch(url, {
      method: 'POST',
      body: formData,
      credentials: "same-origin"
    });
  
  }