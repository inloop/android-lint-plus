package eu.inloop.lint;

public class MethodProblem {

    private String mClassName;
    private boolean mStaticMethod;
    private String mMessage;
    private String mIssueLink;

    public MethodProblem(String className, boolean staticMethod, String message, String issueLink) {
        mClassName = className;
        mStaticMethod = staticMethod;
        mMessage = message;
        mIssueLink = issueLink;
    }

    public String getClassName() {
        return mClassName;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getIssueLink() {
        return mIssueLink;
    }

    public boolean isStaticMethod() {
        return mStaticMethod;
    }
}
