CURRENT_PORT=$(sudo cat /etc/nginx/conf.d/service-url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> Current port of running WAS is ${CURRENT_PORT}."

if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PORT=8081
else
  echo "> No WAS is connected to nginx"
fi

TARGET_PID=$(sudo lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

if [ ! -z ${TARGET_PID} ]; then
  echo "> Kill WAS running at ${TARGET_PORT}."
  sudo kill ${TARGET_PID}
fi

sudo nohup java -jar -Dserver.port=${TARGET_PORT} -Dspring.config.location=/home/ubuntu/zzz-project/build/libs/application-aws.properties /home/ubuntu/zzz-project/build/libs/RoomEscape-0.0.1-SNAPSHOT.jar > /home/ubuntu/zzz-project/build/libs/nohup$(date +%Y)-$(date +%m)-$(date +%d)-$(date +%H):$(date +%M).out 2>&1 &
echo "> Now new WAS runs at ${TARGET_PORT}."
exit 0