package com.regisx001.blog.exceptions;

public class ArticleNotApprovedException extends RuntimeException {
    public ArticleNotApprovedException(String message) {
        super(message);
    }

}
