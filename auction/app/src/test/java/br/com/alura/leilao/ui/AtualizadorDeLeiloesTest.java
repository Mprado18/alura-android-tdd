package br.com.alura.leilao.ui;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.alura.leilao.api.retrofit.client.LeilaoWebClient;
import br.com.alura.leilao.api.retrofit.client.RespostaListener;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.ui.recyclerview.adapter.ListaLeilaoAdapter;

@RunWith(MockitoJUnitRunner.class)
public class AtualizadorDeLeiloesTest {

    @Mock
    Context context;
    @Mock
    private ListaLeilaoAdapter adapter;
    @Mock
    private LeilaoWebClient client;
    @Mock
    private AtualizadorDeLeiloes.ErroCarregaLeiloesListener listener;

    @Test
    public void deve_AtualizarListaDeLeiloes_QuandoBuscarLeiloesDaApi() {
        AtualizadorDeLeiloes atualizadorDeLeiloes = new AtualizadorDeLeiloes();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) {
                RespostaListener<List<Leilao>> argument = invocationOnMock.getArgument(0);
                argument.sucesso(new ArrayList<>(Arrays.asList(
                        new Leilao("Computador"),
                        new Leilao("Carro")
                )));
                return null;
            }
        }).when(client).todos(ArgumentMatchers.<RespostaListener<List<Leilao>>>any());

        atualizadorDeLeiloes.buscaLeiloes(adapter, client, listener);

        verify(client).todos(ArgumentMatchers.<RespostaListener<List<Leilao>>>any());
        verify(adapter).atualiza(new ArrayList<>(Arrays.asList(
                new Leilao("Computador"),
                new Leilao("Carro")
        )));
    }

    @Test
    public void deve_ApresentarMensagemDeFalha_QuandoFalharABuscaDeLeiloes() {
        AtualizadorDeLeiloes atualizadorDeLeiloes = new AtualizadorDeLeiloes();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) {
                RespostaListener<List<Leilao>> argument = invocationOnMock.getArgument(0);
                argument.falha(anyString());
                return null;
            }
        }).when(client).todos(ArgumentMatchers.<RespostaListener<List<Leilao>>>any());

        atualizadorDeLeiloes.buscaLeiloes(adapter, client, listener);

        verify(listener).erroAoCarregar(anyString());
    }

}