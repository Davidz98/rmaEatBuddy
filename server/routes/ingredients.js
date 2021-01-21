const express = require("express");

const ingredientController = require("../controllers/ingredient");

const router = express.Router();

router.get("/meat", ingredientController.ingredientMeatController);

router.get("/fruit", ingredientController.ingredientFruitController);

router.get("/vegetable", ingredientController.ingredientVegetableController);

module.exports = router;