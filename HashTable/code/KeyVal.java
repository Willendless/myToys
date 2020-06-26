/**
 * @Author: Willendless
 * @Date: 2020-06-25
 * @Description: Do not edit
 * @LastEditTime: 2020-06-25
 * @FilePath: \code\KeyVal.java
 */
public class KeyVal {
    Long key;
    Long value;

    KeyVal(long key, long value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (obj instanceof KeyVal) {
            KeyVal other = (KeyVal) obj;
            return other.key == key && other.value == value;
        }

        return false;
    }
}
