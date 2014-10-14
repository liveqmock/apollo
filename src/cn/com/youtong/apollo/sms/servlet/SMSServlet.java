/*     */ package cn.com.youtong.apollo.sms.servlet;
/*     */ 
/*     */ import cn.com.youtong.apollo.common.Warning;
/*     */ import cn.com.youtong.apollo.services.Factory;
/*     */ import cn.com.youtong.apollo.servlet.RootServlet;
/*     */ import cn.com.youtong.apollo.sms.db.SMSDao;
/*     */ import cn.com.youtong.apollo.sms.form.MessageInfoEntity;
/*     */ import cn.com.youtong.apollo.sms.form.MessageReceiveEntity;
/*     */ import cn.com.youtong.apollo.sms.form.PhoneBookEntity;
/*     */ import cn.com.youtong.apollo.sms.util.SMSUtil;
/*     */ import cn.com.youtong.apollo.usermanager.User;
/*     */ import cn.com.youtong.apollo.usermanager.UserManager;
/*     */ import cn.com.youtong.apollo.usermanager.UserManagerFactory;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import net.sf.json.JSONArray;
/*     */ 
/*     */ public class SMSServlet extends RootServlet
/*     */   implements Serializable
/*     */ {
/*     */   public static final String MANAGERMESSAGE_PAGE = "/jsp/smsManager/index.jsp";
/*     */   public static final String SENDING_PAGE = "/jsp/smsManager/sending.jsp";
/*     */   public static final String SENDMESGHISTORY_PAGE = "/jsp/smsManager/sendmesghistory.jsp";
/*     */   public static final String SHOW_RECEIVEMSGHISTORY_PAGE = "/jsp/smsManager/receivemsghistory.jsp";
/*     */   public static final String SHOW_SENDMESSAGE_PAGE = "showSMSPage";
/*     */   public static final String SHOW_SENDING_PAGE = "sending";
/*     */   public static final String SUBMIT_SENDING_MESSAGE = "sendmsg";
/*     */   public static final String GET_PHONEBOOK_MESSAGE = "getphonebook";
/*     */   public static final String QUICKFIND_PERSON_NAME = "qucikfindpersonname";
/*     */   public static final String SAVE_PHONEBOOK = "savephonebook";
/*     */   public static final String UPDATE_PHONEBOOK = "updatephonebook";
/*     */   public static final String DELETE_PHONEBOOK = "deletephonebook";
/*     */   public static final String SHOW_SENDMESGHISTORY_PAGE = "sendmesghistory";
/*     */   public static final String FIND_ALL_SENDING_RECORD = "findallsendingrecord";
/*     */   public static final String DELETE_MSG_BY_ID = "deletemsgbyid";
/*     */   public static final String QUICK_FIND_MESSAGE = "quickfindmessage";
/*     */   public static final String SHOW_RECEIVEMSGHISTORY = "showreceivemsghistory";
/*     */   public static final String FIND_ALL_RECEIVE_MSG_Info = "findallreceivemsginfo";
/*     */   public static final String FIND_ALL_RECEIVE_MSG_InfoBYCONDTION = "findallreceivemsginfobycondtion";
/*     */   public static final String DELETE_RECEIVE_MSG_BYID = "deletereceivemsgbyid";
/*     */   public static final String DELETE_RECEIVE_ALL_MSG = "deletereceiveallmsg";
/*     */ 
/*     */   public void perform(HttpServletRequest req, HttpServletResponse res)
/*     */     throws ServletException, IOException, Warning
/*     */   {
/*  67 */     if (req.getParameter("operation") != null) {
/*  68 */       if ("sendmesghistory".equals(req.getParameter("operation")))
/*     */       {
/*  70 */         showHistoryPage(req, res);
/*  71 */         return;
/*     */       }
/*  73 */       if (req.getParameter("operation").equals("showSMSPage")) {
/*  74 */         showSendMessagePage(req, res);
/*  75 */         return;
/*     */       }
/*  77 */       if (req.getParameter("operation").equals("sending")) {
/*  78 */         showSendingPage(req, res);
/*  79 */         return;
/*     */       }
/*  81 */       if ("sendmsg".equals(req.getParameter("operation"))) {
/*  82 */         sendingMessage(req, res);
/*  83 */         return;
/*     */       }
/*  85 */       if ("getphonebook".equals(req.getParameter("operation")))
/*     */       {
/*  87 */         getPhoneBook(req, res);
/*  88 */         return;
/*     */       }
/*     */ 
/*  93 */       if ("qucikfindpersonname".equals(req.getParameter("operation"))) {
/*  94 */         qucikfindpersonname(req, res);
/*  95 */         return;
/*     */       }
/*     */ 
/* 100 */       if ("savephonebook".equals(req.getParameter("operation"))) {
/* 101 */         savephonebook(req, res);
/* 102 */         return;
/*     */       }
/*     */ 
/* 107 */       if ("updatephonebook".equals(req.getParameter("operation"))) {
/* 108 */         updatephonebook(req, res);
/* 109 */         return;
/*     */       }
/*     */ 
/* 114 */       if ("deletephonebook".equals(req.getParameter("operation"))) {
/* 115 */         deletephonebook(req, res);
/* 116 */         return;
/*     */       }
/*     */ 
/* 122 */       if ("findallsendingrecord".equals(req.getParameter("operation"))) {
/* 123 */         findAllSendingRecord(req, res);
/* 124 */         return;
/*     */       }
/*     */ 
/* 129 */       if ("deletemsgbyid".equals(req.getParameter("operation"))) {
/* 130 */         deleteMsgById(req, res);
/* 131 */         return;
/*     */       }
/*     */ 
/* 136 */       if ("quickfindmessage".equals(req.getParameter("operation"))) {
/* 137 */         quickfindmessage(req, res);
/* 138 */         return;
/*     */       }
/*     */ 
/* 143 */       if ("showreceivemsghistory".equals(req.getParameter("operation"))) {
/* 144 */         showreceivemsghistorypage(req, res);
/* 145 */         return;
/*     */       }
/*     */ 
/* 150 */       if ("findallreceivemsginfo".equals(req.getParameter("operation"))) {
/* 151 */         findAllReceiveMsgInfo(req, res);
/* 152 */         return;
/*     */       }
/*     */ 
/* 157 */       if ("findallreceivemsginfobycondtion".equals(req.getParameter("operation"))) {
/* 158 */         findAllReceiveMsgInfoByCondtion(req, res);
/* 159 */         return;
/*     */       }
/*     */ 
/* 164 */       if ("deletereceivemsgbyid".equals(req.getParameter("operation")))
/*     */       {
/* 166 */         deleteReceiveMsgById(req, res);
/* 167 */         return;
/*     */       }
/*     */ 
/* 172 */       if ("deletereceiveallmsg".equals(req.getParameter("operation")))
/*     */       {
/* 174 */         deleteReceiveAllMsg(req, res);
/* 175 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void deleteReceiveAllMsg(HttpServletRequest req, HttpServletResponse res)
/*     */     throws Warning, IOException, ServletException
/*     */   {
/* 188 */     SMSDao sdao = new SMSDao();
/* 189 */     String name = req.getParameter("name");
/* 190 */     boolean flag = sdao.deleteAllMessageInfoByTableName(name);
/* 191 */     if (flag)
/* 192 */       outString("{success:true}", res);
/*     */     else
/* 194 */       outString("{success:false}", res);
/*     */   }
/*     */ 
/*     */   public void deleteReceiveMsgById(HttpServletRequest req, HttpServletResponse res)
/*     */     throws Warning, IOException, ServletException
/*     */   {
/* 203 */     String id = req.getParameter("id");
/* 204 */     String tablename = req.getParameter("tablename");
/* 205 */     SMSDao sdao = new SMSDao();
/* 206 */     boolean flag = sdao.deleteMsgRecById(id, tablename);
/* 207 */     if (flag)
/* 208 */       outString("{success:true}", res);
/*     */     else
/* 210 */       outString("{success:false}", res);
/*     */   }
/*     */ 
/*     */   public void findAllReceiveMsgInfo(HttpServletRequest req, HttpServletResponse res)
/*     */     throws Warning, IOException, ServletException
/*     */   {
/* 220 */     String start = req.getParameter("start");
/* 221 */     String limit = req.getParameter("limit");
/* 222 */     String name = req.getParameter("names");
/* 223 */     SMSDao sdao = new SMSDao();
/* 224 */     List list = new ArrayList();
/* 225 */     list = sdao.findAllReceiveMsg(start, limit);
/* 226 */     JSONArray array = JSONArray.fromObject(list);
/* 227 */     StringBuilder builder = new StringBuilder();
/* 228 */     String size = "0";
/* 229 */     if (list.size() != 0) {
/* 230 */       size = ((MessageReceiveEntity)list.get(0)).getRownum();
/*     */     }
/* 232 */     builder.append("{'totalCount':" + size + ",'data':");
/* 233 */     builder.append(array.toString());
/* 234 */     builder.append("}");
/* 235 */     String str = builder.toString();
/* 236 */     outString(str, res);
/*     */   }
/*     */ 
/*     */   public void findAllReceiveMsgInfoByCondtion(HttpServletRequest req, HttpServletResponse res)
/*     */     throws Warning, IOException, ServletException
/*     */   {
/* 244 */     String start = req.getParameter("start");
/* 245 */     String limit = req.getParameter("limit");
/* 246 */     String name = req.getParameter("names");
/* 247 */     SMSDao sdao = new SMSDao();
/* 248 */     String v = req.getParameter("v");
/* 249 */     String condtion = URLDecoder.decode(v, "utf-8");
/* 250 */     List list = new ArrayList();
/* 251 */     list = sdao.findAllReceiveMsgByCondtion(condtion, start, limit);
/* 252 */     JSONArray array = JSONArray.fromObject(list);
/* 253 */     StringBuilder builder = new StringBuilder();
/* 254 */     String size = "0";
/* 255 */     if (list.size() != 0) {
/* 256 */       size = ((MessageReceiveEntity)list.get(0)).getRownum();
/*     */     }
/* 258 */     builder.append("{'totalCount':" + size + ",'data':");
/* 259 */     builder.append(array.toString());
/* 260 */     builder.append("}");
/* 261 */     String str = builder.toString();
/* 262 */     outString(str, res);
/*     */   }
/*     */ 
/*     */   public void showreceivemsghistorypage(HttpServletRequest req, HttpServletResponse res)
/*     */     throws Warning, IOException, ServletException
/*     */   {
/* 270 */     go2UrlWithAttibute(req, res, "/jsp/smsManager/receivemsghistory.jsp");
/*     */   }
/*     */ 
/*     */   public void quickfindmessage(HttpServletRequest req, HttpServletResponse res)
/*     */     throws Warning, IOException, ServletException
/*     */   {
/* 278 */     String start = req.getParameter("start");
/* 279 */     String limit = req.getParameter("limit");
/* 280 */     String name = req.getParameter("names");
/* 281 */     String v = req.getParameter("v");
/* 282 */     String condtion = URLDecoder.decode(v, "utf-8");
/* 283 */     String table = "msg_sentbox";
/* 284 */     SMSDao sdao = new SMSDao();
/* 285 */     List list = new ArrayList();
/* 286 */     if ("yes".equals(name))
/*     */     {
/* 288 */       System.out.println("yes");
/* 289 */       table = "msg_sentbox";
/* 290 */     } else if ("no".equals(name)) {
/* 291 */       System.out.println("no");
/* 292 */       table = "msg_failedbox";
/*     */     } else {
/* 294 */       System.out.println("else");
/* 295 */       table = "msg_sentbox";
/*     */     }
/* 297 */     list = sdao.findAllRecordByCondtion(table, condtion, start, limit);
/* 298 */     JSONArray array = JSONArray.fromObject(list);
/* 299 */     StringBuilder builder = new StringBuilder();
/* 300 */     String size = "0";
/* 301 */     if (list.size() != 0) {
/* 302 */       size = ((MessageInfoEntity)list.get(0)).getRownums();
/*     */     }
/* 304 */     builder.append("{'totalCount':" + size + ",'data':");
/* 305 */     builder.append(array.toString());
/* 306 */     builder.append("}");
/* 307 */     String str = builder.toString();
/* 308 */     outString(str, res);
/*     */   }
/*     */ 
/*     */   public void deleteMsgById(HttpServletRequest req, HttpServletResponse res)
/*     */     throws Warning, IOException, ServletException
/*     */   {
/* 315 */     String id = req.getParameter("mid");
/* 316 */     String tablename = req.getParameter("tablename");
/* 317 */     SMSDao sdao = new SMSDao();
/* 318 */     boolean flag = sdao.deleteMsgRecById(id, tablename);
/* 319 */     if (flag)
/* 320 */       outString("{success:true}", res);
/*     */     else
/* 322 */       outString("{success:false}", res);
/*     */   }
/*     */ 
/*     */   public void findAllSendingRecord(HttpServletRequest req, HttpServletResponse res)
/*     */     throws Warning, IOException, ServletException
/*     */   {
/* 335 */     String start = req.getParameter("start");
/* 336 */     String limit = req.getParameter("limit");
/* 337 */     String name = req.getParameter("names");
/* 338 */     SMSDao sdao = new SMSDao();
/* 339 */     List list = new ArrayList();
/* 340 */     if ("yes".equals(name))
/*     */     {
/* 342 */       list = sdao.findAllSendingRecord(start, limit);
/* 343 */     } else if ("no".equals(name))
/* 344 */       list = sdao.findAllSendingFailedRecord(start, limit);
/*     */     else {
/* 346 */       list = sdao.findAllSendingRecord(start, limit);
/*     */     }
/* 348 */     JSONArray array = JSONArray.fromObject(list);
/* 349 */     StringBuilder builder = new StringBuilder();
/* 350 */     String size = "0";
/* 351 */     if (list.size() != 0) {
/* 352 */       size = ((MessageInfoEntity)list.get(0)).getRownums();
/*     */     }
/* 354 */     builder.append("{'totalCount':" + size + ",'data':");
/* 355 */     builder.append(array.toString());
/* 356 */     builder.append("}");
/* 357 */     String str = builder.toString();
/* 358 */     outString(str, res);
/*     */   }
/*     */ 
/*     */   private void showHistoryPage(HttpServletRequest req, HttpServletResponse res)
/*     */     throws Warning, IOException, ServletException
/*     */   {
/* 372 */     go2UrlWithAttibute(req, res, "/jsp/smsManager/sendmesghistory.jsp");
/*     */   }
/*     */ 
/*     */   public void deletephonebook(HttpServletRequest req, HttpServletResponse res)
/*     */     throws Warning, IOException, ServletException
/*     */   {
/* 380 */     String ids = req.getParameter("id");
/* 381 */     SMSDao dao = new SMSDao();
/* 382 */     boolean flag = dao.deletePhoneBooks(ids);
/* 383 */     if (flag)
/* 384 */       outString("{success:true,msg:'修改通讯录'}", res);
/*     */     else
/* 386 */       outString("{success:false,msg:'修改通讯录'}", res);
/*     */   }
/*     */ 
/*     */   public void updatephonebook(HttpServletRequest req, HttpServletResponse res)
/*     */     throws Warning, IOException, ServletException
/*     */   {
/* 396 */     String company = req.getParameter("company");
/* 397 */     String name = req.getParameter("name");
/* 398 */     String tel = req.getParameter("tel");
/* 399 */     String id = req.getParameter("id");
/* 400 */     String _company = URLDecoder.decode(company, "utf-8");
/* 401 */     String _name = URLDecoder.decode(name, "utf-8");
/* 402 */     String _tel = URLDecoder.decode(tel, "utf-8");
/* 403 */     PhoneBookEntity phonebook = new PhoneBookEntity();
/* 404 */     phonebook.setId(new Integer(id));
/* 405 */     phonebook.setCompanyname(_company);
/* 406 */     phonebook.setPersonname(_name);
/* 407 */     phonebook.setPhdate(SMSUtil.getSystemDate(new Date()));
/* 408 */     phonebook.setTelno(_tel);
/* 409 */     phonebook.setPhdate(SMSUtil.getSystemDate(new Date()));
/* 410 */     SMSDao dao = new SMSDao();
/* 411 */     boolean flag = dao.updatePhoneBook(phonebook);
/* 412 */     if (flag)
/* 413 */       outString("{success:true,msg:'修改通讯录'}", res);
/*     */     else
/* 415 */       outString("{success:false,msg:'修改通讯录'}", res);
/*     */   }
/*     */ 
/*     */   public void savephonebook(HttpServletRequest req, HttpServletResponse res)
/*     */     throws Warning, IOException, ServletException
/*     */   {
/* 426 */     SMSDao dao = new SMSDao();
/* 427 */     String company = req.getParameter("company");
/* 428 */     String name = req.getParameter("name");
/* 429 */     String tel = req.getParameter("tel");
/* 430 */     String _company = URLDecoder.decode(company, "utf-8");
/* 431 */     String _name = URLDecoder.decode(name, "utf-8");
/* 432 */     String _tel = URLDecoder.decode(tel, "utf-8");
/* 433 */     PhoneBookEntity phonebook = new PhoneBookEntity();
/* 434 */     phonebook.setCompanyname(_company);
/* 435 */     phonebook.setPersonname(_name);
/* 436 */     phonebook.setPhdate(SMSUtil.getSystemDate(new Date()));
/* 437 */     phonebook.setTelno(_tel);
/* 438 */     Integer id = dao.savePhoneBook(phonebook);
/* 439 */     if (id.intValue() != 0)
/* 440 */       outString("{success:true,msg:'" + id.intValue() + "'}", res);
/*     */     else
/* 442 */       outString("{success:false,msg:'" + id.intValue() + "'}", res);
/*     */   }
/*     */ 
/*     */   public void qucikfindpersonname(HttpServletRequest req, HttpServletResponse res)
/*     */     throws Warning, IOException, ServletException
/*     */   {
/* 453 */     String value = req.getParameter("names");
/* 454 */     String content = URLDecoder.decode(value, "utf-8");
/* 455 */     SMSDao sdao = new SMSDao();
/* 456 */     List list = new ArrayList();
/* 457 */     if (("".equals(content)) || (content == null)) {
/* 458 */       list = sdao.findAll();
/*     */     } else {
/* 460 */       String[] str = content.split(" ");
/* 461 */       String names = "'" + content.replaceAll(" ", "','") + "'";
/* 462 */       list = sdao.findPhoneBookByNames(names);
/*     */     }
/* 464 */     JSONArray array = JSONArray.fromObject(list);
/* 465 */     outString(array.toString(), res);
/*     */   }
/*     */ 
/*     */   public void getPhoneBook(HttpServletRequest req, HttpServletResponse res)
/*     */     throws Warning, IOException, ServletException
/*     */   {
/* 474 */     SMSDao sdao = new SMSDao();
/* 475 */     List list = sdao.findAll();
/* 476 */     JSONArray array = JSONArray.fromObject(list);
/* 477 */     outString(array.toString(), res);
/*     */   }
/*     */   public void outString(String str, HttpServletResponse res) {
/*     */     try {
/* 481 */       res.setContentType("text/html;charset=UTF-8");
/* 482 */       res.setCharacterEncoding("utf-8");
/* 483 */       PrintWriter out = res.getWriter();
/* 484 */       out.write(str);
/*     */     } catch (IOException e) {
/* 486 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String changeISOUncode(String str)
/*     */   {
/* 500 */     String name = "";
/*     */     try
/*     */     {
/* 504 */       String utf8_value = str;
/* 505 */       byte[] b = utf8_value.getBytes("ISO-8859-1");
/* 506 */       name = new String(b, "utf-8");
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 511 */       e.printStackTrace();
/*     */     }
/* 513 */     return name;
/*     */   }
/*     */ 
/*     */   private void sendingMessage(HttpServletRequest req, HttpServletResponse res)
/*     */     throws Warning, IOException, ServletException
/*     */   {
/* 528 */     req.setCharacterEncoding("utf-8");
/* 529 */     res.setCharacterEncoding("utf-8");
/* 530 */     String type = "捷报催报";
/*     */ 
/* 532 */     String message = req.getParameter("message");
/*     */ 
/* 534 */     String nos = req.getParameter("telnos");
/*     */ 
/* 536 */     String datetime = req.getParameter("senddate");
/* 537 */     String content = URLDecoder.decode(message, "utf-8");
/* 538 */     String[] numbers = nos.split(",");
/* 539 */     List list = new ArrayList();
/* 540 */     for (String str : numbers) {
/* 541 */       MessageInfoEntity mie = new MessageInfoEntity();
/* 542 */       mie.setMcontent(content);
/* 543 */       mie.setMdatetime(datetime);
/* 544 */       mie.setMtelno(str);
/* 545 */       mie.setMtype(type);
/* 546 */       list.add(mie);
/*     */     }
/* 548 */     SMSDao smsdao = new SMSDao();
/* 549 */     smsdao.addMessage(list);
/* 550 */     outString("{success:true}", res);
/*     */   }
/*     */ 
/*     */   private void showSendingPage(HttpServletRequest req, HttpServletResponse res)
/*     */     throws Warning, IOException, ServletException
/*     */   {
/* 563 */     go2UrlWithAttibute(req, res, "/jsp/smsManager/sending.jsp");
/*     */   }
/*     */ 
/*     */   private void showSendMessagePage(HttpServletRequest req, HttpServletResponse res)
/*     */     throws Warning, IOException, ServletException
/*     */   {
/* 579 */     UserManager userManager = null;
/* 580 */     userManager = ((UserManagerFactory)
/* 581 */       Factory.getInstance(UserManagerFactory.class.getName()))
/* 582 */       .createUserManager();
/* 583 */     User user = userManager.getUserByID(getLoginUser(req).getUserID());
/* 584 */     req.setAttribute("userInfo", user);
/*     */ 
/* 586 */     Collection allUser = userManager.getAllUsers();
/* 587 */     req.setAttribute("allUser", allUser);
/*     */ 
/* 589 */     Collection partGroupInfo = userManager.getUserByID(user.getUserID())
/* 590 */       .getGroups();
/* 591 */     req.setAttribute("partGroupInfo", partGroupInfo);
/*     */ 
/* 593 */     Collection groupInfo = userManager.getGroupsNotContainUser(user
/* 594 */       .getUserID());
/* 595 */     req.setAttribute("groupInfo", groupInfo);
/*     */ 
/* 597 */     Collection allRole = userManager.getAllRoles();
/* 598 */     req.setAttribute("allRole", allRole);
/*     */ 
/* 603 */     go2UrlWithAttibute(req, res, "/jsp/smsManager/index.jsp");
/*     */   }
/*     */ }

/* Location:           G:\中普友通\hbsaserver\hbsaserver\WEB-INF\classes\
 * Qualified Name:     cn.com.youtong.apollo.sms.servlet.SMSServlet
 * JD-Core Version:    0.6.2
 */