package br.projeto.blastin.joptional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class JOptionalTest {

    @Test
    void criarJOptionalComObjetoNulo() {

        Assertions
                .assertThrows(NullPointerException.class, () -> JOptional.de(null));

    }

    @Test
    void objetoComPredicadoFalso() {

        Assertions
                .assertTrue(JOptional.de(1).filtro(integer -> integer > 2).vazio());

    }

    @Test
    void objetoComPredicadoVerdadeiro() {

        Assertions
                .assertTrue(JOptional.de(10).filtro(integer -> integer > 5).presente());

    }

    @Test
    void inteiroParaDouble() {

        Assertions
                .assertEquals(Double.class, JOptional.de(0).mapeado(integer -> integer + 0.0).obter().getClass());

    }

    @Test
    void mapeandoDeObjetoNulo() {

        Assertions
                .assertThrows(NoSuchElementException.class, JOptional.dePossivelNulo(null).mapeado(o -> "")::obter);

    }

    @Test
    void gerandoExcessao() {

        Assertions
                .assertThrows(RuntimeException.class, () -> JOptional.dePossivelNulo(null).ouExcessao(RuntimeException::new));

    }

    @Test
    void ignorandoExcessao() {

        Assertions
                .assertDoesNotThrow(() -> JOptional.de("123s").ouExcessao(RuntimeException::new));

    }

    @Test
    void objetoPresente() {

        final AtomicBoolean status = new AtomicBoolean(false);

        JOptional
                .de(1)
                .sePresente(integer -> status.set(true));

        Assertions
                .assertTrue(status.get());

    }

    @Test
    void objetoNaoPresente() {

        final AtomicBoolean status = new AtomicBoolean(false);

        JOptional
                .dePossivelNulo(null)
                .sePresente(integer -> status.set(true));

        Assertions
                .assertFalse(status.get());

    }

    @Test
    void seNaoPresente() {

        final AtomicBoolean status = new AtomicBoolean(false);

        JOptional
                .nulo()
                .seNaoPresente(() -> status.set(true))
                .sePresente(o -> status.set(false));

        Assertions
                .assertTrue(status.get());

    }

    @Test
    void sePresente() {

        final AtomicBoolean status = new AtomicBoolean(false);

        final AtomicInteger valor = new AtomicInteger(0);

        JOptional
                .de(1)
                .seNaoPresente(() -> status.set(true))
                .sePresente(integer -> valor.incrementAndGet());

        Assertions
                .assertFalse(status.get());

        Assertions
                .assertEquals(1, valor.get());

    }

    @Test
    void ouNecessario() {

        final int valor = -1;

        Assertions
                .assertEquals(valor, JOptional.nulo().ou(valor));

    }

    @Test
    void ouNaoNecessario() {

        final int valor = 1;

        Assertions
                .assertEquals(valor, JOptional.de(valor).ou(-1));

    }

    @Test
    void complexo() {

        final AtomicInteger valor = new AtomicInteger(0);

        final Integer saida =

                JOptional
                        .de(200)
                        .filtro(numero -> numero < 2000)
                        .seNaoPresente(valor::incrementAndGet)
                        .mapeado(String::valueOf)
                        .filtro(s -> s.startsWith("2"))
                        .seNaoPresente(valor::incrementAndGet)
                        .mapeado(s -> s.replace("0", "1"))
                        .mapeado(Integer::parseInt)
                        .ouExcessao(NoSuchElementException::new)
                        .obter();

        Assertions
                .assertEquals(211, saida);

        Assertions
                .assertEquals(0, valor.get());

    }
}