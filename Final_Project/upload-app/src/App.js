import logo from './logo.svg';
import './App.css';
import axios from 'axios';
import {Progress} from 'reactstrap';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import React, { Component, useState } from 'react';
import { uploadFile } from 'react-s3';
//import UploadImageToS3WithNativeSdk from "./components/UploadImageToS3WithNativeSdk"
//import UploadImageToS3WithReactS3 from "./components/UploadImageToS3WithReactS3"


const S3_BUCKET =process.env.REACT_APP_S3_BUCKET;
const DIR_NAME = process.env.REACT_APP_DIR_NAME;
const REGION =process.env.REACT_APP_REGION;
const ACCESS_KEY =process.env.REACT_APP_ACCESS_KEY;
const SECRET_ACCESS_KEY =process.env.REACT_APP_SECRET_ACCESS_KEY;

const config = {
  bucketName: S3_BUCKET,
  dirName: DIR_NAME,
  region: REGION,
  accessKeyId: ACCESS_KEY,
  secretAccessKey: SECRET_ACCESS_KEY,
}


function App() {
  const [selectedFile, setSelectedFile] = useState(null);

    const handleFileInput = (e) => {
        console.log(config)
        setSelectedFile(e.target.files[0]);
    }

    const handleUpload = async (file) => {
        uploadFile(file, config)
            .then(data => console.log(data))
            .catch(err => console.error(err))

        setTimeout(function(){
              var btn = document.getElementById("download");
              var chk = document.getElementById("chk");
              
              btn.disabled = !chk.onclick;
              },30000);
    }

    return (
      <div class="container">
        <div class = "row">
        <div class="col-md-6"> 
        <div class="form-group files">
          <div class="myDiv">
          <h2>KNN Classifier</h2>
            </div>
        <input type="file" class="form-control" onChange={handleFileInput}/>
        </div>
        <button id="chk" onClick={() => handleUpload(selectedFile)}> Upload to Classify</button>
        <a href='https://knnbucket.s3.amazonaws.com/downloadfiles/KNeighborsOut.csv' download>
        <button disabled id="download">Click to download output</button>
        </a>
        
    </div>
    </div>
    </div>)


}

export default App;


