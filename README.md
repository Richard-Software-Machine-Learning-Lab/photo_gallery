# Android Photo Gallery App

## Overview
This is an Android photo gallery application that integrates with RESTful APIs, including Flickr for image retrieval and OSMDroid for map-based geolocation features. The app allows users to browse, search, and save images while displaying geotagged photos on an interactive map.

## Tech Stack
### **Frontend (Android App)**
- **Language:** Java/Kotlin
- **Framework:** Android SDK
- **UI Components:** Jetpack Compose / XML Layouts
- **Map Integration:** OSMDroid

### **Backend (API Integration & Data Retrieval)**
- **RESTful API Consumption:** Retrofit / OkHttp
- **External APIs:**
    - **Flickr API** (for retrieving images based on user search or geolocation)
    - **OSMDroid** (for displaying maps and geolocation features)

### **Storage & Data Management**
- **Local Database:** Room Database / SQLite

## Features
### **Core Features**
✅ **Photo Gallery Display** – Fetch and display images from Flickr based on user queries.  
✅ **Map Integration** – Use OSMDroid to display geotagged images on an interactive map.  
✅ **Search Functionality** – Search images by keywords, tags, or location.  
✅ **Offline Mode** – Store and view favorited images without an internet connection.

### **User Experience Features**
✅ **Image Zoom & Fullscreen View** – Allow users to zoom into images.  

### **Performance & Security**
✅ **Efficient Caching** – Reduce API calls using local storage and caching.  
✅ **Rate Limiting Handling** – Manage API request limits for Flickr. 