package com.trainingquizzes.english.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "equalsValidator")
public class EqualsValidator implements Validator<Object> {
	
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		Object userEmail = component.getAttributes().get("userEmail");
		Object passwordEmail = component.getAttributes().get("passwordEmail");
		
        if (value == null || userEmail == null && passwordEmail == null) {
            return;
        }

        if (!value.equals(userEmail) && passwordEmail == null) {
            throw new ValidatorException(new FacesMessage("E-mails are not equal"));
        }
        
        if (!value.equals(passwordEmail) && userEmail == null) {
            throw new ValidatorException(new FacesMessage("Password are not equal"));
        }

	}

}
