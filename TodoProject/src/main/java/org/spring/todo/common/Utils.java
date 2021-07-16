package org.spring.todo.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class Utils {
	
	public static ResponseEntity<?> validation(BindingResult result) {
        if(result.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            for(FieldError error: result.getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return null;
	}
	
	public static ResponseEntity<?> validationDate(Date startingDate, Date endingDate) {
		if (startingDate.after(endingDate)) {
			Map<String, String> errorMap = new HashMap<>();
			errorMap.put("endingDate", "Ending Date is after Starting Date");
			return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
		}
		return null;
	}
	
}
