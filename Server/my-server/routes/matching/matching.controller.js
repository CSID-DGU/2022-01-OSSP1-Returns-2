var db = require('../../db/connect');
const matchingData = require("../../algorithms/matchingDataPreprocessing");
const return_departure_time = require("./departure_time");
const matching_start = require("../../algorithms/matching");
const main = require('../../algorithms/s2test');
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
      return;
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
          room_id : result[i].room_id,
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
        return;
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
  console.log(courseNo);
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
          room_id : result[i].room_id,
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
      return;
    }
    
  });
  
}

/**
 * room_id로 방 정보 가져오기
 */

 module.exports.room = (req,res) => {
  const conn = db.conn();
  var id = req.query.room_id;
  console.log(id);
  var sql = "SELECT * FROM Activating_Room as a inner JOIN RunningCourseAndTrack as r on (a.start_latitude = r.course_start_latitude and a.start_longitude = r.course_start_longitude AND a.flag = 0 AND a.room_id = ?)";
  var params = [id];
  
  //Activating_Room 의 start_latitude, start_longitude 와 RunningCourseAndTrack의 course_start_longitude 와 course_start_latitude의 정보가 동일 한 것을 다 가져옴 
  conn.query(sql,params, function(err, result){
    if (err) {
      console.log(err);
      conn.end();
    }else{
      console.log(result);
      res.json({
        msg : "룸넘버에 해당 하는 매칭방 정보 조회 완료",
        result : true,
        courseNo: result[0].course_no,
        departure_time: result[0].departure_time,
        running_time: result[0].running_time,
        mate_gender: result[0].mate_gender,
        start_latitude: result[0].start_latitude,
        start_longitude: result[0].start_longitude,
      });
      return;
    }
    
  });
  
}





module.exports.input = (res) => {
  const conn = db.conn();
  matchingDataSet = matchingData;
  var list = matchingDataSet;
  var list2 = [];
  for (let i = 0; i < list.length; i++) {
    list2[i] = Object.values(list[i]);
    matching_number = (i+1).toString();
    list2[i].push(matching_number);
  }


  var sql =
    "insert into Matching_Rating (user_id, matching_user_id, rating, matching_number) values ?";

  conn.query(sql, [list2], function (err, result) {
    if (err) {
      console.log(err);
      db.conn();
    } else {
      res.json({
        result: true,
        msg: "매칭 데이터 입력 완료",
      });
      return;
    }
  });
  
};

/*
매칭 API
*/
module.exports.matching = (req, res) => {
  // try {
  //   const conn = await db.conn();
  // } catch (e) {
  //   console.log(e);
  // }
  // try {
  //   const conn = await db.conn();
  // } catch (e) {
  // }
  const conn = db.conn();
  let nickname = req.body.nickname;
  let departure_time = req.body.departure_time;
  let running_time = req.body.running_time;
  let mate_gender = req.body.mate_gender;
  let user_lat = parseFloat(req.body.user_lat);
  let user_lng = parseFloat(req.body.user_lng);
  let sql1 = "";
  let sql2 = "";
  let sql3 = "";
  let params1 = [];
  let params2 = [];
  let recommend_user_nickname = "";

  //유저주변 5km이내에 포함된 코스들의 코스 넘버 리턴
  let availableCourse = main(user_lat,user_lng);

  //running_time 하한,상한 (+-30분)
  let lower_running_time = (running_time - 30).toString();
  let upper_running_time = (running_time + 30).toString();

  //return_departure_time 함수를 이용하여 출발시간 범위 알아내기
  departure_time_range = return_departure_time(departure_time);

  //gender가 상관없음이면 조건검색에서 gender 삭제, 상관없음이 아니면 조건검색에 포함.
  if (mate_gender === "") {
    sql1 =
      "SELECT * FROM Activating_Room as a, RunningCourseAndTrack as r Where a.start_latitude = r.course_start_latitude AND a.running_time >= ? AND a.running_time <= ? AND a.departure_time >= ? AND a.departure_time <= ?";
    params1 = [ 
      lower_running_time,
      upper_running_time,
      departure_time_range[0],
      departure_time_range[1],
    ];
  } else {
    sql1 =
      "SELECT * FROM Activating_Room Activating_Room as a, RunningCourseAndTrack as r Where a.start_latitude = r.course_start_latitude AND a.running_time >= ? AND a.running_time <= ? AND a.departure_time <= ? AND a.departure_time >= ? AND a.mate_gender = ?";
    params1 = [
      lower_running_time,
      upper_running_time,
      departure_time_range[0],
      departure_time_range[1],
      mate_gender,
    ];
  }
  conn.query(sql1, params1, (err, result) => {
    if (err) {
      console.log(err);
      conn.end();
    }
    // else {
    //   var arr = []; //나중에 매칭방 정보 리턴해줄 때 사용
    //   var total_nickname = []; //매칭 가능한 닉네임들 저장
    //   for (var i = 0; i < result.length; i++) {
    //     arr.push({
    //       nickname: result[i].nickname,
    //       room_id: result[i].room_id,
    //       running_time: result[i].running_time,
    //     });
    //     total_nickname.push(result[i].nickname);
    //   }
    //   if (result.length === 0) {
    //     res.json({
    //       result: false,
    //       msg: "필수 조건을 만족하는 방이 없습니다",
    //     });
    //   } else {
    //     res.json({
    //       result: true,
    //       msg: "추천 가능한 닉네임 조회",
    //       data: arr,
    //     });
    //   }
    // }
    var arr = []; //나중에 매칭방 정보 리턴해줄 때 사용
    var total_nickname = []; //매칭 가능한 닉네임들 저장
    for (var i = 0; i < result.length; i++) {
      arr.push({
        nickname: result[i].nickname,
        room_id: result[i].room_id,
        course_no: result[i].course_no,
        running_time: result[i].running_time,
      });
      // total_nickname.push(arr[i].nickname);
    }
    if (result.length === 0) {
      sql3 =
        "select * FROM Activating_Room as a, RunningCourseAndTrack as r Where a.start_latitude = r.course_start_latitude ";
      conn.query(sql3, (err, result) => {
        if(err){
           console.log(err);
           conn.end();
        } else {
          for(let i=0; i<availableCourse.length;i++) {
            for(let j=0; j<result.length;j++){
              
              if(availableCourse[i] == result[j].courseNo){
                sql2 = "UPDATE Users SET room_id = ? WHERE nickname = ?";
                params2 = [result[j].room_id, nickname];

                conn.query(sql2, params2, (err, result) => {
                  if (err) {
                    console.log(err);
                    conn.end();
                  } else {
                    res.status(200).json({
                      result: true,
                      msg: "필수 조건을 만족하는 방이 없어서 가장 가까운 매칭방으로 매칭되었습니다.",
                    });
                    conn.end();
                  }
                });
                
              }
              
            }
          }
          
      }
      })  
  
    }
    let recommend_list = matching_start(nickname);

    // console.log(total_nickname);

    //리턴받은 매칭방 중 유저의 5km이내에 있는 코스들이 있는 매칭방의 user_nickname만 추천 가능한 닉네임으로 추가 
    for (let i = 0; i < arr.length; i++) {
      for (let j =0; j < availableCourse.length; j++) {
        if(arr[i].course_no === availableCourse[j].course_no) {
            total_nickname.push(arr[i].nickname);
        }
      }
    }

    console.log(total_nickname);

    //추천리스트에 1순위부터 차례대로 돌면서 현재 Activating_Room에 있는 유저가 있는지 확인
    for (let i = 0; i < recommend_list.length; i++) {
      for (let j = 0; j < total_nickname.length; j++) {
        if (recommend_list[i] == total_nickname[j]) {
          recommend_user_nickname = recommend_list[i];
          //추천할 유저가 존재하면
          recommend_room_id = result[j].room_id;

          sql2 = 'UPDATE Users SET room_id = ? WHERE nickname = ?';

          params2 = [recommend_room_id, nickname];  
          
          console.log("체크");
          conn.query(sql2, params2, (err, result) => {
            if (err) {
              console.log(err);
              conn.end();
            } else {
              res.status(200).json({
                result: true,
                recommend_user: recommend_user_nickname,
                room_id: recommend_room_id,
                msg: "매칭 성공!",
              });
              return;
            }
          }); 
          
        
        }
      }
    }
      console.log("예러ㅓ러ㅓ ")
    //추천할 유저가 존재하지 않으면
    if (recommend_user_nickname.length === 0) {
            sql3 =
              "select * FROM Activating_Room as a, RunningCourseAndTrack as r Where a.start_latitude = r.course_start_latitude ";
            conn.query(sql3, (err, result) => {
              if (err) {
                console.log(err);
                conn.end();
              } else {
                outer:for (let i = 0; i < availableCourse.length; i++) {
                  inner:for (let j = 0; j < result.length; j++) {
                    if (availableCourse[i].course_no == result[j].course_no) {
                      params2 = [result[j].room_id, nickname];
                      break outer;
                    }
                  }
                  
                }
                sql2 = "UPDATE Users SET room_id = ? WHERE nickname = ?";
                      

                      conn.query(sql2, params2, (err,result) => {
                        if(err){
                          console.log(err);
                          conn.end();
                        } else {
                          console.log("mark");
                          console.log(result)
                          res.status(200).json({
                            result: true,
                            msg: "필수 조건을 만족하는 방이 없어서 가장 가까운 매칭방으로 매칭되었습니다.",
                  
                          }); 
                          return;
                        }
                      })
                      
              }        
            });  
    }
  });
 
};