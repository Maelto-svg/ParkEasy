#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <WiFi.h>
#include <HTTPClient.h>

#define SCREEN_WIDTH 128
#define SCREEN_HEIGHT 32
#define TEMT6000_PIN 34

const char* ssid = "non";
const char* password = "ouioui78";
const char* apiEndpoint = "http://parkeasy.cleverapps.io//api/parking/spots";

Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, -1);
bool isAboveThreshold = false;
bool lastState = false;

void setup() {
 Serial.begin(115200);
 pinMode(TEMT6000_PIN, INPUT);
 analogReadResolution(12);

 if (!display.begin(SSD1306_PAGEADDR, 0x3C)) {
   Serial.println("Failed to initialize OLED display!");
   while (true);
 }
 display.clearDisplay();
 display.display();

 WiFi.begin(ssid, password);
 while (WiFi.status() != WL_CONNECTED) {
   delay(500);
   Serial.print(".");
 }
 Serial.println("\nConnected to WiFi");
}

void loop() {
 int rawValue = analogRead(TEMT6000_PIN);
 float percentage = (rawValue / 4095.0) * 100.0;
 
 isAboveThreshold = (percentage > 10.0);
 if (isAboveThreshold != lastState) {
   sendToAPI(isAboveThreshold);
   lastState = isAboveThreshold;
 }
 
 
 displayData(rawValue, percentage);
 Serial.printf("Raw: %d | %.1f%%\n", rawValue, percentage);
 delay(500);
}

void displayData(int rawValue, float percentage) {
 display.clearDisplay();
 display.setTextSize(1);
 display.setTextColor(SSD1306_WHITE);
 display.setCursor(0, 0);
 display.print("Raw: ");
 display.println(rawValue);
 display.setCursor(0, 10);
 display.print("Light: ");
 display.print(percentage, 1);
 display.println("%");
 display.display();
}

void sendToAPI(bool isAbove) {
  if(WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
   const char* endpoint = isAbove ? "http://parkeasy.cleverapps.io//api/parking/spots/1/free" : "http://parkeasy.cleverapps.io//api/parking/spots/1/occupy";
    http.begin(endpoint);
    http.setAuthorization("user", "password");
    int httpResponseCode = http.POST("");
    
    // Affichage sur OLED
    display.clearDisplay();
    display.setTextSize(1);
    display.setTextColor(SSD1306_WHITE);
    display.setCursor(0, 0);
    display.print("API Call: ");
    display.println(isAbove ? "Above" : "Below");
    display.setCursor(0, 10);
    display.print("Response: ");
    display.println(httpResponseCode);
    display.display();
    
    // Affichage sur moniteur série
    Serial.printf("API Call: %s | Code: %d\n", 
      isAbove ? "Above" : "Below", 
      httpResponseCode);
    
    http.end();
    delay(2000); // Pause pour lire le résultat
  }
}