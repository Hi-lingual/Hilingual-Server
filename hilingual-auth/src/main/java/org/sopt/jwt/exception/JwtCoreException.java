package org.sopt.jwt.exception;


import org.sopt.exception.code.ErrorCode;

public abstract class JwtCoreException extends JwtBaseException {
    protected JwtCoreException(ErrorCode errorCode) { super(errorCode); }
}
