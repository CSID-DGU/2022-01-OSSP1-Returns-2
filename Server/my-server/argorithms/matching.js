const {CF,evaluation} = require('nodeml');
const matchingData = require('./matchingDataPreprocessing');

let train = [], test = [];
for (let i = 0; i < matchingData.length; i++) {
  if (Math.random() > 0.8) test.push(matchingData[i]);
  else train.push(matchingData[i]);
}

const cf = new CF();

cf.maxRelatedItem = 10;
cf.maxRelatedUser = 10;

cf.train(train, 'user_id', 'match_user_id', 'rating');

let gt = cf.gt(test, 'user_id', 'match_user_id', 'rating');
let result = cf.recommendGT(gt,10);
let ndcg = evaluation.ndcg(gt, result);

// for (let i=0; i<result.length;i<++) {
//     keys = Object.keys(result[i]);
    
// }
keys1 = Object.keys(result);
values1 = Object.values(result);
let final_recommend = [];

for (let i = 0; i<keys1.length; i++) {
     // 유저와 추천 유저가 같다면 다음 추천 유저를 추천
     if (keys1[i] === Object.values(values1[i][0])[0]) {
        final_recommend.push(Object.values(values1[i][1])[0]);
    }
    else {
        final_recommend.push(Object.values(values1[i][0])[0]);
    }
}

console.log(ndcg);
console.log(result);
console.log(final_recommend);