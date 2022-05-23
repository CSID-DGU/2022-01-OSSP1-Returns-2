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
//import dotenv from 'dotenv'
require("dotenv").config();

var connection = mysql.createConnection({
  host: process.env.HOST_NAME,
  user: process.env.USER_NAME,
  database: process.env.DATABASE_NAME,
  password: process.env.DATABASE_PASSWORD,
  port: 3306,
});

//회원가입
app.post("/user/join", function (req, res) {
  var nickname = req.body.nickname;
  var email = req.body.email;
  var password = req.body.password;
  var user_location_latitude = null;
  var user_location_longitude = null;
  var gender = req.body.gender;
  var career = req.body.career;
  var activity_place = req.body.activity_place;
  var average_face = null;
  var running_type = null;
  var match_with_course = null;
  var match_with_track = null;
  var id = req.body.id;
  var running_count = 0;
  var average_distance = 0.0;

  var sql =
    "INSERT INTO Users (nickname, email, password, user_location_latitude, user_location_longitude, gender, career, activity_place, average_face, running_type, match_with_course, match_with_track, id, running_count, average_distance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  var params = [
    nickname,
    email,
    password,
    user_location_latitude,
    user_location_longitude,
    gender,
    career,
    activity_place,
    average_face,
    running_type,
    match_with_course,
    match_with_track,
    id,
    running_count,
    average_distance,
  ];

  connection.query(sql, params, function (err, result) {
    if (err) console.log(err);
    else {
      res.json({
        result: true,
        msg: "회원가입에 성공했습니다.",
      });
    }
  });
});

//로그인
app.post("/user/login", function (req, res) {
  var id = req.body.id;
  var password = req.body.password;
  var sql = "select * from Users where id = ? AND password = ?";
  var params = [id, password];
  connection.query(sql, params, function (err, result) {
    if (err) console.log(err);
    else {
      if (result.length === 0) {
        res.json({
          result: false,
          msg: "존재하지 않는 계정입니다!",
        });
      } else if (password !== result[0].password) {
        res.json({
          result: false,
          msg: "비밀번호가 틀렸습니다!",
        });
      } else {
        res.json({
          result: true,
          msg: "로그인 성공!",
          nickname: result[0].nickname,
          email: result[0].email,
          password: result[0].password,
          user_location_latitude: result[0].user_location_latitude,
          user_location_longitude: result[0].user_location_longitude,
          gender: result[0].gender,
          career: result[0].career,
          activity_place: result[0].activity_place,
          average_face: result[0].average_face,
          running_type: result[0].running_type,
          match_with_course: result[0].match_with_course,
          match_with_track: result[0].match_with_track,
          id: result[0].id,
          running_count: result[0].running_count,
          average_distance: result[0].average_distance,
        });
      }
    }
  });
});

//매칭방 생성
app.post("/matching/join", function (req, res) {
  var nickname = req.body.nickname;
  var departure_time = req.body.departure_time;
  var running_time = req.body.running_time;
  var mate_gender = req.body.mate_gender;
  var mate_level = req.body.mate_level;
  var start_latitude = req.body.start_latitude;
  var start_longitude = req.body.start_longitude;

  var sql =
    "INSERT INTO Activating_Room (room_id, nickname, departure_time, running_time, mate_gender, mate_level, start_latitude, start_longitude) VALUES ((SELECT IFNULL(MAX(Room_id)+1,1) FROM Activating_Room a), ?, ?, ?, ?, ?, ?, ?)";

  var params = [
    nickname,
    departure_time,
    running_time,
    mate_gender,
    mate_level,
    start_latitude,
    start_longitude,
  ];
  connection.query(sql, params, function (err, result) {
    if (err) console.log(err);
    else {
      res.json({
        result: true,
        msg: "매칭방 생성 완료",
      });
    }
  });
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
