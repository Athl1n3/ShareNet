# ShareNet Social App using Room Persistence Library

This is demonstration of a simple social app using Room Persistence Library
The Room library acts as an abstract layer for underlying SQLite database.
The app interface is based on the interface(Only) of Googles Realtime database sample and its basic idea
The app allows users(local users) to make their own posts and delete them and view others posts
The app contains implementations of:  
*Broadcast Receiver + Forground service: that enables user to make a post using notification direct reply feature  
*LiveData + ViewModel (For updating RecyclerView)
*RecyclerView (Adding and Deleting data)
*Fragments using pager
*Sign In/Up activity window  
*Menu with 2 items for user settings and logout
*DialogFragment  
*SharedPreferences - Write/Read data from preferences file  
*Singleton for database access  
*SQL database using Room Persistence Library 
