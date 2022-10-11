const botoesApagar = document.getElementsByClassName("editar-anuncio-imagem-apagar-btn");
const anuncioThumbnails = Array.from(document.getElementsByClassName("anuncio-imagem-thumbnail"));
const uploadInput = document.getElementById("upload-input");
const visualizacaoImagem = document.getElementById("img-principal");

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
})