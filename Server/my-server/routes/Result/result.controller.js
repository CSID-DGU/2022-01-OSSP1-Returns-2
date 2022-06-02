var db = require('../../db/connect');

const faceinfo= require('./avg_face');
/**
 * 러닝 결과 API
 */
module.exports.input = (req, res) => {
    const conn = db.conn();

    //이거도 아직 스트링임. 러닝탭에서 이 유저의 러닝 코스가 뭔지 표시해 줘야함..
    var course_name = parseInt(req.body.courseNo);

    var total_time = req.body.time;
    var total_distance = parseFloat(req.body.distance); //double
    var nickname = req.body.nickname;
    var avg_face = faceinfo(req.body.distance,req.body.time);
    var course_rating = parseFloat(req.body.run_rate);
    var matching_rating = parseFloat(req.body.run_rate);

    var sql1= "INSERT into User_Running_Records (Record_id, nickname, course_name, total_time, avg_face, total_distance,matching_rating,course_rating) value ((SELECT IFNULL(MAX(Record_id)+1,1) FROM  User_Running_Records u), ?, ?, ?, ?, ?,?,?);"
    var params = [
        nickname,
        course_name,
        total_time,
        avg_face,
        total_distance,
        course_rating,
        matching_rating,
    ];

    conn.query(sql1, params, function (err, result){
        if (err) {
            console.log(err);
            conn.end();
        } else {
            res.json({
                result: true,
                msg: "러닝 결과 전송 완료.",
            });
        }
    });
};