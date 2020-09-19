# reddittopsreader

## Overview
A simple Reddit Client that shows the top 50 entries on Reddit.

## Architecture
This application was done using a Repository patter:
![Pattern](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)
Notice that each component depends only on the component one level below it. For example, activities and fragments depend only on a view model. The repository is the only class that depends on multiple other classes; in this example, the repository depends on a persistent data model and a remote backend data source.

## Technologies Used:
* **androidx** components: Activity, Fragment, ViewModel, RecyclerView ...
* **Dagger and Hilt** for dependencies injection
* **Retrofit** to fetch the Reddit API
* **Room** to achieve data persistance
* **Glide** to load images from URLs into the UI
