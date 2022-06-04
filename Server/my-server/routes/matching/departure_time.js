/*
departure_time을 인자로 받아서 시간부분만 -1시간 +1시간 두개 구해서
리턴해줌
*/
function departure_time(departure_time) {
  //결과를 담을 배열 선언
  let result = [];

  // 0시 이면
  if (parseFloat(departure_time.substring(12, 13)) === 0) {
    result1 =
      departure_time.substring(0, 11) + "11" + departure_time.substring(13, 19);
    result2 =
      departure_time.substring(0, 12) +
      (parseInt(departure_time.substring(12, 13)) + 1) +
      departure_time.substring(13, 19);
    result.push(result1);
    result.push(result2);
  } else {
    result1 =
      departure_time.substring(0, 12) +
      (parseInt(departure_time.substring(12, 13)) - 1) +
      departure_time.substring(13, 19);
    result2 =
      departure_time.substring(0, 12) +
      (parseInt(departure_time.substring(12, 13)) + 1) +
      departure_time.substring(13, 19);
    result.push(result1);
    result.push(result2);
  }
  return result;
}

// time = "2022-05-02 00:59:00";
// result = departure_time(time);
// console.log(result);

module.exports = departure_time;
