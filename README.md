# Project1_2 DKE Group 17

# MISSION TITAN

# Installation

Required Gradle versions: 7.0.2 . note if some problems arise,it could be that the -all distribution package is
necessary

### Required gradle-wrapper.properties

```properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-7.0.2-all.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

### Preferred gradle dependencies / testing task properties

```groovy
dependencies {
    implementation 'org.junit.jupiter:junit-jupiter:5.7.0'
    implementation group: 'au.com.bytecode', name: 'opencsv', version: '2.4'
    implementation group: 'org.apache.commons', name: 'commons-csv', version: '1.8'
    compileOnly 'org.jetbrains:annotations:20.1.0'
    testImplementation 'org.junit.jupiter:junit-jupiter:5.6.2'
}

test {
    useJUnitPlatform()
    testLogging {
        events 'passed', 'skipped', 'failed'
    }
    maxParallelForks 1
    minHeapSize "128m"
    maxHeapSize "512m"
    failFast true
    onOutput { descriptor, event -> logger.lifecycle("Test: " + descriptor + " \noutput: " + event.message )}
    finalizedBy jacocoTestReport
}

```

### Compiler - Java language level 16 
#### Enable preview 16 (Records, Patterns, local enums, and interfaces)


```xml
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
    <component name="CompilerConfiguration">
        <bytecodeTargetLevel target="16" />
    </component>
</project>
```

# Usage

[Config.java](C:\JAVA\SolarSystem3D\src\main\java\API\Config.java)

#### Check out all the available simulations it is possible to select!

#####First order ODE

Lorenz System simulation ![lorenz]

[lorenz]:C:\JAVA\SolarSystem3D\src\main\resources\Gifs\LorenzGif.gif

simulating fluid dynamics
#####Second order ODE

Double Pendulum simulation ![pendulum]

[pendulum]: C:\JAVA\SolarSystem3D\src\main\resources\Gifs\PendulumGif.gif  

simulating chaotic motions

A lot of configurations are available in runtime too, so make sure you try all of them 







### Initialise the assist window and the simulation instance

[Main.java](C:\JAVA\SolarSystem3D\src\main\java\phase3\Main.java)

```java
import group17.phase1.group17.phase1.Titan.Interfaces.SimulationInterface;

public class Main {

    public static SimulationInterface simulation;
    public static void main(String[] args) {
        simulation = new Simulation();
        simulation.init();
        simulation.start();
    }
}
```


### Set up a simulation

[Simulation.java](C:\JAVA\SolarSystem3D\src\main\java\phase3\Simulation\Simulation.java)

```java
@Override 
public void init() {
    this.system.init(); //init system and give the runner the initial state ready
    this.graphics.init();
    this.runner.init();
}

@Override 
public void start() {
    this.running = true;
    this.runner.runSimulation();
}
```

### Runner default loop
```Java
@Override
public synchronized void loop() {
    StateInterface<Vector3dInterface> currentState = simInstance().getSystem().getState();
    simInstance().getGraphics().start(currentState);
    simInstance().getSystem().updateState(
    getSolver().step(getSolver().getFunction(), currentTime[0], currentState, step_size[0]));
    currentTime[0] += step_size[0];
}
```



By default, it is possible to allow the program to generate runtime live reports , and it is possible to let it
automatically store txt or csv files. Data for the error evaluation have been collected from the [NASA Horizon Website].

[Nasa Horizon Website]: <https://ssd.jpl.nasa.gov/horizons.cgi>

```java
public class Config{
    public static final ErrorPrint OUT_ERROR = ErrorPrint.CSV;
}
```

If this configuration is selected during the simulation process, the generated files will be collected in the
resources/ErrorData folder, organised by solver and step size used If selected during tests (by enabling
[GeneralSystemTest.java]) these files will be collected in the test/java/resources/ErrorData folder,as by standard
organization.

[GeneralSystemTest.java]: C:\JAVA\SolarSystem3D\src\test\java\GeneralSystemTest.java

NOTE : the ErrorData folder MUST be present in both cases, if this options is selected NOTE : to allow the report
generation it is **_required_**  to let the simulation Clock to get to the **_first of the month at the exact time "00:
00:00"_**. Check out the implementation of [Clock.java](C:\JAVA\SolarSystem3D\src\main\java\API\System\Clock.java) or stick to
step size exactly dividing a minute by a natural, integer number.


## Contributing

Group 17 ,
Data Science and Artificial Intelligence,
Maastricht University 2020/2021 .
People who contributed at this project :
Dan Parii, 
Lorenzo Pompigna,
Nikola Prianikov,
Axel Rozental,
Konstantin Sandfort, 
Abhinandan Vasudevan (phase 1 only)
