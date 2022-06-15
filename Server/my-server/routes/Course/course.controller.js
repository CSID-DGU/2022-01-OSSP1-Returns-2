var db = require('../../db/connect');

var courseJSON = require('../../dataset/runninCourseDataset.json');
var courseRecommend = require('../../algorithms/courseRecommend');
/**
 * 코스 정보 입력 API
 */

module.exports.input = (res) => {
  const conn = db.conn();
  var SeoulRunningCourse = JSON.parse(courseJSON);
  var list = Object.values(SeoulRunningCourse);
  var list2 = [];
  for (let i = 0; i < list.length; i++) {
    list2[i] = Object.values(list[i]);
    var sql =
      "insert into RunningCourseAndTrack (course_no,course_location,course_name,course_length,course_start_latitude,course_start_longitude,course_final_latitude,course_final_longitude,course_level,course_time,course_match_count) values ?";

    conn.query(sql, [list2], function (err, result) {
      if (err) {
        console.log(err);
        db.conn();
      } else {
        res.json({
          result: true,
          msg: "러닝 코스 입력 완료",
        });
      }
    });
  }
};
  

/**
 * 모든 러닝코스 출발 좌표값 가져오는 API
 */

module.exports.selectALL = (req, res) => {
  const conn = db.conn();
  
  var sql = "select course_start_latitude,course_start_longitude FROM RunningCourseAndTrack ";
  conn.query(sql,function(err,result){
    
    if(err) {
      console.log(err);
      conn.end();
    }else{
      var arr =[];
      for (var i =0; i<result.length;i++){
        arr.push({
          courseNo : "course" + String(i+1),
          latitude : result[i].course_start_latitude,
          longitude : result[i].course_start_longitude
        });
      }
      res.json({
        result: true,
        msg: "모든 러닝코스 조회",
        data : arr, 
      });
    }
  });
};

/**
 * 코스 추천 
 */



module.exports.courseRecommend = (req,res) =>{

  const conn = db.conn();
  var user_id = req.query.user_id;
  // 해당 유저의 러닝 데이터 가저오기 
  var sql = 'SELECT course_name, MAX(course_rating) FROM User_Running_Records where nickname = ?';
  var params = [
    user_id
  ]
  conn.query(sql,params,function(err, result){
    if(err){
      console.log(err);
      
    }else{
      console.log(result[0].course_name)
      courseRecommend(parseInt(result[0].course_name)).then(function(result){
        res.json({
          result :true,
          msg : "코스 " + String(result) + "을 추천합니다.",
          data : result
        })
      });
      
    }

      
  });
  

}