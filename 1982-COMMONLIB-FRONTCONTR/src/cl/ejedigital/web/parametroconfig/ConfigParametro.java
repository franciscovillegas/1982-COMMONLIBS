package cl.ejedigital.web.parametroconfig;

import java.util.ArrayList;

public class ConfigParametro {

	private String		idParametro;
	private String		defOption;
	private String		selectedValue;
	private String		selectedKey;
	private String		idGrupo;
	private String		idTipo;
	private final Grupo			grupo;
	private final Options		options;
	private final OptionType	optionType;

	ConfigParametro(Grupo	grupo, Options	options, OptionType	optionType,
						String idParametro, String defOption, String selectedValue, String selectedKey, String idGrupo,
						int idTipo) {
		super();
		this.grupo = grupo;
		this.options = options;
		this.optionType = optionType;
		this.idParametro = idParametro;
		this.defOption = defOption;
		this.selectedValue = selectedValue;
		this.selectedKey = selectedKey;
		this.idGrupo = idGrupo;
	}

	public String getIdParametro() {
		return idParametro;
	}

	public String getDefOption() {
		return defOption;
	}

	public String getSelectedValue() {
		return selectedValue;
	}

	public String getSelectedKey() {
		return selectedKey;
	}

	public String getIdGrupo() {
		return idGrupo;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public Options getOption() {
		return options;
	}

	public OptionType getOptionType() {
		return optionType;
	}

	public Options getOptions() {
		return options;
	}

	public String getIdTipo() {
		return idTipo;
	}
	
	public String toString() {
		return selectedKey;
	}

}

class Grupo {

	private String	idGrupo;
	private String	nombre;

	public Grupo(String idGrupo, String nombre) {
		super();
		this.idGrupo = idGrupo;
		this.nombre = nombre;
	}

	public String getIdGrupo() {
		return idGrupo;
	}

	public String getNombre() {
		return nombre;
	}

}

class Options extends ArrayList {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1463517792332555162L;

	public boolean add(Option o) {
		// TODO Auto-generated method stub
		return super.add(o);
	}

	public Option getOption(int index) {
		// TODO Auto-generated method stub
		return (Option) super.get(index);
	}
}

class Option {

	private int		idCorr;
	private String	idParametro;
	private String	optionValue;
	private String	optionKey;

	public Option(int idCorr, String idParametro, String optionValue, String optionKey) {
		super();
		this.idCorr = idCorr;
		this.idParametro = idParametro;
		this.optionValue = optionValue;
		this.optionKey = optionKey;
	}

	public int getIdCorr() {
		return idCorr;
	}

	public String getIdParametro() {
		return idParametro;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public String getOptionKey() {
		return optionKey;
	}

}

class OptionType {

	private int		idTipo;
	private String	nombre;

	public OptionType(int idTipo, String nombre) {
		super();
		this.idTipo = idTipo;
		this.nombre = nombre;
	}

	public int getIdTipo() {
		return idTipo;
	}

	public String getNombre() {
		return nombre;
	}

}
