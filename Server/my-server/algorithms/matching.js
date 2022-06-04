const { CF, evaluation } = require("nodeml");
const matchingData = require("./matchingDataPreprocessing");

function matching(user_nickname) {
  let train = [],
    test = [];
  for (let i = 0; i < matchingData.length; i++) {
    if (Math.random() > 0.8) test.push(matchingData[i]);
    else train.push(matchingData[i]);
  }

  const cf = new CF();

  // cf.maxRelatedItem = 40;
  // cf.maxRelatedUser = 40;

  cf.train(train, "user_id", "match_user_id", "rating");

  let gt = cf.gt(test, "user_id", "match_user_id", "rating");
  let result = cf.recommendGT(gt, 40);
  let ndcg = evaluation.ndcg(gt, result);

  // for (let i=0; i<result.length;i<++) {
  //     keys = Object.keys(result[i]);

  // }
  keys1 = Object.keys(result);
  values1 = Object.values(result);
  let final_recommend = [];

  // for (let i = 0; i < keys1.length; i++) {
  //   // 유저와 추천 유저가 같다면 다음 추천 유저를 추천
  //   if (keys1[i] === Object.values(values1[i][0])[0]) {
  //     final_recommend.push(Object.values(values1[i][1])[0]);
  //   } else {
  //     final_recommend.push(Object.values(values1[i][0])[0]);
  //   }
  // }

  //매칭을 원하는 user_id 에게 추천해주는 matching_user_id 찾아 리턴
  //인덱스 찾기
  let find_index = 0;

  for (let i = 0; i < result.length; i++) {
    if (result[i] === user_nickname) {
      find_index = i;
    }
  }

  for (let j = 0; j < values1[find_index].length; j++) {
    if (user_nickname === Object.values(values1[find_index][j])[0]) {
    } else {
      final_recommend.push(Object.values(values1[find_index][j])[0]);
    }
  }

  // final_recommend.push(result[find_index]);

  //평가지표 (1에 가까울수록 좋음)
  console.log(ndcg);
  console.log(final_recommend);
  // console.log(final_recommend);

  //매칭을 원하는 유저에게 추천하는 유저 리스트를 리턴해줌!
  return final_recommend;
}

// console.log(matching("user1"));

module.exports = matching;
