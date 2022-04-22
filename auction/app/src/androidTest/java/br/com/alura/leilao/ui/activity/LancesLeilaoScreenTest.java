package br.com.alura.leilao.ui.activity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.database.dao.UsuarioDAO;
import br.com.alura.leilao.utils.BaseTestIntegration;
import br.com.alura.leilao.R;
import br.com.alura.leilao.formatter.FormatadorDeMoeda;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

public class LancesLeilaoScreenTest extends BaseTestIntegration {

    @Rule
    public ActivityTestRule<ListaLeilaoActivity> mainActivity =
            new ActivityTestRule<>(ListaLeilaoActivity.class, true, false);

    @Before
    public void setup() throws IOException {
        limpaBancoDadosApi();
        limpaDbInterno();
    }

    @Test
    public void deve_AtualizarLancesDoLeilao_QuandoReceberUmLance() throws IOException {
        //Salvar leilão na API
        tentaSalvarLeilaoNaApi(new Leilao("Console"));

        //Inicializar main activity
        mainActivity.launchActivity(new Intent());

        //Clica no leilão
        onView(withId(R.id.lista_leilao_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Clica no fab de lances do leilão
        onView(allOf(withId(R.id.lances_leilao_fab_adiciona), isDisplayed())).perform(click());

        //Verifica se aparece dialog de aviso por não ter usuário com título e descrição esperada
        onView(allOf(withText("Usuários não encontrados"), withId(R.id.alertTitle)))
                .check(matches(isDisplayed()));

        onView(allOf(
                withText("Não existe usuários cadastrados! Cadastre um usuário para propor o lance."),
                withId(android.R.id.message)))
                .check(matches(isDisplayed()));

        //Clica no botão "Cadastrar usuário"
        onView(allOf(withText("Cadastrar usuário"), isDisplayed())).perform(click());

        //Clica no fab tela de lista de usuário
        onView(allOf(withId(R.id.lista_usuario_fab_adiciona), isDisplayed()))
                .perform(click());

        //Clica no edittext e preenche com o nome do usuário
        onView(allOf(withId(R.id.form_usuario_nome_edittext), isDisplayed()))
                .perform(click(), typeText("Marcos"), closeSoftKeyboard());

        //Clica em adicionar
        onView(allOf(withId(android.R.id.button1), withText("Adicionar"), isDisplayed()))
                .perform(scrollTo(), click());

        //Clica no botão de backstack
        Espresso.pressBack();

        //Clica no fab lances de leilão
        onView(allOf(withId(R.id.lances_leilao_fab_adiciona), isDisplayed())).perform(click());

        //Verifica visibilidade do dialog com o título esperado
        onView(allOf(withText("Novo lance"), withId(R.id.alertTitle))).check(matches(isDisplayed()));

        //Clica no edittext de valor e preenche
        onView(allOf(withId(R.id.form_usuario_valor_edittext), isDisplayed()))
                .perform(click(), typeText("200.00"), closeSoftKeyboard());

        //Seleciona o usuário
        onView(allOf(withId(R.id.form_lance_usuario), isDisplayed())).perform(click());
        onData(is(new Usuario(1, "Marcos")))
                .inRoot(isPlatformPopup())
                .perform(click());

        //Clica no botão "Propor"
        onView(allOf(withText("Propor"), isDisplayed())).perform(click());

        //Realiza assertion para as views de maior e menor lance e também para os maiores lances
        FormatadorDeMoeda formatador = new FormatadorDeMoeda();
        onView(withId(R.id.lances_leilao_maior_lance))
                .check(matches(allOf(withText(formatador.formata(200)), isDisplayed())));

        onView(withId(R.id.lances_leilao_menor_lance))
                .check(matches(allOf(withText(formatador.formata(200)), isDisplayed())));

        onView(withId(R.id.lances_leilao_maiores_lances))
                .check(matches(allOf(withText(formatador.formata(200) + " - (1) Marcos\n"),
                        isDisplayed()))
                );

    }

    @Test
    public void deve_AtualizarLancesDoLeilao_QuandoReceberTresLances() throws IOException {
        tentaSalvarLeilaoNaApi(new Leilao("Console"));
        tentaSalvarUsuariosBD(new Usuario("Marcos"), new Usuario("Angie"));

        mainActivity.launchActivity(new Intent());

        onView(withId(R.id.lista_leilao_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(allOf(withId(R.id.lances_leilao_fab_adiciona), isDisplayed())).perform(click());

        propoeNovoLance("200", 1, "Marcos");
        propoeNovoLance("300", 2, "Angie");
        propoeNovoLance("400", 3, "Marcos");

        FormatadorDeMoeda formatador = new FormatadorDeMoeda();
        onView(withId(R.id.lances_leilao_maior_lance))
                .check(matches(allOf(withText(formatador.formata(200)), isDisplayed())));

        onView(withId(R.id.lances_leilao_menor_lance))
                .check(matches(allOf(withText(formatador.formata(200)), isDisplayed())));

        onView(withId(R.id.lances_leilao_maiores_lances))
                .check(matches(allOf(
                        withText(formatador.formata(400) + " - (1) Marcos\n" +
                                formatador.formata(300) + " - (2) Angie\n" +
                                formatador.formata(200) + " - (1) Marcos\n"), isDisplayed()))
                );
    }

    @Test
    public void deve_AtualizarLancesDoLeilao_QuandoReceberUmLanceMuitoAlto() throws IOException {
        tentaSalvarLeilaoNaApi(new Leilao("Console"));
        tentaSalvarUsuariosBD(new Usuario("Marcos"));

        mainActivity.launchActivity(new Intent());

        onView(withId(R.id.lista_leilao_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(allOf(withId(R.id.lances_leilao_fab_adiciona), isDisplayed())).perform(click());

        propoeNovoLance("20000000", 1, "Marcos");

        FormatadorDeMoeda formatador = new FormatadorDeMoeda();
        onView(withId(R.id.lances_leilao_maior_lance))
                .check(matches(allOf(withText(formatador.formata(20000000)), isDisplayed())));

        onView(withId(R.id.lances_leilao_menor_lance))
                .check(matches(allOf(withText(formatador.formata(20000000)), isDisplayed())));

        onView(withId(R.id.lances_leilao_maiores_lances))
                .check(matches(allOf(withText(formatador.formata(20000000) + " - (1) Marcos\n"),
                        isDisplayed()))
                );
    }

    private void propoeNovoLance(String valorLance, int idUsuario, String nomeUsuario) {
        onView(allOf(withText("Novo lance"), withId(R.id.alertTitle))).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.form_usuario_valor_edittext), isDisplayed()))
                .perform(click(), typeText(valorLance), closeSoftKeyboard());

        onView(allOf(withId(R.id.form_lance_usuario), isDisplayed())).perform(click());
        onData(is(new Usuario(idUsuario, nomeUsuario)))
                .inRoot(isPlatformPopup())
                .perform(click());

        onView(allOf(withText("Propor"), isDisplayed())).perform(click());
    }

    private void tentaSalvarUsuariosBD(Usuario... usuarios) {
        UsuarioDAO dao = new UsuarioDAO(InstrumentationRegistry.getTargetContext());

        for (Usuario usuario: usuarios) {
            Usuario usuarioSalvo = dao.salva(usuario);
            if (usuarioSalvo == null) {
                Assert.fail("Usuário " + usuario + " não foi salvo");
            }
        }
    }

    @After
    public void tearDown() throws IOException {
        limpaBancoDadosApi();
        limpaDbInterno();
    }

}
