<div align="center">
<img src="https://github.com/AriZume/TechLog23-24/blob/master/project/app/src/main/ic_new-playstore.png"/>
<h3 align="center">GeoSnap</h3>
<p align="center">
Geotagged Photo Sharing
<br/>
<br/>
<a href="https://github.com/AriZume/TechLog23-24"><strong>Explore the docs</strong></a>
</p>
</div>

 ### Built With

This project was built with the following:
- <a href="https://www.java.com/en/">Java</a>.
- <a href="https://firebase.google.com/docs/reference">Firebase API</a> for the database management.
- <a href="https://developers.google.com/maps">Google Maps API</a> for the map integration.
- <a href="https://www.geeksforgeeks.org/image-loading-caching-library-android-set-2/">Glide</a> for the image loader.
- <a href="https://developer.android.com/studio">Android Studio</a> for the IDE.


 ## About The Project
 
<p align="center">
<img src="https://github.com/AriZume/TechLog23-24/tree/master/sources/run.gif"  title="run"/>
</p>

GeoSnap is a dynamic and engaging platform designed to bring the world closer through the power of location-based photo sharing. GeoSnap offers a unique way to capture, share, and discover moments from around the globe.

## Getting Started
 
 ### Installation
 
<p>Please follow the following steps for successful installation:</p>

1. Install <a href="https://developer.android.com/studio">Android Studio</a>.
   
2. Clone the repo
   ```sh
   gh repo clone AriZume/TechLog23-24
   ```


## How To Run

To run GeoSnap, follow these steps:

1. **Open the folder of your local repository in Android Studio and select a phone emulator to run it**.

2. **Once you open the emulator, capture some pictures by using its camera and launch the app. The app should be already installed in the emulator**.


There's an example of how it should look like:
<br>
<img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/android.png"  title="android"/>


 ## Features

The application has four main use cases, namely posting, searching, editing photo data and viewing posts stored in a database.

### Users can

- Upload one or multiple photos to the same post with a single description and location. Each post can be categorized using a tag to facilitate its search.
- Edit the photos of their post before posting it with their own data, such as changing the location and date if they do not exist.
- View all posts in the database, which are placed on the homepage map using pins. 
- Search for posts according to the tags of each post.

## Project Plan
A project plan defines the strategy and approach for a project. It outlines the project's objectives, scope, timeline, resources and tasks. The project plan serves as a roadmap to guide the project team through the execution and completion of the project.
<img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/project_plan.png" />

For more detais, check out this <a href="https://github.com/users/AriZume/projects/1/views/3">link</a> or our report (**report en.pdf**) in the <a href="https://github.com/AriZume/TechLog23-24/tree/master/sources/">sources</a> folder..

 ## Usage

### Home button
The Home button provides access to several key features of the app:

- **Location Permission**: When first accessed, the app requests permission to access your location to enable geotagging of photos. This permission is essential for tagging photos with their specific locations.
- **Home**: This is the main feed where you can view posts from other users, including photos, descriptions, tags, and locations. It's the central hub for discovering new content.
- **Pins**: Tapping this shows an interactive map with pins indicating the locations of posts. Each pin represents a geotagged photo.
- **Pins Metadata (When Pressed)**: Pressing a pin on the map brings up detailed information about the associated post, including the photo, description, tags, and location data.

<table>
  <tr>
    <td>
     Location permission
      <img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/location_permission.jpg"  title="location_permission"/>
    </td>
    <td>
     Home
      <img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/home.jpg"  title="home"/>
    </td>
    <td>
     Pins
      <img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/pins.jpg"  title="pins"/>
    </td>
    <td>
     Pins metadata (when pressed)
      <img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/pins_metadata.jpg"  title="pins_metadata"/>
    </td>
</tr>
</table>

### Post button

The Post button is central to creating and sharing new content:

- **Gallery Permission**: When creating a post, the app requests permission to access your gallery for uploading photos. This permission is crucial for selecting images to share.
- **Add a Tag Button**: This allows you to add a tag to your post, making it easier for others to search and find your photos based on specific themes or interests.
- **Saving Tag**: Ensures that any tag added to your post are properly saved and associated with your photo.
- **Location Off Error**: If location services are off and the photo lacks metadata, this error message prompts you to enable location services or manually enter location information.
	- **Edit Picture Info Button**: Allows you to manually edit the date and location of a photo when metadata is unavailable, ensuring your post is complete.
	- **Manual Date and Location (Edit Picture Info Button)**: Lets you manually set the date and location for your photo, which is useful if the automatic metadata is missing or incorrect.
	- **Saving Manual Date and Location**: Ensures that the manually entered date and location information is correctly stored with your post.

<table>
<tr>
    <td>
     Gallery permission
      <img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/gallery_permission.jpg"  title="gallery_permission"/>
    </td>
    <td>
     Add a tag button
      <img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/add_a_tag.jpg"  title="add_a_tag"/>
    </td>
    <td>
     Saving tag
      <img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/tag_saving.jpg"  title="tag_saving"/>
    </td>
    <td>
     Location off error 
      <img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/location_off.jpg"  title="location_off"/>
    </td>
    <td>
     Edit picture info button 
      <img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/edit_picture_info.jpg"  title="edit_picture_info"/>
    </td>
    <td>
     Manual date and location (edit picture info button)
      <img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/manual_date_and_location.jpg"  title="manual_date_and_location"/>
    </td>
    <td>
     Saving manual date and location
      <img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/manual_saving.jpg"  title="manual_saving"/>
    </td>
</tr>
</table>


### Search button

The Search button helps users find specific content within the app:

- **Search**: Allows users to search for posts using tags. This feature is essential for locating specific content.
- **Search with Tag**: Filters search results to show posts with specific tags, refining the search to find exactly what you're looking for.
- **Viewing Post in Search**: Displays detailed information about any post found through search, including the photo, description, tag, date and time data.

<table>
<tr>
    <td>
     Search 
      <img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/search.jpg"  title="search"/>
    </td>
    <td>
     Search with tag
      <img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/search_with_tag.jpg"  title="search_with_tag"/>
    </td>
    <td>
     Viewing post in search
      <img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/view_post_in_search.jpg"  title="view_post_in_search"/>
    </td>
</tr>
</table>


### Firebase

Firebase serves as the backend for storing and managing posts:

Database:
- **All Posts Stored**: Posts are stored in the Firebase Database with unique IDs based on the date and time they were created. This ensures each photo is uniquely identified.
- **Posts Structure**: The structure of the database organizes posts with unique IDs for each image, each representing as an object, associating them with their metadata individually while sharing a common description and a tag from their post.
- **Posts Information**: For each image stored in the database, detailed information is available, including the photo's unique ID, date and time, url referring to its location in Firebase Storage, dimensions (height and width), size and geolocation data (latitude and longitude).

Storage:
- **Images Stored in Storage**: Images are stored in Firebase Storage, with each image's information linked in the database, ensuring secure storage and easy accessibility. The available information that can be observed for each image is the name, size, type/format, date and time of creation and the url mentioned above.

<table>
<tr>
    <td>
     All posts stored in database
      <img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/db_info.png"  title="db_info"/>
    </td>
    <td>
     Posts structure 
      <img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/db_posts.png"  title="db_posts"/>
    </td>
    <td>
     Posts information (for one image)
      <img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/post_info_in_db.png"  title="post_info_in_db"/>
    </td>
    <td>
     Some images stored in storage (information for one image)
      <img src="https://github.com/AriZume/TechLog23-24/tree/master/screenshots/db_images_storage.png"  title="db_images_storage"/>
    </td>
</tr>
</table>


 
## Collaborators

<p>A special thanks to the following for their contributions and support:</p>
<table>
<tr>

<td align="center">
<a href="https://github.com/AriZume">
<img src="https://avatars.githubusercontent.com/u/128248493?v=4" width="100px" alt="Konstantinos Tzegkas"/><br>
<sub>
<b>Konstantinos Tzegkas (Project Manager)</b>
</sub>
</a>
</td>

<td align="center">
<a href="https://github.com/helenzina">
<img src="https://avatars.githubusercontent.com/u/128386591?v=4" width="100px" alt="Helen Zina"/><br>
<sub>
<b>Helen Zina</b>
</sub>
</a>
</td>

<td align="center">
<a href="https://github.com/alk-an">
<img src="https://avatars.githubusercontent.com/u/147655333?v=4" width="100px;" alt="Alkinoos Anastasiadis"/><br>
<sub>
<b>Alkinoos Anastasiadis</b>
</sub>
</a>
</td>

<td align="center">
<a href="https://github.com/kapadokos">
<img src="https://avatars.githubusercontent.com/u/128246366?v=4" width="100px" alt="Konstantinos Tsonidis"/><br>
<sub>
<b>Konstantinos Tsonidis</b>
</sub>
</a>
</td>

<td align="center">
<a href="https://github.com/d00om">
<img src="https://avatars.githubusercontent.com/u/82510443?v=4" width="100px" alt="Stergios Parasxos"/><br>
<sub>
<b>Stergios Parasxos</b>
</sub>
</a>
</td>

<td align="center">
<a href="https://github.com/Tziouve">
<img src="https://avatars.githubusercontent.com/u/81091539?v=4" width="100px" alt="Stelios Tziouvakas"/><br>
<sub>
<b>Stelios Tziouvakas</b>
</sub>
</a>
</td>

</tr>
</table>

 ## License

Distributed under the MIT License. See the LICENSE file for more information.

 ## Contact
 
If you have any questions or suggestions, feel free to reach out to us:
- Helen Zina - helenz1@windowslive.com
- Project Link: https://github.com/AriZume/TechLog23-24


 ## Acknowledgments

The resources that helped us through this whole process were the following:

- [Metadata-Extract_Sources](https://github.com/AriZume/TechLog23-24/blob/master/sources/MetadataExtract_sources.pdf)
- [User Interface Sources](https://github.com/AriZume/TechLog23-24/blob/master/sources/User%20Interface%20Sources.odt)
- [Database Sources](https://github.com/AriZume/TechLog23-24/blob/master/sources/db%20for%20geosnap%20source%20.docx)
- [Maps Sources](https://github.com/AriZume/TechLog23-24/blob/master/sources/maps%20sources.docx)
- [Store-Retrieve Data Sources](https://github.com/AriZume/TechLog23-24/blob/master/sources/store%20and%20retrieve%20data.docx)


For more information, read the english version of our report (**report en.pdf**) in the <a href="https://github.com/AriZume/TechLog23-24/tree/master/sources/">sources</a> folder.
