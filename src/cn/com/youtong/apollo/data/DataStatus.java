package cn.com.youtong.apollo.data;

/**
 * �ϱ������ݴ�������״̬,
 * <ol>
 *    <li>�ϱ�</li>
 *    <li>���ͨ��</li>
 *    <li>�˻�</li>
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
         * �ϱ��ͷ��״̬
         */
        public final static int UNREPORTED_UNENVLOP = 0;
        public final static int UNREPORTED_ENVLOP =3;
        public final static int REPORTED_UNENVLOP =4;
        public final static int REPORTED_ENVLOP=5;
        
        /**
         * ���ͨ��״̬
         */
        public final static int AUDITED = 1;
        /**
         * �˻�״̬
         */
        public final static int WITHDRAWAL = 2;

        /**
         * �������
         */
//        public final static int ENVLOP = 3;

        /**
         * û���ϱ�
         */
        public final static int UNREPORT = -100;
}

