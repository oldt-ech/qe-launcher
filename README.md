# qe-launcher

This program is meant to restart Quake Enhanced servers on a schedule. It was written because Quake Enhanced servers are only visible in the lobby browser for 24 hours, and also the servers will frequently disconnect from game lobbies.

It expects:

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
    * Stuff between `->` and `=` characters is just a description (it shouldn't contain things like `os->` or `quake->`)
    * The `#` characters are comments
    * To make sure your commands run in order, use an XXXX number format: 0001 for first, 0002 for second ... 0100 for 100th
    * Save your file somewhere with a `.properties` extension
    * Run the program like this `java -Dprops=c:\somefolder\example.properties -jar qe-runner-X.Y.jar`

#### Default config

```

# =======================================================
# force restart all servers, killing all processes
# =======================================================

# kill all quake instances
0001_os->kill = taskkill /F /IM Quake_x64_steam.exe /T

# moving files: double quotes around each parameter, and double backslashes for directories. <TIMESTAMP> gets replaced with a timestamp
# 0002_os->cmd = cmd.exe /c move "C:\\Users\\games\\Saved Games\\Nightdive Studios\\Quake\\stderr.txt" "C:\\Users\\games\\Saved Games\\Nightdive Studios\\Quake\\stderr_<TIMESTAMP>.txt" 
# 0003_os->cmd = cmd.exe /c move "C:\\Users\\games\\Saved Games\\Nightdive Studios\\Quake\\stdout.txt" "C:\\Users\\games\\Saved Games\\Nightdive Studios\\Quake\\stdout_<TIMESTAMP>.txt"

# set max number of players
# 0004_setMaxPlayers->val = 4

# set game mode
# 0005_setGameModeIndex->val = 2

# for launching quake: no double quotes around, but do use double backslashes for directories
0006_quake->launch = C:\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1

# execute a config: 
# - put a file named qelauncher.cfg in C:\Users\<user>\Saved Games\Nightdive Studios\Quake 
# - this will re-focus the Quake window using the command passed to it, and then exec the config. Don't include any parameters
# 0007_exeConfig->focus = C:\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe

```
#### Available commands
| Command                 	| Input                                    						| Default value if not in properties file                                                                                               	| Description                                                                                                                                                                                                                                                 	| Additional Notes                                                                                                                                                                                                                                                |
|-------------------------	|------------------------------------------						|---------------------------------------------------------------------------------------------------------------------------------------	|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ifDownDetermine->       	| URI                                      						| N/A                                                                                                                                   	| Checks URI to see if a server is online. Sets flag internally for use in other 'ifDown' commands.                                                                                                                                                           	| Expects a report with uptime entries. [Documentation about this endpoint](https://www.247ffa.com/2022/10/server-status-api-updates-superdanman.html)  |
| ifDownQuake->           	| Path to Quake Enhanced                   						| N/A                                                                                                                                   	| If online flag (from ifDownDetermine) is false, runs quake->                                                                                                                                                                                                	|                                                                                                                                                                                                                                                                 |
| ifDownExeDownConfig->   	| Path to Quake Enhanced without arguments 						| N/A                                                                                                                                   	| If online flag (from ifDownDetermine) is false, focuses the Quake window, clears all Bethesda screens (escape key is pressed), and executes qelauncherdown.cfg                                                                                              	| This can be used to quit quake, by putting 'quit' in the config. See examples below                                                                                                                                                                                                 |
| ifDownExeConfig->       	| Path to Quake Enhanced without arguments 						| N/A                                                                                                                                   	| If online flag (from ifDownDetermine) is false, runs exeConfig->                                                                                                                                                                                            	|                                                                                                                                                                                                                                                                 |
| ifDownOs->              	| Command line program                     						| N/A                                                                                                                                   	| If online flag (from ifDownDetermine) is false, runs os->                                                                                                                                                                                                   	| This can be used to back-up log files after lobby disconnects                                                                                                                                                                                                   |
| os->                    	| Command line program                     						| N/A                                                                                                                                   	| Runs a general command prompt command                                                                                                                                                                                                                       	| This can be anything that you'd run at a command prompt. Useful for backing up files, etc.                                                                                                                                                                      |
| exeConfig->             	| Path to Quake Enhanced without arguments 						| N/A                                                                                                                                   	| Focuses the Quake window, clears all Bethesda screens (escape key is pressed), and executes qelauncher.cfg                                                                                                                                                  	| Focusing the window is required for hosting multiple Quake instances and sending them commands                                                                                                                                                                  |
| setMaxPlayers->         	| Numeric value 2-X                        						| Maximum value for slider in Quake menu screen                                                                                         	| Sets number of players that can connect for quake->                                                                                                                                                                                                         	|                                                                                                                                                                                                                                                                 |
| setGameModeIndex->      	| Numeric value                            						| 2                                                                                                                                     	| Sets game mode for quake-> <br> 1 = Cooperative <br> 2 = Deathmatch <br> 3 = Horde <br> 4 = Capture the Flag                                                                                                                                                  |                                                                                                                                                                                                                                                                 |
| quake->                 	| Path to Quake Enhanced with command line aruguments           | N/A                                                                                                           							| Starts a public Quake server, uses setMaxPlayers-> and setGameModeIndex-> values                                                                                                                                                                              |                                                                                                                                                                                                                                                                 |

#### Deprecated commands																																																																																																																																																																																				
| Command                 	| Input                                    						| Default value if not in properties file                                                                                               	| Description                                                                                                                                                                                                                                                 	| Additional Notes                                                                                                                                                                                                                                                |
|-------------------------	|------------------------------------------						|---------------------------------------------------------------------------------------------------------------------------------------	|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|	
| setValidationEndpoint-> 	| URI                                      						| https://api.247ffa.com/api/v1/servers                                                                                                 	| Endpoint to call that lists server statuses                                                                                                                                                                                                                 	|                                                                                                                                                                                                                                                                 |
| quakeIfDown->           	| Steam mini-id                            						| N/A                                                                                                                                   	| If setValidationEndpoint-> says the server is offline by polling twice over two minutes: <br>1. Brings setValidationQuake-> instance into focus by running it with no args<br>2. Types quit in console<br>3. Launches deathmatch server using setValidationQuake-> value 		|                                                                                                                                                                                                                                                                 |
| setValidationQuake->    	| Path to Quake Enhanced                   						| C:\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 	| Command to start a deathmatch server when used with quakeIfDown->                                                                                                                                                                                           	| This can only by used with quakeIfDown->                                                                                                                                                                                                                        |

## Running Launches/Re-launches on a Schedule

I do this with two Windows tasks scheduled every 24 hours to kill all running servers, and every hour for re-launches due to lobby disconnects and failed starts.

To create Windows tasks, open up Task Scheduler from the Start menu and select `Create a Basic Task` in the right hand column. The command to run is java.exe from the JDK download, and put `-jar qe-runner-X.Y.jar` and your parameters into the arguments field. 


### Examples from my servers
I run six sandboxed servers on one machine, and my API is used to relaunch if needed. Each server has entries in the config files below. 

For a one server setup or to not use my API to check for online status, the default config is better to use.

#### Windows task: every 24 hours - force kill all Quake processes and back up logs
* Program: `C:\jdk-18.0.2.1\bin\java.exe`
* Add Arguments: `-Dprops=C:\quakeserverlauncher\killer.properties -jar C:\quakeserverlauncher\qe-runner-4.0.jar`
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

# start again
0014_quake->launch = C:\\Sandbox\\use\\quakehost1\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 +saved2 1
0015_quake->launch = C:\\Sandbox\\use\\quakehost2\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 +saved2 2
0016_quake->launch = C:\\Sandbox\\use\\quakehost3\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 +saved2 3
0017_quake->launch = C:\\Sandbox\\use\\quakehost4\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 +saved2 4
0018_quake->launch = C:\\Sandbox\\use\\quakehost5\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 +saved2 5
0019_quake->launch = C:\\Sandbox\\use\\quakehost6\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 +saved2 6



```

#### Windows task: hourly - restarts if server is down
* Program: `C:\jdk-18.0.2.1\bin\java.exe`
* Add Arguments: `-Dprops=C:\quakeserverlauncher\validator-launcher.properties -jar C:\quakeserverlauncher\qe-runner-4.0.jar`
* Properties:

```
# create configs
#-------------------------

# qelauncherdown.cfg: quit if lobby disconnected or quake server unable to start previously
#0001_os->cmd = cmd.exe /c echo quit > "C:\\Sandbox\\use\\quakehost1\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\qelauncherdown.cfg"
#0002_os->cmd = cmd.exe /c echo quit > "C:\\Sandbox\\use\\quakehost2\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\qelauncherdown.cfg"
#0003_os->cmd = cmd.exe /c echo quit > "C:\\Sandbox\\use\\quakehost3\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\qelauncherdown.cfg"
#0004_os->cmd = cmd.exe /c echo quit > "C:\\Sandbox\\use\\quakehost4\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\qelauncherdown.cfg"
#0005_os->cmd = cmd.exe /c echo quit > "C:\\Sandbox\\use\\quakehost5\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\qelauncherdown.cfg"
#0006_os->cmd = cmd.exe /c echo quit > "C:\\Sandbox\\use\\quakehost6\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\qelauncherdown.cfg"

# qelauncher.cfg: nothing here yet, just echos success
#0007_os->cmd = cmd.exe /c echo echo server started > "C:\\Sandbox\\use\\quakehost1\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\qelauncher.cfg"
#0008_os->cmd = cmd.exe /c echo echo server started > "C:\\Sandbox\\use\\quakehost2\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\qelauncher.cfg"
#0009_os->cmd = cmd.exe /c echo echo server started > "C:\\Sandbox\\use\\quakehost3\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\qelauncher.cfg"
#0010_os->cmd = cmd.exe /c echo echo server started > "C:\\Sandbox\\use\\quakehost4\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\qelauncher.cfg"
#0011_os->cmd = cmd.exe /c echo echo server started > "C:\\Sandbox\\use\\quakehost5\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\qelauncher.cfg"
#0012_os->cmd = cmd.exe /c echo echo server started > "C:\\Sandbox\\use\\quakehost6\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\qelauncher.cfg"

# relaunch if down, checking 30 minute uptime history
# notes: 
# - The notepad entries below are to catch any spare key presses. Ex: if you have 'quit' in the ifDownExeDownConfig-> config, a tilde will be pressed after entering the command
#   in the formerly active window (the tilde is needed to close console when not using 'quit'). This tilde can cause problems with multiple instances of Quake on same machine,
#   if not caught in notepad.
# - The numeric value in https://api.247ffa.com/api/v1/stats/reports/online_history/1128505857... is the server id from https://api.247ffa.com/api/v1/servers
#   Email me oldtech@247ffa.com to add yours.
# - OS commands to back up stderr/stdout log files must be done when Quake is down, or they will be 0KB.
#-------------------------


0100_ifDownDetermine->call https://api.247ffa.com/api/v1/stats/reports/online_history/1128505857?from=30
0110_ifDownOs->cmd = notepad
0120_ifDownExeDownConfig->focus = C:\\Sandbox\\use\\quakehost1\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe
0130_ifDownOs->cmd = taskkill /F /IM notepad.exe /T
0140_ifDownOs->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost1\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stderr.txt" "C:\\quakeserverlauncher\\log_1_stderr_<TIMESTAMP>.txt"  
0150_ifDownOs->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost1\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stdout.txt" "C:\\quakeserverlauncher\\log_1_stdout_<TIMESTAMP>.txt" 
0160_ifDownQuake->launch = C:\\Sandbox\\use\\quakehost1\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 +saved2 1
0170_ifDownExeConfig->focus = C:\\Sandbox\\use\\quakehost1\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe

0200_ifDownDetermine->call https://api.247ffa.com/api/v1/stats/reports/online_history/1426512674?from=30
0210_ifDownOs->cmd = notepad
0220_ifDownExeDownConfig->focus = C:\\Sandbox\\use\\quakehost2\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe
0230_ifDownOs->cmd = taskkill /F /IM notepad.exe /T
0240_ifDownOs->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost2\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stderr.txt" "C:\\quakeserverlauncher\\log_2_stderr_<TIMESTAMP>.txt"  
0250_ifDownOs->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost2\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stdout.txt" "C:\\quakeserverlauncher\\log_2_stdout_<TIMESTAMP>.txt" 
0260_ifDownQuake->launch = C:\\Sandbox\\use\\quakehost2\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 +saved2 2
0270_ifDownExeConfig->focus = C:\\Sandbox\\use\\quakehost2\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe

0300_ifDownDetermine->call https://api.247ffa.com/api/v1/stats/reports/online_history/1425838691?from=30
0310_ifDownOs->cmd = notepad
0320_ifDownExeDownConfig->focus = C:\\Sandbox\\use\\quakehost3\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe
0330_ifDownOs->cmd = taskkill /F /IM notepad.exe /T
0340_ifDownOs->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost3\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stderr.txt" "C:\\quakeserverlauncher\\log_3_stderr_<TIMESTAMP>.txt"  
0350_ifDownOs->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost3\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stdout.txt" "C:\\quakeserverlauncher\\log_3_stdout_<TIMESTAMP>.txt" 
0360_ifDownQuake->launch = C:\\Sandbox\\use\\quakehost3\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 +saved2 3
0370_ifDownExeConfig->focus = C:\\Sandbox\\use\\quakehost3\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe

0400_ifDownDetermine->call https://api.247ffa.com/api/v1/stats/reports/online_history/1426388016?from=30
0410_ifDownOs->cmd = notepad
0420_ifDownExeDownConfig->focus = C:\\Sandbox\\use\\quakehost4\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe
0430_ifDownOs->cmd = taskkill /F /IM notepad.exe /T
0440_ifDownOs->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost4\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stderr.txt" "C:\\quakeserverlauncher\\log_4_stderr_<TIMESTAMP>.txt"  
0450_ifDownOs->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost4\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stdout.txt" "C:\\quakeserverlauncher\\log_4_stdout_<TIMESTAMP>.txt" 
0460_ifDownQuake->launch = C:\\Sandbox\\use\\quakehost4\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 +saved2 4
0470_ifDownExeConfig->focus = C:\\Sandbox\\use\\quakehost4\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe

0500_ifDownDetermine->call https://api.247ffa.com/api/v1/stats/reports/online_history/1426333927?from=30
0510_ifDownOs->cmd = notepad
0520_ifDownExeDownConfig->focus = C:\\Sandbox\\use\\quakehost5\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe
0530_ifDownOs->cmd = taskkill /F /IM notepad.exe /T
0540_ifDownOs->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost5\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stderr.txt" "C:\\quakeserverlauncher\\log_5_stderr_<TIMESTAMP>.txt"  
0550_ifDownOs->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost5\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stdout.txt" "C:\\quakeserverlauncher\\log_5_stdout_<TIMESTAMP>.txt" 
0560_ifDownQuake->launch = C:\\Sandbox\\use\\quakehost5\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 +saved2 5
0570_ifDownExeConfig->focus = C:\\Sandbox\\use\\quakehost5\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe

0600_ifDownDetermine->call https://api.247ffa.com/api/v1/stats/reports/online_history/1426297538?from=30
0610_ifDownOs->cmd = notepad
0620_ifDownExeDownConfig->focus = C:\\Sandbox\\use\\quakehost6\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe
0630_ifDownOs->cmd = taskkill /F /IM notepad.exe /T
0640_ifDownOs->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost6\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stderr.txt" "C:\\quakeserverlauncher\\log_6_stderr_<TIMESTAMP>.txt"  
0650_ifDownOs->cmd = cmd.exe /c move "C:\\Sandbox\\use\\quakehost6\\user\\current\\Saved Games\\Nightdive Studios\\Quake\\stdout.txt" "C:\\quakeserverlauncher\\log_6_stdout_<TIMESTAMP>.txt" 
0660_ifDownQuake->launch = C:\\Sandbox\\use\\quakehost6\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1 +saved2 6
0670_ifDownExeConfig->focus = C:\\Sandbox\\use\\quakehost6\\drive\\C\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe


```

## General issues and notes

- The way this program works is a fragile process, simulating key presses. Don't move the mouse or press keys until it's done.
- There will be several 20-60 second delays as stuff loads. The whole process to start the server takes a couple minutes. These built-in delays work for me, but your mileage may vary depending on your network and computer speeds. If you'd like to change the default timings, launch the program with -DdebugLaunchDelay=X and -DdebugMenuDelay=Y. They take values in milliseconds. Watch out though... overriding these defaults is 95% likely break things. The defaults were put in after a month of testing, and they try account for things like Steam updates and syncing.
- To run as Windows tasks: the computer needs to be unlocked and the Windows user needs be logged in

## Thanks

Huge thanks to everybody on the Quake Enhanced Discord for testing and the great ideas.