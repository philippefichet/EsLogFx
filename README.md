# ESLogFX

## Lancement

java -Deslogfx.configfile=/chemin/de/la/configuration -jar EsLogFx-1.0-SNAPSHOT.jar

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
    "dateFormat": "timestamp"  // optionnel
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