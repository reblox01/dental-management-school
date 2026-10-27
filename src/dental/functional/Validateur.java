package dental.functional;

import dental.exception.ValidationException;

/**
 * Interface fonctionnelle personnalisée (@FunctionalInterface).
 *
 * Définition pédagogique :
 * Une interface fonctionnelle possède exactement UNE seule méthode abstraite (Single Abstract Method - SAM).
 * Elle peut également contenir des méthodes par défaut ('default') et des méthodes statiques ('static').
 *
 * @param <T> le type de la valeur à valider
 */
@FunctionalInterface
public interface Validateur<T> {

    /**
     * Méthode abstraite unique (SAM) :
     * Valide la donnée fournie et lève une ValidationException si la contrainte est violée.
     *
     * @param valeur la donnée à contrôler
     * @throws ValidationException si la validation échoue
     */
    void valider(T valeur) throws ValidationException;

    /**
     * Méthode par défaut (default method) :
     * Permet la composition et le chaînage fluide de plusieurs validateurs
     * selon le patron de conception "Chain of Responsibility".
     *
     * @param autre le validateur suivant à exécuter
     * @return un nouveau validateur combiné exécutant 'this' puis 'autre'
     */
    default Validateur<T> andThen(Validateur<T> autre) {
        if (autre == null) {
            return this;
        }
        return valeur -> {
            this.valider(valeur);
            autre.valider(valeur);
        };
    }
}
