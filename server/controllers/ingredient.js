const Ingredient = require("../models/ingredient");


exports.ingredientMeatController = async function(req, res, next){
    try{
        const meatIngredients = await Ingredient.find({tip : "meso"});
        console.log(meatIngredients);
        res.status(200).json(meatIngredients);
    } catch(err){
        if (err.statusCode == null){
            err.statusCode = 500;
        }
        next(err);
    }
}

exports.ingredientFruitController = async function(req, res, next){
    try{
        const fruitIngredients = await Ingredient.find({tip : "voce"});
        console.log(fruitIngredients);
        res.status(200).json(fruitIngredients);
    } catch(err){
        if (err.statusCode == null){
            err.statusCode = 500;
        }
        next(err);
    }
}

exports.ingredientVegetableController = async function(req, res, next){
    try{
        const vegetableIngredients = await Ingredient.find({tip : "povrce"});
        console.log(vegetableIngredients);
        res.status(200).json(vegetableIngredients);
    } catch(err){
        if (err.statusCode == null){
            err.statusCode = 500;
        }
        next(err);
    }
}