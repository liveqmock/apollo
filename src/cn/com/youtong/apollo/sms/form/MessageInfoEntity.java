/*    */ package cn.com.youtong.apollo.sms.form;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class MessageInfoEntity
/*    */   implements Serializable
/*    */ {
/*    */   private String rownums;
/*    */   private Integer mid;
/*    */   private String mtelno;
/*    */   private String mcontent;
/*    */   private String mdatetime;
/*    */   private String mtype;
/*    */   private String mrecepersonname;
/*    */   private String mreccompanyname;
/*    */ 
/*    */   public String getMrecepersonname()
/*    */   {
/* 39 */     return this.mrecepersonname;
/*    */   }
/*    */   public void setMrecepersonname(String mrecepersonname) {
/* 42 */     this.mrecepersonname = mrecepersonname;
/*    */   }
/*    */   public String getMreccompanyname() {
/* 45 */     return this.mreccompanyname;
/*    */   }
/*    */   public void setMreccompanyname(String mreccompanyname) {
/* 48 */     this.mreccompanyname = mreccompanyname;
/*    */   }
/*    */   public Integer getMid() {
/* 51 */     return this.mid;
/*    */   }
/*    */   public void setMid(Integer mid) {
/* 54 */     this.mid = mid;
/*    */   }
/*    */   public String getMtelno() {
/* 57 */     return this.mtelno;
/*    */   }
/*    */   public void setMtelno(String mtelno) {
/* 60 */     this.mtelno = mtelno;
/*    */   }
/*    */   public String getMcontent() {
/* 63 */     return this.mcontent;
/*    */   }
/*    */   public void setMcontent(String mcontent) {
/* 66 */     this.mcontent = mcontent;
/*    */   }
/*    */   public String getMdatetime() {
/* 69 */     return this.mdatetime;
/*    */   }
/*    */   public void setMdatetime(String mdatetime) {
/* 72 */     this.mdatetime = mdatetime;
/*    */   }
/*    */ 
/*    */   public String getMtype()
/*    */   {
/* 78 */     return this.mtype;
/*    */   }
/*    */ 
/*    */   public void setMtype(String mtype)
/*    */   {
/* 84 */     this.mtype = mtype;
/*    */   }
/*    */   public String getRownums() {
/* 87 */     return this.rownums;
/*    */   }
/*    */   public void setRownums(String rownums) {
/* 90 */     this.rownums = rownums;
/*    */   }
/*    */ }

/* Location:           G:\中普友通\hbsaserver\hbsaserver\WEB-INF\classes\
 * Qualified Name:     cn.com.youtong.apollo.sms.form.MessageInfoEntity
 * JD-Core Version:    0.6.2
 */