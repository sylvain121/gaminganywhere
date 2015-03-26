var express = require('express');
var router = express.Router();
var exec = require('../cmd/exec');


router.get('/screenSmall', function(req, res, next) {
    exec.setScreenSize(1024, 768, function(err, child){
       if(err){
           res.status(500).send(err);
       }
        res.status(200).send(child);
    });
});

router.get('/startd3', function(req, res, next) {
    exec.startd3(function(err, child){
        if(err){
            res.status(500).send(err);
        }
        res.status(200).send(child);
    });
});

router.get('/startStreamServer', function(req, res, next) {
    exec.startStreamServer(function(err, child){
        if(err){
            res.status(500).send(err);
        }
        res.status(200).send(child);
    });
});

module.exports = router;
