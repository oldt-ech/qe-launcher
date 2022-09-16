# qe-launcher

This program is meant to restart Quake Enhanced servers on a schedule. It expects Steam to already be running, and you should probably use a recent [Java >= 11](https://jdk.java.net/18/) version.

## General Operation

When started with `java -jar qe-runner-X.Y.jar`

* Kills all Quake Enhanced processes that are running
* Starts Quake Enhanced, the default command is `C:\Program Files x86)\Steam\steamapps\common\Quake\rerelease\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1`
* Starts a multiplayer game with default game settings

## <a name="customizing"></a>Customizing

If your Quake install location is different than the default, you'd like to change CVARs, or run commands like backing up files:

* Create a text file, paste in the code block below, then change to your values as needed:
    
    * You can have as many lines in the file as you want, they will be executed in the order of the number at the beginning
    * The `os->` tag runs a general command prompt command
    * The `quake->` tag launches a Quake multiplayer match with the specified paremeters
    * Stuff between `->` and `=` characters is just a description (it shouldn't contain `os->` or `quake->`)
    * The `#` characters are comments

```

# kill all quake instances
1_os->kill = taskkill /F /IM Quake_x64_steam.exe /T

# moving files: double quotes around each parameter, and double backslashes for directories. <TIMESTAMP> gets replaced with a timestamp
# 2_os->cmd = cmd.exe /c move "C:\\Users\\games\\Saved Games\\Nightdive Studios\\Quake\\stderr.txt" "C:\\Users\\games\\Saved Games\\Nightdive Studios\\Quake\\stderr_<TIMESTAMP>.txt" 
# 3_os->cmd = cmd.exe /c move "C:\\Users\\games\\Saved Games\\Nightdive Studios\\Quake\\stdout.txt" "C:\\Users\\games\\Saved Games\\Nightdive Studios\\Quake\\stdout_<TIMESTAMP>.txt"

# for launching quake: no double quotes around, but do use double backslashes for directories
4_quake->launch = C:\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1

```

* Save it somewhere with a `.properties` extension
* Run the program like this `java -Dprops=c:\somefolder\example.properties -jar qe-runner-X.Y.jar`


## Changing Game Settings

I currently do this with a Quake C mod, it checks the `saved2` CVAR and does what it needs to. I chose this route because navigating menus on a timer isn't the most robust or reliable method of running a server, selecting maps and such was too fragile last time I tried.

If you want to give it a shot without a mod though, this program is simple enough to modify. The only tricky part is you may need to add in [Direct Input scan code support](https://github.com/umer0586/winKeyboard) for some of the drop down menus. It's likely easier to not use Java in that case and go with a quick C program. 

## Running on a schedule

I do this with a Windows Task scheduled for every 24 hours. To create one, open up Task Scheduled and select `Create a Basic Task` in the right hand column. The command to run is java.exe from the JDK download, and put `-jar qe-runner-X.Y.jar` and your parameters into the arguments field. 

### Example from my server

* Program: `C:\jdk-18.0.2.1\bin\java.exe`

* Add Arguments: `-Dprops=247ffa.properties -jar C:\quakeserverlauncher\qe-runner-3.5.jar`

### Some caveats when running the scheduled task

- The computer needs to be unlocked
- The user needs be logged in

## General issues

- This is a really fragile process simulating key presses. Don't move the mouse or press keys until it's done.
- There will be several 10-15 second delays as stuff loads. The whole process to start the server takes a couple minutes. These built-in delays work for me, but your mileage may vary depending on your network and computer speeds.

## Next steps

- Add some commands to allow custom delay timings
- Integrate https://api.247ffa.com/api/v1/servers for monitoring and restarting as needed

## Upgrading from 3.4 to 3.5

- The format of the properties file changed in a way that's not backwards compatible, check out the example above in the [Customizing](#customizing) section