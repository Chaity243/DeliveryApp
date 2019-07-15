# Delivery Mobile APP

### Business requirement
An user should be able to see the list of  delivery items where each item has brief information including photo and description. On click of particular item user must be able to locate the item on map with the description of item mentioned.

### Libraries Used

  * Retrofit
  * Dagger2
  * Picasso
  * Room
  * Paging
  * RxJava

### User Requirements
- Retrieve list of deliveries from the API
- Display list of deliveries.
- Show details when user select an item in the list.
- Add marker on the map based on the provided lat/lng.

### Things done in the project
Project is developed using MVVM Clean architecture.
App is fetching the list of deliveries from the Network and saving them into the DB for local caching. Then the app displays the list from cache and request from server if needed. The list use a page size of 20 to fetch the list from server.

## How to compile
- Android Studio IDE (3.4.1)
- Android SDK (28)
- Change the "GOOGLE_MAPS_API_KEY" in the gradle.properties file to make the google map work. To generate an Android Key on Google Developer console with the package com.chaitanya.android.delivery and your keystore's SHA1
