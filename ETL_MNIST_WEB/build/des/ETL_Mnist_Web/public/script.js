function processFiles(files) {
var file = files[0];
 
var reader = new FileReader();
 
reader.onload = function (e) {
// Cuando éste evento se dispara, los datos están ya disponibles.
// Se trata de copiarlos a una área <div> en la página.
var output = document.getElementById("fileOutput");
output.textContent = e.target.result;
};
reader.readAsText(file);
}
