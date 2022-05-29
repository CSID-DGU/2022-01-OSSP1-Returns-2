function preprocessing(total_distance, avg_face) {
  let running_type = 0;
  if (total_distance <= 1) {
    running_type = 1; //단거리
    if (avg_face.substring(3, 5) < 3) {
      //0분대, 1분대, 2분대
      avg_face = 1;
    } else if (avg_face.substring(3, 5) == 3) {
      //3분대
      if (avg_face.substring(6, 8) <= 5) {
        avg_face = 1;
      } else if (avg_face.substring(6, 8) <= 25) {
        avg_face = 2;
      } else if (avg_face.substring(6, 8) <= 45) {
        avg_face = 3;
      } else if (avg_face.substring(6, 8) <= 59) {
        //3분 46초~ 3분 59초
        avg_face = 4;
      }
    } else if (avg_face.substring(3, 5) == 4) {
      //4분대
      if (avg_face.substring(6, 8) <= 0) {
        avg_face = 4;
      } else if (avg_face.substring(6, 8) <= 20) {
        avg_face = 5;
      } else if (avg_face.substring(6, 8) <= 40) {
        avg_face = 6;
      } else if (avg_face.substring(6, 8) <= 59) {
        //4분 41초 ~ 4분 59초
        avg_face = 7;
      }
    } else if (avg_face.substring(3, 5) == 5) {
      //5분대
      if (avg_face.substring(6, 8) <= 0) {
        avg_face = 7;
      } else if (avg_face.substring(6, 8) <= 20) {
        avg_face = 8;
      } else if (avg_face.substring(6, 8) <= 35) {
        avg_face = 9;
      } else if (avg_face.substring(6, 8) <= 55) {
        avg_face = 10;
      } else if (avg_face.substring(6, 8) <= 59) {
        //5분 56초 ~ 5분 59초
        avg_face = 11;
      }
    } else if (avg_face.substring(3, 5) == 6) {
      //6분대
      if (avg_face.substring(6, 8) <= 15) {
        avg_face = 11;
      } else if (avg_face.substring(6, 8) <= 30) {
        avg_face = 12;
      } else if (avg_face.substring(6, 8) <= 50) {
        avg_face = 13;
      } else if (avg_face.substring(6, 8) <= 59) {
        // 6분 51초 ~ 6분 59초
        avg_face = 14;
      }
    } else if (avg_face.substring(3, 5) == 7) {
      //7분대
      if (avg_face.substring(6, 8) <= 15) {
        avg_face = 14;
      } else if (avg_face.substring(6, 8) >= 30) {
        avg_face = 15;
      }
    } else {
      //9분대 부터
      avg_face = 15;
    }
  } else if (total_distance <= 5) {
    running_type = 5; //중거리
    if (avg_face.substring(3, 4) < 3) {
      //0분대 , 1분대, 2분대
      avg_face = 1;
    } else if (avg_face.substring(3, 4) == 3) {
      //3분대
      if (avg_face.substring(6, 7) <= 25) {
        avg_face = 1;
      } else if (avg_face.substring(6, 7) <= 45) {
        avg_face = 2;
      } else if (avg_face.substring(6, 7) <= 59) {
        //3분 46초~ 3분 59초
        avg_face = 3;
      }
    } else if (avg_face.substring(3, 4) == 4) {
      //4분대
      if (avg_face.substring(6, 7) <= 5) {
        avg_face = 3;
      } else if (avg_face.substring(6, 7) <= 20) {
        avg_face = 4;
      } else if (avg_face.substring(6, 7) <= 45) {
        avg_face = 5;
      } else if (avg_face.substring(6, 7) <= 59) {
        //4분 46초 ~ 4분 59초
        avg_face = 6;
      }
    } else if (avg_face.substring(3, 4) == 5) {
      //5분대
      if (avg_face.substring(6, 7) <= 0) {
        avg_face = 6;
      } else if (avg_face.substring(6, 7) <= 25) {
        avg_face = 7;
      } else if (avg_face.substring(6, 7) <= 40) {
        avg_face = 8;
      } else if (avg_face.substring(6, 7) <= 59) {
        //5분 41초 ~ 5분 59초
        avg_face = 9;
      }
    } else if (avg_face.substring(3, 4) == 6) {
      //6분대
      if (avg_face.substring(6, 7) <= 0) {
        avg_face = 9;
      } else if (avg_face.substring(6, 7) <= 20) {
        avg_face = 10;
      } else if (avg_face.substring(6, 7) <= 35) {
        avg_face = 11;
      } else if (avg_face.substring(6, 7) <= 59) {
        // 6분 36초 ~ 6분 59초
        avg_face = 12;
      }
    } else if (avg_face.substring(3, 4) == 7) {
      //7분대
      if (avg_face.substring(6, 7) <= 0) {
        avg_face = 12;
      } else if (avg_face.substring(6, 7) <= 15) {
        avg_face = 13;
      } else if (avg_face.substring(6, 7) <= 35) {
        avg_face = 14;
      } else if (avg_face.substring(6, 7) >= 55) {
        avg_face = 15;
      }
    } else {
      //나머지
      avg_face = 15;
    }
  } else if (total_distance > 5) {
    running_type = 10; //장거리
    if (avg_face.substring(3, 5) < 3) {
      //0분대, 1분대, 2분대
      avg_face = 1;
    } else if (avg_face.substring(3, 5) == 3) {
      //3분대
      if (avg_face.substring(6, 8) <= 35) {
        avg_face = 1;
      } else if (avg_face.substring(6, 8) <= 55) {
        avg_face = 2;
      } else if (avg_face.substring(6, 8) <= 59) {
        //3분 56초~ 3분 59초
        avg_face = 3;
      }
    } else if (avg_face.substring(3, 5) == 4) {
      //4분대
      if (avg_face.substring(6, 8) <= 10) {
        avg_face = 3;
      } else if (avg_face.substring(6, 8) <= 32) {
        avg_face = 4;
      } else if (avg_face.substring(6, 8) <= 55) {
        avg_face = 5;
      } else if (avg_face.substring(6, 8) <= 59) {
        //4분 56초 ~ 4분 59초
        avg_face = 6;
      }
    } else if (avg_face.substring(3, 5) == 5) {
      //5분대
      if (avg_face.substring(6, 8) <= 15) {
        avg_face = 6;
      } else if (avg_face.substring(6, 8) <= 35) {
        avg_face = 7;
      } else if (avg_face.substring(6, 8) <= 55) {
        avg_face = 8;
      } else if (avg_face.substring(6, 8) <= 59) {
        //5분 56초 ~ 5분 59초
        avg_face = 9;
      }
    } else if (avg_face.substring(3, 5) == 6) {
      //6분대
      if (avg_face.substring(6, 8) <= 15) {
        avg_face = 9;
      } else if (avg_face.substring(6, 8) <= 35) {
        avg_face = 10;
      } else if (avg_face.substring(6, 8) <= 55) {
        avg_face = 11;
      } else if (avg_face.substring(6, 8) <= 59) {
        // 6분 56초 ~ 6분 59초
        avg_face = 12;
      }
    } else if (avg_face.substring(3, 5) == 7) {
      //7분대
      if (avg_face.substring(6, 8) <= 15) {
        avg_face = 12;
      } else if (avg_face.substring(6, 8) <= 30) {
        avg_face = 13;
      } else if (avg_face.substring(6, 8) <= 45) {
        avg_face = 14;
      } else if (avg_face.substring(6, 8) <= 59) {
        //7분 46초 ~ 7분 59초
        avg_face = 15;
      }
    } else if (avg_face.substring(3, 5) == 8) {
      //8분대
      if (avg_face.substring(6, 8) >= 10) {
        avg_face = 15;
      }
    } else {
      //9분대 ~
      avg_face = 15;
    }
  }
  return [running_type, avg_face];
}

let total_distance = 6;
let avg_face = "00:04:30";

let result = preprocessing(total_distance, avg_face);
console.log("transport running_type : " + result[0]);
console.log("transport avg_face : " + result[1]);

export default preprocessing;
