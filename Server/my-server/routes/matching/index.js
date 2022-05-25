const express = require('express');
const router = express.Router();
const matching = require('./matching.controller');

router.post('/create',matching.create);

module.exports = router;