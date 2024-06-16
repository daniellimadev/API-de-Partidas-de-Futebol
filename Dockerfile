# Usando a imagem oficial do Maven para construir o projeto
FROM maven:3.9-amazoncorretto-21

# Definindo o diretório de trabalho no contêiner
WORKDIR /app

# Copiando o arquivo pom.xml e outros arquivos de configuração para o contêiner
COPY pom.xml .

# Baixando as dependências do projeto
RUN mvn dependency:go-offline

# Copiando o restante do código do projeto
COPY src ./src

# Construindo o projeto
RUN mvn package -DskipTests

# Usando a imagem oficial do OpenJDK para rodar a aplicação
FROM openjdk:21

# Definindo o diretório de trabalho no contêiner
WORKDIR /app

# Copiando o artefato construído do estágio anterior
COPY target/FutebolAPI-0.0.1-SNAPSHOT.jar app.jar

# Expondo a porta que a aplicação vai rodar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
