package com.example.developer.validator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;


public class AtLeastOneValidator implements ConstraintValidator<AtLeastOne, Object>{

    private String[] fields;

    @Override
    public void initialize(AtLeastOne constraintAnnotation) {
        this.fields = constraintAnnotation.fields();
    }


    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            for (String fieldName : fields) {
                Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(obj);

                if (field.getType().equals(MultipartFile.class)) {
                    MultipartFile file = (MultipartFile) value;
                    if (file != null && !file.isEmpty()) {
                        return true;
                    }
                } else if (field.getType().equals(String.class)) {
                    String str = (String) value;
                    if (str != null && !str.trim().isEmpty()) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // Log exception
            e.printStackTrace();
        }
        return false;
    }
}
