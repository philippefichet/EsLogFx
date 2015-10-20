# ESLogFX
[![Stories in Ready](https://badge.waffle.io/philippefichet/EsLogFx.svg?label=ready&title=Ready)](http://waffle.io/philippefichet/EsLogFx) [![Build Status](https://travis-ci.org/philippefichet/EsLogFx.svg?branch=master)](https://travis-ci.org/philippefichet/EsLogFx)

Interface de visualisation des logs dans ElasticSearch

Java 8+ requis et JavaFX 8

## Compilation

```shell
mvn package
```

## Lancement

```shell
java -Deslogfx.configfile=/chemin/de/la/configuration -Deslogfx.cssfile=/chemin/de/la/feuille/de/style -jar target/EsLogFx-1.0-SNAPSHOT.jar
```

## Fichier de configuration : 
```javascript
{
    "url": "https://localhost:9200/elasticsearchindex/",
    "authScope": "localhost", // optionnel
    "login": "identifiant", // optionnel
    "password": "mot_de_pass", // optionnel
    "fieldDate": "timestamp", // optionnel
    "fieldMessage": "msg", // optionnel
    "fieldMessageNumberLine" : 2, // optionnel
    "dateFormat": "timestamp", // optionnel
    "dateFormatShow": "yyyy-MM-dd HH:mm:ss", // optionnel
    "fieldExclude": [  // optionnel
        "date"
    ]
}

```

- url : adresse complete de l'index elasticsearch contenant les logs (obligatoire)
- authScope domaine pour autentification http basic, si manquant pas d'autorisation http
- login login pour autentification http basic, si manquant pas d'autorisation http
- password mot de passe pour autentification http basic, si manquant pas d'autorisation http
- fieldDate nom du champs contenant la date du logs, si manquant recherche du champs de date par analyse du format de date "dateFormat"
- fieldMessage nom du champs contenant le message du log, "message" par défaut
- fieldMessageNumberLine nombre de ligne afficher dans la liste des logs du message, 1 par défaut
- dateFormat format de la date, "yyyy-MM-dd'T'HH:mm:ss.SSSZZ" par défaut (voir http://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html)
- dateFormatShow Format de la date à afficher "yyyy-MM-dd'T'HH:mm:ss.SSSZZ" par défaut (voir http://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html)
- fieldExclude Liste des colonnes qui ne seront pas afficher

## JavaFx Options
- -Dprism.lcdtext=false permet de désactiver la gestion du texte avec sous pixel, peut améliorer la lisibilité
