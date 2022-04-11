package br.com.alura.leilao.ui.activity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static br.com.alura.leilao.matchers.ViewMatcher.apareceLeilaoNaPosicao;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.R;
import br.com.alura.leilao.api.retrofit.client.TesteWebClient;
import br.com.alura.leilao.model.Leilao;

public class ListaLeilaoScreenTest {

    private static final String ERRO_FALHA_LIMPAZA_BD = "Banco de dados não foi limpo!";
    private static final String ERRO_LEILAO_NAO_FOI_SALVO = "Leilão não foi salvo: ";

    @Rule
    public ActivityTestRule<ListaLeilaoActivity> activity = new ActivityTestRule<>(
            ListaLeilaoActivity.class,
            true,
            false
    );
    private final TesteWebClient webClient = new TesteWebClient();

    @Before
    public void setup() throws IOException {
        limpaBancoDadosApi();
    }

    //@After
    //Pós execução para retornar tudo ao padrão de antes (tearDown)

    @Test
    public void deve_AparecerUmLeilao_QuandoCarregarUmLeilaoNaAPI() throws IOException {
        tentaSalvarLeilaoNaApi(new Leilao("Console"));

        activity.launchActivity(new Intent());

        onView(withId(R.id.lista_leilao_recyclerview))
                .check(matches(apareceLeilaoNaPosicao(
                        0,
                        "Console",
                        0.00)
                ));
    }

    @Test
    public void deve_AaparecerDoisLeiloes_QuandoCarregarDoisLeiloisDaApi() throws IOException {
        tentaSalvarLeilaoNaApi(
                new Leilao("Console"),
                new Leilao("Computador")
        );

        activity.launchActivity(new Intent());

        onView(withId(R.id.lista_leilao_recyclerview))
                .check(matches(apareceLeilaoNaPosicao(
                        0,
                        "Console",
                        0.00)
                ));
        onView(withId(R.id.lista_leilao_recyclerview))
                .check(matches(apareceLeilaoNaPosicao(
                        1,
                        "Computador",
                        0.00)
                ));
    }

    private void limpaBancoDadosApi() throws IOException {
        boolean bancoDeDadosNaoFoiLimpo = !webClient.limpaBancoDeDados();
        if (bancoDeDadosNaoFoiLimpo) {
            Assert.fail(ERRO_FALHA_LIMPAZA_BD);
        }
    }

    private void tentaSalvarLeilaoNaApi(Leilao... leiloes) throws IOException {
        for (Leilao leilao : leiloes) {
            Leilao leilaoSalvo = webClient.salva(leilao);
            if (leilaoSalvo == null) {
                Assert.fail(ERRO_LEILAO_NAO_FOI_SALVO + leilao.getDescricao());
            }
        }
    }

}