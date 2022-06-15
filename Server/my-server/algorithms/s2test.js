const s2 = require('@radarlabs/s2');
const course = require('../dataset/runninCourseDataset.json');

function coursetoToken (){
    var cellList = [];
    var courselength = course.course.length;
    for (let i = 0; i<courselength;i++){
        
        cellList.push({
            id : course.course[i].course_no,
            coursetoken : new s2.CellId(new s2.LatLng(course.course[i].course_start_latitude,course.course[i].course_start_longitude))
        })
    }
    return cellList;
}

function courseInCell(user_lat,user_lng) {
    
    //유저 위치에 대한 s2 latlng object 생성
    const s2LatLong = new s2.LatLng(user_lat,user_lng);

    //cell covering option 지정
    const cellCoveringOptions = {min: 11, max: 11, max_cells:11};

    //유저의 위치를 중심으로 5000m의 반경 검색을 포함하는 셀을 가져온다
    const coveringCells = s2.RegionCoverer.getRadiusCovering(s2LatLong, 5000,cellCoveringOptions);

    // console.log(coveringCells.cellIds().map((cellId) => cellId.token()));
    return coveringCells;
}

function main(latitude,longitude){
    var courseToken = coursetoToken();
    var courseIncell1 = courseInCell(latitude,longitude);

    var test = [];
    // for (let i =0; i<courseIncell1.length; i++){
    //     for (let j = 0; j<courseToken.length;j++){
    //         if(courseInCell){
    //             test.push({
    //                 id : courseToken[j].id
    //             })
    //         }
    //     }
    // }
    for (let i =0; i<courseToken.length;i++){
        if(courseIncell1.contains(courseToken[i].coursetoken)){
            test.push({
                course_no : courseToken[i].id
            })
        }
    }
    console.log(test);
    return test;
}

module.exports = main;