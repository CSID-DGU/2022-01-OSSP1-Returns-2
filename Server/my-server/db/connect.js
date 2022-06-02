//DB연결
const { stringify } = require("jade/lib/utils");
var db = require("mysql");

//import dotenv from 'dotenv'
require("dotenv").config();

module.exports.conn = function (){
  const conn = db.createConnection({
    host: process.env.HOST_NAME,
    user: process.env.USER_NAME,
    database: process.env.DATABASE_NAME,
    password: process.env.DATABASE_PASSWORD,
    port: 3306,
    dateStrings: 'date'
  });
  conn.connect(function(err){
    if(err){
      console.error('DB Connect error : '+err.stack);
      return;
    }
    console.log("DB Connect successfull! Id : "+ conn.threadID);
  });
  return conn;
}
