:: Script to start standard subscriber
@echo off

:: broker settings
:: 1883 is standard port
@SET BROKER_IP=localhost
@SET BROKER_PORT=1883

@SET TOPICS="#"

:: client settings
@SET CLIENT_ID="test_subscriber"

@SET MOSQ_BASE_DIR="C:\Program Files (x86)\mosquitto"

@SET CMD=%MOSQ_BASE_DIR%"\mosquitto_sub.exe" -h %BROKER_IP% -p %BROKER_PORT% -i %CLIENT_ID% -t %TOPICS% -v

echo Subscriber started
echo This script shows all messages published to the broker
echo Stop script with ctrl+C ...
echo.

echo %CMD%
%CMD%