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
    if (err) {
      console.log(err);
      db.conn();
    }else {
      res.json({
        result: true,
        msg: "매칭방 생성 완료",
      });
    }
  });
}


/**
 * 활성화된 매칭방 전체 조회 API
 */

module.exports.load = (req,res) => {
  const conn = db.conn();
  var sql = "SELECT * FROM Activating_Room where flag = 0";
  conn.query(sql,function(err,result){
    if(err){
      console.log(err);
      conn.end();
    }else{
      var arr =[];
      for (var i =0; i<result.length;i++){
        arr.push({
          rome_id : result[i].rome_id,
          nickname : result[i].nickname,
          departure_time : result[i].departure_time,
          running_time : result[i].running_time,
          mate_gender : result[i]. mate_gender,
          mate_level : result[i].mate_level,
          start_latitude : result[i].start_latitude,
          start_longitude : result[i].start_longitude
        });
        res.json({
          msg : "활성화된 매칭방 전체 조회 완료",
          data : arr,
          result : true
        });
      }
    }
  });
}

/**
 * 코스 이름을 불러와 매칭방 가져오기
 */

module.exports.objActivate = (req,res) => {
  const conn = db.conn();
  var courseNo = req.query.courseNo;
  var sql = "SELECT * FROM Activating_Room as a inner JOIN RunningCourseAndTrack as r on (a.start_latitude = r.course_start_latitude and a.start_longitude = r.course_start_longitude AND a.flag = 0 AND r.course_no = ?)";
  var params = [courseNo];
  
  //Activating_Room 의 start_latitude, start_longitude 와 RunningCourseAndTrack의 course_start_longitude 와 course_start_latitude의 정보가 동일 한 것을 다 가져옴 
  conn.query(sql,params, function(err, result){
    if (err) {
      console.log(err);
      conn.end();
    }else{
      console.log(result);
      var arr = [];
      for (var i =0; i<result.length;i++){
        arr.push({
          rome_id : result[i].rome_id,
          nickname : result[i].nickname,
          departure_time : result[i].departure_time,
          running_time : result[i].running_time,
          mate_gender : result[i].mate_gender,
          mate_level : result[i].mate_level,
          start_latitude : result[i].start_latitude,
          start_longitude : result[i].start_longitude
        });
      }
      console.log(arr);
      res.json({
        msg : "코스 에 해당 하는 매칭방 정보 조회 완료",
        data : arr,
        result : true
      });
    }
    
  });

}