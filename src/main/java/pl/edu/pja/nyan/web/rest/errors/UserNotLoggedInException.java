package pl.edu.pja.nyan.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class UserNotLoggedInException extends AbstractThrowableProblem {
    public UserNotLoggedInException() {
        super(null, "User is not logged in", Status.UNAUTHORIZED);
    }
}
