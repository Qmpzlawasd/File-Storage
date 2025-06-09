//
//package ro.unibuc.comment.Configuration;
//
//import jakarta.persistence.RollbackException;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler({DataIntegrityViolationException.class, RollbackException.class})
//    public String handleDatabaseException(Exception ex, Model model) {
//        model.addAttribute("errorMessage", "Database error: " + ex.getMessage());
//        return "error-page";
//    }
//
//    @ExceptionHandler(Exception.class)
//    public String handleGenericException(Exception ex, Model model) {
//        model.addAttribute("errorMessage", "Unexpected error: " + ex.getMessage());
//        return "error-page";
//    }
//}
////