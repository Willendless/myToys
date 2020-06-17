/**
 * @Author: Willendless
 * @Date: 2020-06-16
 * @Description: file matcher
 * @LastEditTime: 2020-06-16
 * @FilePath: \code\Matcher.java
 */
package code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import code.SearchTarget.SearchTargetItem;

public class Matcher {

    private SearchTarget searchTarget;

    public Matcher(SearchTarget searchTarget) {
        this.searchTarget = searchTarget;
    }

    public MatchStat match(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        MatchStat re = new MatchStat();
        String line = reader.readLine();
        int i = 1;
        while (line != null) {
            for (SearchTargetItem s : searchTarget.searchSet) {
                if (lineMatch(line, s)) {
                    re.inc(s);
                    re.addLine(i, line);
                }
            }
            line = reader.readLine();
            i++;
        }
        reader.close();
        return re;
    }

    private boolean lineMatch(String line, SearchTargetItem item) {
        System.out.println("> line: " + "(" + line + ")" + " " + item._target);
        line = line.toLowerCase();
        return (item._target.equals("") || line.indexOf(item._target) != -1)
               && (item._nonTarget.equals("") || line.indexOf(item._nonTarget) == -1);
    }
}
