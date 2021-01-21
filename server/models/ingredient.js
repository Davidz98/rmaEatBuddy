const mongoose = require("mongoose");

const Schema = mongoose.Schema;

const ingredientSchema = new Schema({
    tip: {
        type: String,
        required: true
    },
    naziv: {
        type: String,
        required: true
    },
    proteini: {
        type: Number,
        required: true
    },
    masti: {
        type: Number,
        required: true
    },
    kalorije: {
        type: Number,
        required: true
    },
    ugljeniHidrati: {
        type: Number,
        required: true
    },
    slika: {
        type: String,
        required: true
    }
    
})

module.exports = mongoose.model("Meal", ingredientSchema);