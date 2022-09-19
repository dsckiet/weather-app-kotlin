<img src = "https://play-lh.googleusercontent.com/tDdNo1FaPq6frl7xVwWbr_F3-E9c1trMskb6JMxMGBEhctTUSOCrF5xst3MJBulOFA=w240-h480-rw" width = "100" height = "100" align = "right"> 
<p align="left">
	<h1 align="left"> Weather 🌤 App </h2>
	<h3 align="left"> Get instant weather updates using the Weather App. <h/3>
	<h4 align="left"> Free open source weather app which shows you the current weather details and forecast for up to seven days. </h4>
</p>
	
---
	
[![PLAYSTORE](https://img.shields.io/badge/Playstore-Download-blue?style=for-the-badge&logo=appveyor)](https://play.google.com/store/apps/details?id=com.dsckiet.weatherapp)     ![forks](https://img.shields.io/github/forks/dsckiet/weather-app-kotlin)     ![license](https://img.shields.io/github/license/dsckiet/weather-app-kotlin)


## Functionalities
- [ ]  Hourly Weather Forecast
- [ ]  7 days Weather Forecast
- [ ]  Time of Sunrise and Sunset
- [ ]  Humidity and Wind Predictions
- [ ]  Celsius to Farenheit conversion
- [ ]  Access user location or provide custom location


## Usage and screenshots

#### View temps in both Fahrehheit and Celsius:
<img src = "/images/weatherapptemps.png" width = "500">
	
	
#### Weather forecast for the next seven days:
<img src = "/images/weatherapp2.png" width = "210">
	
	
#### Specify custom location or access current location:
<img src = "/images/weatherapplocation.png" width = "210">




## Instructions to run

* __Pre-requisites:__
	-  Android Studio v4.0
	-  A working Android physical device or emulator with USB debugging enabled

* __Directions to setup/install__
	- Clone this repository to your local storage using Git bash:
	```bash
	https://github.com/dsckiet/weather-app-kotlin
	```
	- Open this project from Android Studio
	- Connect to an Android physical device or emulator
	- To install the app into your device, run the following using command line tools
	```bash
	gradlew installDebug
	```

* __Directions to execute__
	-  To launch hands free, run the following using command line tools
	```bash
	adb shell monkey -p com.example.weatherapp -c android.intent.category.LAUNCHER 1
	```

---

## Contributors
* [Ananya Punia](https://github.com/ananyapunia28)
* [Rishabh Jain](https://github.com/jainrishabh29)
	
:star: To contribute, please go through our [contributing guide](https://github.com/manaswii/weather-app-kotlin/blob/main/Contributing.md).

