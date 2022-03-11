package br.com.alura.leilao.formatter;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class FormatadorDeMoedasTest {

    @Test
    public void deve_formatarParaMoeda_QuandoRecebeValorDouble() {
        FormatadorDeMoeda formatador = new FormatadorDeMoeda();

        String moedaFormatada = formatador.formata(200.0);
        assertThat(moedaFormatada, is(equalTo("R$ 200,00")));
    }
}