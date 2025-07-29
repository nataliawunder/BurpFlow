package ui;

import javax.swing.*;
import java.awt.*;

public class ReadmePanel extends JPanel {

    public ReadmePanel() {
        setLayout(new BorderLayout());

        JEditorPane readmePane = new JEditorPane();
        readmePane.setContentType("text/html");
        readmePane.setEditable(false);
        readmePane.setText(getStyledReadmeHtml());

        JScrollPane scrollPane = new JScrollPane(readmePane);
        add(scrollPane, BorderLayout.CENTER);
    }

    private String getStyledReadmeHtml() {
        return """
            <html>
                <body style='font-family: Sans-Serif; font-size: 10px; padding: 7px;'>
                    <h1>BurpFlow</h1>
                    <hr>
                    
                    <h2>BurpFlow allows web vulnerability testers to group related HTTP requests
                    into user-defined, logical workflows representing application behavior, including multi-step actions 
                    or entire sessions, to improve efficiency and traceability.</h2>
                    <h2>Created by Natalia Wunder</h2>

                    <br>
                    <h2>Features</h2>
                    <ul>
                        <li>Capture HTTP requests from Proxy, sequentially or manually</li>
                        <li>Organize traffic into named flows</li>
                        <li>Display requests & responses in sequence in the BurpFlow Tab</li>
                    </ul>

                    <br>
                    <h2>How to Use</h2>
                    <h3>Proxy Tab</h3>
                    <p>In the Proxy Tab, right-click on a request to begin a flow. You can begin a flow two ways:</p>
                    <ol>
                        <li>Click <b>Begin Flow on Next Request</b> to capture flows sequentially by simply navigating the application. To stop this flow, right-click on any request and click <b>Stop Current Flow</b></li>
                        <li>Click <b>Add Request to Flow</b> to manually add requests by selecting requests from the Proxy history</li>
                    </ol>

                    <h3>BurpFlow Tab</h3>
                    <p>Flows will be shown in the BurpFlow Tab. Flows can be added, renamed, deleted, and refreshed using the buttons in the Flow Sidebar. To view a flow and it's requests, click on a flow in the sidebar.</p>
                    <p>Once a flow is selected:</p>
                    <ul>
                        <li>Requests and pertinent information will show in the Request Grid in sequential order</li>
                        <li>By clicking on a row, the HTTP Request and Response will be displayed in the HTML Editors</li>
                        <li>When right-clicking on a row, the request can be sent to Burp's Intruder or Repeater</li>
                        <li>When right-clicking on one or multiple rows, the requests can be deleted from the flow</li>
                    </ul>

                    <br>
                    <h2>Notes & Recommendations</h2>
                    <ul>
                        <li><b>Scope MUST be set in order for sequential flows to capture.</b></li>
                        <li><b>Only manually added flows will persist in projects.</b> In order to save sequential flows, please manually add them to a flow before leaving a project.</li>
                        <li><b>While using BurpFlow, do not clear or delete the Proxy History</b> The Proxy ID only shows for manually added requests only, but if you clear or delete Proxy history, the ID shown in BurpFlow will NOT match the ID shown in the Proxy.</li>
                        <li><b>Ensure a flow is selected and highlighted blue in the sidebar when selecting or right-clicking a request.</b> The action may not occur if the flow is not highlighted.</li>
                    </ul>

                    <br>
                    <h2>Build</h2>
                    <p> To build the .jar file, ensure you have installed Gradle, then run <b>./gradlew jar</b> in the terminal. The jar file will be placed in the build/libs folder.
                    Import the jar file named BurpFlow.jar into Burp Suite as a Java Extension by navigating to the Extensions Tab and pressing Add.</p>

                    <br>
                    <h2>Future Implementations</h2>
                    <ul>
                        <li>Persistence for sequential flows</li>
                        <li>Full ID implementation that matches Burp's Proxy</li>
                        <li>Configuration to toggle Proxy highlighting off</li>
                    </ul>

                    <br>
                    <h2>Help & Feedback</h2>
                    <p>For feedback and bug reports, please contact natalia.wunder@guidepointsecurity.com or visit BurpFlow's GitHub Repository at https://github.com/nataliawunder/BurpFlow.</p>

                    <br>
                    <h2>Acknowledments</h2>
                    <p>A special thank you to Aaron Levin and the GPSU Team for all your guidance and support during my internship at GuidePoint Security University. This project wouldn't have been possible without you! 
                    Additionally, thank you to Elle Stehli and Gaberiel Bearden for your help in implementation ideas for the Proxy ID functionality and cross-functionality debugging, respectively.</p>
                    <br>
                </body>
            </html>
        """;
    }
}