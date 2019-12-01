# FractalTrip
FractalTrip is a real-time visualiser of Mandelbrot and Julia sets for Android 4.4+!
What you see is complex planes with Mandelbrot and Julia sets plotted on them. Actually, the sets are only black regions of the image.
But with a little bit of magic(which happens in fragment shader) they become coloful and spectacular!
Take a trip to undiscovered infinite world of fractals and complex numbers!

![Julia2](https://user-images.githubusercontent.com/47830605/69922535-b827d900-14a5-11ea-9b32-0e0a86f7dba8.gif)
![Julia3](https://user-images.githubusercontent.com/47830605/69922546-e0173c80-14a5-11ea-905b-d05a69855f84.gif)
# Controls
If your device is in portrait orientation then Mandelbrot set is at the top, Julia - at the bottom. If in landscape - you will see only Julia set.
You can zoom and move through Julia set in both orientations. Julia set depends on the parameter from the Mandelbrot set. If you touch
any area of Mandelbrot set, Julia set will immediately change.

# Installation 
Just clone the project and build it via Android Studio.

# Pro tip
Most interesting variations of Julia set happen, when you touch Mandelbrot set on the periphery of the black zone(cardiod).
Feel free to experiment and enjoy your trip to Fractal World!
