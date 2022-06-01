let matchingJSON = require("../dataset/matchingDataSet.json");

let matchingData = [];
matchingJSON.matchingDataSet.forEach((item) => {
    data_dictionary = {}
    data_dictionary['user_id'] = item.user_id;
    data_dictionary['match_user_id'] = item.match_users_id;
    data_dictionary['rating'] = item.rating;
    
    matchingData.push(data_dictionary);
})

module.exports = matchingData;

// let user_id = matchingData[0][0]; //매칭을 원하는 유저의 아이디 추출
// let match_user_ids = []; // 매칭된 유저들의 id를 저장할 배열
// let ratings = []; //매칭된 유저들의 rating을 저장할 배열
// let str = ""
// let final_Data = [];

// for (let i = 0; i < matchingData.length; i++) {
//     ratings.push(matchingData[i][2]); //rating 저장
//     for (let j = 0; j < matchingData[i][1].length; j++) { // 매칭된 유저의 수만큼 반복문 돌려서 match_user_ids 배열에 추가
//         str += matchingData[i][1][j].split(",");
        
//         if (j == matchingData[i][1].length - 1) str += ",";
//     }
// }
//  match_user_ids.push(str);
 
// console.log(user_id);
// console.log(ratings);
// console.log(match_user_ids[0]);


