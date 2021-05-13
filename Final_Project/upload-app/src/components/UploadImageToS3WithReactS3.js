import React , {useState} from 'react';
import { uploadFile } from 'react-s3';


const S3_BUCKET ='knnbucket';
const DIR_NAME = "uploadfiles";
const REGION ='us-east-1';
const ACCESS_KEY ='AKIAX6M7U3EZAEG7T2MP';
const SECRET_ACCESS_KEY ='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';

const config = {
    bucketName: S3_BUCKET,
    dirName: DIR_NAME,
    region: REGION,
    accessKeyId: ACCESS_KEY,
    secretAccessKey: SECRET_ACCESS_KEY,
}

const UploadImageToS3WithReactS3 = () => {

    const [selectedFile, setSelectedFile] = useState(null);

    const handleFileInput = (e) => {
        setSelectedFile(e.target.files[0]);
    }

    const handleUpload = async (file) => {
        uploadFile(file, config)
            .then(data => console.log(data))
            .catch(err => console.error(err))
    }

    return <div>
        <div>React S3 File Upload</div>
        <input type="file" onChange={handleFileInput}/>
        <button onClick={() => handleUpload(selectedFile)}> Upload to S3</button>
        <button disable href='https://knnbucket.s3.amazonaws.com/KNeighborsOut.csv' download>Click to download</button>
    </div>
}

export default UploadImageToS3WithReactS3;