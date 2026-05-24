# GNU Makefile for Parte1
JAVA?=java
JAVAC?=javac
JAVA8?=$(JAVA)
JAVAC8?=$(if $(filter %/java,$(JAVA8)),$(patsubst %/java,%/javac,$(JAVA8)),$(JAVAC))
JFLAGS=-g
SRCDIR=src
BINDIR=bin

.PHONY: all rmi soap run-rmi-server run-rmi-client run-soap-server run-soap-client clean

all: rmi soap

rmi: $(BINDIR)
	$(JAVAC) $(JFLAGS) -d $(BINDIR) $(SRCDIR)/*.java

soap: $(BINDIR)
	$(JAVAC8) $(JFLAGS) -d $(BINDIR) $(SRCDIR)/porto/*.java

$(BINDIR):
	mkdir -p $(BINDIR)

run-rmi-server: rmi
	$(JAVA) -cp $(BINDIR) PortoServer

run-rmi-client: rmi
	$(JAVA) -cp $(BINDIR) DocaClient $(ARGS)

run-soap-server: soap
	$(JAVA8) -cp $(BINDIR) porto.PortoServerPublisher $(ARGS)

run-soap-client: soap
	$(JAVA8) -cp $(BINDIR) porto.PortoClient $(ARGS)

clean:
	rm -rf $(BINDIR)
