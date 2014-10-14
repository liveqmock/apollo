/*     */ package cn.com.youtong.apollo.sms.form;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class MessageReceiveEntity
/*     */   implements Serializable
/*     */ {
/*     */   private Integer id;
/*     */   private String rownum;
/*     */   private String message;
/*     */   private String sender;
/*     */   private String msgarrivedtime;
/*     */   private String msgtitle;
/*     */   private String phonecompanyname;
/*     */   private String phonepersonname;
/*     */   private String phonetel;
/*     */ 
/*     */   public Integer getId()
/*     */   {
/*  20 */     return this.id;
/*     */   }
/*     */ 
/*     */   public void setId(Integer id)
/*     */   {
/*  26 */     this.id = id;
/*     */   }
/*     */ 
/*     */   public String getRownum()
/*     */   {
/*  32 */     return this.rownum;
/*     */   }
/*     */ 
/*     */   public void setRownum(String rownum)
/*     */   {
/*  38 */     this.rownum = rownum;
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/*  44 */     return this.message;
/*     */   }
/*     */ 
/*     */   public void setMessage(String message)
/*     */   {
/*  50 */     this.message = message;
/*     */   }
/*     */ 
/*     */   public String getSender()
/*     */   {
/*  56 */     return this.sender;
/*     */   }
/*     */ 
/*     */   public void setSender(String sender)
/*     */   {
/*  62 */     this.sender = sender;
/*     */   }
/*     */ 
/*     */   public String getMsgarrivedtime()
/*     */   {
/*  68 */     return this.msgarrivedtime;
/*     */   }
/*     */ 
/*     */   public void setMsgarrivedtime(String msgarrivedtime)
/*     */   {
/*  74 */     this.msgarrivedtime = msgarrivedtime;
/*     */   }
/*     */ 
/*     */   public String getMsgtitle()
/*     */   {
/*  80 */     return this.msgtitle;
/*     */   }
/*     */ 
/*     */   public void setMsgtitle(String msgtitle)
/*     */   {
/*  86 */     this.msgtitle = msgtitle;
/*     */   }
/*     */ 
/*     */   public String getPhonecompanyname()
/*     */   {
/*  92 */     return this.phonecompanyname;
/*     */   }
/*     */ 
/*     */   public void setPhonecompanyname(String phonecompanyname)
/*     */   {
/*  99 */     this.phonecompanyname = phonecompanyname;
/*     */   }
/*     */ 
/*     */   public String getPhonepersonname()
/*     */   {
/* 106 */     return this.phonepersonname;
/*     */   }
/*     */ 
/*     */   public void setPhonepersonname(String phonepersonname)
/*     */   {
/* 112 */     this.phonepersonname = phonepersonname;
/*     */   }
/*     */ 
/*     */   public String getPhonetel()
/*     */   {
/* 118 */     return this.phonetel;
/*     */   }
/*     */ 
/*     */   public void setPhonetel(String phonetel)
/*     */   {
/* 124 */     this.phonetel = phonetel;
/*     */   }
/*     */ }

/* Location:           G:\中普友通\hbsaserver\hbsaserver\WEB-INF\classes\
 * Qualified Name:     cn.com.youtong.apollo.sms.form.MessageReceiveEntity
 * JD-Core Version:    0.6.2
 */