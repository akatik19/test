@echo off
setlocal EnableDelayedExpansion
:: Script for drawer led testing
:: Test will not terminate

:: broker settings
:: 1883 is standard port
@SET BROKER_IP=localhost
@SET BROKER_PORT=1883

:: topic to publish to
@SET ON_TOPIC="devices/blink/start"
@SET OFF_TOPIC="devices/blink/stop"
:: message
@SET MESSAGE=

:: client settings
@SET CLIENT_ID="downshutter"
@SET MOSQ_BASE_DIR="C:\Program Files (x86)\mosquitto"

@SET DRAWER_COUNT=5
@SET LED_COUNT=2

@SET SLEEP_TIME=2000

:: This test will run forever
@SET /a lastbox=-1
:loop
for /l %%x in (0, 1, %DRAWER_COUNT%) do (
   
   @echo Testing drawer %%x
   for /l %%c in (0, 1, %LED_COUNT%) do (
		if %%x NEQ %lastbox% (
			echo Turnoff !lastbox!
			%MOSQ_BASE_DIR%"\mosquitto_pub.exe" -h %BROKER_IP% -p %BROKER_PORT% -i %CLIENT_ID% -t %OFF_TOPIC% -m "{ 'boxid' : !lastbox!, 'color' : %%c }"
		)
		%MOSQ_BASE_DIR%"\mosquitto_pub.exe" -h %BROKER_IP% -p %BROKER_PORT% -i %CLIENT_ID% -t %ON_TOPIC% -m "{ 'boxid' : %%x, 'color' : %%c }"
	)
	
	@SET /a lastbox=%%x
	:: sleep
	ping -n 2 -w %SLEEP_TIME% 127.0.0.1 > nul
)

for /l %%x in (0, 1, %DRAWER_COUNT%) do (
   
   for /l %%c in (0, 1, %LED_COUNT%) do (
		%MOSQ_BASE_DIR%"\mosquitto_pub.exe" -h %BROKER_IP% -p %BROKER_PORT% -i %CLIENT_ID% -t %OFF_TOPIC% -m "{ 'boxid' : %%x, 'color' : %%c }"
	)
)

goto loop

echo.