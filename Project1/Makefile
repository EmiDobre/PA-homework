
.PHONY: build clean

build: Nostory.class Semnale.class Sushi.class Feribot.class

run-p1:
	java Feribot
run-p2:
	java Nostory
run-p3:
	java Sushi
run-p4:
	java Semnale

# Nu uitați să modificați numele surselor.
Feribot.class: Feribot.java
	javac $^
Nostory.class: Nostory.java
	javac $^
Semnale.class: Semnale.java
	javac $^
Sushi.class: Sushi.java
	javac $^

# Vom șterge fișierele bytecode compilate.
clean:
	rm -f *.class
