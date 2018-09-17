package br.com.fiap.letsclean.entity;

public class Atividade {

	private Long id;
	private String nome;
	private String desc;
	private Grupo grupo;
	private Usuario usuario;
	private Comodo comodo;

	public Atividade() {
	}

	public Atividade(Long id, String nome, String desc, Grupo grupo, Usuario usuario, Comodo comodo) {
		this.id = id;
		this.nome = nome;
		this.desc = desc;
		this.grupo = grupo;
		this.usuario = usuario;
		this.comodo = comodo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Comodo getComodo() {
		return comodo;
	}

	public void setComodo(Comodo comodo) {
		this.comodo = comodo;
	}

}
