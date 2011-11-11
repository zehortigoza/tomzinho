#include <Servo.h>

char incomingChar;
char side;
int servoNumber = 0;
char posText[3];
int iPosText = 0;
int pos = 0;

Servo R1;
Servo R2;
Servo R3;
Servo R4;
Servo L1;
Servo L2;
Servo L3;
Servo L4;

void setup() {
  Serial.begin(9600);
  R1.attach(6);
  R2.attach(7);
  R3.attach(8);
  R4.attach(9);
  L1.attach(10);
  L2.attach(11);
  L3.attach(12);
  L4.attach(13);
  
  R1.write(110);
  L1.write(100);
  
  R2.write(90);
  R3.write(90);
  R4.write(90);
  
  L2.write(90);
  L3.write(90);
  L4.write(90);
}

void loop() {  
  if (Serial.available() > 0) {
    incomingChar = Serial.read();    
    if(incomingChar >= '0' && incomingChar <= '9') {//is a number
      if(servoNumber > 0) {
        if(iPosText < 3) {
          posText[iPosText] = incomingChar;
          iPosText++;
        } else {
          pos = atoi(posText);
          checkAndExecute();
          emptyVars();
        }        
      } else {
        servoNumber = incomingChar - '0';//convert char to int
      }
    } else {
      if(incomingChar == ' ' || incomingChar == '.') {
        pos = atoi(posText);
        checkAndExecute();
        emptyVars();
      } else {
        emptyVars();
        side = toupper(incomingChar);
      }
    }
  }
}

void process() {
  if(side == 'R'){
    switch(servoNumber){
      case 1:
        R1.write(pos);
        Serial.print("R1 pos=");
        Serial.println(pos);
        break;
      case 2:
        R2.write(pos);
        Serial.print("R2 pos=");
        Serial.println(pos);
        break;
      case 3:
        R3.write(pos);
        Serial.print("R3 pos=");
        Serial.println(pos);
        break;
      case 4:
        R4.write(pos);
        Serial.print("R4 pos=");
        Serial.println(pos);
        break;
      default:
        Serial.println("Unknown servo");
    }
  } else {
    if(side == 'L') {
      switch(servoNumber){
        case 1:
          L1.write(pos);
          Serial.print("L1 pos=");
          Serial.println(pos);
          break;
        case 2:
          L2.write(pos);
          Serial.print("L2 pos=");
          Serial.println(pos);
          break;
        case 3:
          L3.write(pos);
          Serial.print("L3 pos=");
          Serial.println(pos);
          break;
        case 4:
          L4.write(pos);
          Serial.print("L4 pos=");
          Serial.println(pos);
          break;
        default:
          Serial.println("Unknown servo");
      }
    } else {
      Serial.println("Unknown side");
    }
  }
}

void emptyVars() {
  side = ' ';
  servoNumber = 0;
  iPosText = 0;
  posText[0] = 0;
  posText[1] = 0;
  posText[2] = 0;
  pos = 0;
}

void checkAndExecute() {
  if(servoNumber >= 1 && servoNumber <= 4 && pos >= 0 && pos <= 180 && (side == 'R' || side == 'L')) {
    process();
  } else {
    Serial.print("Invalid arguments: ");
    Serial.print(side);
    Serial.print(servoNumber);
    Serial.println(pos);
  }
}
