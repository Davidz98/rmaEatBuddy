const express = require("express");
const { body } = require("express-validator/check");

const loginController = require("../controllers/auth").loginController;
const signupController = require("../controllers/auth").signupController;
const User = require("../models/user");

const router = express.Router();

router.post("/login", loginController);
router.post("/signup", [
    body("username").custom((value, {req})=>{
        return User.findOne({username: value}).then((userDoc)=>{
            if (userDoc){
                return Promise.reject("User already exists.");
            }
        })
})], signupController);

module.exports = router;