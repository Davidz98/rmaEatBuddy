const express = require("express");
const bodyParser = require("body-parser");
const mongoose = require("mongoose");

const path = require("path");
const ingredientsRouter = require("./routes/ingredients");
const authRouter = require("./routes/auth");

const app = express();
app.use(bodyParser.json());

app.use("/images", express.static(path.join(__dirname, "images")))

app.use((req, res, next) => {
    res.setHeader("Access-Control-Allow-Origin", "*");
    res.setHeader(
      "Access-Control-Allow-Methods",
      "OPTIONS, GET, POST, PUT, PATCH, DELETE"
    );
    res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    next();
  });


app.use("/ingredients", ingredientsRouter);
app.use("/auth", authRouter);

app.use("", (req, res, next)=>{
  console.log("greksa");
  next()
})

app.use((error, req, res, next) => {
  const status = error.statusCode || 500;
  const message = error.message;
  const data = error.data;
  res.status(status).json({
      message: message,
      data: data,
  });
});

mongoose
.connect(
"mongodb+srv://david:lozinkamongo@cluster0-ituk5.mongodb.net/eatBuddy?retryWrites=true"
)
.then((results) => {
const server = app.listen(8080);
})
.catch((err) => console.log(err));