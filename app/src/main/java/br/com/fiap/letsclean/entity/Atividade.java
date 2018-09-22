package br.com.fiap.letsclean.entity;

public class Atividade {

	private Long id;
	private String nome;
	private String desc;
	private Long grupoId;
	private Long comodoId;
	private Long userId;
	private Long status;

	public Atividade() {
	}

	public Atividade(Long id, String nome, String desc, Long grupoId, Long comodoId, Long userId, Long status) {
		this.id = id;
		this.nome = nome;
		this.desc = desc;
		this.grupoId = grupoId;
		this.comodoId = comodoId;
		this.userId = userId;
		this.status = status;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
}
