package br.com.alura.leilao.utils;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Assert;

import java.io.IOException;

import br.com.alura.leilao.BuildConfig;
import br.com.alura.leilao.api.retrofit.client.TesteWebClient;
import br.com.alura.leilao.model.Leilao;

public abstract class BaseTestIntegration {

    private static final String ERRO_FALHA_LIMPAZA_BD = "Banco de dados não foi limpo!";
    private static final String ERRO_LEILAO_NAO_FOI_SALVO = "Leilão não foi salvo: ";

    private final TesteWebClient webClient = new TesteWebClient();

    protected void limpaBancoDadosApi() throws IOException {
        boolean bancoDeDadosNaoFoiLimpo = !webClient.limpaBancoDeDados();
        if (bancoDeDadosNaoFoiLimpo) {
            Assert.fail(ERRO_FALHA_LIMPAZA_BD);
        }
    }

    protected void tentaSalvarLeilaoNaApi(Leilao... leiloes) throws IOException {
        for (Leilao leilao : leiloes) {
            Leilao leilaoSalvo = webClient.salva(leilao);
            if (leilaoSalvo == null) {
                Assert.fail(ERRO_LEILAO_NAO_FOI_SALVO + leilao.getDescricao());
            }
        }
    }

    protected void limpaDbInterno() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        appContext.deleteDatabase(BuildConfig.BANCO_DADOS);
    }

}
