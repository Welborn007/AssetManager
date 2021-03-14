

1] Add the library bitmapConverter inside setting.gradle and add inside app buil.gradle as below

implementation project(":bitmapConverter")

2] Inside the library is a singleton class created to get the Instance of the library to get access to the methods

3] To get the bitmap from the drawable consume as below

Controller.getInstance(this).convertToBitmap(drawable)
