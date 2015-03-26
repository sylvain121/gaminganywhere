
function setSmallScreen() {
    $.get("/screenSmall", function(data){
        toastr.success('screen set to 1024 x 768');
    });
}

function startDiablo3(){
    $.get("/startd3", function(data){
        toastr.success('launching diablo 3');
    });
}

function startStreamServer() {
    $.get("/startStreamServer", function(data){
        toastr.success('start gaminganywhere server');
    });
}