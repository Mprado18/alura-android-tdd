package br.com.alura.leilao.ui.activity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static br.com.alura.leilao.matchers.ViewMatcher.apareceLeilaoNaPosicao;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.utils.BaseTestIntegration;
import br.com.alura.leilao.R;
import br.com.alura.leilao.model.Leilao;

public class ListaLeilaoScreenTest extends BaseTestIntegration {

    @Rule
    public ActivityTestRule<ListaLeilaoActivity> activity = new ActivityTestRule<>(
            ListaLeilaoActivity.class,
            true,
            false
    );

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

    @Test
    public void deve_AparecerUltimoLeilao_QuandoCarregarDezLeiloesDaApi() throws IOException {
        tentaSalvarLeilaoNaApi(
                new Leilao("Console"),
                new Leilao("Computador"),
                new Leilao("TV"),
                new Leilao("Carro"),
                new Leilao("Cama"),
                new Leilao("Apartamento"),
                new Leilao("Smartphone"),
                new Leilao("Notebook"),
                new Leilao("Macbook"),
                new Leilao("Harley Davidson")
        );

        activity.launchActivity(new Intent());

        onView(withId(R.id.lista_leilao_recyclerview))
                .perform(RecyclerViewActions.scrollToPosition(9))
                .check(matches(apareceLeilaoNaPosicao(
                        9,
                        "Harley Davidson",
                        0.00)
                ));
    }

}