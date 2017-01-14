package comm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class jdbctools {
	private static Connection conn = null;

	public static Connection getconn() {
		if (conn == null) {
			try {
				conn = jdbcuilt.getConnection();
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return conn;

	}

	public static boolean isCunZai(String id, String pswd) {
		boolean ok = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			if (conn == null) {
				conn = getconn();
			}
			stmt = conn.createStatement();
			rs = stmt
					.executeQuery("select * from user_zhanghaomima where user_zhanghao='"
							+ id + "' and user_pswd='" + pswd + "'");

			ok = rs.next();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				jdbcuilt.close(null, stmt, rs);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ok;
	}

	public static user getUser(String id) throws SQLException {
		user u = new user(Integer.parseInt(id));

		if (conn == null) {
			conn = getconn();
		}
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt
				.executeQuery("select user_name from user_zhanghaomima where user_zhanghao='"
						+ id + "'");
		if (rs.next()) {
			u.setName(rs.getString("user_name"));
		}

		jdbcuilt.close(null, stmt, rs);
		return u;
	}

	public static boolean addpinglun(pinglun pl) {
		boolean b = false;
		if (conn == null) {
			conn = getconn();
		}
		Statement stmt = null;
		try {
			String sql = "insert into pingyubiao (PY_ID,PINGYU_USERID,PINGYU,PINGYU_TIME,SSID) values(pl_id_sqn.nextval,"
					+ pl.getUser().getZhanghao()
					+ ",'"
					+ pl.getPlnr()
					+ "',sysdate,"
					+ pl.getSsid() + ")";

			stmt = conn.createStatement();
			int i = stmt.executeUpdate(sql);

			conn.commit();

			if (i != 0) {
				b = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				jdbcuilt.close(null, stmt, null);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}
	//得到点赞的用户
	public static ArrayList<user> getDzusers(String ssid){
		String sql = "select * from user_zhanghaomima where user_zhanghao in (select user_id from dianzanuser where dianzanuser.ss_id = "+ssid+")";
		ArrayList<user> users = new ArrayList<user>();
		if (conn == null) {
			conn = getconn();
		}
		Statement stmt = null;
		ResultSet rs = null;
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				int id = rs.getInt(rs.findColumn("user_zhanghao"));
				String name = rs.getString(rs.findColumn("user_name"));
				user u = new user(id);
				u.setName(name);
				users.add(u);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return users;
	}
	//根据说说id查出所有评论
	public static ArrayList<pinglun> getpinglun(int ssid) {
		ArrayList<pinglun> al = new ArrayList<pinglun>();

		if (conn == null) {
			conn = getconn();
		}
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select * from pingyubiao where SSID=" + ssid
					+ " order by PINGYU_TIME desc";

			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				pinglun pl = new pinglun();
				pl.setPlnr(rs.getString("PINGYU"));
				pl.setPltime(rs.getString("PINGYU_TIME"));
				pl.setSsid(ssid + "");
				pl.setUser(getUser(rs.getString("PINGYU_USERID")));
				al.add(pl);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				jdbcuilt.close(null, stmt, rs);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return al;
	}

	public static shuoshuo getshuoshuo_ssid(int id, String userid) throws SQLException {
		shuoshuo ss = new shuoshuo();

		if (conn == null) {
			conn = getconn();
		}
		String sql = "select * from shuoshuo where ss_id=" + id;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {

			ss.setSsid(id + "");
			ss.setUser(jdbctools.getUser(rs.getString("USER_ID")));
			ss.setFabutime(rs.getString("FABU_TIME"));
			ss.setNeirong(rs.getString("NEIRONG"));
			ss.setdz(!isDZ(userid, ss.getSsid()));
			sql = "select count(*) from dianzanuser where ss_id=" + id;
			rs = stmt.executeQuery(sql);
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			}
			ss.setDianzanshu(count + "");

			sql = "select YUAN_SSID from zhuangfa_tab where ZF_SS_ID=" + id;
			rs = stmt.executeQuery(sql);
			int yuanid = -1;
			if (rs.next()) {
				yuanid = rs.getInt(1);
			}
			ss.setForm_ssid(yuanid);

		} else {
			System.out.println("没找到该条说说");
		}

		jdbcuilt.close(null, stmt, rs);
		return ss;
	}

	public static ArrayList<shuoshuo> getshuoshuo(int i, String userid) throws SQLException {
		ArrayList<shuoshuo> al = new ArrayList<shuoshuo>();
		if(conn == null){
			conn = getconn();
		}
		Statement stmt = conn.createStatement();

	
		String sql = "select ss_id,user_id,fabu_time,neirong,rownum from (select ss.*,rownum from shuoshuo ss order by fabu_time desc ) where rownum<="
				+ (4 * i);

		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			shuoshuo ss = new shuoshuo();
			ss.setFabutime(rs.getString("fabu_time"));
			ss.setNeirong(rs.getString("neirong"));
			ss.setUser(getUser(rs.getString("user_id")));
			ss.setSsid(rs.getString("ss_id"));
			ss.setdz(!isDZ(userid, ss.getSsid()));
			String sql1 = "select count(*) from dianzanuser where ss_id="
					+ ss.getSsid();
			int dzcount = 0;
			Statement stmt1 = conn.createStatement();
			ResultSet rsyuan = stmt1.executeQuery(sql1);
			if (rsyuan.next()) {
				dzcount = rsyuan.getInt(1);
			}
			ss.setDianzanshu(dzcount + "");

			sql1 = "select YUAN_SSID from zhuangfa_tab where ZF_SS_ID="
					+ ss.getSsid();
			stmt1 = conn.createStatement();
			rsyuan = stmt1.executeQuery(sql1);
			if (rsyuan.next()) {
				String yuan_ssid = rsyuan.getString(1);
				ss.setForm_ssid(Integer.parseInt(yuan_ssid));
			} else {
				ss.setForm_ssid(-1);
			}
			al.add(ss);
			jdbcuilt.close(null, stmt1, rsyuan);
		}

		jdbcuilt.close(null, stmt, rs);

		/*
		 * SS_ID USER_ID FABU_TIME NEIRONG DIANZANNUMBER
		 */
		return al;
	}
	
	public static ArrayList<shuoshuo> getshuoshuo(String ssid,
			String selectType, boolean me, String zhanghao) throws SQLException {
		ArrayList<shuoshuo> al = new ArrayList<shuoshuo>();
		if (conn == null) {
			conn = getconn();
		}
		Statement stmt = conn.createStatement();

		ResultSet rs1 = stmt.executeQuery("select count(*) from shuoshuo");

		int count = 0;

		if (rs1.next()) {

			count = rs1.getInt(1);

		}

		String sql = "select ss_id,user_id,fabu_time,neirong,rownum from (select ss.*,rownum from shuoshuo ss order by fabu_time desc)";

		if (me) {
			sql = sql + "where user_id = " + zhanghao;
		}

		String where = "";
		if (!("".equals(selectType) || selectType == null)) {
			if (sql.contains("where")) {
				where = " and ";
			} else {
				where = " where ";
			}
			if ("old".equals(selectType)) {
				if (!"-1".equals(ssid)) {
					where = where + "ss_id < " + ssid;
				} else {
					where = "";
				}
			} else if ("new".equals(selectType)) {
				if (!"-1".equals(ssid)) {
					where = where + "ss_id > " + ssid;
				} else {
					where = "";
				}
			}
		}
		sql = sql + where;
		ResultSet rs = stmt.executeQuery(sql);
		int i = 1;
		while (rs.next()) {
			shuoshuo ss = new shuoshuo();
			ss.setFabutime(rs.getString("fabu_time"));
			ss.setNeirong(rs.getString("neirong"));
			ss.setUser(jdbctools.getUser(rs.getString("user_id")));
			ss.setSsid(rs.getString("ss_id"));
			ss.setdz(!isDZ(zhanghao, ss.getSsid()));
			String sql1 = "select count(*) from dianzanuser where ss_id="
					+ ss.getSsid();
			int dzcount = 0;
			Statement stmt1 = conn.createStatement();
			ResultSet rsyuan = stmt1.executeQuery(sql1);
			if (rsyuan.next()) {
				dzcount = rsyuan.getInt(1);
			}
			ss.setDianzanshu(dzcount + "");

			sql1 = "select YUAN_SSID from zhuangfa_tab where ZF_SS_ID="
					+ ss.getSsid();
			stmt1 = conn.createStatement();
			rsyuan = stmt1.executeQuery(sql1);
			if (rsyuan.next()) {
				String yuan_ssid = rsyuan.getString(1);
				ss.setForm_ssid(Integer.parseInt(yuan_ssid));
			} else {
				ss.setForm_ssid(-1);
			}
			al.add(ss);
			jdbcuilt.close(null, stmt1, rsyuan);
			if(i >= 4 && "old".equals(selectType)){
				break;
			}
			i ++;
		}

		jdbcuilt.close(null, stmt, rs);

		/*
		 * SS_ID USER_ID FABU_TIME NEIRONG DIANZANNUMBER
		 */
		return al;
	}

	public static ArrayList<shuoshuo> selecttype(int i) throws SQLException {
		ArrayList<shuoshuo> al = new ArrayList<shuoshuo>();
		if (conn == null) {
			conn = getconn();
		}
		Statement stmt = conn.createStatement();

		ResultSet rs1 = stmt.executeQuery("select count(*) from shuoshuo");

		int count = 0;

		if (rs1.next()) {

			count = rs1.getInt(1);

		}

		String sql = "select ss_id,user_id,fabu_time,neirong,rownum from (select ss.*,rownum from shuoshuo ss order by fabu_time desc ) where rownum<="
				+ (4 * i);

		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			shuoshuo ss = new shuoshuo();
			ss.setFabutime(rs.getString("fabu_time"));
			ss.setNeirong(rs.getString("neirong"));
			ss.setUser(jdbctools.getUser(rs.getString("user_id")));
			ss.setSsid(rs.getString("ss_id"));

			String sql1 = "select count(*) from dianzanuser where ss_id="
					+ ss.getSsid();
			int dzcount = 0;
			Statement stmt1 = conn.createStatement();
			ResultSet rsyuan = stmt1.executeQuery(sql1);
			if (rsyuan.next()) {
				dzcount = rsyuan.getInt(1);
			}
			ss.setDianzanshu(dzcount + "");

			sql1 = "select YUAN_SSID from zhuangfa_tab where ZF_SS_ID="
					+ ss.getSsid();
			stmt1 = conn.createStatement();
			rsyuan = stmt1.executeQuery(sql1);
			if (rsyuan.next()) {
				String yuan_ssid = rsyuan.getString(1);
				ss.setForm_ssid(Integer.parseInt(yuan_ssid));
			} else {
				ss.setForm_ssid(-1);
			}
			al.add(ss);
			jdbcuilt.close(null, stmt1, rsyuan);
		}

		jdbcuilt.close(null, stmt, rs);

		/*
		 * SS_ID USER_ID FABU_TIME NEIRONG DIANZANNUMBER
		 */
		return al;
	}

	public static shuoshuo getzuijinss(String userid) throws Exception {
		shuoshuo ss = new shuoshuo();
		String sql = "select * from shuoshuo where user_id=" + userid
				+ " order by fabu_time desc";
		if (conn == null) {
			conn = getconn();
		}
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			ss.setNeirong(rs.getString("NEIRONG"));
			ss.setFabutime(rs.getString("FABU_TIME"));
		}
		return ss;
	}

	public static boolean addshuoshuo(shuoshuo ss) throws SQLException {
		boolean ok = false;
		if (conn == null) {
			conn = getconn();
		}
		String sql = "insert into shuoshuo (ss_id,user_id,fabu_time,neirong) values (shuoshuo_ssid.nextval,"
				+ ss.getUser().getZhanghao() + ",sysdate,'" + ss.getNeirong() + "')";
		Statement stmt = conn.createStatement();
		int i = stmt.executeUpdate(sql);
		conn.commit();
		if (i != 0) {
			ok = true;
		}

		jdbcuilt.close(null, stmt, null);
		return ok;
	}

	public static boolean deletess(int ssid, int fromssid) throws SQLException {
		boolean b = false;
		if (conn == null) {
			conn = getconn();
		}
		Statement stmt = conn.createStatement();
		String sql = "";
		if (fromssid != 0 && fromssid != -1) {
			sql = "delete zhuangfa_tab where yuan_ssid=" + fromssid;
		}
		if (!"".equals(sql)) {
			stmt.executeUpdate(sql);
			sql = "";
		}
		sql = "delete dianzanuser where ss_id=" + ssid;
		if (!"".equals(sql)) {
			stmt.executeUpdate(sql);
			sql = "";
		}
		sql = "delete pingyubiao where ssid=" + ssid;
		if (!"".equals(sql)) {
			stmt.executeUpdate(sql);
			sql = "";
		}
		sql = "delete shuoshuo where ss_id=" + ssid;
		int i = stmt.executeUpdate(sql);
		if (i != 0) {
			b = true;
		}
		conn.commit();
		jdbcuilt.close(null, stmt, null);
		return b;
	}

	public static int selectShuoshuo(String userid) throws SQLException {
		if (conn == null) {
			conn = getconn();
		}
		int count = 0;
		String sql = "select count(*) from shuoshuo where USER_ID=" + userid;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		rs.next();
		count = rs.getInt(1);

		jdbcuilt.close(null, stmt, rs);
		return count;
	}

	public static ArrayList<shuoshuo> getMyshuoshuo(String id, int i)
			throws SQLException {
		if (conn == null) {
			conn = getconn();
		}
		ArrayList<shuoshuo> al = new ArrayList<shuoshuo>();

		Statement stmt = conn.createStatement();
		String sql = "select ss_id,user_id,fabu_time,neirong,rownum from (select ss_id,user_id,fabu_time,neirong,rownum from shuoshuo where user_id="
				+ id + " order by fabu_time desc ) where rownum<=" + (4 * i);

		ResultSet rs = stmt.executeQuery(sql);
		boolean b = true;
		while ((b = rs.next())) {
			shuoshuo ss = new shuoshuo();
			ss.setSsid(rs.getString("ss_id"));
			ss.setFabutime(rs.getString("fabu_time"));
			ss.setNeirong(rs.getString("neirong"));
			ss.setUser(jdbctools.getUser(rs.getString("user_id")));

			String sql1 = "select count(*) from dianzanuser where SS_ID="
					+ ss.getSsid();
			int dzcount = 0;
			Statement stmt1 = conn.createStatement();
			ResultSet rsyuan = stmt1.executeQuery(sql1);
			if (rsyuan.next()) {
				dzcount = rsyuan.getInt(1);
			}
			ss.setDianzanshu(dzcount + "");

			sql1 = "select YUAN_SSID from zhuangfa_tab where ZF_SS_ID="
					+ ss.getSsid();
			stmt1 = conn.createStatement();
			rsyuan = stmt1.executeQuery(sql1);
			if (rsyuan.next()) {
				String yuan_ssid = rsyuan.getString(1);
				ss.setForm_ssid(Integer.parseInt(yuan_ssid));
			} else {
				ss.setForm_ssid(-1);
			}

			al.add(ss);
			jdbcuilt.close(null, stmt1, rsyuan);
		}

		jdbcuilt.close(null, stmt, rs);

		return al;
	}

	public static boolean dianzan(String userid, String ssid) {
		if (conn == null) {
			conn = getconn();
		}
		boolean ok = false;
		String sql = "insert into dianzanuser (dz_id,ss_id,user_id,dianzan_time) values (dz_id_sqn.nextval,"
				+ ssid + "," + userid + ",sysdate)";
		Statement stmt = null;
		try {
			stmt = conn.createStatement();

			int i = stmt.executeUpdate(sql);

			conn.commit();
			if (i != 0) {
				ok = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				jdbcuilt.close(null, stmt, null);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ok;
	}

	public static boolean quxiaodianzan(String userid, String ssid) {
		if (conn == null) {
			conn = getconn();
		}
		boolean ok = false;
		String sql = "delete dianzanuser where ss_id=" + ssid + " and user_id="
				+ userid;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			int i = stmt.executeUpdate(sql);

			conn.commit();
			if (i != 0) {
				ok = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				jdbcuilt.close(null, stmt, null);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ok;
	}

	public static boolean isDZ(String userid, String ssid) {
		if (conn == null) {
			conn = getconn();
		}
		boolean ok = false;

		String sql = "select count(*) from dianzanuser where ss_id=" + ssid
				+ " and user_id=" + userid;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			rs.next();
			int i = rs.getInt(1);
			if (i == 0) {
				ok = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				jdbcuilt.close(null, stmt, rs);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ok;
	}

	public static boolean zhuanfa(int userid, int yuan_ssid) {
		if (conn == null) {
			conn = getconn();
		}
		boolean ok1 = false;
		boolean ok2 = false;
		boolean ok3 = false;
		boolean ok4 = false;
		String sql = "";
		shuoshuo ss = new shuoshuo();
		try {
			Statement stmt = conn.createStatement();
			sql = "select NEIRONG from shuoshuo where ss_id=" + yuan_ssid;// 获取到要转发的说说内容
			ResultSet rs = stmt.executeQuery(sql);
			if ((ok1 = rs.next())) {
				ss.setNeirong(rs.getString("neirong"));
			} else {
				System.out.println("转发的说说不存在！！！");
			}
			ss.setUser(jdbctools.getUser("" + userid));

			sql = "select shuoshuo_ssid.nextval from dual";
			int currid = -1;
			rs = stmt.executeQuery(sql);
			ok4 = rs.next();
			if (ok4) {
				currid = rs.getInt(1);
			}

			sql = "insert into shuoshuo (ss_id,user_id,fabu_time,neirong) values ("
					+ currid
					+ ","
					+ ss.getUser().getZhanghao()
					+ ",sysdate,'"
					+ ss.getNeirong() + "')";// 将说说正式发布

			int i = stmt.executeUpdate(sql);
			if (i != 0) {
				ok3 = true;
			} else {
				System.out.println("转法说说发布失败");
			}
			sql = "insert into zhuangfa_tab (ZF_ID,YUAN_SSID,ZF_SS_ID,zf_time) values (zhuangfa_sqn.nextval,"
					+ yuan_ssid + "," + currid + ",sysdate)";// 将转发记录下来

			i = stmt.executeUpdate(sql);
			if (i != 0) {
				ok2 = true;
			} else {
				System.out.println("记录转发失败");
			}
			if (ok1 & ok2 & ok3) {
				conn.commit();
			} else {
				System.out.println("转发失败");
			}
			jdbcuilt.close(null, stmt, rs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return (ok1 & ok2 & ok3);
	}

}
