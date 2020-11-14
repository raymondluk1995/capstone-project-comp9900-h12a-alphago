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
 *               It will catch all kinds of (subclass) of RuntimeException in controller.
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Catch Shiro Exception
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public Result handle401(ShiroException e) {
        e.printStackTrace();
        return Result.fail(HttpStatus.BAD_REQUEST.value() , e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException.class)
    public Result unauthorized(UnauthenticatedException e) {
        e.printStackTrace();
        return Result.fail(HttpStatus.UNAUTHORIZED.value() , HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e) {
        e.printStackTrace();
        log.error("Assert Exception:-------------->{}",e.getMessage());
        return Result.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JWTDecodeException.class)
    public Result handler(JWTDecodeException e){
        e.printStackTrace();
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public Result handler(FileSizeLimitExceededException e){
        log.error("File is too large: ",e);
        return Result.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e) {
        e.printStackTrace();
        log.error("Assert Exception:-------------->{}",e.getMessage());
        return Result.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = AuctionNotFoundException.class)
    public Result handler(AuctionNotFoundException e) {
        log.error("Assert Exception:-------------->{}",e.getMessage());
        return Result.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = AuctionFinishedException.class)
    public Result handler(AuctionFinishedException e) {
        log.error("Assert Exception:-------------->{}",e.getMessage());
        return Result.fail(e.getMessage());
    }
}