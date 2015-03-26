var sys = require('sys');
var exec = require('child_process').exec;
var child;

var
    START_STREAM_SERVER =    "$HOME/sh/startStreamer",
    START_DIABLO_3 =         "$HOME/sh/diablo3",
    FIX_D3_CPU =             "$HOME/sh/d3FixCPU",
    SET_CPU_PERFORMANCE =    "sudo scale performance",
    DISPLAY =                "DISPLAY=:0 ";

module.exports = {

    exec : execute,

    setScreenSize: setScreenSize,
    startd3: startd3,
    startStreamServer:startStreamServer



};



function execute(command, cb){

    child = exec(command, function (error, stdout, stderr) {
        sys.print('stdout: ' + stdout);
        sys.print('stderr: ' + stderr);
        if (error ) {
            return cb(error);
        }
        else {
            return cb(null,child);
        }
    });
}

function setScreenSize(width, height, cb){
    execute(DISPLAY+"xrandr -s "+width+"x"+height, cb)
}

function startd3(cb){
    execute(DISPLAY+START_DIABLO_3, cb)
}

function startStreamServer(cb){
    execute(DISPLAY+START_STREAM_SERVER, cb)
}
