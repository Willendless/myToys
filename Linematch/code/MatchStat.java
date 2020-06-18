/**
 * @Author: Willendless
 * @Date: 2020-06-16
 * @Description: Do not edit
 * @LastEditTime: 2020-06-18
 * @FilePath: \code\MatchStat.java
 */
package code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import code.SearchTarget.SearchTargetItem;

public class MatchStat {

    private Map<SearchTargetItem, Integer> searchItemMap = new HashMap<>();

    private List<String> matchLines = new ArrayList<>();

    public void inc(SearchTargetItem item) {
        if (!searchItemMap.containsKey(item))
            searchItemMap.put(item, 1);
        else
            searchItemMap.put(item, searchItemMap.get(item) + 1);
    }

    public void addLine(int lineNum, String line) {
        String s = "(" + lineNum + ")" + " " + line;
        if (!matchLines.contains(s))
            matchLines.add(s);
    }

    @Override
    public String toString() {
        StringBuilder re = new StringBuilder();
        for (Map.Entry<SearchTargetItem, Integer> en
            : searchItemMap.entrySet()) {
            re.append(en.getKey() + ": " + en.getValue());
            if (en.getValue() == 1) {
                re.append(" occurrence" + "\n");
            } else {
                re.append(" occurrences" + "\n");
            }
        }
        re.append("\n");
        int i = 1;
        for (String line : matchLines) {
            re.append(i + ". " + line + "\n");
            i++;
        }
        return re.toString();
    }
    
}