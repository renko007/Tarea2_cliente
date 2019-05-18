JFLAGS = -g
JC = javac
JVM = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	NetworkClient.java \

MAIN=NetworkClient

default: classes

classes: $(CLASSES:.java=.class)

%.class: %.java
	$(JC) $(JFLAGS) *.java

run: classes
	$(JVM) $(MAIN)

clean:
	$(RM) *.class
