package br.com.fiap.letsclean.entity;

public class Comodo {

	private Long id;
	private String nome;
	private Long grupoId;
	private Long userId;
	private Long atividadeId;

	public Comodo() {
	}

	public Comodo(Long id, String nome, Long grupoId, Long userId, Long atividadeId) {
		this.id = id;
		this.nome = nome;
		this.grupoId = grupoId;
		this.userId = userId;
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

	public Long getGrupoId() {
		return grupoId;
	}

	public void setGrupoId(Long grupoId) {
		this.grupoId = grupoId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAtividadeId() {
		return atividadeId;
	}

	public void setAtividadeId(Long atividadeId) {
		this.atividadeId = atividadeId;
	}
}
