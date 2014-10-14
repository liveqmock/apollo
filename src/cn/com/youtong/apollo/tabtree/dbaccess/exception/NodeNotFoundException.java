package cn.com.youtong.apollo.tabtree.dbaccess.exception;

public class NodeNotFoundException extends Exception {

        /**
         * Constructor
         */
        public NodeNotFoundException() {
                super();
        }

        /**
         * Constructor
         * @param message	Message
         */
        public NodeNotFoundException(String message) {
                super(message);
        }

}
