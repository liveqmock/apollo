package cn.com.youtong.apollo.data;

/**
 * 上报的数据存在三种状态,
 * <ol>
 *    <li>上报</li>
 *    <li>审核通过</li>
 *    <li>退回</li>
 * </ol>
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yutop</p>
 * @author wjb
 * @version 1.0
 */
public interface DataStatus
{
        /**
         * 上报和封存状态
         */
        public final static int UNREPORTED_UNENVLOP = 0;
        public final static int UNREPORTED_ENVLOP =3;
        public final static int REPORTED_UNENVLOP =4;
        public final static int REPORTED_ENVLOP=5;
        
        /**
         * 审核通过状态
         */
        public final static int AUDITED = 1;
        /**
         * 退回状态
         */
        public final static int WITHDRAWAL = 2;

        /**
         * 封存数据
         */
//        public final static int ENVLOP = 3;

        /**
         * 没有上报
         */
        public final static int UNREPORT = -100;
}

