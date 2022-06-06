const { CF, evaluation } = require("nodeml");
const matchingData = require("./matchingDataPreprocessing");

function matching(user_nickname) {
  let train = [],
    test = [];
  for (let i = 0; i < matchingData.length; i++) {
    if (Math.random() > 0.8) test.push(matchingData[i]);
    else train.push(matchingData[i]);
  }
  // for (let i = 0; i < matchingData.length; i++) {
  //   train.push(matchingData[i]);
  //   if (Math.random() > 0.8) test.push(matchingData[i]);
  // }
  const cf = new CF();

  // cf.maxRelatedItem = 40;
  // cf.maxRelatedUser = 40;

  cf.train(train, "user_id", "match_user_id", "rating");

  let gt = cf.gt(test, "user_id", "match_user_id", "rating");
  let result = cf.recommendGT(gt, 10);
  let recommend_list = cf.recommendToUser(user_nickname, 37);
  let ndcg = evaluation.ndcg(gt, result);

  // for (let i=0; i<result.length;i<++) {
  //     keys = Object.keys(result[i]);

  // }
  keys1 = Object.keys(recommend_list);
  values1 = Object.values(recommend_list);
  let final_recommend = [];

  console.log(Object.values(values1[0])[0]);

  for (let i = 0; i < keys1.length; i++) {
    //유저와 추천 유저가 같다면 추천 리스트에서 제거
    if (user_nickname === Object.values(values1[i])[0]) {
    } else {
      final_recommend.push(Object.values(values1[i])[0]);
    }
  }

  //매칭을 원하는 user_id 에게 추천해주는 matching_user_id 찾아 리턴
  //인덱스 찾기
  // let find_index = 0;

  // for (let i = 0; i < result.length; i++) {
  //   if (result[i] === user_nickname) {
  //     find_index = i;
  //   }
  // }

  // for (let j = 0; j < values1[find_index].length; j++) {
  //   if (user_nickname === Object.values(values1[find_index][j])[0]) {
  //   } else {
  //     final_recommend.push(Object.values(values1[find_index][j])[0]);
  //   }
  // }

  // final_recommend.push(result[find_index]);

  //평가지표 (1에 가까울수록 좋음)
  console.log(ndcg);
  console.log(final_recommend);
  // console.log(recommend_list);

  // console.log(final_recommend);

  //매칭을 원하는 유저에게 추천하는 유저 리스트를 리턴해줌!
  return final_recommend;
}

module.exports = matching;
