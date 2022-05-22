web: java $JAVA_OPTS -Dspring.profiles.active="postgres,heroku" -DTOPJAVA_ROOT="." -jar target/dependency/webapp-runner.jar -Dserver.port=5000 --session-store memcache target/*.war
