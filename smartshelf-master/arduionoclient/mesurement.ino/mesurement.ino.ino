#include "HX711.h"

#define calibration_factor 208 //This value is obtained using the SparkFun_HX711_Calibration sketch

#define DOUT_D1  3
#define DOUT_D2  4
#define DOUT_D3  5
#define DOUT_D4  6

#define CLK  2

HX711 scale_d1(DOUT_D1, CLK);
HX711 scale_d2(DOUT_D2, CLK);
HX711 scale_d3(DOUT_D3, CLK);
HX711 scale_d4(DOUT_D4, CLK);

void setup() {
  Serial.begin(9600);

  scale_d1.set_scale(calibration_factor); //This value is obtained by using the SparkFun_HX711_Calibration sketch
  scale_d1.tare(); //Assuming there is no weight on the scale at start up, reset the scale to 0
  scale_d2.set_scale(calibration_factor); //This value is obtained by using the SparkFun_HX711_Calibration sketch
  scale_d2.tare();
  scale_d3.set_scale(calibration_factor); //This value is obtained by using the SparkFun_HX711_Calibration sketch
  scale_d3.tare();
  scale_d4.set_scale(calibration_factor); //This value is obtained by using the SparkFun_HX711_Calibration sketch
  scale_d4.tare();
  Serial.println("Readings:");
}

void loop() {
  Serial.print("Reading: ");
  Serial.print(scale_d1.get_units(), 3);
  Serial.print(scale_d2.get_units(), 3);
  Serial.print(scale_d3.get_units(), 3);
  Serial.print(scale_d4.get_units(), 3); //scale.get_units() returns a float
  Serial.print(" gram"); //You can change this to kg but you'll need to refactor the calibration_factor
  Serial.println();
  delay(1000);
}

