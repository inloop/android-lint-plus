/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.inloop.lint;

import java.util.Collections;
import java.util.List;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;

@SuppressWarnings("javadoc")
public class SimpleMethodDetectorTest extends LintDetectorTest {

    @Override
    protected Detector getDetector() {
        return new SimpleMethodDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return Collections.singletonList(SimpleMethodDetector.ISSUE);
    }

    public void testCalculateSignal() throws Exception {
        assertEquals("src/test/pkg/SimpleMethodTest.java:6: Warning: Value could be smaller than 0 (but docs says - range is from 0 to N). [UpdateNetwork]\n" +
                "    WifiManager.calculateSignalLevel(-78, 1);\n" +
                "    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                "0 errors, 1 warnings\n",
            lintProject(
                java("src/test/pkg/SimpleMethodTest.java",
                    "package test.pkg;\n" +
                        "import android.net.wifi.WifiManager;\n" +
                        "\n" +
                        "public class WifiManagerTest {\n" +
                        "  public static void test() {\n" +
                        "    WifiManager.calculateSignalLevel(-78, 1);\n" +
                        "}\n" +
                        "}")
            ));
    }

    public void testUpdateNetwork() throws Exception {
        assertEquals("src/test/pkg/SimpleMethodTest.java:6: Warning: Value could be smaller than 0 (but docs says - range is from 0 to N). [UpdateNetwork]\n" +
                "    WifiManager.calculateSignalLevel(-78, 1);\n" +
                "    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                "0 errors, 1 warnings\n",
            lintProject(
                java("src/test/pkg/SimpleMethodTest.java",
                    "package test.pkg;\n" +
                        "import android.net.wifi.WifiManager;\n" +
                        "\n" +
                        "public class WifiManagerTest {\n" +
                        "  public static void test() {\n" +
                        "    WifiManager wifiManager = new WifiManager();\n" +
                        "    wifiManager.updateNetwork(network1);\n" +
                        "}\n" +
                        "}")
            ));
    }

    public void testAlarmManager() throws Exception {
        assertEquals("No warnings.\n",
            lintProject(
                java("src/test/pkg/SimpleMethodTest.java",
                    "package test.pkg;\n" +
                        "import android.app.AlarmManager;\n" +
                        "import android.app.PendingIntent;\n" +
                        "import android.content.Intent;\n" +
                        "\n" +
                        "public class AlarmManagerTest {" +
                        "public static void test(Context context) {\n" +
                        "    int REQUEST_CODE = 1;\n" +
                        "    Intent serviceIntent = null;\n" +
                        "    PendingIntent pendingIntent = PendingIntent.getService(context, REQUEST_CODE, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);\n" +
                        "    AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);\n" +
                        "    int INTERVAL_MIN = 4;\n" +
                        "    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + INTERVAL_MIN, INTERVAL_MIN, pendingIntent);\n" +
                        "}\n" +
                        "}")
            ));
    }

}