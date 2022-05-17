var createError = require("http-errors");
var express = require("express");
var bodyParser = require("body-parser");
var path = require("path");
var cookieParser = require("cookie-parser");
var logger = require("morgan");

var indexRouter = require("./routes/index");
var usersRouter = require("./routes/users");

var app = express();

// 사용자가 웹사이트로 전달하는 정보들을 검사하는 미들웨어
app.use(bodyParser.json({ extended: true }));
// json이 아닌 post형식으로 올때 파서
app.use(bodyParser.urlencoded({ extended: true }));

//DB연결
var mysql = require("mysql");

var connection = mysql.createConnection({
  host: "database-1.cx2za6xyjcju.ap-northeast-2.rds.amazonaws.com",
  user: "admin",
  database: "RunningApp",
  password: "rlawlgh1234",
  port: 3306,
});

// view engine setup
app.set("views", path.join(__dirname, "views"));
app.set("view engine", "jade");

app.use(logger("dev"));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, "public")));

app.use("/", indexRouter);
app.use("/users", usersRouter);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
  next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get("env") === "development" ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render("error");
});

module.exports = app;
