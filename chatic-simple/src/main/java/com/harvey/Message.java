package com.harvey;

import lombok.Data;

/**
 * @author harvey
 */
@Data
public class Message {
    private String srcUsername;
    private String tarUsername;
    private String content;
}
