package br.projeto.blastin.joptional;

/**
 * @author lisboa.jeff@gmail.com < Jefferson Lisboa >
 * <p>
 * Predicado para objeto do tipo T
 */
public interface Predicado<T> {

    boolean teste(final T t);

}
