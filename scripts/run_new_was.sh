CURRENT_PORT=$(sudo cat /etc/nginx/conf.d/service-url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0
# service_url.inc에서 현재 서비스를 하고 있는 WAS의 포트 번호를 읽어온다
echo "> Current port of running WAS is ${CURRENT_PORT}."

# 현재 포트 번호가 8081이면 새로 WAS를 띄울 타겟 포트는 8082, 혹은 그 반대 상황이라면 8081을 지정한다
if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PORT=8081
else
  echo "> No WAS is connected to nginx"
fi

TARGET_PID=$(sudo lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')
# 만약 타겟포트에도 WAS가 떠 있다면 kill하고
if [ ! -z ${TARGET_PID} ]; then
  echo "> Kill WAS running at ${TARGET_PORT}."
  sudo kill ${TARGET_PID}
fi
# 터미널 엑세스가 끊겨도 실행한 프로세스가 계속 동작하도록 nohup으로 실행시킨다
sudo nohup java -jar -Dserver.port=${TARGET_PORT} -Dspring.config.location=/home/ubuntu/zzz-project/build/libs/application-aws.properties /home/ubuntu/zzz-project/build/libs/RoomEscape-0.0.1-SNAPSHOT.jar > /home/ubuntu/zzz-project/build/libs/nohup$(date +%Y)-$(date +%m)-$(date +%d)-$(date +%H):$(date +%M).out 2>&1 &
# 새로운 WAS를 띄운다
echo "> Now new WAS runs at ${TARGET_PORT}."
exit 0