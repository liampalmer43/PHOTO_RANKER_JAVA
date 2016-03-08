JFLAGS = -g
JC = javac
JVM= java
FILE=
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
    Main.java \
    ImageView.java \
    ImageCollectionView.java \
    ImageModel.java \
    ImageCollectionModel.java \
    Toolbar.java

MAIN = Main

default: classes

classes: $(CLASSES:.java=.class)

run: classes
	$(JVM) $(MAIN)

clean:
	$(RM) *.class

