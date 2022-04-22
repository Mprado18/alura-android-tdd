package br.com.alura.leilao.matchers;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import br.com.alura.leilao.R;
import br.com.alura.leilao.formatter.FormatadorDeMoeda;

public class ViewMatcher {

    public static Matcher<? super View> apareceLeilaoNaPosicao(
            final int posicaoEsperada,
            final String descricaoEsperada,
            final double maiorLanceEsperado
    ) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            private final Matcher<View> isDisplayed = isDisplayed();
            private final FormatadorDeMoeda formatadorDeMoeda = new FormatadorDeMoeda();
            private final String valorFormatado = formatadorDeMoeda.formata(maiorLanceEsperado);

            @Override
            public void describeTo(Description description) {
                description.appendText("View com descrição ").appendValue(descricaoEsperada)
                        .appendText(", maior lance ").appendValue(valorFormatado)
                        .appendText(" na posição ").appendValue(posicaoEsperada)
                .appendText(" ");
                description.appendDescriptionOf(isDisplayed);
            }

            @Override
            protected boolean matchesSafely(RecyclerView item) {
                RecyclerView.ViewHolder viewDoViewHolder =
                        item.findViewHolderForAdapterPosition(posicaoEsperada);
                if (viewDoViewHolder == null) {
                    throw new IndexOutOfBoundsException(
                            "View do ViewHolder na posição esperada " + posicaoEsperada + " não foi encontrado!"
                    );
                }
                View itemView = viewDoViewHolder.itemView;

                boolean temDescricaoEsperada = verificaDescricaoEsperada(itemView);
                boolean temMaiorLanceEsperado = verificaMaiorLanceEsperado(itemView);

                return temDescricaoEsperada && temMaiorLanceEsperado && isDisplayed
                        .matches(viewDoViewHolder);
            }

            private boolean verificaMaiorLanceEsperado(View itemView) {
                TextView itemMaiorLance = itemView.findViewById(R.id.item_leilao_maior_lance);
                return itemMaiorLance.getText().toString()
                        .equals(valorFormatado) && isDisplayed.matches(itemMaiorLance);
            }

            private boolean verificaDescricaoEsperada(View itemView) {
                TextView itemDescricao = itemView.findViewById(R.id.item_leilao_descricao);
                return itemDescricao.getText().toString()
                        .equals(descricaoEsperada) && isDisplayed.matches(itemDescricao);
            }
        };
    }

}
