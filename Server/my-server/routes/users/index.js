const express = require("express");
const router = express.Router();
const user = require("./users.controller");

router.post("/login", user.login);
router.post("/join", user.join);
router.post("/profile", user.profile);
module.exports = router;
