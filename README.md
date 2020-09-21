# reddittopsreader

## Overview
A simple Reddit Client that shows the top 50 entries on Reddit with the following features:
```
- Pull to Refresh
- Saving pictures in the picture gallery
- App state-preservation/restoration
- Indicator of unread/read post (updated status, after post it’s selected)
- Dismiss Post Button (remove the cell from list. Animations required)
- Dismiss All Button (remove all posts. Animations required)
- Support split layout (left side: all posts / right side: detail post)
```
## About Pagination Support:
Paginating the data is somehow incompatible with some of the features listed above. When data is paginated, the application is aware of only a small dataset within the universe of elements stored in the remote web service, due to this:
* The "Dismiss All" feature is incompatible. What is "all" in the context of paged data? The elements we hold on memory? There could be more on top of it on the list, or above it, but we don't know them. We didn't fetch those items yet.
* The "Swipe to Refresh" feature. Again, what should we refresh? What we hold in memory? There could be more on top of it, or above it, but we don't know it! We just know the pages we hold in memory. We didn't fetch what's below it, and we discarded what's above. Besides, when paginating data your dataset is always up to date. As you scroll, new data comes from the server, and the list is always updated.
* Showing only the "Top 50" posts looses purpose. We can fetch data indefinetly in small chunks. This allows infinite scrolling. Limiting the amount of items to 50 not only kills the benefit of paginating the data, it makes the implementation a lot more complex and less maintainable.

Due to all this, Pagination Support was not included in the original implementation found on the master branch.
For the purpose of demonstrating how a paginated implementation would be, the feature was included in the paging_support side branch. This branch includes:
* Pagination Support
* No "Swipe to Refresh" feature
* No "Dismiss All" feature
* No "50 posts" limit
**Note: Doing an update installation from master to paging_support and vice-versa is not supported. The Room database entities are different.**

## Architecture
This application implements a Repository patter:
![Pattern](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)
Notice that each component depends only on the component one level below it. For example, activities and fragments depend only on a view model. The repository is the only class that depends on multiple other classes; in this example, the repository depends on a persistent data model and a remote backend data source.

## Technologies Used:
* **androidx** components: Activity, Fragment, ViewModel, RecyclerView ...
* **Dagger and Hilt** for dependencies injection
* **Retrofit** to fetch the Reddit API
* **Room** to achieve data persistance
* **Glide** to load images from URLs into the UI

## About Unit Testing
Unit Testing on this project is being perfomed using **Robolectric** and **Mockito**.
**All the data providing classes are being covered** by Unit Tests. **That is, the ViewModel layer and below on the Architecture diagram**.
Hilt Injection is not being used in Unit Tests because **the SOLID Inversion Principle is being achieved by moving the dependencies of a class to the constructor whenever it is possible**. Every tested class exhibits this pattern. Due to this, **Subjects Under Test can be constructed and provided with mocks without the need of using the Injection Framework**.
Regarding UI tests, it could be tested with Unit Tests, but is is more convenient to test the UI using espresso. But this technology is not included in this project.
