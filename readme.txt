wodka

requirements:
    java runtime environment.
        Reason: To run wodka.
        Version: 1.4+
        Homepage: http://java.sun.com/j2se


running the wodka breeder.
    -Extract this zip file anywhere on your computer. E.g. c:\Program Files, 
        /usr/local, or whereever you want.
    -Start the sodarace application using the following link: 
        http://www.sodaplay.com/constructor/beta/racer.jnlp
    -Click Accept All on the sodarace application.
    -Execute the breeder.bat bin directory.
    -Create a new Genetic Algorithm by clicking new on the breeder.
    -Click start on the wodka application. Random generated 
        robots should approache on the sodarace application and 
        start their first race.

wodka home.
    Wodka creates a directory in your home directory where you can find all
    results concerning wodka. $home/wodka

    ATTENTION: The java home directory is created when you start wodka the first time. 
       Do not expect it to be there before you started wodka.

    Logging.
        In the $wodka_home/admin directory you can find a properties file (log4j.properties) 
        to configure the wodka logging. Refer to the comments in that file to learn how 
        to configure the wodka logging.
        The wodka logfile is by default also generated in the admin directory.


optional requirements:
    Java2Html.
        Reason: Necessary if you want to generate browsable java 
            code for the documentation.
        Version: any
        Homepage: http://www.java2html.com
        
    Ant
        Reason: Necessary if you want to generate browsable java code for the documentation.
        Version: 1.5+, 1.6 recommended to use the new scp and ssh features.
        Homepage: http://jakarta.apache.org/ant

