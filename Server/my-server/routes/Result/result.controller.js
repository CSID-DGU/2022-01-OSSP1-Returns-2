var db = require('../../db/connect');
const preprocessing = require('../../preprocessing/preprocessing');
const faceinfo= require('./avg_face');
const mysql = require('mysql');
/**
 * 러닝 결과 API
 */
module.exports.input = (req, res) => {
    const conn = db.conn();
    var course_name = parseInt(req.body.courseNo);
    var total_time = req.body.time;
    var total_distance = parseFloat(req.body.distance); //double
    var nickname = req.body.nickname;
    var avg_face = faceinfo(req.body.distance,req.body.time);
    var course_rating = parseFloat(req.body.run_rate);
    var matching_rating = parseFloat(req.body.run_rate);
    var running_type = preprocessing(total_distance,avg_face);
    
    var sql1= 'INSERT into User_Running_Records (Record_id, nickname, course_name, total_time, avg_face, total_distance, matching_rating,course_rating) value ((SELECT IFNULL(MAX(Record_id)+1,1) FROM  User_Running_Records u), ?, ?, ?, ?, ?,?,?);';
    
    var params1= [
        nickname,
        course_name,
        total_time,
        avg_face,
        total_distance,
        course_rating,
        matching_rating,
    ];

    let sql1s = mysql.format(sql1,params1);

    var sql2 = ' UPDATE Users SET average_face = ?, running_type =?, running_count = running_count+1, new_run_distance = ?, new_course_no = ? WHERE nickname = ?;';

    var params2 = [
        avg_face,
        running_type,
        total_distance, 
        course_name,
        nickname,
    ]
    let sql2s = mysql.format(sql2,params2);
    
    conn.query( sql1s+sql2s, function (err, result){
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