const express = require('express');
const router = express.Router();

const user = require('./users/index');
const matching = require('./matching/index');
const course = require('./Course/index');
const result = require('./Result/index');

router.use('/matching', matching);
router.use('/user', user);
router.use('/course',course);
router.use('/result', result);

module.exports = router;