var db = require('../../db/connect');

/**
 * 러닝 결과 API
 */
module.exports.input = (req, res) => {
    const conn = db.conn();

    //이거도 아직 스트링임. 러닝탭에서 이 유저의 러닝 코스가 뭔지 표시해 줘야함..
    var courseNo = req.body.courseNo;

    //String형태로 넘어오는데 바꿔줘야해!
    // var time = req.body.time; 

    var distance = req.body.distance; //double
    var run_rate = req.body.run_rate; //float

    // var sql = "INSERT"; //sql문 작성 필요

    var params = [
        courseNo,
        time,
        distance,
        run_rate
    ];

    conn.query(sql, params, function (err, result){
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