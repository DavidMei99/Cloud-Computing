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
a. Login to AWS Console with an IAM role of Administrator access.
b. Go to EC2 > Network & Security > Elastic IPs

