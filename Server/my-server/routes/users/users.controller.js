var db = require("../../db/connect");

/**
 *
 * 로그인 API
 */
module.exports.login = (req, res) => {
  var conn = db.conn();
  var id = req.body.id;
  var password = req.body.password;
  var sql = "select * from Users where id = ? AND password = ?";
  var params = [id, password];
  conn.query(sql, params, (err, result) => {
    if (err) {
      console.log(err);
      conn.end();
    } else {
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
          id: result[0].id,
        });
      }
    }
  });
};

/**
 *
 * 회원가입 API
 *
 */
module.exports.join = (req, res) => {
  const conn = db.conn();

  var nickname = req.body.nickname;
  var email = req.body.email;
  var password = req.body.password;
  var user_location_latitude = null;
  var user_location_longitude = null;
  var gender = req.body.gender;
  var career = req.body.career;
  var activity_place = req.body.activity_place;
  var average_face = "00:00:00";
  var running_type = null;
  var id = req.body.id;

  var sql =
    "INSERT INTO Users (nickname, email, password, user_location_latitude, user_location_longitude, gender, career, activity_place, average_face, running_type, id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
    id,
  ];

  conn.query(sql, params, function (err, result) {
    if (err) {
      console.log(err);
      conn.end();
    } else {
      res.json({
        result: true,
        msg: "회원가입에 성공했습니다.",
      });
    }
  });
};

/**
 * 프로필 api
 */
module.exports.profile = (req, res) => {
  const conn = db.conn();
  var id = req.body.id;
  var sql = "select * from Users where id = ?";
  var params = [id];
  conn.query(sql, params, function (err, result) {
    if (err) {
      console.log(err);
      conn.end();
    } else {
      res.json({
        result: true,
        msg: "프로필 조회",
        nickname: result[0].nickname,
        running_count: result[0].running_count,
        average_face: result[0].average_face,
        average_distance: result[0].new_run_distance,
      });
    }
  });
};
