package iuh.fit.se.techgalaxy.frontend.admin.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidateDiscountProduct implements ConstraintValidator<DiscountConstraint, Double>{
    private int max;
    private int min;
    @Override
    public void initialize(DiscountConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.max = constraintAnnotation.max();
        this.min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
         if(value == null){
                return false;
          }
          return value >= min && value <= max;
    }
}
