package br.net.rwd.dao.impl;

import org.springframework.stereotype.Component;

import br.net.rwd.dao.UsuarioDao;
import br.net.rwd.entidades.Usuario;

@Component("usuarioDao")
public class UsuarioDaoImpl extends DaoGenericoImpl<Usuario, Integer> implements
		UsuarioDao {
	
}
