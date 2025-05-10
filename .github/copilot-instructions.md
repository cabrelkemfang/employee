### Instructions optimisées et refactorisées

Voici une version simplifiée et sans duplications des instructions pour générer et expliquer du code, tout en respectant
les principes de Domain-Driven Design, SOLID, Clean Code et les bonnes pratiques de tests.

---

### 1. **Architecture DDD et Hexagonale**

- Séparez les couches : **API (controllers)**, **Application (services)**, **Domaine (entités, value objects, agrégats)
  **, **Infrastructure (repositories)**.
- Utilisez un **ubiquitous language** pour nommer les classes et packages en fonction du métier.
- Implémentez les **ports** (interfaces) pour les cas d’usage dans le domaine et les **adapters** (infrastructure) pour
  les interactions externes.
- Assurez l’indépendance du domaine vis-à-vis des frameworks ou détails d’infrastructure.

---

### 2. **Principes SOLID**

- Respectez les principes **SRP**, **OCP**, **LSP**, **ISP**, **DIP**.
- Favorisez l’injection de dépendances et la modularité.
- Utilisez des interfaces pour les abstractions et évitez les implémentations concrètes dans le domaine.

---

### 3. **Clean Code**

- Adoptez un **naming explicite**, des **méthodes courtes**, et évitez la duplication.
- Gérez les exceptions avec des messages clairs et un logging approprié.
- Préférez les types explicites aux types primitifs dans le domaine.

---

### 4. **Design Patterns**

- Appliquez les patterns adaptés (Factory, Strategy, Repository, Specification, etc.) et expliquez leur usage.

---

### 5. **Tests et Qualité**

- Respectez les principes **FIRST** : Fast, Isolated, Repeatable, Self-validating, Timely.
- Organiser les tests avec le modèle Given-When-Then
  Given : Préparez les données et les dépendances nécessaires.
  When : Exécutez la méthode ou le comportement à tester.
  Then : Vérifiez que les résultats sont conformes aux attentes.
- Fournissez des **tests unitaires** (JUnit 5 + Mockito) et des **tests d’intégration** si nécessaire.
- Préférez **AssertJ** pour des assertions lisibles et expressives :
  ```java
  assertThat(actual).isEqualTo(expected);
  assertThatThrownBy(() -> service.methodThatThrows())
      .isInstanceOf(ExpectedException.class)
      .hasMessage("Expected message");
  ```
- Testez les cas normaux, limites et les exceptions.
- Utilisez des outils comme **JaCoCo** pour mesurer la couverture.
- use displayname() to set the display name of the test method.
  . Exemple : `@DisplayName("should return employee when id is valid")`.
- coverture de plus de 90% de la classe
- test names should be descriptive and follow a consistent naming convention.
  - Exemple : `shouldReturnEmployeeWhenIdIsValid` pour un test qui vérifie le retour d'un employé valide.

---

### 6. **Documentation**

- Fournissez une documentation claire au format Markdown.
- Incluez des instructions d’installation, d’exécution et de test.
- Ajoutez des exemples d’utilisation et des cas d’erreur.

---

### 7. **Revue de Code**

- Vérifiez la lisibilité, la maintenabilité et la performance.
- Proposez des améliorations pour optimiser le code et respecter les normes de sécurité.
- Vérifiez la gestion des exceptions et la couverture des tests.

---

### 8. **Livrables**

- Structurez le projet selon les conventions Maven/Gradle (`src/main/java`, `src/test/java`).
- Ajoutez des **JavaDocs** pour les classes et méthodes publiques.

---

### 9. **Génération d’objets mock**

- Utilisez **Podam** pour générer des objets de test complexes sans écrire manuellement des builders ou des factories :
  ```java
  PodamFactory factory = new PodamFactoryImpl();
  var employee = factory.manufacturePojo(Employee.class);
  ```

Analyze the codebase and identify areas of improvement. Create a detailed list of actionable improvement tasks and save
it as an enumerated checklist to `docs/tasks.md`.
Each item should start with a placeholder [ ] to be checked off when completed. Ensure the tasks are logically ordered
and cover both architectural and code-level improvements.