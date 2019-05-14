package com.kevin.context.exception;

import com.kevin.context.common.Response;
import com.kevin.context.constant.SystemConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局异常处理
 * @author kevin
 * @date 2019-04-29
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(value = ForbiddenException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public Response<String> handleForbiddenException(Exception e) throws Exception{
        return new Response<>(SystemConstant.RESPONSE_CODE_FAILED, e.getMessage());
    }

    @ExceptionHandler(value = GoneException.class)
    @ResponseStatus(value = HttpStatus.GONE)
    public Response<String> handleGoneException(Exception e) throws Exception{
        return new Response<>(SystemConstant.RESPONSE_CODE_FAILED, e.getMessage());
    }

    @ExceptionHandler(value = InvalidRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Response<String> handleInvalidRequestException(Exception e) throws Exception{
        return new Response<>(SystemConstant.RESPONSE_CODE_FAILED, e.getMessage());
    }

    @ExceptionHandler(value = NotAcceptableException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public Response<String> handleNotAcceptableException(Exception e) throws Exception{
        return new Response<>(SystemConstant.RESPONSE_CODE_FAILED, e.getMessage());
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Response<String> handleNotFoundException(Exception e) throws Exception{
        return new Response<>(SystemConstant.RESPONSE_CODE_FAILED, e.getMessage());
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Response<String> handleUnauthorizedException(Exception e) throws Exception{
        return new Response<>(SystemConstant.RESPONSE_CODE_FAILED, e.getMessage());
    }

    @ExceptionHandler(value = UnprocesableEntityException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public Response<String> handleUnprocesableEntityException(Exception e) throws Exception{
        return new Response<>(SystemConstant.RESPONSE_CODE_FAILED, e.getMessage());
    }

    @ExceptionHandler(value = {
        BizException.class,
        Exception.class
    })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<String> handleBizException(Exception e) throws Exception{
        return new Response<>(SystemConstant.RESPONSE_CODE_FAILED, e.getMessage());
    }

}
