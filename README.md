# Dental Management — Version Académique (Java Standard & Swing)

Système de gestion de cabinet dentaire réalisé dans le cadre du projet académique de Java.

## 🎯 Contraintes & Spécifications du Projet

- **Langage** : Java Standard (JDK 17/21 compatible, syntaxe Java standard)
- **Interface Graphique** : **Java Swing pur** (aucun framework tiers type Spring, JavaFX, etc.)
- **Persistance** : Base de données relationnelle **SQLite** via **JDBC standard** (`PreparedStatement`)
- **Architecture** : Modèle-Vue-Contrôleur/DAO propre et modulaire
- **Sécurité** : Protection stricte contre les injections SQL via requêtes paramétrées, hachage sécurisé des mots de passe (SHA-256 + sel).

---

## 📚 Feuille de Route Pédagogique (Progression Commit par Commit)

Le projet est structuré pas à pas pour illustrer chaque concept clé du cours de Java :

1. **Step 0 — Initialisation & Interface Swing de Base** : Squelette du projet, fenêtre de connexion Swing, utilitaires graphiques.
2. **Step 1 — Héritage (`extends`)** : Classe de base `User` avec ses sous-classes spécialisées `Dentiste` et `Assistant`.
3. **Step 2 — Classes Abstraites (`abstract`)** : Rendre `User` abstraite avec les méthodes `decrireRole()` et `aPrivilege(String)` polymorphiques.
4. **Step 3 — Exceptions Personnalisées** : Hiérarchie d'exceptions métier (`AuthentificationException`, `ValidationException`...) et blocs `try-catch-finally`.
5. **Step 4 — Persistance SQLite & JDBC** : `DatabaseManager`, requêtes sécurisées avec `PreparedStatement`, initialisation automatique des tables `users` et `patients`.
6. **Step 5 — Interfaces Fonctionnelles Personnalisées** : Interface `@FunctionalInterface Validateur<T>` avec méthode par défaut `andThen` pour la validation des formulaires.
7. **Step 6 — Interfaces Fonctionnelles Pré-définies (`java.util.function`)** : Utilisation de `Predicate<T>`, `Function<T, R>`, `Consumer<T>`, `Supplier<T>` pour le filtrage et les statistiques.
8. **Step 7 — Expressions Lambda & Références de Méthodes** : Expressions lambda `->`, références de méthodes `::`, et tris avec `Comparator`.
9. **Step 8 — Sécurité (@securly), Audit Clean Code & Finalisation**.

---

## 🚀 Compilation et Exécution

### Pré-requis
- Java Development Kit (JDK 17 ou supérieur)

### Ligne de commande :
```bash
# Compilation
javac -encoding UTF-8 -cp "lib/sqlite-jdbc-3.45.1.0.jar;src" -d bin src/dental/*.java src/dental/*/*.java

# Exécution
java -cp "bin;lib/sqlite-jdbc-3.45.1.0.jar" dental.Main
```

Ou double-cliquez directement sur :
- `compile.bat` pour compiler
- `run.bat` pour lancer
