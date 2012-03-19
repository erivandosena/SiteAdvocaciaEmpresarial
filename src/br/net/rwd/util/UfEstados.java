package br.net.rwd.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.rwd.enumeracao.EnumEstado;

@Controller("ufEstados")
@Scope("session")
public class UfEstados {

	@SuppressWarnings("unchecked")
	public Map getEstados() {
		Map<EnumEstado, String> mapParam = new HashMap<EnumEstado, String>();
		for (EnumEstado type : EnumEstado.values()) {
			mapParam.put(type, type.name());
		}
		return mapParam;
	}
}
