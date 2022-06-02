
function avg_face(total_distance,total_time) {
   
    // time은 "00:00:00" string -> 짤라 주기 분 단위로 넣기 

    var time = parseFloat(total_time.substring(0, 2)) * 60 +parseFloat(total_time.substring(3, 5)) + parseFloat(total_time.substring(6,8)) / 60;

    return (time/total_distance).toFixed(2);
}  

module.exports = avg_face;
