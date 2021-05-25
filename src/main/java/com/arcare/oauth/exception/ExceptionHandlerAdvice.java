package com.arcare.oauth.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
/**
 * 
 * @author FUHSIANG_LIU
 *
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

	@ExceptionHandler(AuthorizeException.class)
	public ResponseEntity<?> handleOAuthException(AuthorizeException e) {
	 	System.out.println("e:"+e.getError());
	    return ResponseEntity
	    		.status(HttpStatus.BAD_REQUEST)
	            .contentType(MediaType.APPLICATION_JSON)
	            .header("Cache-Control", "no-store")
	            .header("Pragma", "no-cache")
	            .body("{\"error\":\""+e.getError()+"\"}");
	} 
	
	@ExceptionHandler(TokenException.class)
	public ResponseEntity<?> handleTokenException(TokenException e) {
	 	System.out.println("e:"+e.getError());
	    return ResponseEntity
	    		.status(HttpStatus.BAD_REQUEST)
	            .contentType(MediaType.APPLICATION_JSON)
	            .header("Cache-Control", "no-store")
	            .header("Pragma", "no-cache")
	            .body("{\"error\":\""+e.getError()+"\"}");
	}  
	
	public static final String DEFAULT_ERROR_VIEW = "error";
	
	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		// If the exception is annotated with @ResponseStatus rethrow it and let
		// the framework handle it - like the OrderNotFoundException example
		// at the start of this post.
		// AnnotationUtils is a Spring Framework utility class.
		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
			throw e;

		// Otherwise setup and send the user to a default error-view.
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", e);
		mav.addObject("url", req.getRequestURL());
		mav.setViewName(DEFAULT_ERROR_VIEW);
		return mav;
	}
}
