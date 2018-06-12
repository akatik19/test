#include <SPI.h>
#include <Ethernet.h>

#include "ArduinoJson.h"
#include "PubSubClient.h"
#include "HX711.h"

//reding sensors:initializing pins
#define calibration_factor_s1 180//This value is obtained using the SparkFun_HX711_Calibration sketch
#define calibration_factor_s2 -180 //
#define DOUT_D1  3
#define DOUT_D2  4
#define CLK  2

HX711 scale_d1(DOUT_D1, CLK);
HX711 scale_d2(DOUT_D2, CLK);


// Update these with values suitable for your network.
byte mac[]    = {  0xDE, 0xED, 0xBA, 0xFE, 0xFE, 0xED };
IPAddress ip(172, 16, 0, 100);
IPAddress server(172, 16, 0, 101);
char rootTopic[] = "devices/weight/%d";
char outTopic[20];
float sensorData[] = {0.0,0.0,0.0,0.0};

EthernetClient ethClient;
PubSubClient client(ethClient);

const unsigned long SEND_WEIGHT_DELAY = 5000;
unsigned long onTime = 0; 


void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  
  if( strcmp(topic,"devices/blink/start") == 0) {
      Serial.println("devices/blink/start");
      int *ledVal;
      ledVal =  readJeson(payload);
      drawerOn(ledVal[0],ledVal[1]);
  }
  
  if( strcmp(topic,"devices/blink/stop") == 0) {
      Serial.println("devices/blink/stop");
      int *ledVal;
      ledVal =  readJeson(payload);
      //Call drawer method to switch off the light 
      Serial.println(ledVal[0]);
      Serial.println(ledVal[1]);
      drawerOff(ledVal[0],ledVal[1]);
   }
}


void ledSetUp(){
  for(int i = 22; i <= 39; i++){
    pinMode(i, OUTPUT);
  }
}


void ledOn(int color, int pin){
   switch(color){
        case 0 :
          Serial.println("red");
          Serial.println(pin);
          digitalWrite(pin, HIGH);
          break;
          
        case 1:
          Serial.print("blue");
          Serial.println(pin + 1);
          digitalWrite(pin + 1, HIGH);
          break;
          
        case 2:
          Serial.print("yellow");
          Serial.println(pin + 2);
          digitalWrite(pin + 2, HIGH);
          break;
          
        default:
          Serial.print("Led Number is out of bound") ;  
      }
}


void ledOff(int color, int pin){
   switch(color){
        case 0 :
          Serial.println("red");
          Serial.println(pin);
          digitalWrite(pin, LOW);
          break;
          
        case 1:
          Serial.print("blue");
          Serial.println(pin + 1);
           digitalWrite(pin + 1, LOW);
          break;
        case 2:
          Serial.print("yellow");
          Serial.println(pin + 2);
          digitalWrite(pin + 2, LOW);
          break;
        default:
          Serial.print("Led Number is out of bound") ;  
      }
}


void drawerOff(int boxNumber, int color){
  switch(boxNumber){
    case 0:
      Serial.println("boxOne Glowerd");
      ledOff(color, 22);// 0th box start at pin 22
      break;
    case 1 :
      Serial.println("boxTwo Glowerd");
      ledOff(color, 25);// 1th box start at 25;
      break;
    case 2:
      Serial.println("boxThree Glowerd");
      ledOff(color, 28);// 2nd box start at 28
      break;
    case 3:
      Serial.println("boxFour Glowerd");
      ledOff(color, 31);//3rd box start at 31
      break;
    case 4:
      Serial.println("boxFive Glowerd");
      ledOff(color, 34);//3rd box start at 31
      break;
    case 5:
      Serial.println("boxSix Glowerd");
      ledOff(color, 37);//3rd box start at 31
      break;
    default:
     Serial.print("No box found");
  }
}


void drawerOn(int boxNumber, int color){
  switch(boxNumber){
    case 0:
      Serial.println("box 0 Glowerd");
      ledOn(color, 22);// 0th box start at pin 7
      break;
    case 1 :
      Serial.println("box 1 Glowerd");
      ledOn(color, 25);// 1th box start at 10;
      break;
    case 2:
      Serial.println("box 2 Glowerd");
      ledOn(color, 28);// 2nd box start at 13
      break;
    case 3:
      Serial.println("box 3 Glowerd");
      ledOn(color, 31);//3rd box start at 16
      break;
    case 4:
      Serial.println("boxFive Glowerd");
      ledOn(color, 34);//3rd box start at 31
      break;
    case 5:
      Serial.println("boxSix Glowerd");
      ledOn(color, 37);//3rd box start at 31
      break;
    default:
     Serial.print("No box found");
  }
}


int* readJeson(const char json[]){
   //char json[] = ;
   StaticJsonBuffer<200> jsonBuffer;
   JsonObject& root = jsonBuffer.parseObject(json); 
   if (!root.success()) {
    Serial.println("parseObject() failed");
    return;
  }
  static int r [] = {0,0};
  r[0] = root["boxid"];
  r[1] = root["color"];
  Serial.print("boxid : ");
  Serial.println(r[0]);
  Serial.print("color : ");
  Serial.println(r[1]);
  return r;
}


void subscribe() {
   client.subscribe("inTopic");
   client.subscribe("devices/blink/start");
   client.subscribe("devices/blink/stop");
}


void readSensors(){
  sensorData[0] = scale_d1.get_units();
  sensorData[1] = scale_d2.get_units();
}

   
void sendWeights() {
  readSensors();
      // Once connected, publish an announcement...
      for (int i = 0;i<=1;i++){
        sprintf(outTopic,rootTopic,i+4);//Sterting weight drawer from 4
        float tempval = sensorData[i] ;
        String sf (tempval, 2);
        const char* sensorVal;
        sensorVal = sf.c_str();
        client.publish(outTopic, sensorVal);
        Serial.println(outTopic);
        Serial.println(sensorVal);
      }
}


void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    
    if (client.connect("arduinoClient")) {
      Serial.println("connected");

      subscribe();
      
    } else {
      
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
          
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
  
}

void setup()
{
  scale_d1.set_scale(calibration_factor_s1); //This value is obtained by using the SparkFun_HX711_Calibration sketch
  scale_d1.tare(); //Assuming there is no weight on the scale at start up, reset the scale to 0
  scale_d2.set_scale(calibration_factor_s2); //This value is obtained by using the SparkFun_HX711_Calibration sketch
  scale_d2.tare();
 
  ledSetUp();
  Serial.begin(57600);
  client.setServer(server, 1883);
  client.setCallback(callback);

  Ethernet.begin(mac, ip);
  // Allow the hardware to sort itself out
  delay(1500);
}

void loop()
{
  // connect to mqtt broker
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
  
  unsigned long currentOnTime = millis();
  if( (currentOnTime - onTime >= SEND_WEIGHT_DELAY) && client.connected() ) {
    onTime = currentOnTime;
    sendWeights();
  }

}
