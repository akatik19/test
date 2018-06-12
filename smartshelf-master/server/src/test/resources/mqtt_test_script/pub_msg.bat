@echo off
:: Test script to publish a message to a defined topic

:: broker settings
:: 1883 is standard port
@SET BROKER_IP=localhost
@SET BROKER_PORT=1883

:: topic to publish to
@SET TOPIC_BLINK="devices/blink/start"
:: message
@SET MESSAGE="{ 'boxid' : 2, 'color' : 1 }"

:: client settings
@SET CLIENT_ID="testclient"
@SET CLIENT_TEST_SUB="testsub"

@SET MOSQ_BASE_DIR="C:\Program Files (x86)\mosquitto"

@SET CMD=%MOSQ_BASE_DIR%"\mosquitto_pub.exe" -h %BROKER_IP% -p %BROKER_PORT% -i %CLIENT_ID% -t %TOPIC_BLINK% -m %MESSAGE%

echo Publish %MESSAGE% to topic %TOPIC%
echo.

::%MOSQ_BASE_DIR%"\mosquitto_sub.exe" -h %BROKER_IP% -p %BROKER_PORT% -i %CLIENT_TEST_SUB% -t %TOPIC%

echo %CMD%
%CMD%

echo.