# Polynames

## Avant-propos

Je suis une petite licorne très très spéciale qui ne veut rien faire comme tout le monde. Aussi j'ai utilisé docker pour faire tourner mon instance de xampp. Et vu que je suis sous Fedora, j'ai même pas pris la peine d'installer Docker, j'utilise Podman qui est préinstallé. Enfin, des fois j'utilise Windows du coup c'est avec Docker, bref.


## PREPARATION

Pour lancer sur docker/podman

```
docker compose up
```

Pour pas être embêté du fait de l'origine de la connexion à la base :

```sql
GRANT ALL ON *.* to "root"@"%" IDENTIFIED BY 'password';
```

Maintenant on peut commencer.