package br.com.fiap.letsclean.entity;

public class Comodo {

	private String id;
	private String nome;
	private String idGrupo;
	private String idUsuario;
	private String idAtividade;

	public Comodo() {
	}

	public Comodo(String id, String nome, String idGrupo, String idUsuario, String idAtividade) {
		this.id = id;
		this.nome = nome;
		this.idGrupo = idGrupo;
		this.idUsuario = idUsuario;
		this.idAtividade = idAtividade;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(String idGrupo) {
		this.idGrupo = idGrupo;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getIdAtividade() {
		return idAtividade;
	}

	public void setIdAtividade(String idAtividade) {
		this.idAtividade = idAtividade;
	}
}
