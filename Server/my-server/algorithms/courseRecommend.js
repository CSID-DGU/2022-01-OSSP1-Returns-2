//const tokenizing = require('./ff');
const courseDataset = require('../dataset/runninCourseDataset.json');
const userRatingData = require('../dataset/courseVectorDataset.json');
var similarity = require( 'compute-cosine-similarity' );
var mecab = require('mecab-ya');

/**
 * 전체 Flow
 * 1. 유저 평점을 기반으로 제일 높은 러닝 코스 정보 가져오기
 * 2. 러닝 코스 데이터 설명 tf-idf
 * 3. 코사인 유사도로 러닝 코스 유사도 측정 
 * 4. 추천 
 */


/**
 * 1. 유저 레이팅 평점 순으로 코스 정렬한 리스트
 */

// var ratingData;

// ratingData = userRatingData.course.sort(function(a,b){
//     return b['코스 평균 평점'] - a['코스 평균 평점'];
// });

// console.log(ratingData);

/**
 *  2-1. 토크나이징
 */


var tokenizing = async function() {
    return new Promise(function(res,rej){
        var tokenMap = [];
        for (let i =0; i<courseDataset.course.length;i++){
            mecab.nouns(courseDataset.course[i].course_describe, function (err, result) {
                tokenMap.push(result);
                //console.log(tokenMap);
                if (i === 18) {
                    //console.log(tokenMap);
                    res(tokenMap);
                }
            });
            
        }
        
    })
}

// var tokenizingCall =  function(){
//     return new Promise(function(res,rej){
//         for (let i = 0; i< 19; i++){
//             await tokenizing(courseDataset.course[i].course_describe,i);
//             if (i === 18)
//         }

//     })
// }
// tokenizingCall().then(function(result){
//     console.log(result)
// })

/**
 * 2-2. tf- idf
 */

var Tfidf = async function() {
    var token1 = await tokenizing();
    const token = token1.reduce(function (acc, cur) {
        return acc.concat(cur);
      });
    //console.log(token);
        return new Promise(function(res,rej){
        //console.log(token);
        var tfArray = [];
        for (let i = 0; i<courseDataset.course.length; i++){
            var tmp = [];
            for (let j = 0; j<token.length;j++){
                var tf = 0;
                var re = new RegExp(token[j],"gi");
                //console.log(re);
                //console.log(courseDataset.course[i].course_describe);
                if(courseDataset.course[i].course_describe.match(re))
                    tf = courseDataset.course[i].course_describe.match(re).length
                tmp.push(tf);
            }
            tfArray.push(tmp);
        }
        //console.log(tfArray);
        // idf 계산
        var tempArray = []; 
        for (let j = 0; j<token.length;j++){
            var tokenCnt = 0;
            var re =  new RegExp(token[j],"gi");
            //console.log(re);
            for (let i = 0; i<courseDataset.course.length; i++){
                if(courseDataset.course[i].course_describe.match(re)){
                    tokenCnt++;
                }
            }
            tempArray.push(Math.log(courseDataset.course.length / (tokenCnt +1))); // idf는 1차원 배열  
        }
        var idfArray = [];
        for(let i = 0; i< courseDataset.course.length;i++){
            idfArray[i] = tempArray;
        }
        //console.log(idfArray);
        //tfidf 계산
        var tfidfArray = [];
        for (let i = 0; i<idfArray.length; i++){
            var temp = [];
            for (let j = 0; j < idfArray[i].length;j++){
                var tfidf = tfArray[i][j] * idfArray[i][j];
                temp.push(tfidf);
            }
            tfidfArray[i] = temp;
        }
        res(tfidfArray);
    })
}
/**
 * 3. 코사인 유사도 측정 
 * 모듈 사용
 */
// console.log(similarity([tfidf[0].tfidf],[tfidf[1].tfidf]));

var sim = async function(){
    // console.log('token : ', await tokenizing());
    //console.log('tfidf : ', await tfidf());
    var tfidf = await Tfidf();
    var simArray = [];
    for(let i =0; i<tfidf.length; i++){
        var temp = [];
        for(let j = 0; j<tfidf.length; j++){
            if (i != j){
                temp.push(similarity(tfidf[i],tfidf[j]));
            }
 
        }
        simArray[i] = temp;
    }
    return simArray;
}
var main = async function(courseNo){
    const simArr = await sim();
    var most = simArr[courseNo-1];
    console.log(most);
    return new Promise(function(res,rej){
     //1차원 배열

    // // 유사도 높은 순으로 정렬 
    // console.log(mostInitial)
    // most.sort(function(a, b)  {
    //     return b - a;
    // });
    // console.log(mostInitial)
    // console.log(most[0])
   for(let i =0; i<most.length;i++){
       if(most[i] ==  Math.max.apply(null, most)){
           res(i+1);
       }
   }
});
}
// main(5).then(function(result){
//     console.log(result)
// });
module.exports = main;

