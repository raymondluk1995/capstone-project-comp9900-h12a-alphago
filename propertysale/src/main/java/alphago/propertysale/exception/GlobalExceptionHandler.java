package alphago.propertysale.exception;

import alphago.propertysale.utils.Result;
import com.auth0.jwt.exceptions.JWTDecodeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;


/**
 * @program: vueblog
 * @description: A Global ExceptionHandler
 * @author: XIAO HAN
 * @create: 2020-09-25 12:17
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 捕捉shiro的异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ShiroException.class)
    public Result handle401(ShiroException e) {
        return Result.fail(HttpStatus.BAD_REQUEST.value() , e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException.class)
    public Result unauthorized(UnauthenticatedException e) {
        return Result.fail(HttpStatus.UNAUTHORIZED.value() , HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e) {
        log.error("Assert异常:-------------->{}",e.getMessage());
        return Result.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JWTDecodeException.class)
    public Result handler(JWTDecodeException e){
//        log.error("解码错误",e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public Result handler(FileSizeLimitExceededException e){
        log.error("文件过大",e);
        return Result.fail(e.getMessage());
    }
}