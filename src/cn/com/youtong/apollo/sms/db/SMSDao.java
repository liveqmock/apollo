/*     */ package cn.com.youtong.apollo.sms.db;
/*     */ 
/*     */ import cn.com.youtong.apollo.common.sql.HibernateUtil;
/*     */ import cn.com.youtong.apollo.sms.form.MessageInfoEntity;
/*     */ import cn.com.youtong.apollo.sms.form.MessageReceiveEntity;
/*     */ import cn.com.youtong.apollo.sms.form.PhoneBookEntity;
/*     */ import java.io.PrintStream;
/*     */ import java.sql.CallableStatement;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.Statement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.sf.hibernate.Query;
/*     */ import net.sf.hibernate.Session;
/*     */ import net.sf.hibernate.Transaction;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ public class SMSDao
/*     */ {
/*  24 */   private Log log = LogFactory.getLog(getClass());
/*     */   private Session session;
/*     */ 
/*     */   public boolean deleteAllMessageInfoByTableName(String tablename)
/*     */   {
/*  33 */     boolean flag = false;
/*  34 */     Connection conn = null;
/*  35 */     PreparedStatement pst = null;
/*  36 */     ResultSet rs = null;
/*  37 */     CallableStatement proc = null;
/*     */     try {
/*  39 */       conn = getConnection();
/*  40 */       pst = conn.prepareStatement("truncate table " + tablename);
/*  41 */       flag = pst.execute();
/*  42 */       flag = true;
/*     */     } catch (Exception e) {
/*  44 */       flag = false;
/*  45 */       e.printStackTrace();
/*     */     } finally {
/*  47 */       closeDB(rs, pst, conn);
/*     */     }
/*  49 */     return flag;
/*     */   }
/*     */ 
/*     */   public List<MessageReceiveEntity> findAllReceiveMsgByCondtion(String condtion, String start, String end)
/*     */   {
/*  57 */     List list = new ArrayList();
/*  58 */     Connection conn = null;
/*  59 */     PreparedStatement pst = null;
/*  60 */     ResultSet rs = null;
/*     */     try {
/*  62 */       String sql = "select a.ID,(select count(*) from msg_inbox a ";
/*  63 */       sql = sql + "left join ytapl_phonebook b on a.Sender = b.ph_tel ";
/*  64 */       sql = sql + "where a.Sender like '%" + condtion + "%' or a.MsgArrivedTime like '%" + condtion + "%' or ";
/*  65 */       sql = sql + "a.Sender like '%" + condtion + "%' or b.ph_personname like '%" + condtion + "%' or ";
/*  66 */       sql = sql + "b.ph_companyname like '%" + condtion + "%') as rownum, ";
/*  67 */       sql = sql + "a.MsgArrivedTime,a.MsgTitle,a.sender,b.* from msg_inbox a ";
/*  68 */       sql = sql + "left join ytapl_phonebook b on a.Sender = b.ph_tel ";
/*  69 */       sql = sql + "where a.Sender like '%" + condtion + "%' or a.MsgArrivedTime like '%" + condtion + "%' or ";
/*  70 */       sql = sql + "a.Sender like '%" + condtion + "%' or b.ph_personname like '%" + condtion + "%' or b.ph_companyname like '%" + condtion + "%' ";
/*  71 */       sql = sql + "order by a.MsgArrivedTime desc limit " + start + "," + end;
/*  72 */       conn = getConnection();
/*  73 */       pst = conn.prepareStatement(sql);
/*     */ 
/*  75 */       rs = pst.executeQuery();
/*  76 */       while (rs.next()) {
/*  77 */         MessageReceiveEntity mie = new MessageReceiveEntity();
/*  78 */         mie.setId(Integer.valueOf(rs.getInt("id")));
/*  79 */         mie.setRownum(rs.getString("rownum"));
/*  80 */         mie.setMessage(rs.getString("msgtitle"));
/*  81 */         mie.setMsgarrivedtime(rs.getString("MsgArrivedTime"));
/*  82 */         mie.setPhonecompanyname(rs.getString("ph_companyname") == null ? "" : rs.getString("ph_companyname"));
/*  83 */         mie.setPhonepersonname(rs.getString("ph_personname") == null ? "" : rs.getString("ph_personname"));
/*  84 */         mie.setPhonetel(rs.getString("sender") == null ? "" : rs.getString("sender"));
/*  85 */         mie.setMsgtitle(rs.getString("msgtitle"));
/*  86 */         list.add(mie);
/*     */       }
/*     */     } catch (Exception e) {
/*  89 */       e.printStackTrace();
/*     */     } finally {
/*  91 */       closeDB(rs, pst, conn);
/*     */     }
/*  93 */     return list;
/*     */   }
/*     */ 
/*     */   public List<MessageReceiveEntity> findAllReceiveMsg(String start, String end)
/*     */   {
/* 100 */     List list = new ArrayList();
/* 101 */     Connection conn = null;
/* 102 */     PreparedStatement pst = null;
/* 103 */     ResultSet rs = null;
/*     */     try {
/* 105 */       String sql = "select a.ID,(select count(*) from msg_inbox) as rownum,a.MsgArrivedTime,a.Sender,a.MsgArrivedTime,a.MsgTitle,b.* from msg_inbox a left join ytapl_phonebook b on a.Sender = b.ph_tel order by a.MsgArrivedTime desc limit " + 
/* 108 */         start + "," + end;
/*     */ 
/* 111 */       conn = getConnection();
/* 112 */       pst = conn.prepareStatement(sql);
/*     */ 
/* 114 */       rs = pst.executeQuery();
/* 115 */       while (rs.next()) {
/* 116 */         MessageReceiveEntity mie = new MessageReceiveEntity();
/* 117 */         mie.setId(Integer.valueOf(rs.getInt("id")));
/* 118 */         mie.setRownum(rs.getString("rownum"));
/* 119 */         mie.setMessage(rs.getString("msgtitle"));
/* 120 */         mie.setMsgarrivedtime(rs.getString("MsgArrivedTime"));
/* 121 */         mie.setPhonecompanyname(rs.getString("ph_companyname") == null ? "" : rs.getString("ph_companyname"));
/* 122 */         mie.setPhonepersonname(rs.getString("ph_personname") == null ? "" : rs.getString("ph_personname"));
/* 123 */         mie.setPhonetel(rs.getString("sender") == null ? "" : rs.getString("sender"));
/* 124 */         mie.setMsgtitle(rs.getString("msgtitle"));
/* 125 */         list.add(mie);
/*     */       }
/*     */     } catch (Exception e) {
/* 128 */       e.printStackTrace();
/*     */     } finally {
/* 130 */       closeDB(rs, pst, conn);
/*     */     }
/* 132 */     return list;
/*     */   }
/*     */ 
/*     */   public List<MessageInfoEntity> findAllRecordByCondtion(String table, String condtion, String start, String end)
/*     */   {
/* 140 */     List list = new ArrayList();
/* 141 */     Connection conn = null;
/* 142 */     PreparedStatement pst = null;
/* 143 */     ResultSet rs = null;
/*     */     try
/*     */     {
/* 146 */       String sql = "select a.ID,(select count(*) from " + table + 
/* 147 */         " a left join ytapl_phonebook b on a.Receiver = b.ph_tel where a.MsgTitle like '%" + 
/* 148 */         condtion + "%' or a.SendTime like '%" + condtion + 
/* 149 */         "%' or b.ph_companyname like '%" + 
/* 150 */         condtion + "%' or b.ph_personname like '%" + 
/* 151 */         condtion + "%' or b.ph_tel like '%" + 
/* 152 */         condtion + "%' order by a.SendTime desc) as rownums, b.*,a.MsgTitle,a.SendTime,a.ID " + 
/* 153 */         "from " + table + " a left join ytapl_phonebook b on a.Receiver = b.ph_tel where a.MsgTitle like '%" + 
/* 154 */         condtion + "%' or a.SendTime like '%" + condtion + 
/* 155 */         "%' or b.ph_companyname like '%" + condtion + "%' or b.ph_personname like '%" + 
/* 156 */         condtion + "%' or b.ph_tel like '%" + condtion + "%' order by a.SendTime desc limit " + start + "," + end;
/*     */ 
/* 159 */       conn = getConnection();
/* 160 */       pst = conn.prepareStatement(sql);
/* 161 */       System.out.println("--->" + sql);
/* 162 */       rs = pst.executeQuery();
/* 163 */       while (rs.next()) {
/* 164 */         MessageInfoEntity mie = new MessageInfoEntity();
/* 165 */         mie.setMcontent(rs.getString("MsgTitle"));
/* 166 */         mie.setMdatetime(rs.getString("SendTime"));
/* 167 */         mie.setMid(Integer.valueOf(rs.getInt("ID")));
/* 168 */         mie.setMreccompanyname(rs.getString("ph_companyname"));
/* 169 */         mie.setMrecepersonname(rs.getString("ph_personname"));
/* 170 */         mie.setMtelno(rs.getString("ph_tel"));
/* 171 */         mie.setMtype("msg_failedbox");
/* 172 */         mie.setRownums(rs.getString("rownums"));
/* 173 */         list.add(mie);
/*     */       }
/*     */     } catch (Exception e) {
/* 176 */       e.printStackTrace();
/*     */     } finally {
/* 178 */       closeDB(rs, pst, conn);
/*     */     }
/* 180 */     return list;
/*     */   }
/*     */ 
/*     */   public List<MessageInfoEntity> findAllSendingFailedRecord(String start, String end)
/*     */   {
/* 189 */     List list = new ArrayList();
/* 190 */     Connection conn = null;
/* 191 */     PreparedStatement pst = null;
/* 192 */     ResultSet rs = null;
/*     */     try {
/* 194 */       String sql = "select (select count(*) from msg_failedbox) as rownums, b.*,a.MsgTitle,a.SendTime,a.ID from msg_failedbox a left join ytapl_phonebook b on a.Receiver = b.ph_tel order by a.SendTime desc limit " + start + "," + end;
/*     */ 
/* 196 */       conn = getConnection();
/* 197 */       pst = conn.prepareStatement(sql);
/* 198 */       rs = pst.executeQuery();
/* 199 */       while (rs.next()) {
/* 200 */         MessageInfoEntity mie = new MessageInfoEntity();
/* 201 */         mie.setMcontent(rs.getString("MsgTitle"));
/* 202 */         mie.setMdatetime(rs.getString("SendTime"));
/* 203 */         mie.setMid(Integer.valueOf(rs.getInt("ID")));
/* 204 */         mie.setMreccompanyname(rs.getString("ph_companyname"));
/* 205 */         mie.setMrecepersonname(rs.getString("ph_personname"));
/* 206 */         mie.setMtelno(rs.getString("ph_tel"));
/* 207 */         mie.setMtype("msg_failedbox");
/* 208 */         mie.setRownums(rs.getString("rownums"));
/* 209 */         list.add(mie);
/*     */       }
/*     */     } catch (Exception e) {
/* 212 */       e.printStackTrace();
/*     */     } finally {
/* 214 */       closeDB(rs, pst, conn);
/*     */     }
/* 216 */     return list;
/*     */   }
/*     */ 
/*     */   public List<MessageInfoEntity> findAllSendingRecord(String start, String end)
/*     */   {
/* 223 */     List list = new ArrayList();
/* 224 */     Connection conn = null;
/* 225 */     PreparedStatement pst = null;
/* 226 */     ResultSet rs = null;
/*     */     try
/*     */     {
/* 229 */       String sql = "select (select count(*) from msg_sentbox) as rownums,b.*,a.MsgTitle,a.SendTime,a.ID from msg_sentbox a left join ytapl_phonebook b on a.Receiver = b.ph_tel order by a.SendTime desc limit " + start + "," + end;
/*     */ 
/* 231 */       conn = getConnection();
/* 232 */       pst = conn.prepareStatement(sql);
/* 233 */       rs = pst.executeQuery();
/* 234 */       while (rs.next()) {
/* 235 */         MessageInfoEntity mie = new MessageInfoEntity();
/* 236 */         mie.setMcontent(rs.getString("MsgTitle"));
/* 237 */         mie.setMdatetime(rs.getString("SendTime"));
/* 238 */         mie.setMid(Integer.valueOf(rs.getInt("ID")));
/* 239 */         mie.setMreccompanyname(rs.getString("ph_companyname"));
/* 240 */         mie.setMrecepersonname(rs.getString("ph_personname"));
/* 241 */         mie.setMtelno(rs.getString("ph_tel"));
/* 242 */         mie.setMtype("msg_sentbox");
/* 243 */         mie.setRownums(rs.getString("rownums"));
/* 244 */         list.add(mie);
/*     */       }
/*     */     } catch (Exception e) {
/* 247 */       e.printStackTrace();
/*     */     } finally {
/* 249 */       closeDB(rs, pst, conn);
/*     */     }
/* 251 */     return list;
/*     */   }
/*     */ 
/*     */   private Connection getConnection()
/*     */   {
/* 258 */     Connection conn = null;
/*     */     try {
/* 260 */       this.session = HibernateUtil.openSession();
/* 261 */       conn = this.session.connection();
/*     */     } catch (Exception e) {
/* 263 */       e.printStackTrace();
/*     */     }
/* 265 */     return conn;
/*     */   }
/*     */ 
/*     */   private void closeDB(ResultSet set, PreparedStatement pst, Connection con) {
/*     */     try {
/* 270 */       if (set != null) {
/* 271 */         set.close();
/*     */       }
/* 273 */       if (pst != null) {
/* 274 */         pst.close();
/*     */       }
/* 276 */       if (con != null)
/* 277 */         con.close();
/*     */     }
/*     */     catch (Exception e) {
/* 280 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void closeDB(ResultSet set, Statement stmt, Connection con) {
/*     */     try {
/* 286 */       if (set != null) {
/* 287 */         set.close();
/*     */       }
/* 289 */       if (stmt != null) {
/* 290 */         stmt.close();
/*     */       }
/* 292 */       if (con != null)
/* 293 */         con.close();
/*     */     }
/*     */     catch (Exception e) {
/* 296 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void rollbackTranscation(Transaction tx)
/*     */   {
/*     */     try {
/* 303 */       if (tx != null)
/* 304 */         tx.rollback();
/*     */     }
/*     */     catch (Exception e) {
/* 307 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public List<PhoneBookEntity> findPhoneBookByNames(String names)
/*     */   {
/* 317 */     List list = new ArrayList();
/* 318 */     Connection con = null;
/* 319 */     PreparedStatement pst = null;
/* 320 */     ResultSet rs = null;
/*     */     try {
/* 322 */       String sql = "select * from ytapl_phonebook where ph_personname in (" + names + ")";
/* 323 */       con = getConnection();
/* 324 */       pst = con.prepareStatement(sql);
/* 325 */       rs = pst.executeQuery();
/* 326 */       while (rs.next()) {
/* 327 */         PhoneBookEntity phonebook = new PhoneBookEntity();
/* 328 */         phonebook.setCompanyname(rs.getString("ph_companyname"));
/* 329 */         phonebook.setId(Integer.valueOf(rs.getInt("ph_id")));
/* 330 */         phonebook.setPersonname(rs.getString("ph_personname"));
/* 331 */         phonebook.setPhdate(rs.getString("ph_date"));
/* 332 */         phonebook.setTelno(rs.getString("ph_tel"));
/* 333 */         list.add(phonebook);
/*     */       }
/*     */     } catch (Exception ex) {
/* 336 */       ex.printStackTrace();
/* 337 */       this.log.error("查询数据失败！", ex);
/*     */     } finally {
/* 339 */       closeDB(rs, pst, con);
/*     */     }
/* 341 */     return list;
/*     */   }
/*     */ 
/*     */   public List<PhoneBookEntity> findAll()
/*     */   {
/* 348 */     List list = new ArrayList();
/*     */     try {
/* 350 */       this.session = HibernateUtil.openSession();
/* 351 */       Query query = this.session.createQuery("from PhoneBookEntity");
/* 352 */       list = query.list();
/*     */     } catch (Exception ex) {
/* 354 */       this.log.error("查询数据失败！", ex);
/*     */     }
/* 356 */     return list;
/*     */   }
/*     */ 
/*     */   public boolean deletePhoneBooks(String ids)
/*     */   {
/* 364 */     boolean flag = false;
/* 365 */     Connection conn = null;
/* 366 */     PreparedStatement pst = null;
/*     */     try {
/* 368 */       conn = getConnection();
/* 369 */       String sql = "delete from YTAPL_Phonebook where ph_id in (" + ids + ")";
/* 370 */       pst = conn.prepareStatement(sql);
/* 371 */       int count = pst.executeUpdate();
/* 372 */       if (count > 0)
/* 373 */         flag = true;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 377 */       ex.printStackTrace();
/* 378 */       this.log.error("删除数据失败请重试！", ex);
/*     */     } finally {
/* 380 */       closeDB(null, pst, conn);
/*     */     }
/* 382 */     return flag;
/*     */   }
/*     */ 
/*     */   public boolean updatePhoneBook(PhoneBookEntity phonebook)
/*     */   {
/* 390 */     boolean flag = false;
/*     */     try {
/* 392 */       this.session = HibernateUtil.openSession();
/* 393 */       Transaction tran = this.session.beginTransaction();
/* 394 */       this.session.update(phonebook);
/* 395 */       tran.commit();
/* 396 */       flag = true;
/*     */     } catch (Exception e) {
/* 398 */       flag = false;
/* 399 */       this.log.error("更新数据失败", e);
/* 400 */       e.printStackTrace();
/*     */     }
/* 402 */     return flag;
/*     */   }
/*     */ 
/*     */   public Integer savePhoneBook(PhoneBookEntity phonebook)
/*     */   {
/* 409 */     Integer id = new Integer(0);
/*     */     try {
/* 411 */       this.session = HibernateUtil.openSession();
/* 412 */       Transaction tran = this.session.beginTransaction();
/* 413 */       this.session.save(phonebook);
/* 414 */       id = phonebook.getId();
/* 415 */       tran.commit();
/*     */     } catch (Exception e) {
/* 417 */       e.printStackTrace();
/*     */     }
/* 419 */     return id;
/*     */   }
/*     */ 
/*     */   public boolean addMessage(List<MessageInfoEntity> list)
/*     */   {
/* 429 */     boolean flag = false;
/* 430 */     Connection conn = null;
/* 431 */     PreparedStatement pst = null;
/* 432 */     ResultSet rs = null;
/* 433 */     Transaction tx = null;
/*     */     try {
/* 435 */       conn = getConnection();
/* 436 */       tx = this.session.beginTransaction();
/* 437 */       conn.setAutoCommit(false);
/* 438 */       String sql = "insert into ytapl_smssend(sm_tel,sm_mcontent,sm_date,sm_type) values (?,?,?,?)";
/* 439 */       pst = conn.prepareStatement(sql);
/* 440 */       for (MessageInfoEntity mg : list)
/*     */       {
/* 442 */         pst.setString(1, mg.getMtelno());
/* 443 */         pst.setString(2, mg.getMcontent());
/* 444 */         pst.setString(3, mg.getMdatetime());
/* 445 */         pst.setString(4, mg.getMtype());
/* 446 */         pst.addBatch();
/*     */       }
/* 448 */       int[] counts = pst.executeBatch();
/* 449 */       conn.commit();
/* 450 */       tx.commit();
/*     */     } catch (Exception e) {
/* 452 */       rollbackTranscation(tx);
/* 453 */       this.log.error("发送短信失败---》", e);
/* 454 */       System.out.println("发送短信失败");
/* 455 */       e.fillInStackTrace();
/*     */     } finally {
/* 457 */       closeDB(null, pst, conn);
/*     */     }
/* 459 */     return flag;
/*     */   }
/*     */ 
/*     */   public boolean deleteMsgRecById(String id, String tablename)
/*     */   {
/* 465 */     boolean flag = false;
/* 466 */     Connection conn = null;
/* 467 */     PreparedStatement pst = null;
/*     */     try {
/* 469 */       conn = getConnection();
/* 470 */       String sql = "delete from " + tablename + " where ID in (" + id + ")";
/* 471 */       pst = conn.prepareStatement(sql);
/* 472 */       int count = pst.executeUpdate();
/* 473 */       if (count > 0) {
/* 474 */         flag = true;
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 479 */       ex.printStackTrace();
/* 480 */       this.log.error("删除数据失败请重试！", ex);
/*     */     } finally {
/* 482 */       closeDB(null, pst, conn);
/*     */     }
/* 484 */     return flag;
/*     */   }
/*     */ }

/* Location:           G:\中普友通\hbsaserver\hbsaserver\WEB-INF\classes\
 * Qualified Name:     cn.com.youtong.apollo.sms.db.SMSDao
 * JD-Core Version:    0.6.2
 */