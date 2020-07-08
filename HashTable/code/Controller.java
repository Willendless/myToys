/**
 * @Author: Willendless
 * @Date: 2020-06-26
 * @Description: Do not edit
 * @LastEditTime: 2020-06-27
 * @FilePath: \code\Controller.java
 */
public class Controller {

    CuckooHash hash;
    MainFrame view;

    Controller(CuckooHash hash) {
        this.hash = hash;
        this.view = new MainFrame(this);
    }

    void init() {
        view.init();
    }

    void set(long key, long value) {
        hash.set(key, value);
        view.repaint();
    }

    void delete(long key) {
        System.out.println("> delete " + key);
        hash.delete(key);
        view.repaint();
    }

    public static void main(String[] args) {
        new Controller(new CuckooHash()).init();
    }
    
}
