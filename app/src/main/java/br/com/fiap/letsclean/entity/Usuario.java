package br.com.fiap.letsclean.entity;

import java.util.List;

public class Usuario {

	private String id;
	private String nome;
	private String email;
	private String senha;
	private Boolean adm;
	private Grupo grupo;
	private List<Comodo> listComodo;
	private List<Atividade> listAtividade;

	public Usuario() {
	}

	public Usuario(String id, String nome, String email, String senha, Boolean adm, Grupo grupo, List<Comodo> listComodo, List<Atividade> listAtividade) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.adm = adm;
		this.grupo = grupo;
		this.listComodo = listComodo;
		this.listAtividade = listAtividade;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Boolean getAdm() {
		return adm;
	}

	public void setAdm(Boolean adm) {
		this.adm = adm;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public List<Comodo> getListComodo() {
		return listComodo;
	}

	public void setListComodo(List<Comodo> listComodo) {
		this.listComodo = listComodo;
	}

	public List<Atividade> getListAtividade() {
		return listAtividade;
	}

	public void setListAtividade(List<Atividade> listAtividade) {
		this.listAtividade = listAtividade;
	}

}
