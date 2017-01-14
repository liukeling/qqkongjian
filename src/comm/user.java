package comm;

import java.io.Serializable;

public class user implements Serializable {
	private String name = "";
	private int zhanghao = -1;

	public user(int zhanghao) {
		this.zhanghao = zhanghao;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setZhanghao(int zhanghao) {
		this.zhanghao = zhanghao;
	}

	public int getZhanghao() {
		return zhanghao;
	}

	public int hashCode() {
		return zhanghao;
	}

	public boolean equals(Object o) {
		boolean ok = false;

		if (o instanceof user) {
			user u = (user) o;
			if (u.getZhanghao() == this.getZhanghao()) {
				ok = true;
			}
		}

		return ok;
	}
}
