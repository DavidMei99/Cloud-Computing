import logo from './logo.svg';
import './App.css';
import React, { Component } from 'react';
import axios from 'axios';
import {Progress} from 'reactstrap';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

class App extends Component {
  constructor(props) {
    super(props);
      this.state = {
        selectedFile: null,
        loaded:0
      }
   
  }

  onChangeHandler=event=>{

    this.setState({
      selectedFile: event.target.files,
     })

  }

  onClickHandler = () => {
  const data = new FormData() 
  for(var x = 0; x<this.state.selectedFile.length; x++) {
    data.append('file', this.state.selectedFile[x])
}
  // send http post request
  axios.post("http://localhost:8000/upload", data, { // receive two parameter endpoint url ,form data 
    onUploadProgress: ProgressEvent => {
    this.setState({
      loaded: (ProgressEvent.loaded / ProgressEvent.total*100),
    })
    },
  })
  .then(res => { 
    toast.success('upload success')
  })
  .catch(err => { 
    toast.error('upload fail')
  })
  }


  render(){
  return (
    <div class="container">
      <div class = "row">
      <div class="col-md-6">
        <div class="form-group files">
          <label>Upload Labels Here </label>
          <input type="file" class="form-control" multiple onChange={this.onChangeHandler}/>
        </div>
        <div class="form-group">
          <ToastContainer />
          <Progress max="100" color="success" value={this.state.loaded} >{Math.round(this.state.loaded,2) }%</Progress>
        </div>  
        <button type="button" class="btn btn-success btn-block" onClick={this.onClickHandler}>Upload</button>

      
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Upload files for processing. 
        </p>
        
      </div>
      </div>
    </div>
  );
}
}

export default App;
