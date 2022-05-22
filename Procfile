web: java -Dserver.port=$PORT $JAVA_OPTS -Dspring.profiles.active="postgres,heroku" -DTOPJAVA_ROOT="." -jar target/dependency/webapp-runner.jar --session-store memcache target/*.war
