![Logo.png](.readme%2FLogo.png)

BACKEND | <a target="_blank" href="https://github.com/NextSap/java.oc-6.front">FRONTEND</a>

# PayMyBuddy
PayMyBuddy est une application web de transfert d'argent.

## Installation

Ces instructions vous permettront d'obtenir une copie du projet sur votre machine locale à des fins de développement et
de test.

### Prérequis

* Installer <a target="_blank" href="https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html">Java
  17+</a>
* Installer <a target="_blank" href="https://maven.apache.org/download.cgi">Maven 3.8.1+</a>
* Installer <a target="_blank" href="https://docs.docker.com/get-docker/">Docker</a>
  et <a target="_blank" href="https://docs.docker.com/compose/install/">Docker Compose</a>

### <p id="starting">Démarrage</p>
Démarrage de l'outil de gestion du projet (<a href="https://github.com/NextSap/ProjectManager/tree/java.oc-6">voir les sources</a>) :

```bash
java -jar Manager.jar true # true indique au programme que vous utilisez une machine Windows, ne mettez aucun argument si vous utilisez une machine MacOS ou Linux
```
Grâce à cet outil, vous pouvez :
- Créer, lancer, éteindre et supprimer les containers Docker de l'application
- Gérer les secrets de l'application de manière sécurisée
- Générer et lancer la documentation Swagger de l'API

Lancement du frontend :  
```bash
cd JAVA.oc-6.front
npm install
npm run dev
```

## Documentation

### Swagger

cf. <a href="#starting">Démarrage</a>

### Postman

Vous pouvez également importer la collection Postman qui correspond à cette API afin d'effectuer les requêtes pour facilement (cf. <a target="_blank" href="https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman">Postman - Importing data into Postman</a>)
```
cd .postman/
```