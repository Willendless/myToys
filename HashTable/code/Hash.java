/**
 * @Author: Willendless
 * @Date: 2020-06-25
 * @Description: Do not edit
 * @LastEditTime: 2020-06-26
 * @FilePath: \code\Hash.java
 */

public interface Hash {
    void set(long key, long value);
    Long get(long key);
    void delete(long key);
}
