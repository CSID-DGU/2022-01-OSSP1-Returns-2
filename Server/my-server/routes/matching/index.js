const express = require('express');
const router = express.Router();
const matching = require('./matching.controller');

router.post('/create',matching.create);
router.get('/searchActivate',matching.load);

module.exports = router;