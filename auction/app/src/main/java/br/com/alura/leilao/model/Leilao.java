package br.com.alura.leilao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.alura.leilao.exception.LanceMenorQueUltimoLanceException;
import br.com.alura.leilao.exception.LanceSeguidoMesmoUserException;
import br.com.alura.leilao.exception.MaximoCincoLancesPorUserException;

public class Leilao implements Serializable {

    private final long id;
    private final String descricao;
    private final List<Lance> lances;

    public Leilao(String descricao) {
        this.id = 0L;
        this.descricao = descricao;
        this.lances = new ArrayList<>();
    }

    public void propoe(Lance lance) {
        valida(lance);
        lances.add(lance);
        Collections.sort(lances);
    }

    private void valida(Lance lance) {
        double valorLance = lance.getValor();
        if (lanceForMenorQueUltimo(valorLance))
            throw new LanceMenorQueUltimoLanceException();
        if (temLances()) {
            Usuario usuarioNovo = lance.getUsuario();

            if (usuarioForMesmoUltimoLance(usuarioNovo))
                throw new LanceSeguidoMesmoUserException();
            if (usuarioFezCincoLances(usuarioNovo))
                throw new MaximoCincoLancesPorUserException();
        }
    }

    private boolean temLances() {
        return !lances.isEmpty();
    }

    private boolean usuarioFezCincoLances(Usuario usuarioNovo) {
        int lancesDoUsuario = 0;
        for (Lance l: lances) {
            final Usuario usuarioExistente = l.getUsuario();
            if (usuarioExistente.equals(usuarioNovo)) {
                lancesDoUsuario++;
                if (lancesDoUsuario == 5) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean usuarioForMesmoUltimoLance(Usuario usuarioNovo) {
        Usuario ultimoUsuario = lances.get(0).getUsuario();
        return usuarioNovo.equals(ultimoUsuario);
    }

    private boolean lanceForMenorQueUltimo(double valorLance) {
        return getMaiorLance() > valorLance;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getMaiorLance() {
        if (lances.isEmpty()) {
           return 0.0;
        }
        return lances.get(0).getValor();
    }

    public double getMenorLance() {
        if (lances.isEmpty()) {
            return 0.0;
        }
        return lances.get(lances.size() - 1).getValor();
    }

    public List<Lance> tresMaioresLances() {
        int quantidadeMaximaLances = lances.size();
        if (quantidadeMaximaLances > 3) {
            quantidadeMaximaLances = 3;
        }
        return lances.subList(0, quantidadeMaximaLances);
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Leilao leilao = (Leilao) o;

        if (id != leilao.id) return false;
        return descricao != null ? descricao.equals(leilao.descricao) : leilao.descricao == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (descricao != null ? descricao.hashCode() : 0);
        return result;
    }
}
