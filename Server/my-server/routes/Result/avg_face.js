
function avg_face(total_distance,total_time) {
   
    // time은 "00:00:00" string -> 짤라 주기 분 단위로 넣기  km 대로 들어옴 거리는

    var time = parseFloat(total_time.substring(0, 2)) * 60 +parseFloat(total_time.substring(3, 5)) + parseFloat(total_time.substring(6,8)) / 60;

    
    var avg_face =  time / total_distance;
    var faceString = convertNumToTime(avg_face);
    var arr = faceString.split(':');
    console.log(arr[1]);
    console.log(typeof arr[1]);
    if (arr[0]>= 60){
        var hourAndMinutes = parseInt(faceString.split(':',1));
        console.log(hourAndMinutes);
        var minute = hourAndMinutes % 60;
        console.log(minute);
        var hour = Math.floor(hourAndMinutes / 60);
        console.log(hour);
        if(hour > 10){
            return String(hour) + ":" + String(minute) + ":" + arr[1];
        }
        else{
            return "0"+ String(hour) + ":" + String(minute) + ":" + arr[1];
        }
        
    }
    else {
        return "00:"+faceString;
    }
}  

function convertNumToTime(number) {
    // Check sign of given number
    var sign = (number >= 0) ? 1 : -1;

    // Set positive value of number of sign negative
    number = number * sign;

    // Separate the int from the decimal part
    var hour = Math.floor(number);
    var decpart = number - hour;

    var min = 1 / 60;
    // Round to nearest minute
    decpart = min * Math.round(decpart / min);

    var minute = Math.floor(decpart * 60) + '';

    // Add padding if need
    if (minute.length < 2) {
    minute = '0' + minute; 
    }

    // Add Sign in final result
    sign = sign == 1 ? '' : '-';

    // Concate hours and minutes
    time = sign + hour + ':' + minute;

    return time;
}

module.exports = avg_face;
