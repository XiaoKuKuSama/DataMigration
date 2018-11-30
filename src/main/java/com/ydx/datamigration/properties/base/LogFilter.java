package com.ydx.datamigration.properties.base;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据源日志过滤器
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogFilter {
    private boolean resultSetLogEnabled = false;
    private boolean connectionLogEnabled = false;
    private boolean statementCreateAfterLogEnabled = false;
    private boolean statementCloseAfterLogEnabled = false;
    private boolean statementPrepareAfterLogEnabled = false;
    private boolean statementParameterClearLogEnabled = false;
    private boolean statementParameterSetLogEnabled = false;

}
