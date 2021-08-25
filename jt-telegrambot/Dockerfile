FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=target/*.jar
ENV BOT_NAME=test_jr2_0_community_bot
ENV BOT_TOKEN=1837768138:AAFGZAFPVaDqAAM-v_D2Fzpgkr7mzZ2MIeg
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dbot.username=${BOT_NAME}", "-Dbot.token=${BOT_TOKEN}", "-jar", "/app.jar"]