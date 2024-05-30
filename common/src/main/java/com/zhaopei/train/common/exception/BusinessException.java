package com.zhaopei.train.common.exception;


public class BusinessException extends RuntimeException{

    private BusinessExceptionEnum e;


    public BusinessException() {
    }

    public BusinessException(BusinessExceptionEnum e) {
        this.e = e;
    }

    /**
     * 获取
     * @return e
     */
    public BusinessExceptionEnum getE() {
        return e;
    }

    /**
     * 设置
     * @param e
     */
    public void setE(BusinessExceptionEnum e) {
        this.e = e;
    }

    public String toString() {
        return "BusinessException{e = " + e + "}";
    }
}
