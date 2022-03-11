package br.com.alura.leilao.ui;

import android.support.v7.widget.RecyclerView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.alura.leilao.database.dao.UsuarioDAO;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.ui.recyclerview.adapter.ListaUsuarioAdapter;

@RunWith(MockitoJUnitRunner.class)
public class AtualizadorDeUsuarioTest {

    @Mock
    private UsuarioDAO dao;
    @Mock
    private ListaUsuarioAdapter adapter;
    @Mock
    private RecyclerView recyclerView;

    @Test
    public void deve_AtualizarListaDeUsuarios_QuandoSalvarUsuario() {
        AtualizadorDeUsuario atualizadorDeUsuario = new AtualizadorDeUsuario(
                dao,
                adapter,
                recyclerView
        );

        Usuario marcos = new Usuario("Marcos");
        Mockito.when(dao.salva(marcos)).thenReturn(new Usuario(1, "Marcos"));
        Mockito.when(adapter.getItemCount()).thenReturn(1);
        atualizadorDeUsuario.salva(marcos);

        Mockito.verify(dao).salva(marcos);
        Mockito.verify(adapter).adiciona(new Usuario(1, "Marcos"));
        Mockito.verify(recyclerView).smoothScrollToPosition(0);
    }

}