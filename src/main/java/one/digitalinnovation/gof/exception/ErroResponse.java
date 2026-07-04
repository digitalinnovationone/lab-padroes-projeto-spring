package one.digitalinnovation.gof.exception;

import java.time.OffsetDateTime;

public class ErroResponse {

	private final OffsetDateTime timestamp;
	private final int status;
	private final String erro;
	private final String mensagem;
	private final String path;

	public ErroResponse(int status, String erro, String mensagem, String path) {
		this.timestamp = OffsetDateTime.now();
		this.status = status;
		this.erro = erro;
		this.mensagem = mensagem;
		this.path = path;
	}

	public OffsetDateTime getTimestamp() {
		return timestamp;
	}

	public int getStatus() {
		return status;
	}

	public String getErro() {
		return erro;
	}

	public String getMensagem() {
		return mensagem;
	}

	public String getPath() {
		return path;
	}
}
