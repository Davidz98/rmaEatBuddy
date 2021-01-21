const { validationResult } = require("express-validator/check");
const bcrypt = require("bcryptjs");
const User = require("../models/user");

exports.loginController = async function (req, res, next) {
    console.log("usao u log in proveru");
    const username = req.body.username;
    const password = req.body.password;
    let loadedUser;
    try {
        const user = await User.findOne({ username: username });
        if (!user) {
        const error = new Error("A user with that username could not be found.");
        error.statusCode = 401;
        throw error;
        }
        loadedUser = user;
        const isEqual = await bcrypt.compare(password, user.password);

        if (!isEqual) {
        const error = new Error("Wrong password.");
        error.statusCode = 401;
        throw error;
        }
        res.status(200).json({ message: "Authentication successful." });
    } catch (err) {
        if (!err.statusCode) {
        err.statusCode = 500;
        }
        next(err);
    }
}

exports.signupController = (req, res, next) => {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
        const error = new Error();
        error.statusCode = 422;
        error.data = errors.array();
        throw error;
    }
    const username = req.body.username;
    const password = req.body.password;
    bcrypt
    .hash(password, 12)
    .then((hashedPw) => {
      const user = new User({
        username: username,
        password: hashedPw
      });
      return user.save();
    })
    .then((result) => {
      res.status(201).json({
        message: "Successful signup."
      });
    })
    .catch((err) => {
      if (!err.statusCode) {
        err.statusCode = 500;
      }
      next(err);
    });
}