#!/usr/bin/env bash

# fetch config from AWS for currently running infrastructure
source ./load_lab_config.sh

NOW=$(date '+%Y%m%d%H%M%S')
LOGFILE="./run-${NOW}.log"


echo "Running Full AWS infrastructure for ${APP_TAG_NAME}:${APP_TAG_VALUE}" | tee ${LOGFILE}
echo "Running run.sh at ${NOW}" | tee -a ${LOGFILE}

PROG="multi-node-threaded-2.0-jar-with-dependencies.jar"

# get public IP addresses of the instances (in the public subnet)
INSTANCES_IPS=$(aws ec2 describe-instances ${PREAMBLE} --filters Name=instance-state-name,Values=running Name=tag:${APP_TAG_NAME},Values=${APP_TAG_VALUE} --query 'Reservations[*].Instances[*].[PublicIpAddress]' --output text | tr '\n' ' ')
PRIVATE_IPS=$(aws ec2 describe-instances ${PREAMBLE} --filters Name=instance-state-name,Values=running Name=tag:${APP_TAG_NAME},Values=${APP_TAG_VALUE} --query 'Reservations[*].Instances[*].[PrivateIpAddress]' --output text | tr '\n' ' ')
echo "Public IP addresses: ${INSTANCES_IPS}" | tee -a ${LOGFILE}
echo "Private IP addresses: ${PRIVATE_IPS}" | tee -a ${LOGFILE}

IFS=' ' read -r -a INSTANCES_IPS_ARRAY <<< "$INSTANCES_IPS"
IFS=' ' read -r -a PRIVATE_IPS_ARRAY <<< "$PRIVATE_IPS"

NUM_IPS=0
for host in ${INSTANCES_IPS}
do
  NUM_IPS=$((NUM_IPS+1))
done
echo "${NUM_IPS}"

echo "${PRIVATE_IPS_ARRAY[0]}"



#empty uploadfiles directory
ssh -i ${KEY_FILE} ${USER}@${ELASTIC_IP} "aws s3 rm s3://${BUCKET_NAME}/${DIR_NAME}/ --recursive --exclude ''"

#empty downloadfiles directory
ssh -i ${KEY_FILE} ${USER}@${ELASTIC_IP} "aws s3 rm s3://${BUCKET_NAME}/downloadfiles/ --recursive --exclude ''"


while true
do
  FILE_UPLOAD=$(ssh -i ${KEY_FILE} ${USER}@${ELASTIC_IP} "aws s3 ls s3://${BUCKET_NAME}/${DIR_NAME}/ | wc -l")
  echo "${FILE_UPLOAD}"
  if [ "${FILE_UPLOAD}" -gt 1 ]
  then 
    break
  else
    continue
  fi
done

SECONDS=0
for ((i = 0 ; i < NUM_IPS-1 ; i++)); do
  #copy files 
  ssh -i ${KEY_FILE} ${USER}@${INSTANCES_IPS_ARRAY[${i}]} "aws s3 cp s3://${BUCKET_NAME}/dataset/ ./multi-node-threaded/digitsDataset" --recursive
  ssh -i ${KEY_FILE} ${USER}@${INSTANCES_IPS_ARRAY[${i}]} "aws s3 cp s3://${BUCKET_NAME}/uploadfiles/ ./multi-node-threaded/digitsDataset" --recursive
  echo "Running ${PROG} at ${USER}@${INSTANCES_IPS_ARRAY[${i}]}:~/ ..." | tee -a ${LOGFILE}
  ssh -i ${KEY_FILE} ${USER}@${INSTANCES_IPS_ARRAY[${i}]} "killall -9 java"
	(ssh -i ${KEY_FILE} ${USER}@${INSTANCES_IPS_ARRAY[${i}]} "java -cp ${PROG} edu.cooper.ece465.ClientMain 8 multi-node-threaded/digitsDataset/valFeatures.csv 6666" | tee -a ${LOGFILE}) & disown %1
done
sleep 1
ssh -i ${KEY_FILE} ${USER}@${ELASTIC_IP} "killall -9 java"
ssh -i ${KEY_FILE} ${USER}@${ELASTIC_IP} "aws s3 cp s3://${BUCKET_NAME}/dataset/ ./multi-node-threaded/digitsDataset" --recursive
ssh -i ${KEY_FILE} ${USER}@${INSTANCES_IPS_ARRAY[${i}]} "aws s3 cp s3://${BUCKET_NAME}/uploadfiles/ ./multi-node-threaded/digitsDataset" --recursive
ssh -i ${KEY_FILE} ${USER}@${ELASTIC_IP} "java -cp ${PROG} edu.cooper.ece465.KNNmain 8 multi-node-threaded/digitsDataset/valFeatures.csv ${PRIVATE_IPS_ARRAY[0]} ${PRIVATE_IPS_ARRAY[1]} ${PRIVATE_IPS_ARRAY[2]} ${PRIVATE_IPS_ARRAY[3]}" | tee -a ${LOGFILE}
ssh -i ${KEY_FILE} ${USER}@${ELASTIC_IP} "aws s3 cp KNeighborsOut.csv s3://${BUCKET_NAME}/downloadfiles/ "
echo "time is"
echo $SECONDS


#clean datasets
ssh -i ${KEY_FILE} ${USER}@${ELASTIC_IP} "rm multi-node-threaded/digitsDataset/*"
for ((i = 0 ; i < NUM_IPS-1 ; i++)); do
  ssh -i ${KEY_FILE} ${USER}@${INSTANCES_IPS_ARRAY[${i}]} "rm multi-node-threaded/digitsDataset/*"
done

echo "Done." | tee -a ${LOGFILE}

exit 0