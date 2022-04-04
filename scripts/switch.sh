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

# Change proxying port into target port
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


# Reload nginx
sudo service nginx reload

echo "> Nginx reloaded."