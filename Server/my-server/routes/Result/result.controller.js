var db = require('../../db/connect');
const preprocessing = require('../../preprocessing/preprocessing');
const faceinfo= require('./avg_face');
const mysql = require('mysql');
const { input } = require('../matching/matching.controller');
/**
 * 러닝 결과 API
 */
module.exports.input = (req, res) => {
  const conn = db.conn();
  var course_name = parseInt(req.body.courseNo);
  var total_time = req.body.time;
  var total_distance = parseFloat(req.body.distance); //double
  var nickname = req.body.nickname;
  var avg_face = faceinfo(req.body.distance, req.body.time);
  var course_rating = parseFloat(req.body.run_rate);
  var matching_rating = parseFloat(req.body.run_rate);
  var running_type = preprocessing(total_distance, avg_face);
  var room_id = req.body.room_id;

  console.log(req.body);

  var sql1 =
    "INSERT into User_Running_Records (Record_id, nickname, course_name, total_time, avg_face, total_distance, matching_rating,course_rating) value ((SELECT IFNULL(MAX(Record_id)+1,1) FROM  User_Running_Records u), ?, ?, ?, ?, ?,?,?);";

  var params1 = [
    nickname,
    course_name,
    total_time,
    avg_face,
    total_distance,
    course_rating,
    matching_rating,
  ];

  let sql1s = mysql.format(sql1, params1);

  var sql2 =
    " UPDATE Users SET average_face = ?, running_type =?, running_count = running_count+1, new_run_distance = ?, new_course_no = ? WHERE nickname = ?;";

  var params2 = [avg_face, running_type, total_distance, course_name, nickname];
  let sql2s = mysql.format(sql2, params2);

  let sql3 = "select nickname FROM Users WHERE room_id = ?";
  let params3 = [room_id,];
  let sql3s = mysql.format(sql3, params3);

  conn.query(sql1s + sql2s + sql3s, function (err, result) {
    if (err) {
      console.log(err);
      conn.end();
    } else {
      console.log(result[2][0].nickname);
    
      input_nickname = [];

      for (let i=0; i<result[2].length;i++){
          if(nickname === result[2][i].nickname){
          } else {
            input_nickname.push(result[2][i].nickname);
          }
      }
      console.log(input_nickname);

      var today = new Date();

      input_list = [];
      input_list2 = [];
      for(let i=0; i<input_nickname.length;i++){
          input_list2.push(today.toString());
          input_list2.push(nickname);
          input_list2.push(input_nickname[i]);
          input_list2.push(matching_rating.toString());
          input_list.push(input_list2);
      }

      console.log(input_list2);
      console.log(input_list);

      sql4 =
        "insert into Matching_Rating  (matching_Date,user_id, matching_user_id, rating) values ?";

      conn.query(sql4, [input_list], function(err,result) {
          if(err){
               console.log(err);
               db.conn();
          }
          else {
                res.json({
                  result: true,
                  msg: "러닝 결과 전송 완료.",
                });
          }
      });

      
    }
  });
};