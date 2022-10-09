# qe-launcher

This program is meant to restart Quake Enhanced servers on a schedule. It was written because Quake Enhanced servers are only visible in the lobby browser for 24 hours, and also the servers will frequently disconnect from game lobbies.

It expects 

* Steam to already be running
* A recent [Java >= 11](https://jdk.java.net/18/) version installed

## Default Behavior

When started with `java -jar qe-runner-X.Y.jar`, the default configuration:

* Kills all Quake Enhanced processes that are running (even if the process is frozen)
* Starts Quake Enhanced with `C:\Program Files x86)\Steam\steamapps\common\Quake\rerelease\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1`
* Starts a multiplayer game with default game settings

## Customizing

The program behavior can be customized using a properties file:

* Create a text file, paste in the code block below, then change to your values as needed. 
    
    * You can have as many lines in the file as you want, they will be executed in the order of the number at the beginning
    * The `os->` tag runs a general command prompt command
    * The `quake->` tag launches a Quake multiplayer match with the specified parameters (no API validation takes place here)
    * The `setMaxPlayers->` tag sets the max number of players that can join. By default this will max out the slider in the menu
    * The `setValidationQuake->` tag sets the Quake Enhanced launch command for restarts after external API validation
    * The `setValidationEndpoint->` tag sets the API endpoint to use for external validation
    * The `quakeIfDown->` command will kill and re-launch specific Quake instances using the validation variables. It takes a steam mini-profile ID as input
    * Stuff between `->` and `=` characters is just a description (it shouldn't contain things like `os->` or `quake->`)
    * The `#` characters are comments
    * To make sure your commands run in order, make sure to use the XXXX number format: 0001 for first, 0002 for second ... 0100 for 100th
    * Save your file somewhere with a `.properties` extension
    * Run the program like this `java -Dprops=c:\somefolder\example.properties -jar qe-runner-X.Y.jar`

Default config:

```

# =======================================================
# force restart all servers, killing all processes
# =======================================================

# kill all quake instances
0001_os->kill = taskkill /F /IM Quake_x64_steam.exe /T

# moving files: double quotes around each parameter, and double backslashes for directories. <TIMESTAMP> gets replaced with a timestamp
# 0002_os->cmd = cmd.exe /c move "C:\\Users\\games\\Saved Games\\Nightdive Studios\\Quake\\stderr.txt" "C:\\Users\\games\\Saved Games\\Nightdive Studios\\Quake\\stderr_<TIMESTAMP>.txt" 
# 0003_os->cmd = cmd.exe /c move "C:\\Users\\games\\Saved Games\\Nightdive Studios\\Quake\\stdout.txt" "C:\\Users\\games\\Saved Games\\Nightdive Studios\\Quake\\stdout_<TIMESTAMP>.txt"

# max number of players
# 0004_setMaxPlayers->val = 4

# for launching quake: no double quotes around, but do use double backslashes for directories
0005_quake->launch = C:\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1

# =======================================================
# validation against API and soft re-launch if down (the app types 'quit' in the console)
# note: my API isn't 100% reliable, use at your own risk :)
# =======================================================
# 0006_setValidationEndpoint->val = https://api.247ffa.com/api/v1/servers
# 0007_setValidationQuake->val = C:\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1
# 0008_quakeIfDown->launch = 1426333927

```

## Changing Game Settings Once the Server is Started

For everything outside of the maxclients setting (this can't be changed in game), I do this with a Quake C mod. It checks the `saved2` CVAR and does what it needs to in the worldspawn (world.qc), then clears the cvar. I chose this route because navigating menus on a timer isn't the most robust or reliable method of running a server, selecting maps and such was too fragile last time I tried.

If you want to give it a shot without a mod though, this program is simple enough to modify. The only tricky part is you may need to add in [Direct Input scan code support](https://github.com/umer0586/winKeyboard) for some of the drop down menus. It's likely easier to not use Java in that case and go with a quick C program. 

## Running Launches/Re-launches on a Schedule

I do this with two Windows tasks scheduled every 24 hours for a complete launch (including killing all frozen processes with the OS command), and every 30 minutes for re-launches due to lobby disconnects and such (this won't kill frozen processes). 

To create Windows tasks, open up Task Scheduler from the Start menu and select `Create a Basic Task` in the right hand column. The command to run is java.exe from the JDK download, and put `-jar qe-runner-X.Y.jar` and your parameters into the arguments field. 


## Examples from my servers
I run six sandboxed servers on one machine, and my API is used to soft-relaunch for things like lobby disconnects. Each server has entries in the config files below. 

For a one server setup or to not use my API, the default config is better to use.

### Windows task: every 24 hours - force kill all Quake processes and back up logs
* Program: `C:\jdk-18.0.2.1\bin\java.exe`
* Add Arguments: `-Dprops=C:\quakeserverlauncher\killer.properties -jar C:\quakeserverlauncher\qe-runner-3.7.jar`
* Properties:

```
# kill all quake instances
0001_os->kill = taskkill /F /IM Quake_x64_steam.exe /T

# archive stdout and err
0002_os->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost1\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stderr.txt" "C:\\quakeserverlauncher\\log_1_stderr_<TIMESTAMP>.txt" 
0003_os->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost1\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stdout.txt" "C:\\quakeserverlauncher\\log_1_stdout_<TIMESTAMP>.txt" 
0004_os->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost2\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stderr.txt" "C:\\quakeserverlauncher\\log_2_stderr_<TIMESTAMP>.txt" 
0005_os->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost2\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stdout.txt" "C:\\quakeserverlauncher\\log_2_stdout_<TIMESTAMP>.txt" 
0006_os->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost3\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stderr.txt" "C:\\quakeserverlauncher\\log_3_stderr_<TIMESTAMP>.txt" 
0007_os->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost3\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stdout.txt" "C:\\quakeserverlauncher\\log_3_stdout_<TIMESTAMP>.txt" 
0008_os->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost4\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stderr.txt" "C:\\quakeserverlauncher\\log_4_stderr_<TIMESTAMP>.txt" 
0009_os->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost4\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stdout.txt" "C:\\quakeserverlauncher\\log_4_stdout_<TIMESTAMP>.txt" 
0010_os->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost5\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stderr.txt" "C:\\quakeserverlauncher\\log_5_stderr_<TIMESTAMP>.txt" 
0011_os->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost5\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stdout.txt" "C:\\quakeserverlauncher\\log_5_stdout_<TIMESTAMP>.txt" 
0012_os->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost6\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stderr.txt" "C:\\quakeserverlauncher\\log_6_stderr_<TIMESTAMP>.txt" 
0013_os->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost6\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stdout.txt" "C:\\quakeserverlauncher\\log_6_stdout_<TIMESTAMP>.txt" 


```

### Windows task: twice hourly - restarts if server is down
* Program: `C:\jdk-18.0.2.1\bin\java.exe`
* Add Arguments: `-Dprops=C:\quakeserverlauncher\validator-launcher.properties -jar C:\quakeserverlauncher\qe-runner-3.7.jar`
* Properties:

```
0001_setValidationEndpoint->val = https://api.247ffa.com/api/v1/servers

0002_setValidationQuake->val = C:\\Sandbox\\use\\quakehost1\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 +saved2 1
0003_quakeIfDown->launch = 1128505857

0004_setValidationQuake->val = C:\\Sandbox\\use\\quakehost2\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 +saved2 2
0005_quakeIfDown->launch = 1426512674

0006_setValidationQuake->val = C:\\Sandbox\\use\\quakehost3\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 +saved2 3
0007_quakeIfDown->launch = 1425838691

0008_setValidationQuake->val = C:\\Sandbox\\use\\quakehost4\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 +saved2 4
0009_quakeIfDown->launch = 1426388016

0010_setValidationQuake->val = C:\\Sandbox\\use\\quakehost5\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 +saved2 5
0011_quakeIfDown->launch = 1426333927

0012_setValidationQuake->val = C:\\Sandbox\\use\\quakehost6\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 +saved2 6
0013_quakeIfDown->launch = 1426297538


```

## General issues and notes

- The way this program works is a fragile process, simulating key presses. Don't move the mouse or press keys until it's done.
- There will be several 20-60 second delays as stuff loads. The whole process to start the server takes a couple minutes. These built-in delays work for me, but your mileage may vary depending on your network and computer speeds.
- The API used to check statuses is dependent on Steam rich presence information. When Steam weekly maintenance happens every Tuesday, there is a high chance that false positives will occur and your servers will be shut down in error. It's up to you if this outweighs the time spent checking for lobby disconnects :)
- To run as Windows tasks: the computer needs to be unlocked and the Windows user needs be logged in
