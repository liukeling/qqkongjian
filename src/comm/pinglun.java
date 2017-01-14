package comm;

public class pinglun {
	private String ssid;
	private user user;
	private String plnr="";
	private String pltime;
	
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getPlnr() {
		return plnr;
	}
	public void setPlnr(String plnr) {
		this.plnr = plnr;
	}
	public String getPltime() {
		return pltime;
	}
	public void setPltime(String pltime) {
		this.pltime = pltime;
	}
	public void setUser(user user) {
		this.user = user;
	}
	public user getUser() {
		return user;
	}
}
