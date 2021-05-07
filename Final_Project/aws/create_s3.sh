#!/usr/bin/env bash

source ./lab_config.sh

aws s3 mb s3://${BUCKET_NAME}
aws s3 cp ../multi-node-threaded/digitsDataset s3://${BUCKET_NAME} --recursive
aws s3 ls s3://${BUCKET_NAME} --recursive