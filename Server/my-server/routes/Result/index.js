const express = require('express');
const router = express.Router();
const result = require('./result.controller');

router.post('/runningResult',result.input);

module.exports = router;