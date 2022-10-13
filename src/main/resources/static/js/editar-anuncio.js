const botoesApagar = document.getElementsByClassName("editar-anuncio-imagem-apagar-btn");
const anuncioThumbnails = Array.from(document.getElementsByClassName("anuncio-imagem-thumbnail"));
const galeriaContainer = document.getElementById("galeria-container");
const uploadInputWrapper = document.getElementById("upload-wrapper");
const uploadInput = document.getElementById("editar-anuncio-upload-input");
const visualizacaoImagem = document.getElementById("img-principal");

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
        }
        
    });
});

uploadInput.addEventListener("change", (e) => {

    let novaImagemWrapper = document.createElement("div");
    novaImagemWrapper.className = "d-flex p-3 px-5 justify-content-center border align-items-center ms-2";

    let spinner = document.createElement("span");
    spinner.className = "spinner-border";
    spinner.setAttribute("role", "status");

    novaImagemWrapper.appendChild(spinner);
    


    galeriaContainer.insertBefore(novaImagemWrapper, uploadInputWrapper);

    setTimeout(() => {
        novaImagemWrapper.removeChild(spinner);
        let novaImagem = document.createElement("img");
        novaImagem.src = e.target;
        novaImagem.className = "img-thumbnail";
        
        novaImagemWrapper.appendChild(novaImagem);

    }, 2000)


});