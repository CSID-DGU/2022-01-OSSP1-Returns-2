const express = require('express');
const router = express.Router();
const course = require('./course.controller');

router.get('/selectALL',course.selectALL);
router.post('/inputdata',course.input);

module.exports = router;