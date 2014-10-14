/*    */ package cn.com.youtong.apollo.sms.util;
/*    */ 
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ 
/*    */ public class SMSUtil
/*    */ {
/*    */   public static String getSystemDate(Date date)
/*    */   {
/* 12 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 13 */     String d = sdf.format(date);
/* 14 */     return d;
/*    */   }
/*    */ }

/* Location:           G:\中普友通\hbsaserver\hbsaserver\WEB-INF\classes\
 * Qualified Name:     cn.com.youtong.apollo.sms.util.SMSUtil
 * JD-Core Version:    0.6.2
 */