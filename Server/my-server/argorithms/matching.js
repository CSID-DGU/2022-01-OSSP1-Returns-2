const {CF,evaluation} = requier('nodeml');

const matching = sample.matching(); //매칭 데이터 셋 {user_id : no, match_user_id : no, rating: num}

let train = [], test = [];
for(let i=0; i< matching.length; i++) {
    if(Math.random() > 0.8 ) test.push(matching[i]);
    else train.push(matching[i]);
}

const cf = new CF();

cf.maxRelatedItem = 10;
cf.maxRelatedUser = 10;

cf.train(train, 'user_id', 'match_user_id', 'rating');

let gt = cf.gt(test, 'user_id', 'match_user_id', 'rating');
let result = cf.recommendGT(gt,10);
let ndcg = evaluation.ndcg(gtr, result);
console.log(ndcg);