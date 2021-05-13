import logo from './logo.svg';
import './App.css';
import axios from 'axios';
import {Progress} from 'reactstrap';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import React, { Component, useState } from 'react';
import { uploadFile } from 'react-s3';
import UploadImageToS3WithNativeSdk from "./components/UploadImageToS3WithNativeSdk"
import UploadImageToS3WithReactS3 from "./components/UploadImageToS3WithReactS3"

const S3_BUCKET ='knnbucket';
const DIR_NAME = "uploadfiles";
const REGION ='us-east-1';
const ACCESS_KEY ='AKIAX6M7U3EZPHC6HEMI';
const SECRET_ACCESS_KEY ='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';



const config = {
    bucketName: S3_BUCKET,
    dirName: DIR_NAME,
    region: REGION,
    accessKeyId: ACCESS_KEY,
    secretAccessKey: SECRET_ACCESS_KEY,
}

// class App extends Component {
//   constructor(props) {
//     super(props);
//       this.state = {
//         selectedFile: null,
//         loaded:0
//       }
   
//   }

//   onChangeHandler=event=>{

//     this.setState({
//       selectedFile: event.target.files,
//      })

//   }

//   onClickHandler = () => {
//   const data = new FormData() 
//   for(var x = 0; x<this.state.selectedFile.length; x++) {
//     data.append('file', this.state.selectedFile[x])
// }
//   // send http post request
//   axios.post(" http://s3.amazonaws.com/knnbucket", data, { // receive two parameter endpoint url ,form data 
//     onUploadProgress: ProgressEvent => {
//     this.setState({
//       loaded: (ProgressEvent.loaded / ProgressEvent.total*100),
//     })
//     },
//   })
//   .then(res => { 
//     toast.success('upload success')
//   })
//   .catch(err => { 
//     toast.error('upload fail')
//   })
//   }


//   render(){
  
//   return (
//     <div class="container">
//       <div class = "row">
//       <div class="col-md-6">
//         <div class="form-group files">
//           <label>Upload Features Here </label>
//           <input type="file" class="form-control" multiple onChange={this.onChangeHandler}/>
//         </div>
//         <div class="form-group">
//           <ToastContainer />
//           <Progress max="100" color="success" value={this.state.loaded} >{Math.round(this.state.loaded,2) }%</Progress>
//         </div>  
//         <button type="button" class="btn btn-success btn-block" onClick={this.onClickHandler}>Upload</button>
  
//         <p>
//           Upload files for processing. 
//         </p>
        
//       </div>
//       </div>
//     </div>
//   );
// }
// }



function App() {
  const [selectedFile, setSelectedFile] = useState(null);

    const handleFileInput = (e) => {
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

  // return (
  //   <div class="container">
  //     <div class = "row">
  //       <div class="col-md-6">
  //         <UploadImageToS3WithReactS3 />
  //       </div>
  //   </div>
  // </div>
  // );
}

export default App;


