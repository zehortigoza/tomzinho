#include <Servo.h>

#define MAX 10

char incomingChar;
char command[MAX];
int i = 0;

Servo R1;
Servo R2;
Servo R3;
Servo R4;

Servo L1;
Servo L2;
Servo L3;
Servo L4;

int pos = 0;

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

void loop(){
  if (Serial.available() > 0) {
    incomingChar = Serial.read();
    if(incomingChar == ' ' || incomingChar == '.') {
      process(command,i);
      for(int z = 0; i < z; z++) {
        command[z] = 0;
      }
      i = 0;
    } else {
      command[i] = incomingChar;
      i++;
    }
  }
}

void process(char line[],int t) {  
  char charpos[MAX-2];
  for(int z = 2 ; z < t; z++){
    charpos[z-2] = line[z];
  }  
  int pos = atoi(charpos);
  
  int servonum = 0;
  switch(line[1]){
    case '1':
      servonum = 1;
      break;
    case '2':
      servonum = 2;
      break;
    case '3':
      servonum = 3;
      break;
    case '4':
      servonum = 4;
      break;
  }
  
  if(! (servonum > 0 && servonum < 5)) {
    return;
  } else {
    Serial.print(line[0]);
    Serial.print(servonum);
    Serial.print("=");
    Serial.println(pos);
  }
  
  if(line[0] == 'R' || line[0] == 'r') {
    switch(servonum) {
      case 1:
        R1.write(pos);
        break;
      case 2:
        R2.write(pos);
        break;
      case 3:
        R3.write(pos);
        break;
      case 4:
        R4.write(pos);
        break;
    }
  } else {
    if(line[0] == 'L' || line[0] == 'l') {
      switch(servonum) {
        case 1:
          L1.write(pos);
          break;
        case 2:
          L2.write(pos);
          break;
        case 3:
          L3.write(pos);
          break;
        case 4:
          L4.write(pos);
          break;
      }
    }
  }
}
