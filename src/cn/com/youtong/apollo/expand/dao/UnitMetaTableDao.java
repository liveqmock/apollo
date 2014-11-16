package cn.com.youtong.apollo.expand.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.youtong.apollo.common.sql.HibernateUtil;
import cn.com.youtong.apollo.expand.entity.UnitMetaFormInfoEntity;

/**
 *针对封面表进行操作的DAO类 
 **/
public class UnitMetaTableDao {
	private Log log = LogFactory.getLog(getClass());
	private Session session;
	public boolean updateUnitMetaForm(UnitMetaFormInfoEntity umfie){
		boolean flag = false;
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = this.getConnection();
			String sql  = "update ytapl_newqykb_fm set qymc=?,zbr=?,lxdh=?,qh=?,fj=?,jygm=?,zzxs=?,xbys=?,bblx=?,szdq=?,sshy=? where unitid = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, umfie.getQymc());
			pst.setString(2, umfie.getZbr());
			pst.setString(3, umfie.getLxdh());
			pst.setString(4, umfie.getQh());
			pst.setString(5, umfie.getFj());
			
			pst.setString(6, umfie.getJygm());
			pst.setString(7, umfie.getZzxs());
			pst.setString(8, umfie.getXbys());
			pst.setString(9, umfie.getBblx());
			
			pst.setString(10, umfie.getSzdq());
			pst.setString(11, umfie.getSshy());
			pst.setString(12, umfie.getUnitid());
			
			int count = pst.executeUpdate();
			if (count>0) {
				flag = true;
			}
			conn.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("更新封面表数据信息",e);
		}finally{
			closeDB(null,pst, conn);
		}
		return flag;
	}
	public UnitMetaFormInfoEntity findUnitMetaByUnitid(String unitid){
		UnitMetaFormInfoEntity umfie = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			String sql  = "select * from ytapl_newqykb_fm where unitid = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, unitid);
			rs = pst.executeQuery();
			while (rs.next()) {
				umfie = new UnitMetaFormInfoEntity();
				umfie.setJygm(rs.getString("jygm"));
				umfie.setBblx(rs.getString("bblx"));
				umfie.setFj(rs.getString("fj"));
				umfie.setGjdm(rs.getString("gjdm"));
				umfie.setQymc(rs.getString("qymc"));
				umfie.setJtdm(rs.getString("jtdm"));
				umfie.setLxdh(rs.getString("lxdh"));
				umfie.setLxdh(rs.getString("lxdh"));
				umfie.setQh(rs.getString("qh"));
				umfie.setQydm(rs.getString("qydm"));
				umfie.setSjdm(rs.getString("sjdm"));
				umfie.setSjh(rs.getString("sjh"));
				umfie.setSshy(rs.getString("sshy"));
				umfie.setSzdq(rs.getString("szdq"));
				umfie.setUnitid(rs.getString("unitid"));
				umfie.setXbys(rs.getString("xbys"));
				umfie.setZbr(rs.getString("zbr"));
				umfie.setZzxs(rs.getString("zzxs"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("读取封面表数据信息",e);
		}finally{
			closeDB(rs, pst, conn);
		}
		return umfie;
	}
	
	
	private Connection getConnection() {
		Connection conn = null;
		try {
			this.session = HibernateUtil.openSession();
			conn = this.session.connection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	private void closeDB(ResultSet set, PreparedStatement pst, Connection con) {
		try {
			if (set != null) {
				set.close();
			}
			if (pst != null) {
				pst.close();
			}
			if (con != null)
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void closeDB(ResultSet set, Statement stmt, Connection con) {
		try {
			if (set != null) {
				set.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (con != null)
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void rollbackTranscation(Transaction tx) {
		try {
			if (tx != null)
				tx.rollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
