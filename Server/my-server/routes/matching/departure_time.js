/*
departure_time을 인자로 받아서 시간부분만 -1시간 +1시간 두개 구해서
리턴해줌
*/
function departure_time(departure_time) {
  //결과를 담을 배열 선언
  let result = [];

  // 0시 이면
  if (parseInt(departure_time.substring(12, 13)) === 0) {
    //1일일 때
    if (parseInt(departure_time.substring(8, 10)) === 1) {
      //1월 일때 ...
      if (parseInt(departure_time.substring(5, 7)) === 1) {
        result1 =
          parseInt(departure_time.substring(0, 4)) -
          1 +
          "-12-31 23" +
          departure_time.substring(13, 19);
      } else if (parseInt(departure_time.substring(5, 7)) === 2) {
        //이전 달이 31일까지
        result1 =
          departure_time.substring(0, 5) +
          "0" +
          (parseInt(departure_time.substring(5, 7)) - 1) +
          "-31 23" +
          departure_time.substring(13, 19);
      } else if (parseInt(departure_time.substring(5, 7)) === 3) {
        //이전달이 28일까지
        result1 =
          departure_time.substring(0, 5) +
          "0" +
          (parseInt(departure_time.substring(5, 7)) - 1) +
          "-28 23" +
          departure_time.substring(13, 19);
      } else if (parseInt(departure_time.substring(5, 7)) === 4) {
        //이전달이 31일까지
        result1 =
          departure_time.substring(0, 5) +
          "0" +
          (parseInt(departure_time.substring(5, 7)) - 1) +
          "-31 23" +
          departure_time.substring(13, 19);
      } else if (parseInt(departure_time.substring(5, 7)) === 5) {
        //이전달이 30일까지
        result1 =
          departure_time.substring(0, 5) +
          "0" +
          (parseInt(departure_time.substring(5, 7)) - 1) +
          "-30 23" +
          departure_time.substring(13, 19);
      } else if (parseInt(departure_time.substring(5, 7)) === 6) {
        //이전달이 31일까지
        result1 =
          departure_time.substring(0, 5) +
          "0" +
          (parseInt(departure_time.substring(5, 7)) - 1) +
          "-31 23" +
          departure_time.substring(13, 19);
      } else if (parseInt(departure_time.substring(5, 7)) === 7) {
        //이전달이 30일까지
        result1 =
          departure_time.substring(0, 5) +
          "0" +
          (parseInt(departure_time.substring(5, 7)) - 1) +
          "-30 23" +
          departure_time.substring(13, 19);
      } else if (parseInt(departure_time.substring(5, 7)) === 8) {
        //이전달이 31일까지
        result1 =
          departure_time.substring(0, 5) +
          "0" +
          (parseInt(departure_time.substring(5, 7)) - 1) +
          "-31 23" +
          departure_time.substring(13, 19);
      } else if (parseInt(departure_time.substring(5, 7)) === 9) {
        //이전달이 31일까지
        result1 =
          departure_time.substring(0, 5) +
          "0" +
          (parseInt(departure_time.substring(5, 7)) - 1) +
          "-31 23" +
          departure_time.substring(13, 19);
      } else if (parseInt(departure_time.substring(5, 7)) === 10) {
        //이전달이 30일까지
        result1 =
          departure_time.substring(0, 5) +
          "0" +
          (parseInt(departure_time.substring(5, 7)) - 1) +
          "-30 23" +
          departure_time.substring(13, 19);
      } else if (parseInt(departure_time.substring(5, 7)) === 11) {
        //이전달이 31일까지
        result1 =
          departure_time.substring(0, 5) +
          (parseInt(departure_time.substring(5, 7)) - 1) +
          "-31 23" +
          departure_time.substring(13, 19);
      } else if (parseInt(departure_time.substring(5, 7)) === 12) {
        //이전달이 30일까지
        result1 =
          departure_time.substring(0, 5) +
          (parseInt(departure_time.substring(5, 7)) - 1) +
          "-30 23" +
          departure_time.substring(13, 19);
      }
    } else {
      //1일이 아닐 때
      if (departure_time.substring(8, 10) < 10) {
        //2~9일 때
        result1 =
          departure_time.substring(0, 8) +
          "0" +
          (parseInt(departure_time.substring(8, 10)) - 1) +
          departure_time.substring(10, 11) +
          "23" +
          departure_time.substring(13, 19);
      } else {
        //10~31일
        result1 =
          departure_time.substring(0, 8) +
          (parseInt(departure_time.substring(8, 10)) - 1) +
          departure_time.substring(10, 11) +
          "23" +
          departure_time.substring(13, 19);
      }
    }
    result2 =
      departure_time.substring(0, 12) +
      (parseInt(departure_time.substring(12, 13)) + 1) +
      departure_time.substring(13, 19);
    result.push(result1);
    result.push(result2);
  } else {
    // 1시~ 23시
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

time = "2022-03-01 00:59:00";
result = departure_time(time);
console.log(result);

module.exports = departure_time;
