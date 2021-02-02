# MusicWiki App
### The app presents an **organized collection of songs**, grouped primarily under different *Genres*. 
Under each genre, the songs are further grouped under *Albums* and *Artists*. Additionally, there 
is a third tab *Tracks* that lists all the tracks under this genre ***at once***

**You can test the application APK file is attached with this project in apk folder in root directory**


- ## Exploring the app features:

    - ### Home Screen
      - The home screen shows the top 10 genres to the user and the list expands to show all the available genres(50).
      - To make things convinient for the users, *a _search bar_ has been added* so that users can directly search for the genre of their choice.
	
	:point_right: Clicking on any genre displays a new genre detail screen to the user.
    
    - ### Genre Detail Screen
      - A back button on top left corner, that takes the user back to the genre detail screen.
      - The name of genre selected on the top.
      - An info button at top right corner, that shows the information of the genre, when clicked.
      - Three tabs, *Artists, Albums and Tracks*, only one of which can be selected to be on display at a time.
        - **Artists**: If selected, lists top artists who have songs in the selected genre.
        - **Albums**: If selected, lists top albums having songs of the selected genre.
        - **Tracks**: Lists top the tracks under this genre.
	   

	 :point_right: For all the three tabs available, a different layout for presenting the track/album/artist has been designed.
	 
    - ### Album Detail Screen
      - A back button on top left corner, that takes the user back to the genre detail screen.
      - An info button on top right corner, that displays the album information to the user, when clicked.
      - The main screen shows:
         - The album cover image.
         - The album title.
         - The artist and the number of plays from the album.
         - The other genres this album belong to, can be swiped horizontally if exceeds the screen width.
         - List of all the tracks in the album, with track title, artist name and album cover image.
         
  :point_right: Any genre can be clicked and then the flow will be same as above.
   
    - ### Artist Detail Screen
      - A back button on top left corner, that takes the user back to the genre detail screen.
      - An info button on top right corner, that displays the artist information to the user, when clicked.
      - The main screen shows:
         - The artist's image.
         - The artist's name.
         - The number of plays for the artist and their follower count.
         - The other genres this artist has songs in, can be swiped horizontally if exceeds the screen width.
         - List of all the *Tracks* by the artist, with track title, artist name and album cover image, can be swiped horizontally if exceeds the screen width.
         - List of all the *Albums* by the artist, with album title, artist name and album cover image, can be swiped horizontally if exceeds the screen width.
         
	 :point_right: Any genre can be clicked and then the flow will be same as above. Same for the album.

- ## Assumptions

    :point_right: No user would like to manually look for his choice genre in a large list of genres,
		  a **search operation is desired**.
		  
    :point_right: Excess textual information is less engaging. So details are not shown directly in launch of any activity
		  Keeping the textual **information behind a info button gives a better appearance**. 
      
    :point_right: Any data from server (artist, tracks, albums or genres) contains lakhs of data items. So App is showing top 50 item from any data list.
