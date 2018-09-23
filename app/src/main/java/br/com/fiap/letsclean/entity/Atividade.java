package br.com.fiap.letsclean.entity;

public class Atividade {

	private Long id;
	private String nome;
	private String nomeResponsavel;
	private String obs;
	private String desc;
	private String validando;
	private Long grupoId;
	private Long comodoId;
	private Long userId;
	private Long status;
	private Long admUser;
	private Long userIdLOGADO;

	public Atividade() {
	}

	public Atividade(Long id, String nome, String nomeResponsavel, String obs, String desc, String validando, Long grupoId, Long comodoId, Long userId, Long status, Long admUser, Long userIdLOGADO) {
		this.id = id;
		this.nome = nome;
		this.nomeResponsavel = nomeResponsavel;
		this.obs = obs;
		this.desc = desc;
		this.validando = validando;
		this.grupoId = grupoId;
		this.comodoId = comodoId;
		this.userId = userId;
		this.status = status;
		this.admUser = admUser;
		this.userIdLOGADO = userIdLOGADO;
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

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getValidando() {
		return validando;
	}

	public void setValidando(String validando) {
		this.validando = validando;
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

	public Long getAdmUser() {
		return admUser;
	}

	public void setAdmUser(Long admUser) {
		this.admUser = admUser;
	}

	public Long getUserIdLOGADO() {
		return userIdLOGADO;
	}

	public void setUserIdLOGADO(Long userIdLOGADO) {
		this.userIdLOGADO = userIdLOGADO;
	}
}
