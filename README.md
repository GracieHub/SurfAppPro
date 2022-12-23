# Mobile App Development Assignment 2 2022

Name: Grace Doyle

## Overview

GeosurfPro is an improvement and updated version of Geosurf2022(Assignment1) android app created in Android studio and written in Kotlin.

This app is for assignment 2 for Mobile app dev as part of HDip in Computer Science in SETU.

Geosurf is an app for surfers to be able to add and store surfspots on an app in their account.
They are also able to add Locations, edit locations, add and delete images and store info such as ability level, surfspot rating and date added. They are able to filter by surfapots by County in Ireland. USer can Login and Singup and Logout With Firebase Auth being used here. Realtime Firebase Database is also being used and images are close to being stored on Firebase Cloud Storage but needs a fix due to region/acces denied

Project is built using the The Model-view-presenter (MVP) Deisign pattern.
The App includes the below features:

+ Users can add a title, location, County, image, Ability rating and date to each surfspot
+ Full CRUD functionality (users can create, read, update and delete surfspots in app )
+ Persistent in JSON implemetned for surfspots and Users
+ User can Sign up (register) and Log in
+ Splash screen when the app launches with the ability to LogOut or continue to account
+ View each surfspot Location on the map activity (connected via API)
+ ability to update location of the surfspot by long click and hold and drag
+ Use of UI elements rating bar and date picker

+ Firebase Authentication for Login, SignUo, and Logout.
+ View all surfspots on map activity with zoom to surfspot and info on surfspot displayed when marker clicked
+ Search for surfspot by County in Ireland with a new SearchView
+ Persistent storage of users, surfspots and images with Firebase realtime DB and storage
+ Navigation with bottom navigation and logout button
+ Use of last known location as default when adding new surfspot location on map
+ Swipe to delete surfspot functionality and click to edit geosurf
+ Dev branch (for Firebase auth and realtime database) and Tagged Releases on Github


## To run the app..

+ Clone this repository with the following command: `git clone https://github.com/GracieHub/SurfAppPro`
+ Open project in Android studio and run (note this will require and emulator running API 29)
+ You will need a Google API key key to run this map app feature

## App Design

### Splash Screen

Custom splash screen containing the app logo runs for few seconds when the appl is launched.

>Splash Screen
![Splash Screen](/app/src/images/splashscreen.PNG)



![Login](/app/src/images/login.PNG)

![Register](/app/src/images/register.PNG)


### List surfspots

All surfpsots are displayed back to the user in a recycler view on the app home page. Each surfspot is clickable so the user can read and edit its details
Surfspots can deleted by swiping left

>List all surfspots

![List Geosurfs](/app/src/images/surfspot_list2.png)

### Add or edit surfspots

Users can add a new surfspot by clicking the + icon on the bottom navigation. Users can add surfspot location, title, description, rating, location, image and County in Ireland.

>Add new surfspot
![Add Geosurf](/app/src/images/surfspots_add.png)

Users may edit existing surfspots by clicking on them.

>Edit surfspot
![Edit Geosurf](/app/src/images/surfspot_edit.PNG)

### Search and filter surfspots

Users can search surfspots by County in Ireland on the main page.

>Search surfspot by County
![Filter Surspots by County](/app/src/images/surfspot_search.PNG)

### View on map

Users can view all surfspots on a map. Individual surfspots can be selected by clicking on the marker and name and description is displayed.

>View all surfspots on map
![Surfspots Map](/app/src/images/surfspots_map.png)

## Persistence/Storage

Firebase is used for authentication and storage of users, surfspots (and images was attempted but denied access and unable to figure out issue, possibly becasue I selected EU time-zone).

>Firebase authentication
![Firebase authentication](/app/src/images/firebase_auth.PNG)

>Firebase realtime database
![Firebase database](/app/src/images/firebase_db.PNG)

## References

Lecture materials and the below resources were consulted.

https://www.youtube.com/watch?v=Q0gRqbtFLcw&ab_channel=Stevdza-San
https://www.geeksforgeeks.org/how-to-change-font-of-toolbar-title-in-an-android-app/
https://www.geeksforgeeks.org/togglebutton-in-kotlin/#:~:text=In%20Android%2C%20ToggleButton%20is%20just,is%20just%20like%20a%20button.
https://www.geeksforgeeks.org/datepicker-in-kotlin/#:~:text=Android%20DatePicker%20is%20a%20user,will%20select%20a%20valid%20date.
https://stackoverflow.com/questions/39192945/serialize-java-8-localdate-as-yyyy-mm-dd-with-gson
https://www.tutorialkart.com/kotlin-android/login-form-example-in-kotlin-android/
