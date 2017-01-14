package comm;

public class shuoshuo {
	private int form_ssid = -1;
	private String neirong="";
	private String fabutime;
	private user user;
	private String ssid;
	private String dianzanshu;
	private boolean dz;
	public void setDianzanshu(String dianzanshu) {
		this.dianzanshu = dianzanshu;
	}
	public String getDianzanshu() {
		return dianzanshu;
	}
	public void setFabutime(String fabutime) {
		this.fabutime = fabutime;
	}
	public String getFabutime() {
		return fabutime;
	}
	public void setNeirong(String neirong) {
		this.neirong = neirong;
	}
	public String getNeirong() {
		return neirong;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getSsid() {
		return ssid;
	}
	public void setForm_ssid(int form_ssid) {
		this.form_ssid = form_ssid;
	}
	public int getForm_ssid() {
		return form_ssid;
	}
	public void setUser(user user) {
		this.user = user;
	}
	public user getUser() {
		return user;
	}
	public void setdz(boolean dz) {
		this.dz = dz;
	}
	public boolean isdz() {
		return dz;
	}
}
