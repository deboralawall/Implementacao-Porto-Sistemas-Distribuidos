# Configurações de ambiente do LabP2D
JAVA=/usr/local/jdk1.8.0_131/bin/java
JAVAC=/usr/local/jdk1.8.0_131/bin/javac
JFLAGS=-g
SRCDIR=src
BINDIR=bin

# Diretório de bibliotecas exibido no slide do professor
JAR_DIR=/usr/local/rabbitmq-jar
RABBIT_JAR=$(JAR_DIR)/amqp-client-4.0.2.jar
SLF4J_API=$(JAR_DIR)/slf4j-api-1.7.21.jar
SLF4J_SIMPLE=$(JAR_DIR)/slf4j-simple-1.7.22.jar

# Junção dos classpaths para compilação e execução
COMP_CP=.:$(RABBIT_JAR):$(SLF4J_API)
RUN_CP=$(BINDIR):$(RABBIT_JAR):$(SLF4J_API):$(SLF4J_SIMPLE)

.PHONY: all rmi run-rmi-server run-queue-service clean

all: rmi

# Compila os arquivos vinculando os pacotes do /usr/local/
rmi: $(BINDIR)
	$(JAVAC) $(JFLAGS) -cp $(COMP_CP) -d $(BINDIR) $(SRCDIR)/*.java

$(BINDIR):
	mkdir -p $(BINDIR)

run-rmi-server: rmi
	$(JAVA) -cp $(BINDIR) PortoServer

run-queue-service: rmi
	$(JAVA) -cp $(RUN_CP) ServicoFilas $(ARGS)

clean:
	rm -rf $(BINDIR)