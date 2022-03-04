package br.com.alura.leilao.model;

import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import br.com.alura.leilao.builder.LeilaoBuilder;
import br.com.alura.leilao.exception.LanceMenorQueUltimoLanceException;
import br.com.alura.leilao.exception.LanceSeguidoMesmoUserException;
import br.com.alura.leilao.exception.MaximoCincoLancesPorUserException;

public class LeilaoTest {

    public static final double DELTA = 0.00001;
    private final Leilao CONSOLE = new Leilao("Console");
    private final Usuario MARCOS = new Usuario("Marcos");

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void deve_DevolverUmaDescricao_QuandoReceberUmaDescricao() {
        //executar ação esperada
        String descricaoDevolvida = CONSOLE.getDescricao();

        //testar resultado esperado
//        assertEquals("Console", descricaoDevolvida);
        assertThat(descricaoDevolvida, is("Console"));
    }

    @Test
    public void deve_DevolverMaiorLance_QuandoRecebeApenasUmLance() {
        CONSOLE.propoe(new Lance(MARCOS, 200.0));

        double maiorLanceDevolvido = CONSOLE.getMaiorLance();
//        assertEquals(200.0, maiorLanceDevolvido, DELTA);
        assertThat(maiorLanceDevolvido, closeTo(200.0, DELTA));
    }

    @Test
    public void deve_DevolverMaiorLance_QuandoRecebeMaisUmDeLance_EmOrdemCrescente() {
        CONSOLE.propoe(new Lance(MARCOS, 100.0));
        CONSOLE.propoe(new Lance(new Usuario("Angie"), 200.0));

        double maiorLanceDevolvido = CONSOLE.getMaiorLance();
        assertEquals(200.0, maiorLanceDevolvido, DELTA);
    }

    @Test
    public void deve_DevolverMenorLance_QuandoRecebeApenasUmLance() {
        CONSOLE.propoe(new Lance(MARCOS, 200.0));

        double menorLanceDevolvido = CONSOLE.getMenorLance();
        assertEquals(200.0, menorLanceDevolvido, DELTA);
    }

    @Test
    public void deve_DevolverMenorLance_QuandoRecebeMaisUmDeLance_EmOrdemCrescente() {
        CONSOLE.propoe(new Lance(MARCOS, 100.0));
        CONSOLE.propoe(new Lance(new Usuario("Angie"), 200.0));

        double menorLanceDevolvido = CONSOLE.getMenorLance();
        assertEquals(100.0, menorLanceDevolvido, DELTA);
    }

    @Test
    public void deve_DevolverTresMaioresLances_QuandoRecebeExatosTresLances() {
        CONSOLE.propoe(new Lance(MARCOS, 200.0));
        CONSOLE.propoe(new Lance(new Usuario("Angie"), 300.0));
        CONSOLE.propoe(new Lance(MARCOS, 400.0));

        List<Lance> tresMaioresLancesDevolvidos = CONSOLE.tresMaioresLances();
//        assertEquals(3, tresMaioresLancesDevolvidos.size());

        //assert que verifica se contém 3 itens na lista
//        assertThat(tresMaioresLancesDevolvidos, hasSize(3));

//        assertEquals(400.0, tresMaioresLancesDevolvidos.get(0).getValor(), DELTA);

        //assert que verifica se contém item na lista
//        assertThat(tresMaioresLancesDevolvidos, hasItem(new Lance(MARCOS, 400.0)));

//        assertEquals(300.0, tresMaioresLancesDevolvidos.get(1).getValor(), DELTA);
//        assertEquals(200.0, tresMaioresLancesDevolvidos.get(2).getValor(), DELTA);

        //assert que verifica se contém todos os lances na lista
//        assertThat(tresMaioresLancesDevolvidos, contains(
//                new Lance(MARCOS, 400.0),
//                new Lance(new Usuario("Angie"), 300.0),
//                new Lance(MARCOS, 200.0)
//        ));

        //assert que verifica qtd de lances e se todos contém todos os lances na lista
        assertThat(tresMaioresLancesDevolvidos, both(Matchers.<Lance>hasSize(3)).and(
                contains(
                        new Lance(MARCOS, 400.0),
                        new Lance(new Usuario("Angie"), 300.0),
                        new Lance(MARCOS, 200.0)
                )
        ));
    }

    @Test
    public void deve_DevolverTresMaioresLances_QuandoNaoRecebeLances() {
        List<Lance> tresMaioresLancesDevolvidos = CONSOLE.tresMaioresLances();

        assertEquals(0, tresMaioresLancesDevolvidos.size());
    }

    @Test
    public void deve_DevolverTresMaioresLances_QuandoRecebeUmLance() {
        CONSOLE.propoe(new Lance(MARCOS, 200.0));

        List<Lance> tresMaioresLancesDevolvidos = CONSOLE.tresMaioresLances();
        assertEquals(1, tresMaioresLancesDevolvidos.size());
        assertEquals(200.0, tresMaioresLancesDevolvidos.get(0).getValor(), DELTA);
    }

    @Test
    public void deve_DevolverTresMaioresLances_QuandoRecebeDoisLances() {
        CONSOLE.propoe(new Lance(MARCOS, 300.0));
        CONSOLE.propoe(new Lance(new Usuario("Angie"), 500.0));

        List<Lance> tresMaioresLancesDevolvidos = CONSOLE.tresMaioresLances();
        assertEquals(2, tresMaioresLancesDevolvidos.size());
        assertEquals(500.0, tresMaioresLancesDevolvidos.get(0).getValor(), DELTA);
        assertEquals(300.0, tresMaioresLancesDevolvidos.get(1).getValor(), DELTA);
    }

    @Test
    public void deve_DevolverTresMaioresLances_QuandoRecebeMaisDeTresLances() {
        final Usuario ANGIE = new Usuario("Angie");
        CONSOLE.propoe(new Lance(MARCOS, 300.0));
        CONSOLE.propoe(new Lance(ANGIE, 400.0));
        CONSOLE.propoe(new Lance(MARCOS, 500.0));
        CONSOLE.propoe(new Lance(ANGIE, 600.0));

        final List<Lance> tresMaioresLancesDevolvidosParaQuatroLances = CONSOLE.tresMaioresLances();
        assertEquals(3, tresMaioresLancesDevolvidosParaQuatroLances.size());
        assertEquals(600.0, tresMaioresLancesDevolvidosParaQuatroLances.get(0).getValor(), DELTA);
        assertEquals(500.0, tresMaioresLancesDevolvidosParaQuatroLances.get(1).getValor(), DELTA);
        assertEquals(400.0, tresMaioresLancesDevolvidosParaQuatroLances.get(2).getValor(), DELTA);

        CONSOLE.propoe(new Lance(MARCOS, 700.0));
        final List<Lance> tresMaioresLancesDevolvidosParaCincoLances = CONSOLE.tresMaioresLances();
        assertEquals(3, tresMaioresLancesDevolvidosParaCincoLances.size());
        assertEquals(700.0, tresMaioresLancesDevolvidosParaCincoLances.get(0).getValor(), DELTA);
        assertEquals(600.0, tresMaioresLancesDevolvidosParaCincoLances.get(1).getValor(), DELTA);
        assertEquals(500.0, tresMaioresLancesDevolvidosParaCincoLances.get(2).getValor(), DELTA);
    }

    @Test
    public void deve_DevolverZero_ParaMaiorLance_QuandoNaoHouverLances() {
        double maiorLanceDevolvido = CONSOLE.getMaiorLance();

        assertEquals(0.0, maiorLanceDevolvido, DELTA);
    }

    @Test
    public void deve_DevolverZero_ParaMenorLance_QuandoNaoHouverLances() {
        double menorLanceDevolvido = CONSOLE.getMenorLance();

        assertEquals(0.0, menorLanceDevolvido, DELTA);
    }

    @Test(expected = LanceMenorQueUltimoLanceException.class)
    public void naoDeve_DevolverAdicionarLance_QuandoForMenorQueMaiorLance() {
        CONSOLE.propoe(new Lance(MARCOS, 500.0));
        CONSOLE.propoe(new Lance(MARCOS, 400.0));
    }

    @Test(expected = LanceSeguidoMesmoUserException.class)
    public void nao_DeveAdicionarLance_QuandoForOMesmoUsuarioDoUltimoLance() {
        CONSOLE.propoe(new Lance(MARCOS, 500.0));
        CONSOLE.propoe(new Lance(MARCOS, 600.0));
    }

    @Test
    public void naoDeve_AdicionarLance_QuandoUsuarioDerCincoLances() {
        final Usuario ANGIE = new Usuario("Angie");
        final Leilao console = new LeilaoBuilder("Console")
                .lance(MARCOS, 100.0)
                .lance(ANGIE, 200.0)
                .lance(MARCOS, 300.0)
                .lance(ANGIE, 400.0)
                .lance(MARCOS, 500.0)
                .lance(ANGIE, 600.0)
                .lance(MARCOS, 700.0)
                .lance(ANGIE, 800.0)
                .lance(MARCOS, 900.0)
                .lance(ANGIE, 1000.0)
                .build();
        try {
            console.quantidadeDeLances();
        } catch (RuntimeException exceptions) {
            exception.expect(MaximoCincoLancesPorUserException.class);
        }
    }
}