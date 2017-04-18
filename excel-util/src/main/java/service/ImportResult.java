package service;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * Created by zc on 2017/4/10.
 */
public class ImportResult<T> {

    private int errorCount;

    private String errorMsg;

    private int succCount = 0;

    private List<T> importBeanList = Lists.newArrayList();

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public void incrErrorCount(int errorCount) {
        this.errorCount += errorCount;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getSuccCount() {
        return succCount;
    }

    public void setSuccCount(int succCount) {
        this.succCount = succCount;
    }

    public void incrSuccCount(int succCount) {
        this.succCount += succCount;
    }

    public List<T> getImportBeanList() {
        return importBeanList;
    }

    public void setImportBeanList(List<T> importBeanList) {
        this.importBeanList = importBeanList;
    }

    @Override
    public String toString() {
//        return "ImportResult{" +
//                "errorCount=" + errorCount +
//                ", errorMsg='" + errorMsg + '\'' +
//                ", succCount=" + succCount +
//                ", importBeanList=" + importBeanList +
//                '}';
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
