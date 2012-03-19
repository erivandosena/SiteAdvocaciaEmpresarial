package br.net.rwd.dao.impl;

import org.springframework.stereotype.Component;

import br.net.rwd.dao.PublicacaoDao;
import br.net.rwd.entidades.Publicacao;

@Component("publicacaoDao")
public class PublicacaoDaoImpl extends DaoGenericoImpl<Publicacao, Integer>
		implements PublicacaoDao {

}
