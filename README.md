# qe-launcher

This program is meant to restart Quake Enhanced servers on a schedule. It expects Steam to already be running, and you should probably use a recent [Java >= 11](https://jdk.java.net/18/) version.

## General Operation

When started with `java -jar qe-runner-X.Y.jar`

1.  Kills all Quake Enhanced processes that are running
2.  Starts Quake Enhanced, the default command is `C:/Program Files x86)/Steam/steamapps/common/Quake/rerelease/Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1`
3.  Starts a multiplayer game with default game settings

## Custom Quake Commands or CVARs

If your Quake install location is different than the default, or you'd like to change CVARs:

1.  Create a text file in this format, changing to your values
    `commands[0] = D:/your/path/here/Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1`
2.  Save it somewhere with a `.properties` extension
3.  Run the program like this `java -Dprops=c:\somefolder\example.properties -jar qe-runner-X.Y.jar`

Note, you can specify [multiple Quake installs](https://github.com/oldt-ech/qe-launcher/blob/main/src/main/resources/247ffa.properties) as well if you have them sandboxed, increase the index to have as many as you want.

## Changing Game Settings

I currently do this with a Quake C mod, it checks the `saved2` CVAR and does what it needs to. I chose this route because navigating menus on a timer isn't the most robust or reliable method of running a server, selecting maps and such was too fragile last time I tried.

If you want to give it a shot without a mod though, this program is simple enough to modify. The only tricky part is you may need to add in [Direct Input scan code support](https://github.com/umer0586/winKeyboard) for some of the drop down menus. It's likely easier to not use Java in that case and go with a quick C program. 

## Running on a schedule

I do this with a Windows Task, create a basic task to run java.exe from the JDK download, put `jar qe-runner-X.Y.jar` and your parameters into the arguments field. Some caveats:

- The computer needs to be unlocked
- The user needs be logged in

## General issues

- This is a really fragile process simulating key presses. Don't move the mouse or press keys until it's done.
- There will be several 10-15 second delays as stuff loads. The whole process to start the server takes a couple minutes. These built-in delays work for me, but your mileage may vary depending on your network and computer speeds.
