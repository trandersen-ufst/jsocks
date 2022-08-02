This project provides the revised source of the
http://sourceforge.net/projects/jsocks/ project as 
required by the LGPL.

The git repository was started on a blank sheet,
and then importing the sources, so all the 
work done is in the git history.  The sf repository
does not contain any versioned files, so there
was no history to import.

A source tree with proper packages has been
set up and cleaned up by Eclipse.

Logging has been migrated to slf4j and a 
launch configuration using slf4j-simple has
been included, which also allows for creating
runnable jars from inside Eclipse 3.5+

/ravn - 2009-09-07

Mavenized.  

/ravn - 2012-11-07

Reorganized, updated libraries, and added
runnable version inspired by https://github.com/ravn/jsocks/pull/2

Java 17 supports Java 7 as the newest. If you need to run jsocks under
Java 6, change
the source and target properties in top level pom.xml, and run Maven on Java 8.

