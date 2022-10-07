/* 
* Scripts for the functionality of the Carousel 
*/

let images = document.getElementsByClassName("carousel-image");
let activeImage = document.getElementsByClassName("carousel-active-image")[0]
let previousButton = document.getElementById("previous-button");
let nextButton = document.getElementById("next-button");
let controllerButtons = document.getElementsByClassName("carousel-slide-controllers")[0];

function onMouseOver() {
	controllerButtons.style.visibility = "visible";
}

function onMouseLeave() {
	controllerButtons.style.visibility = "hidden";
}

const setNextImage = (direction) => {
	let currentIndex = Array.from(images).findIndex(img => img.src === activeImage.src);
	if(direction === 'forward') {
		let nextIndex = currentIndex + 1;
		if(nextIndex === images.length) {
			nextIndex = 0;
		}
		activeImage.src = images[nextIndex].src;
		activeImage.alt = images[nextIndex].alt;
	}
	
	else if (direction === 'backward') {
		let nextIndex = currentIndex - 1;
		if(nextIndex < 0) {
			nextIndex = images.length - 1;
		}
		activeImage.src = images[nextIndex].src;
		activeImage.alt = images[nextIndex].alt;
	}
}

previousButton.addEventListener("click", () => {
	setNextImage('backward');
})

nextButton.addEventListener("click", () => {
	setNextImage('forward');
})

for(let i = 0; i < images.length; i++) {
	images[i].addEventListener('click', (e) => {
		activeImage.src = e.target.src;
		activeImage.alt = e.target.alt;
	})
}