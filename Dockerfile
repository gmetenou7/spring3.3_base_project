# Utilisez une image de base contenant Maven et JDK
FROM maven:3.8.4-openjdk-17 AS build

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier les fichiers de projet Maven dans le conteneur
COPY pom.xml .

# Télécharger les dépendances de Maven (pour les mettre en cache)
RUN mvn dependency:go-offline -B

# Copier le reste des fichiers du projet
COPY src ./src

# Construire l'application
RUN mvn -f pom.xml clean package install -DskipTests -Pprod

# Utiliser une image plus légère pour l'exécution
FROM openjdk:17-jdk-alpine AS run

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le jar construit depuis l'étape précédente
COPY --from=build /app/target/*.jar app.jar

# Exposer le port sur lequel l'application fonctionne
EXPOSE 8080

# Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "app.jar"]