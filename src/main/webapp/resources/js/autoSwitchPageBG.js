var srcBgArray = ["resources/images/page-bg1.jpg","resources/images/page-bg3.jpg"];

$(document).ready(function() {
    $("#bg-body").bcatBGSwitcher({
        urls: srcBgArray,
        alt: "Full screen background image",
        timeout: 10000,
        speed: 3000,
        loadImgCallback: "getImgNaturalDimensions(this)"
    });
    window.onresize = function() {
        autoAdjustImageSize();
    };
});

function getImgNaturalDimensions(img) {
    var nWidth = 0, nHeight = 0;
    if (img.naturalWidth) {
        nWidth = img.naturalWidth;
        nHeight = img.naturalHeight;
        adjustImageSize(img, [nWidth, nHeight]);
    } else {                            // IE6/7/8
        var image = new Image();
        image.src = img.src;
        image.onload = function() {
            adjustImageSize(img, [image.width, image.height]);
        };
    }
}

function adjustImageSize(img, imgNaturalDimensions) {
    var displayStatus = img.style.display;
    img.height = -1;
    img.attributes.removeNamedItem("height");
    img.width = -1;
    img.attributes.removeNamedItem("width");
    if(document.body.clientWidth < document.body.clientHeight){
        img.height = document.body.clientHeight;
    }else{
        var scale = parseFloat(imgNaturalDimensions[0]) / parseFloat(imgNaturalDimensions[1]);
        if((document.body.clientWidth / scale) < document.body.clientHeight){
            img.height = document.body.clientHeight;
        }else{
            img.width = document.body.clientWidth;
        }
    }
    img.style.display = displayStatus;
}

function autoAdjustImageSize() {
    var imgSwitcher = document.getElementById("bg-body");
    var imgArray =  imgSwitcher.getElementsByTagName("img");
    if(imgArray){
        for(i = 0; i < imgArray.length; i++){
            getImgNaturalDimensions(imgArray[i]);
        }
    }
}
