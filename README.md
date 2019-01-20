# NTUHackathon2018

OCR android application built during one-day hackathon.

The app lets the user take a picture or load an image from gallery. The image is processed using the android-vision library and the text output sent to a server which check for potential errors. The server sends back information on incorrect or misspelled words. Thereafter, the user is taken to a page where they are able to quickly edit the text by correcting the highlighted portions containing spelling issues. 

The server script was written using flask in python and runs on a Heroku server. The app was written using Android-studio. Due to time limitations, the 'online spellcheck' was not tested properly and hence does not work as well as can be expected.
