package br.net.rwd.dao.impl;

import org.springframework.stereotype.Component;

import br.net.rwd.dao.AdministradorDao;
import br.net.rwd.entidades.Administrador;

@Component("administradorDao") 
public class AdministradorDaoImpl extends
		DaoGenericoImpl<Administrador, Integer> implements AdministradorDao {
	
}
