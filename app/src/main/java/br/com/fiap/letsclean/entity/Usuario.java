package br.com.fiap.letsclean.entity;

import java.util.List;

public class Usuario {

	private Long id;
	private String nome;
	private String email;
	private String senha;
	private Long admUser;
	private Long atividadeId;
	private Long grupoId;
	private Long comodoId;

	public Usuario() {
	}

	public Usuario(Long id, String nome, String email, String senha, Long admUser, Long atividadeId, Long grupoId, Long comodoId) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.admUser = admUser;
		this.atividadeId = atividadeId;
		this.grupoId = grupoId;
		this.comodoId = comodoId;
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

	public Long getAdmUser() {
		return admUser;
	}

	public void setAdmUser(Long admUser) {
		this.admUser = admUser;
	}

	public Long getAtividadeId() {
		return atividadeId;
	}

	public void setAtividadeId(Long atividadeId) {
		this.atividadeId = atividadeId;
	}

	public Long getGrupoId() {
		return grupoId;
	}

	public void setGrupoId(Long grupoId) {
		this.grupoId = grupoId;
	}

	public Long getComodoId() {
		return comodoId;
	}

	public void setComodoId(Long comodoId) {
		this.comodoId = comodoId;
	}
}
