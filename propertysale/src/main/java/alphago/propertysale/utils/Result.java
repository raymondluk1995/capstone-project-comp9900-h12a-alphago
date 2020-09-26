package alphago.propertysale.utils;

import lombok.Data;

/**
 * @program: vueblog
 * @description:
 * @author: XIAO HAN
 * @create: 2020-09-19 23:07
 **/
@Data
// 结果封装
public class Result {
    private int code;
    private String msg;
    private Object result;

    public Result() {
    }

    public Result(int code, String msg, Object result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public static Result success(Object result){
        return new Result(200 , "success" , result);
    }

    public static Result fail(String failMsg){
        return new Result(400 , failMsg , null);
    }
}