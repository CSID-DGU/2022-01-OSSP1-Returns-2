const express = require("express");
const router = express.Router();
const matching = require("./matching.controller");

router.post("/create", matching.create);
router.get("/searchActivate", matching.load);
router.get("/courseActivate", matching.objActivate);
router.post("/inputdata", matching.input);
router.post("/matching", matching.matching);
module.exports = router;
