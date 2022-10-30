const galeriaContainer = document.getElementById("galeria-container");
const uploadInputWrapper = document.getElementById("upload-wrapper");
const uploadInput = document.getElementById("editar-anuncio-upload-input");
const thumbnailInput = document.getElementById("editar-anuncio-thumbnail");
const formulario = document.getElementById("novo-anuncio-form");

const UPLOAD_URL = `upload/`;
const FOTO_URL = `fotos/`;
const ANUNCIOS_URL = `${window.location.origin}/anuncios`;

let thumbnailIds = [];
let imagensAdicionadas = [];

uploadInput.addEventListener("change", (e) => {
    
    let imagens = Array.from(uploadInput.files);

    if(imagens.length > 0 && imagens.length <= 6) {
        
        imagens.forEach((imagem, indice) => {
            
            imagensAdicionadas = [...imagensAdicionadas, imagem];

            let novaImagemWrapper = document.createElement("div");
            novaImagemWrapper.className = "anuncio-imagem-thumbnail-wrapper";
    
            // Adiciona o ícone de carregamento enquanto a imagem não é preenchida
            let spinner = document.createElement("span");
            spinner.className = "spinner-border";
            spinner.setAttribute("role", "status");
            novaImagemWrapper.appendChild(spinner);
            
            galeriaContainer.insertBefore(novaImagemWrapper, uploadInputWrapper);

            novaImagemWrapper.removeChild(spinner);

            // Adicionando botão de remover
            let novoBotao = document.createElement("button");
            novoBotao.type = "button"
            novoBotao.className = "btn btn-close editar-anuncio-imagem-apagar-btn";
            novoBotao.id = `btn-${indice}`;

            // Adicionando nova imagem
            let novaImagem = document.createElement("img");
            novaImagem.src = URL.createObjectURL(imagem);
            novaImagem.className = "anuncio-imagem-thumbnail me-2";
            novaImagem.id = indice;
            
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
                const anuncioThumbnails = Array.from(document.getElementsByClassName("anuncio-imagem-thumbnail"));
                anuncioThumbnails.forEach(thumb => {
                    if(thumb.classList.contains("anuncio-imagem-ativa")) {
                        thumb.classList.remove("anuncio-imagem-ativa");
                    }
                });
                console.log(thumbnailIds);
                if(!novaImagem.classList.contains("anuncio-imagem-ativa")) {
                    novaImagem.classList.add("anuncio-imagem-ativa");
                    thumbnailInput.value = indice;
                }

            })

            novoBotao.addEventListener("click", (e) => {
                const anuncioThumbnails = Array.from(document.getElementsByClassName("anuncio-imagem-thumbnail"));

                let id = indice;
                // Caso não haja mais imagens no anúncio, seta a thumbnail para o número zero, indicando sem imagem.
                if(anuncioThumbnails.length < 1) {
                    thumbnailInput.value = 0;
                }
        
                novaImagem.removeEventListener("mouseenter", () => {}, true);
                novaImagem.removeEventListener("mouseleave", () => {}, true);
                galeriaContainer.removeChild(novaImagemWrapper);
                
                // Ao apagar imagem no servidor, remove da lista de thumbnails.
                let indiceFotoApagada = anuncioThumbnails.findIndex(thumb => thumb.id == id);
                imagensAdicionadas.splice(indiceFotoApagada, 1);
                anuncioThumbnails.splice(indiceFotoApagada, 1);
                thumbnailIds.splice(indiceFotoApagada, 1);

        });
        })
    }
});

formulario.addEventListener("submit", async (e) => {
    e.preventDefault();
    const dados = new FormData(e.target);

    let spinner = document.createElement("span");
    spinner.className = "spinner-border";
    spinner.setAttribute("role", "status");
    spinner.style = "z-index: 10; color: green;"

    const telaCarregamento = document.createElement("div");
    telaCarregamento.style = "position: absolute; width: 100%; min-height: 100%; background: rgba(0,0,0,0.7); top: 0; left: 0; display: flex; flex-direction: column; align-items: center; justify-content: center; z-index: 9";
    telaCarregamento.appendChild(spinner)

    document.body.appendChild(telaCarregamento);

    let respostaAnuncioEnviado = await postDados(ANUNCIOS_URL, dados);

    if(!respostaAnuncioEnviado.ok) {
        document.body.removeChild(telaCarregamento);
        mostrarToast(document.querySelector("form") , `Todos os campos são obrigatórios`, 'erro', 5000);
    } else {
        let respostaAnuncioJson = await respostaAnuncioEnviado.json();

        const idAnuncio = respostaAnuncioJson.id;
        let thumbnails = document.querySelectorAll(".anuncio-imagem-thumbnail");

        if(imagensAdicionadas.length > 0) {

            // Realiza o upload de todas as imagens que selecionamos
            for(const foto of imagensAdicionadas) {
                let fotoUpada = await postArquivo(`${ANUNCIOS_URL}/${idAnuncio}/upload`, foto);
                if(fotoUpada.ok) {
                    let respostaUpload = await fotoUpada.json();
                    thumbnailIds.push(respostaUpload.id);
                }
            }

            
            if(thumbnailIds.length > 0) {

                let indiceThumbEscolhida = Array.from(thumbnails).findIndex(thumb => thumb.classList.contains("anuncio-imagem-ativa"));
    
                if(indiceThumbEscolhida == null || indiceThumbEscolhida == -1) {
                    thumbnailInput.value = thumbnailIds[0];
                } else {
                    thumbnailInput.value = thumbnailIds[indiceThumbEscolhida];
                }

                const thumbnailId = new FormData();
                thumbnailId.append("thumbnailId", thumbnailInput.value);
            
                let envioThumbnail = await postDados(`${ANUNCIOS_URL}/${idAnuncio}/thumbnail`, thumbnailId);
                
                if(!envioThumbnail.ok) {
                    mostrarToast(document.querySelector("form") , `Falha ao salvar thumbnail`, 'erro', 5000)
                } else {
                    let divSucesso = criarMensagemDeSucesso();

                    telaCarregamento.removeChild(spinner);
                    telaCarregamento.appendChild(divSucesso);

                    setTimeout(() => {
                        window.location = `${window.location.origin}/meus-anuncios`;
                    }, 3000);
                }
            }
        } else {
            let divSucesso = criarMensagemDeSucesso();
            telaCarregamento.remove(spinner);
            telaCarregamento.appendChild(divSucesso);

            setTimeout(() => {
                window.location = `${window.location.origin}/meus-anuncios`;
            }, 3000);
        }
    }
});
    
function criarMensagemDeSucesso() {
    let divSucesso = document.createElement("div");
    divSucesso.style = "display: flex; flex-direction: column; width: 100%; align-items: center; color: white; z-index: 11;";

    let iconeSucesso = document.createElement("i");
    iconeSucesso.className = "bi bi-check-circle-fill";
    iconeSucesso.style = "font-size: 8rem; color: #1C7C54;";

    let textoSucesso = document.createElement("p");
    textoSucesso.textContent = "Anúncio criado com sucesso!";
    textoSucesso.style = "font-size: 2rem; color: white; font-weight: bold;";

    divSucesso.appendChild(iconeSucesso);
    divSucesso.appendChild(textoSucesso);
    return divSucesso;
}

// Envia um arquivo ao servidor de uma URL com uma requisição POST
async function postDados(url, data) {
  
    return fetch(url, {
      method: 'POST',
      body: data,
      credentials: "same-origin",
    });
}

// Envia um arquivo ao servidor de uma URL com uma requisição POST
async function postArquivo(url, data) {
    const formData  = new FormData();

    formData.append("file", data);
  
    return fetch(url, {
      method: 'POST',
      body: formData,
      credentials: "same-origin"
    });
}