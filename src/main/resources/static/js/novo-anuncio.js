const galeriaContainer = document.getElementById("galeria-container");
const uploadInputWrapper = document.getElementById("upload-wrapper");
const uploadInput = document.getElementById("editar-anuncio-upload-input");
const thumbnailInput = document.getElementById("editar-anuncio-thumbnail");
const formulario = document.getElementById("novo-anuncio-form");

const UPLOAD_URL = `upload/`;
const FOTO_URL = `fotos/`;
const ANUNCIOS_URL = `${window.location.origin}/anuncios`;

let thumbnailEscolhida = null;

uploadInput.addEventListener("change", (e) => {
    
    let imagens = Array.from(uploadInput.files);

    if(imagens.length > 0 && imagens.length <= 6) {
        const anuncioThumbnails = Array.from(document.getElementsByClassName("anuncio-imagem-thumbnail"));
        const thumbnailContainers = Array.from(document.getElementsByClassName('anuncio-imagem-thumbnail-wrapper'));

        imagens.forEach((imagem, indice) => {

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
                anuncioThumbnails.forEach(thumb => {
                    if(thumb.classList.contains("anuncio-imagem-ativa")) {
                        thumb.classList.remove("anuncio-imagem-ativa");
                    }
                });
        
                if(!novaImagem.classList.contains("anuncio-imagem-ativa")) {
                    novaImagem.classList.add("anuncio-imagem-ativa");
                    thumbnailInput.value = indice;
                }
            })

            novoBotao.addEventListener("click", (e) => {
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
                anuncioThumbnails.splice(indiceFotoApagada, 1);
        });
        })
    }
});
// TODO: COMPLETAR A IMPLEMENTAÇÃO DA CRIAÇÃO DO ANÚNCIO, ATUALMENTE O FORM DATA NÃO ESTÁ CONSEGUINDO PEGAR OS CAMPOS DO FORMULÁRIO.
formulario.addEventListener("submit", (e) => {
    e.preventDefault();
    const dados = new FormData(e.target);

    let spinner = document.createElement("span");
    spinner.className = "spinner-border";
    spinner.setAttribute("role", "status");
    spinner.style = "z-index: 1;"

    const telaCarregamento = document.createElement("div");
    telaCarregamento.style = "position: absolute; width: 100%; height: 100vh, min-height: 100vh; background: rgba(0,0,0,0.7); top: 0; left: 0; display: flex; align-items: center; justify-content: center; z-index: 10";
    telaCarregamento.appendChild(spinner)

    document.body.appendChild(telaCarregamento);

    postDados(ANUNCIOS_URL, dados)
        .then(async (response) => {
            const resposta = await response.json();
            if(response.status != 201) {
                mostrarToast(document.querySelector("main") , `Falha ao salvar anúncio: ${resposta.body}`, erro, 5000)
            } else {
                const idAnuncio = resposta.id;
                const fotosAEnviar = Array.from(uploadInput.files);
                const thumbnails = document.querySelectorAll(".anuncio-imagem-thumbnail");
                if(fotosAEnviar.length > 0) {
                    fotosAEnviar.forEach((foto, indice) => {
                        postArquivo(`${ANUNCIOS_URL}/${idAnuncio}/upload`, foto)
                        .then(async (response) => {
                            const respostaUpload = await response.json();
                            thumbnails[indice].id = respostaUpload.id;
                            console.log(respostaUpload)
                        });
                    });

                    // Caso sucedido, retornar à página de meus-anuncios com a mensagem de sucesso
                    // window.location = `${window.location.origin}/meus-anuncios`;
                }
            }
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
async function postDados(url, data) {
  
    return await fetch(url, {
      method: 'POST',
      body: data,
      credentials: "same-origin",
    });
}

// Envia um arquivo ao servidor de uma URL com uma requisição POST
async function postArquivo(url, data) {
    const formData  = new FormData();

    formData.append("file", data);
  
    return await fetch(url, {
      method: 'POST',
      body: formData,
      credentials: "same-origin"
    });
}