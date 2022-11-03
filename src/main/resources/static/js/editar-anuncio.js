const botoesApagar = Array.from(document.getElementsByClassName("editar-anuncio-imagem-apagar-btn"));
const anuncioThumbnails = Array.from(document.getElementsByClassName("anuncio-imagem-thumbnail"));
const galeriaContainer = document.getElementById("galeria-container");
const uploadInputWrapper = document.getElementById("upload-wrapper");
const uploadInput = document.getElementById("editar-anuncio-upload-input");
const thumbnailContainers = Array.from(document.getElementsByClassName('anuncio-imagem-thumbnail-wrapper'));
const thumbnailInput = document.getElementById("editar-anuncio-thumbnail");

const ID_ANUNCIO = window.location.pathname.split("/")[2];
const UPLOAD_URL = `upload/`;
const FOTO_URL = `fotos/`

// Configura a funcionalidade do botão de apagar imagem
botoesApagar.map(botao => {
    botao.addEventListener("click", (e) => {
        e.preventDefault();
        let id = e.target.id.split("-")[1];
        let thumbnail = anuncioThumbnails.filter(thumb => thumb.id == id)[0].parentNode;

        deleteData(`${FOTO_URL}${id}`)
            .then(async response => {
                thumbnail.removeEventListener("mouseenter", () => {}, true);
                thumbnail.removeEventListener("mouseleave", () => {}, true);
                galeriaContainer.removeChild(thumbnail);
            })
            .catch(async erro => {
                mostrarToast("Erro ao apagar foto - ", await erro.text)
            })
        ;
        
        // Ao apagar imagem no servidor, remove da lista de thumbnails.
        let indiceFotoApagada = anuncioThumbnails.findIndex(thumb => thumb.id == id);
        anuncioThumbnails.splice(indiceFotoApagada, 1);

        // Caso não haja mais imagens no anúncio, seta a thumbnail para o número zero, indicando sem imagem.
        if(anuncioThumbnails.length < 1) {
            thumbnailInput.value = 0;
        }
    });
})

thumbnailContainers.map(container => {
    container.addEventListener("mouseenter", (e) => {
        const botaoFechar = e.target.childNodes[1];
        if(!botaoFechar.classList.contains("btn-ativo")) {
            botaoFechar.classList.add("btn-ativo");
        }
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
        let anuncioThumbnails = Array.from(document.getElementsByClassName("anuncio-imagem-thumbnail"));
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

    let envioArquivo = await postData(UPLOAD_URL, uploadInput.files[0]);

    if(!envioArquivo.ok) {
        mostrarToast(document.querySelector("form") , `${await envioArquivo.text()}`, 'erro', 5000);
        galeriaContainer.removeChild(novaImagemWrapper);
    } else {
        let respostaJson = await envioArquivo.json();
    
        // Remove o spinner de carregamento
        novaImagemWrapper.removeChild(spinner);
    
        // Adicionando botão de remover
        let novoBotao = document.createElement("button");
        novoBotao.className = "btn btn-close editar-anuncio-imagem-apagar-btn";
        novoBotao.id = `btn-${respostaJson.id}`;
    
        // Adicionando nova imagem
        let novaImagem = document.createElement("img");
        novaImagem.src = await respostaJson.path;
        novaImagem.className = "anuncio-imagem-thumbnail me-2";
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
    
        novaImagem.addEventListener("click", (e) => {
            let anuncioThumbnails = Array.from(document.getElementsByClassName("anuncio-imagem-thumbnail"));
            let thumbnailInput = document.getElementById("editar-anuncio-thumbnail")
            anuncioThumbnails.forEach(thumb => {
                if(thumb.classList.contains("anuncio-imagem-ativa")) {
                    thumb.classList.remove("anuncio-imagem-ativa");
                }
            });
    
            if(!novaImagem.classList.contains("anuncio-imagem-ativa")) {
                novaImagem.classList.add("anuncio-imagem-ativa");
                thumbnailInput.value = novaImagem.id;
            }
            
        });
    
        novoBotao.addEventListener("click", (e) => {
            let id = respostaJson.id;
            
            if(anuncioThumbnails.length <= 1) {
                thumbnailInput.value = null;
            }
            
            deleteData(`${FOTO_URL}${id}`)
            .then(async response => {
                galeriaContainer.removeChild(novaImagemWrapper);
                    novaImagemWrapper.removeEventListener("mouseenter", () => {}, true);
                    novaImagemWrapper.removeEventListener("mouseleave", () => {}, true);
            })
            .catch(async erro => {
                mostrarToast("Erro ao apagar foto - ", await erro.text)
            });

            e.preventDefault();
        });
        
    }
});


// Envia um arquivo ao servidor de uma URL com uma requisição POST
async function postData(url, data) {
    const formData  = new FormData();

    formData.append("file", data);
  
    return fetch(url, {
      method: 'POST',
      body: formData,
      credentials: "same-origin"
    });
  
  }