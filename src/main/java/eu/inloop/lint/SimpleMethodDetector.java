package eu.inloop.lint;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.JavaParser;
import com.android.tools.lint.detector.api.*;
import lombok.ast.AstVisitor;
import lombok.ast.MethodInvocation;

public class SimpleMethodDetector extends Detector implements Detector.JavaScanner {

    /** Invocations of setJavaScriptEnabled */
    public static final Issue ISSUE = Issue.create("UpdateNetwork", //$NON-NLS-1$
        Constants.BRIEF,
        Constants.EXPLANATION,
        Constants.CATEGORY,
        Constants.PRIORITY,
        Constants.SEVERITY,
        new Implementation(SimpleMethodDetector.class, Scope.JAVA_FILE_SCOPE));

    private static final HashMap<String, MethodProblem> METHODS;
    private static final ArrayList<String> METHOD_NAMES;

    static {
        METHODS = new HashMap<String, MethodProblem>();
        //WifiManager
        METHODS.put("updateNetwork", new MethodProblem("android.net.wifi.WifiManager", false, "On some devices this method may not work.", ""));
        METHODS.put("calculateSignalLevel", new MethodProblem( "android.net.wifi.WifiManager", true, "Value could be smaller than 0 (but docs says - range is from 0 to N).", ""));

        //SensorManager
        METHODS.put("getRotationMatrixFromVector", new MethodProblem("android.hardware.SensorManager", true, "On some samsung devices this method does not work correctly.", ""));

        //AlarmManager
        MethodProblem alarmManagerProblem = new MethodProblem("android.app.AlarmManager", false, "Too many alarms on this flag.", "");
        METHODS.put("set", alarmManagerProblem);
        METHODS.put("setExact", alarmManagerProblem);
        METHODS.put("setInexactRepeating", alarmManagerProblem);
        METHODS.put("setRepeating", alarmManagerProblem);

        ArrayList<String> methodNames = new ArrayList<String>();
        for (String method : METHODS.keySet()) {
            methodNames.add(method);
        }
        METHOD_NAMES = methodNames;
    }

    public SimpleMethodDetector() {
    }

    // ---- Implements JavaScanner ----
    @Override
    public void visitMethod(@NonNull JavaContext context, @Nullable AstVisitor visitor,
                            @NonNull MethodInvocation node) {

        MethodProblem methodProblem = METHODS.get(node.astName().astValue());
        JavaParser.ResolvedNode resolvedNode = context.resolve(node);
        if (!(resolvedNode instanceof JavaParser.ResolvedMethod)) {
            return;
        }

        JavaParser.ResolvedMethod method = (JavaParser.ResolvedMethod)resolvedNode;
        boolean isStaticCurrent = Modifier.isStatic(method.getModifiers());
        boolean isStaticExpected = methodProblem.isStaticMethod();
        boolean validModifiers = (isStaticCurrent && isStaticExpected) || (!isStaticCurrent && !isStaticExpected);

        Object evaluate = ConstantEvaluator.evaluate(context, node.astArguments().last());
        if (validModifiers && method.getContainingClass().isSubclassOf(methodProblem.getClassName(), false)) {
            context.report(ISSUE, context.getLocation(node), methodProblem.getMessage());
        }
    }

    @Override
    public List<String> getApplicableMethodNames() {
        return METHOD_NAMES;
    }
}
