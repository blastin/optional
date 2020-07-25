package br.projeto.blastin.joptional;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @author Jefferson Lisboa <lisboa.jeff@gmail.com>
 * <p>
 * @implNote JOptonal é uma releitura da biblioteca Optional,
 * o qual inclui algumas novas operações
 */
public final class JOptional<J> {

    private static final JOptional<Object> JOPTIONAL_NULO = new JOptional<>(null);

    public static <J> JOptional<J> de(final J j) {
        return new JOptional<>(Objects.requireNonNull(j));
    }

    public static <J> JOptional<J> dePossivelNulo(final J j) {
        return new JOptional<>(j);
    }

    @SuppressWarnings("unchecked")
    public static <J> JOptional<J> nulo() {
        return (JOptional<J>) JOPTIONAL_NULO;
    }

    private JOptional(final J j) {
        this.j = j;
    }

    private final J j;

    public JOptional<J> filtro(final Predicado<? super J> predicado) {

        Objects.requireNonNull(predicado);

        if (presente() && predicado.teste(j)) {
            return this;
        }

        return nulo();

    }

    public boolean presente() {

        return j != null;

    }

    public <S> JOptional<S> mapeado(final Funcao<? super J, ? extends S> funcao) {

        Objects.requireNonNull(funcao);

        if (presente()) {
            return JOptional.dePossivelNulo(funcao.aplicar(j));
        }

        return nulo();

    }

    public boolean vazio() {
        return !presente();
    }

    public <T extends Throwable> JOptional<J> ouExcessao(final Provedor<? extends T> provedor) throws T {

        Objects.requireNonNull(provedor);

        if (vazio()) throw provedor.prover();

        return this;

    }

    public J obter() {

        if (vazio()) throw new NoSuchElementException();

        return j;

    }

    public void sePresente(final Consumidor<? super J> consumidor) {

        Objects.requireNonNull(consumidor);

        if (presente()) consumidor.consumir(j);

    }

    public JOptional<J> seNaoPresente(final Acao acao) {

        Objects.requireNonNull(acao);

        if (vazio()) acao.executar();

        return this;

    }

    public J ou(final J j) {

        if (vazio()) return j;

        return this.j;

    }

}
