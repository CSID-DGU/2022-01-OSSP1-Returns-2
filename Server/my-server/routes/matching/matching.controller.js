var db = require('../../db/connect');


/**
 * 매칭방 생성 API
 */

module.exports.create = (req,res) =>{
  const conn = db.conn();
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

  conn.query(sql, params, function (err, result) {
    if (err) console.log(err);
    else {
      res.json({
        result: true,
        msg: "매칭방 생성 완료",
      });
    }
  });
}