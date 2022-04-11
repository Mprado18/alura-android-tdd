package br.com.alura.leilao.api.retrofit.client;

import java.io.IOException;

import br.com.alura.leilao.api.retrofit.TesteRetrofitInicializador;
import br.com.alura.leilao.api.retrofit.service.TesteService;
import br.com.alura.leilao.model.Leilao;
import retrofit2.Call;
import retrofit2.Response;

public class TesteWebClient extends WebClient {

    private final TesteService service;

    public TesteWebClient() {
        this.service = new TesteRetrofitInicializador().getTesteService();
    }

    public Leilao salva(Leilao leilao) throws IOException {
        Call<Leilao> call = service.salva(leilao);
        Response<Leilao> response = call.execute();
        if (temDados(response)) {
            return response.body();
        }
        return null;
    }

    public boolean limpaBancoDeDados() throws IOException {
        Call<Void> call = service.limparBD();
        Response<Void> response = call.execute();
        return response.isSuccessful();
    }

}
