package eu.inloop.lint;


import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Severity;

/**
 * Those constants are default Issue values for most issues. However you can use different values in issues.
 */
public final class Constants {

    public static final String BRIEF = "Looks for device specific known problematic methods.";
    public static final String EXPLANATION = "more info";
    public static final Category CATEGORY = Category.CORRECTNESS;
    public static final Severity SEVERITY = Severity.WARNING;
    public static final int PRIORITY = 5;

}
