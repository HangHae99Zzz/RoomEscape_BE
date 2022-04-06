# Crawl current connected port of WAS
CURRENT_PORT=$(cat /etc/nginx/conf.d/service-url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> Nginx currently proxies to ${CURRENT_PORT}."

# Toggle port number
if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PORT=8081
else
  echo "> No WAS is connected to nginx"
  exit 1
fi

# 새로 띄운 WAS의 포트를 nginx가 읽을 수 있도록 service-url.inc에 내용을 덮어쓴다.   tee = 출력 내용을 파일로 만들어주는 커맨드
echo "set \$service_url http://127.0.0.1:${TARGET_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

echo "> Now Nginx proxies to ${TARGET_PORT}."
# 포트 전환하면서, 바꾼거말고 다른거 kill
if [ ${TARGET_PORT} == 8081 ]
then
  KILL_PORT=8082
  TARGET_PORT=$(lsof -ti tcp:${KILL_PORT})
  echo "> ${KILL_PORT} 포트를 종료합니다."
  kill -9 ${TARGET_PORT}
else
  KILL_PORT=8081
  TARGET_PORT=$(lsof -ti tcp:${KILL_PORT})
  echo "> ${KILL_PORT} 포트를 종료합니다."
  kill -9 ${TARGET_PORT}
fi


# Nginx 서버의 재시작 없이 바로 새로운 설정값으로 서비스를 이어나갈 수 있도록 리로드를 통해 서비스 하는 포트를 스위칭하는 스크립트
sudo service nginx reload

echo "> Nginx reloaded."