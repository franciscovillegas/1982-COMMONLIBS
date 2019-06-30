package portal.com.eje.portal.msggenerico;

import portal.com.eje.portal.vo.vo.Vo;

public class MsgGenericoVo extends Vo {
	private String title;
	private String msg;
	private String url;

	public MsgGenericoVo(String title, String msg, String url) {
		super();
		this.title = title;
		this.msg = msg;
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public String getMsg() {
		return msg;
	}

	public String getUrl() {
		return url;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
