int red = 34;
int blue = 35;
int yellow = 36;

void setup() {
  // put your setup code here, to run once:
  pinMode(red,OUTPUT);
  pinMode(blue,OUTPUT);
  pinMode(yellow,OUTPUT);

}

void loop() {
  // put your main code here, to run repeatedly:
  
  digitalWrite(red,HIGH);
  digitalWrite(blue,HIGH);
  digitalWrite(yellow,HIGH);
  delay(10); 
  digitalWrite(red,LOW);
  digitalWrite(blue,LOW);
  digitalWrite(yellow,LOW);
  delay(10);
   
 
}
