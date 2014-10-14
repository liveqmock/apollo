package cn.com.youtong.apollo.script;

/**
 * 脚本数组对象
 */
public interface ScriptArray {

	/**
	 * 判断数组中指定index的元素是否存在
	 * @param index index
	 * @return 指定index的元素存在，返回true；否则，返回false
	 */
	public boolean has(int index);

	/**
	 * 得到数组中指定index的元素
	 * @param index index
	 * @return 数组中指定index的元素
	 */
	public Object get(int index);

	/**
	 * 更新数组中指定index的元素
	 * @param index index
	 * @param value 新的元属
	 */
	public void put(int index, Object value);
}