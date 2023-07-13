![Logo.png](.readme%2FLogo.png)

# PayMyBuddy
PayMyBuddy est une application web de transfert d'argent.

## Installation

Ces instructions vous permettront d'obtenir une copie du projet sur votre machine locale à des fins de développement et
de test.

### Prérequis

* Installer <a target="_blank" href="https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html">Java
  17+</a>
* Installer <a target="_blank" href="https://maven.apache.org/download.cgi">Maven 3.8.1+</a>
* Installer <a target="_blank" href="https://developer.hashicorp.com/vault/tutorials/getting-started/getting-started-install#install-vault">Vault 1.14.0+</a>
* Installer <a target="_blank" href="https://dev.mysql.com/downloads/mysql/">MySQL 8.0.33+</a>

### Démarrage

1. Lancer MySQL et lancer le fichier `database.sql` pour initialiser la base de données.


2. Démarrage du serveur Vault :

```bash
vault server -dev --dev-root-token-id="00000000-0000-0000-0000-000000000000"
```

3. Ajout des secrets :

```bash
vault kv put secret/paymybuddy secretKey="secretValue"
```
**⚠️ Secrets requis :**
```
- jwt.secret
- spring.datasource.username
- spring.datasource.password
```

4. Démarrage du backend :

```bash
mvn spring-boot:run
```

5. Démarrage du <a href="https://github.com/NextSap/JAVA.oc-6.front">frontend<a/>