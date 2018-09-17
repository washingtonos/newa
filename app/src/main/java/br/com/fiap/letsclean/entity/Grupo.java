package br.com.fiap.letsclean.entity;

public class Grupo{

	private String id;
	private String nome;
	private String descricao;
	private String idUser;
	private String id_comodo;
	private String id_atividade;


	public Grupo() {
	}

	public Grupo(String id, String nome, String descricao, String idUser, String id_comodo, String id_atividade) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.idUser = idUser;
		this.id_comodo = id_comodo;
		this.id_atividade = id_atividade;
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getId_comodo() {
		return id_comodo;
	}

	public void setId_comodo(String id_comodo) {
		this.id_comodo = id_comodo;
	}

	public String getId_atividade() {
		return id_atividade;
	}

	public void setId_atividade(String id_atividade) {
		this.id_atividade = id_atividade;
	}
}