# Installation

## Environment Requirement
- AWS CLI version 2

## Enter AWS CLI Scripts Directory
```
cd Cloud-Computing/Final_Project/aws
```

## Setup AWS Services
### 1. Create VPC
```
./create_vpc.sh
```
### 2. Create SSH Keypair
```
./create_keypair.sh
```
### 3. Create EC2 Instances
```
./create_ec2_in_vpc.sh
```
### 4. Create S3 Bucket
```
./create_s3.sh
```
### 5. Allocate and Associate Elastic IP Address
a. Login to AWS Console with an IAM role of Administrator access  
b. Go to EC2 > Network & Security > Elastic IPs  
c. Click "Allocate Elastic IP address"  
d. In the newly allocated elastic ip, click "Associate Elastic IP address" and associate to the lst EC2 Instance created  
e. Copy the Allocation Address and Allocation ID into lab_config.sh

### 6. Deploy the Backend Server Program
```
./deploy.sh
```

### 7. Run the Backend Server
```
./run.sh
```

## Access the Frontend
The frontend webpage is written in NodeJS  
A customed web-app can be created by adding the following:  
- Enter the react app directory
```
cd Cloud-Computing/Final_Project/upload-app
```
- Include a local .env file in the base directory saving private AWS info in the format below  
```
REACT_APP_S3_BUCKET=XXXXXXX
REACT_APP_DIR_NAME=XXXXXXX
REACT_APP_REGION=XXXXXXX
REACT_APP_ACCESS_KEY=XXXXXXX
REACT_APP_SECRET_ACCESS_KEY=XXXXXXX
```
- Create Another S3 Bucket to host the static website and change the bucket name in the following line in packge.json file:
```
"deploy": "aws s3 sync build/ s3://YOURBUCKET--acl public-read",
```

- Build and Deploy 
```
npm run build
npm run deploy
```

