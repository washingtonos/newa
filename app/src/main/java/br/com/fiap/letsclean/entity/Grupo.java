package br.com.fiap.letsclean.entity;

public class Grupo{

	private Long id;
	private String nome;
	private String descricao;
	private Long admUser;
	private Long userId;
	private Long comodoId;
	private Long atividadeId;

	public Grupo() {
	}

	public Grupo(Long id, String nome, String descricao, Long admUser, Long userId, Long comodoId, Long atividadeId) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.admUser = admUser;
		this.userId = userId;
		this.comodoId = comodoId;
		this.atividadeId = atividadeId;
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getAdmUser() {
		return admUser;
	}

	public void setAdmUser(Long admUser) {
		this.admUser = admUser;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getComodoId() {
		return comodoId;
	}

	public void setComodoId(Long comodoId) {
		this.comodoId = comodoId;
	}

	public Long getAtividadeId() {
		return atividadeId;
	}

	public void setAtividadeId(Long atividadeId) {
		this.atividadeId = atividadeId;
	}
}