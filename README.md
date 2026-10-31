# TP 2 – Les sockets en mode connecté TCP : E/R de chaînes de caractères et d'objets

**Faculté des Sciences de Sfax – Département d'informatique et des Communications**
**Matière :** Développement d'applications réparties – LSI 3 (2026 – 2027)
**Enseignante des TPs :** Zeineb DAOUD
**Étudiante :** Samar GHLISSI

---

## 1. Objectifs

- Échanger des **chaînes de caractères** entre un client et un serveur avec les sockets TCP (Activité 2-1).
- Échanger des **objets Java sérialisés** entre un client et un serveur (Activité 2-2).
- Manipuler les flux du package `java.io` et l'interface `Serializable`.

**Environnement :** JDK 21, Eclipse IDE for Java Developers, Git / GitHub.

---

## 2. Structure du projet

```
TP2/
├── README.md
├── captures                        # captures d'écran
└── src/
    ├── chaines/                    # Activité 2-1
    │   ├── Serveur.java
    │   └── Client.java
    └── objets/                     # Activité 2-2
        ├── Operation.java          
        ├── Serveurobjet.java
        └── Clientobjet.java
```

Le code est organisé en deux **packages** : `chaines` et `objets`. Le port utilisé est **1234** (`localhost`).

---

## 3. Activité 2-1 : Échange de chaînes de caractères (CC)

### Problème
Dans le TP1 (Activité 1-2), client et serveur n'échangeaient qu'un seul octet : impossible d'envoyer une opération complète comme `55 * 25`.

### Solution
| Étape | Réalisation |
|---|---|
| a | Le client envoie 3 chaînes : opérande 1, opérateur, opérande 2 |
| b | Le serveur calcule l'opération (`+`, `-`, `*`, `/`) |
| c | Le serveur envoie le résultat au client |
| d | Le client lit et affiche le résultat |

- **Validation de la syntaxe côté client** (expression régulière `nombre opérateur nombre`) : les saisies incorrectes comme `25 ** 55` ou `abc + 3` ne sont jamais envoyées.
- Gestion de la **division par zéro** et d'un opérateur inconnu côté serveur.
- `exit` côté client envoie `FIN` et ferme la connexion.
- Le serveur traite chaque client dans un **thread** séparé.

### Classes `java.io` utilisées
`InputStreamReader`, `BufferedReader`, `OutputStreamWriter`, `PrintWriter`.

### Exécution
1. Lancer **`chaines.Serveur`** (Run As → Java Application).
2. Lancer **`chaines.Client`**, puis taper une opération dans la console.



### Exemple
```
> 55 * 25
Résultat : 1375
> 25 ** 55
Syntaxe incorrecte. Format attendu : <nombre> <+|-|*|/> <nombre>
> 10 / 0
Résultat : Erreur : division par zéro
> exit
```

### Captures d'écran
- Espace de travail et packages : `![](images/workspace.png)`
- Exécution (consoles serveur et client) : `![] (images/execution1.png)`

---

## 4. Activité 2-2 : Échange d'objets

### Principe
Au lieu d'envoyer trois chaînes, le client envoie **un seul objet** `Operation` qui contient toutes les données de l'opération.

| Étape | Réalisation |
|---|---|
| a | Classe `Operation implements Serializable` (opérande 1, opérateur, opérande 2, `serialVersionUID`) |
| b | Le client envoie l'objet avec `ObjectOutputStream.writeObject()` |
| c | Le serveur reçoit l'objet avec `ObjectInputStream.readObject()` |
| d | Le serveur récupère les données via les getters |
| e | Le serveur effectue le calcul demandé |
| f | Le serveur renvoie le résultat |
| g | Le client lit et affiche le résultat |

> **Remarque :** de chaque côté, `ObjectOutputStream` est créé et vidé (`flush`) **avant** `ObjectInputStream`. Sinon les deux programmes restent bloqués en attendant l'en-tête du flux.

### Exécution
1. Lancer **`objets.Serveurobjet`**.
2. Lancer **`objets.Clientobjet`**, puis taper une opération.




### Captures d'écran

- Exécution (consoles serveur et client) : `![](images/execution2.png)`

---

## 5. Comparaison des deux approches

| | Activité 2-1 (CC) | Activité 2-2 (objets) |
|---|---|---|
| Flux | `BufferedReader` / `PrintWriter` | `ObjectInputStream` / `ObjectOutputStream` |
| Données envoyées | 3 lignes de texte | 1 objet `Operation` |
| Conversion côté serveur | `Double.parseDouble` sur chaque chaîne | Aucune, les types sont déjà corrects |
| Prérequis | Aucun | Classe `Serializable` partagée par client et serveur |

---

## 6. Problèmes rencontrés

- **`Address already in use: bind`** : un ancien serveur tournait encore sur le port 1234. Solution : l'arrêter avec le carré rouge de la console Eclipse, ou changer de port dans le client et le serveur.
- **Blocage client/serveur en mode objets** : créer `ObjectOutputStream` avant `ObjectInputStream` et appeler `flush()`.

---



**Lien du repository :** `<à compléter>`
