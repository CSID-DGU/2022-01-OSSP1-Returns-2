const matchingJSON = require("../dataset/matchingDataSet.json");

let matchingData = [];
matchingJSON.matchingDataSet.forEach((item) => {
    data_dictionary = {}
    data_dictionary['user_id'] = item.user_id;
    data_dictionary['match_user_id'] = item.match_users_id;
    data_dictionary['rating'] = item.rating;
    
    matchingData.push(data_dictionary);
})

module.exports = matchingData;



