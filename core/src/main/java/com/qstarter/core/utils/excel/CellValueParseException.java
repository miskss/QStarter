package com.qstarter.core.utils.excel;

/**
 * @author peter
 * date 2020/7/30 9:58
 */
public class CellValueParseException extends RuntimeException {
    private static final long serialVersionUID = -6164385484623679758L;

    public CellValueParseException(String message) {
        super(message);
    }

    public CellValueParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public CellValueParseException(Throwable cause) {
        super(cause);
    }
}
