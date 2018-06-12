@echo off
:: Script to switch on all LEDs

:: broker settings
:: 1883 is standard port
@SET BROKER_IP=localhost
@SET BROKER_PORT=1883

:: topic to publish to
@SET TOPIC_BLINK="devices/blink/start"
:: message
@SET MESSAGE=

:: client settings
@SET CLIENT_ID="downshutter"
@SET MOSQ_BASE_DIR="C:\Program Files (x86)\mosquitto"

:: For 6 drawers type 5
@SET DRAWER_COUNT=5
:: For 3 LEDs type 2
@SET LED_COUNT=2

for /l %%x in (0, 1, %DRAWER_COUNT%) do (
   
   for /l %%c in (0, 1, %LED_COUNT%) do (
		@echo Switch off Drawer %%x LED %%c
		%MOSQ_BASE_DIR%"\mosquitto_pub.exe" -h %BROKER_IP% -p %BROKER_PORT% -i %CLIENT_ID% -t %TOPIC_BLINK% -m "{ 'boxid' : %%x, 'color' : %%c }"
	)
)

echo.