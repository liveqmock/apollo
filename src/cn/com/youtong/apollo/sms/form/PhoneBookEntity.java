/*    */ package cn.com.youtong.apollo.sms.form;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class PhoneBookEntity
/*    */   implements Serializable
/*    */ {
/*    */   private Integer id;
/*    */   private String companyname;
/*    */   private String personname;
/*    */   private String telno;
/*    */   private String phdate;
/*    */ 
/*    */   public String getPhdate()
/*    */   {
/* 14 */     return this.phdate;
/*    */   }
/*    */   public void setPhdate(String phdate) {
/* 17 */     this.phdate = phdate;
/*    */   }
/*    */   public Integer getId() {
/* 20 */     return this.id;
/*    */   }
/*    */   public void setId(Integer id) {
/* 23 */     this.id = id;
/*    */   }
/*    */   public String getCompanyname() {
/* 26 */     return this.companyname;
/*    */   }
/*    */   public void setCompanyname(String companyname) {
/* 29 */     this.companyname = companyname;
/*    */   }
/*    */   public String getPersonname() {
/* 32 */     return this.personname;
/*    */   }
/*    */   public void setPersonname(String personname) {
/* 35 */     this.personname = personname;
/*    */   }
/*    */   public String getTelno() {
/* 38 */     return this.telno;
/*    */   }
/*    */   public void setTelno(String telno) {
/* 41 */     this.telno = telno;
/*    */   }
/*    */ }

/* Location:           G:\中普友通\hbsaserver\hbsaserver\WEB-INF\classes\
 * Qualified Name:     cn.com.youtong.apollo.sms.form.PhoneBookEntity
 * JD-Core Version:    0.6.2
 */