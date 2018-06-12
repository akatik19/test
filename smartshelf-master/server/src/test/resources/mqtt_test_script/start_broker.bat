:: Starts the MQTT Broker
@echo off

:: broker settings
:: 1883 is standard port
@SET BROKER_IP=localhost
@SET BROKER_PORT=1883

@SET MOSQ_BASE_DIR="C:\Program Files (x86)\mosquitto"

@SET CMD=%MOSQ_BASE_DIR%"\mosquitto.exe" -v

echo Starting MQTT Broker...

echo %CMD%
%CMD%