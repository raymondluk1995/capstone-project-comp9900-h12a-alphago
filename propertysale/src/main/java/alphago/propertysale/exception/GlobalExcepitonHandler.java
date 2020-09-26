package alphago.propertysale.exception;

import alphago.propertysale.utils.Result;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @program: vueblog
 * @description:
 * @author: XIAO HAN
 * @create: 2020-09-25 12:17
 **/
@RestControllerAdvice
public class GlobalExcepitonHandler {
    // 捕捉shiro的异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public Result handle401(ShiroException e) {
        return Result.fail(e.getMessage());
    }

}