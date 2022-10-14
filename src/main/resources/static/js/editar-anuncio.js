const botoesApagar = document.getElementsByClassName("editar-anuncio-imagem-apagar-btn");
const anuncioThumbnails = Array.from(document.getElementsByClassName("anuncio-imagem-thumbnail"));
const galeriaContainer = document.getElementById("galeria-container");
const uploadInputWrapper = document.getElementById("upload-wrapper");
const uploadInput = document.getElementById("editar-anuncio-upload-input");
const thumbnailInput = document.getElementById("editar-anuncio-thumbnail");
const visualizacaoImagem = document.getElementById("img-principal");

const ID_ANUNCIO = window.location.pathname.split("/")[2];
const UPLOAD_URL = `upload/`;

let thumbnailEscolhida = null;

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
            thumbnailEscolhida = imagemClicada;
            thumbnailInput.value = thumbnailInput;
        }
        
    });
});

uploadInput.addEventListener("change", async (e) => {

    let novaImagemWrapper = document.createElement("div");
    novaImagemWrapper.className = "anuncio-imagem-thumbnail-wrapper";

    let spinner = document.createElement("span");
    spinner.className = "spinner-border";
    spinner.setAttribute("role", "status");

    novaImagemWrapper.appendChild(spinner);
    
    galeriaContainer.insertBefore(novaImagemWrapper, uploadInputWrapper);

    sendData(UPLOAD_URL, uploadInput.files[0])
        .then(async (response) => {
            let respostaJson = await response.json();
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
        });


});


async function sendData(url, data) {
    const formData  = new FormData();

    formData.append("file", data);
  
    return await fetch(url, {
      method: 'POST',
      body: formData,
      credentials: "same-origin"
    });
  
  }