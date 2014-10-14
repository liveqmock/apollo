package cn.com.youtong.apollo.tabtree.comm;

import com.cc.framework.common.InvalidEnumType;
import com.cc.framework.common.SimpleEnumType2;

/**
 * Enumeration of Region Types
 *
 * @author		<a href="mailto:hschulz@scc-gmbh.com">Harald Schulz</a>
 * @version	$Revision: 1.5 $
 */
public class RegionType implements com.cc.framework.common.SimpleEnumType2 {

        /** RegionType: NONE */
        public static final RegionType NONE		= new RegionType("N", "None");

        /** RegionType: WORLD */
        public static final RegionType WORLD	= new RegionType("W", "World");

        /** RegionType: GROUP */
        public static final RegionType GROUP	= new RegionType("G", "Group");

        /** RegionType: COUNTRY */
        public static final RegionType COUNTRY	= new RegionType("C", "Country");

        /**
         * Collection of all possible Types
         */
        private static final RegionType[] ALL = {GROUP, COUNTRY};

        /**
         * Internal Key
         */
        private String key = "";

        /**
         * Display value for the Key
         */
        private String value = "";


        // -------------------------------------------------
        //                    Methods
        // -------------------------------------------------

        /**
         * Constructor
         * @param	key		Key
         * @param	value	Display-value for the Key
         */
        public RegionType(String key, String value) {
                super();
                this.key = key;
                this.value = value;
        }

        /**
         * @see SimpleEnumType2#elements()
         */
        public SimpleEnumType2[] elements() {
                return ALL;
        }

        /**
         * @see SimpleEnumType2#getKey()
         */
        public String getKey() {
                return key;
        }

        /**
         * @see SimpleEnumType2#getValue()
         */
        public String getValue() {
                return value;
        }

        /**
         * Compares two Objects with there Keys
         * @param	obj	Object to compare
         * @return	boolean
         */
        public boolean equals(Object obj) {

                if (obj instanceof RegionType) {
                        RegionType other = (RegionType) obj;

                        return key.equals(other.getKey());
                }

                return super.equals(obj);
        }

        /**
         * Compares the argument with an internal stored Object
         * @param	code	String to compare
         * @return	RegionType
         * @throws	InvalidEnumType	If the argument is not of this type
         */
        public static RegionType parse(String code) throws InvalidEnumType {

                for (int index = 0; index < ALL.length; index++) {
                        if (ALL[index].getKey().equals(code)) {
                                return ALL[index];
                        }
                }

                throw new InvalidEnumType("Invalid RegionType: " + code);
        }

        /**
         * Returns the Key
         * @return	String
         */
        public String toString() {
                return key;
        }

}
